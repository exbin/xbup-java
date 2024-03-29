/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.parser.token.event;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TailDataInputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.stream.FinishableStream;
import org.exbin.xbup.core.ubnumber.type.UBENat32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Basic XBUP level 0 event reader - producer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBEventReader implements XBEventProducer {

    private XBParserMode parserMode = XBParserMode.FULL;
    private InputStream source;
    private XBEventListener listener;

    public XBEventReader() {
    }

    public XBEventReader(InputStream inputStream) throws IOException {
        this();
        openStream(inputStream);
    }

    public XBEventReader(InputStream inputStream, XBParserMode parserMode) throws IOException {
        this();
        this.parserMode = parserMode;
        openStream(inputStream);
    }

    private void openStream(InputStream stream) throws IOException {
        source = stream;
    }

    /**
     * Opens input byte-stream.
     *
     * @param stream input stream
     * @throws java.io.IOException if input/output error
     */
    public void open(InputStream stream) throws IOException {
        openStream(stream);
    }

    /**
     * Processes all events and send them to target.
     *
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void read() throws XBProcessingException, IOException {
        // Process header
        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
            XBHead.checkXBUPHead(source);
        }

        // Process single node
        readNode();
    }

    /**
     * Reads single node and all its child nodes.
     *
     * If parent node is in terminated mode, this might just close parent node.
     */
    private void readNode() throws XBProcessingException, IOException {
        // Size limits provides list of limits in tree structure
        // with null value for terminated data parts
        List<Integer> sizeLimits = new ArrayList<>();

        // Process blocks until whole tree is processed
        do {
            UBNat32 attributePartSize = new UBNat32();
            int attributePartSizeLength = attributePartSize.fromStreamUB(source);
            shrinkStatus(sizeLimits, attributePartSizeLength);
            if (attributePartSize.getLong() == 0) {
                // Process terminator
                if (sizeLimits.isEmpty() || sizeLimits.get(sizeLimits.size() - 1) != null) {
                    throw new XBParsingException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
                }

                sizeLimits.remove(sizeLimits.size() - 1);

                if (sizeLimits.isEmpty()) {
                    // Process tail data
                    if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL && source.available() > 0) {
                        listener.putXBToken(XBDataToken.create(new TailDataInputStreamWrapper(source)));
                    }
                }

                listener.putXBToken(XBEndToken.create());
            } else {
                // Process regular block
                int attributePartSizeValue = attributePartSize.getInt();
                shrinkStatus(sizeLimits, attributePartSizeValue);

                UBENat32 dataPartSize = new UBENat32();
                int dataPartSizeLength = dataPartSize.fromStreamUB(source);
                Integer dataPartSizeValue = dataPartSize.isInfinity() ? null : dataPartSize.getInt();

                listener.putXBToken(XBBeginToken.create(dataPartSizeValue == null ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED));

                if (attributePartSizeValue == dataPartSizeLength) {
                    // Process data block
                    FinishableStream dataWrapper = (dataPartSizeValue == null)
                            ? new TerminatedDataInputStreamWrapper(source)
                            : new FixedDataInputStreamWrapper(source, dataPartSizeValue);
                    listener.putXBToken(XBDataToken.create((InputStream) dataWrapper));
                    dataWrapper.finish();
                    shrinkStatus(sizeLimits, (int) dataWrapper.getLength());

                    if (sizeLimits.isEmpty()) {
                        // Process tail data
                        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL && source.available() > 0) {
                            listener.putXBToken(XBDataToken.create(new TailDataInputStreamWrapper(source)));
                        }
                    }

                    listener.putXBToken(XBEndToken.create());
                } else {
                    // Process standard block
                    sizeLimits.add(dataPartSizeValue);

                    // Process attributes
                    attributePartSizeValue -= dataPartSizeLength;
                    while (attributePartSizeValue > 0) {
                        UBNat32 attribute = new UBNat32();
                        int attributeLength = attribute.fromStreamUB(source);
                        if (attributeLength > attributePartSizeValue) {
                            throw new XBParsingException("Attribute overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                        }

                        attributePartSizeValue -= attributeLength;
                        listener.putXBToken(XBAttributeToken.create(attribute));
                    }
                }
            }

            // Enclose all completed blocks
            while (sizeLimits.size() > 0 && sizeLimits.get(sizeLimits.size() - 1) != null && sizeLimits.get(sizeLimits.size() - 1) == 0) {
                sizeLimits.remove(sizeLimits.size() - 1);

                if (sizeLimits.isEmpty()) {
                    // Process tail data
                    if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL && source.available() > 0) {
                        TailDataInputStreamWrapper dataWrapper = new TailDataInputStreamWrapper(source);
                        listener.putXBToken(XBDataToken.create(dataWrapper));
                    }
                }

                listener.putXBToken(XBEndToken.create());
            }
        } while (!sizeLimits.isEmpty());
    }

    /**
     * Closes input stream.
     *
     * @throws java.io.IOException if input/output error
     */
    public void close() throws IOException {
        source.close();
    }

    /**
     * Shrinks limits accross all depths.
     *
     * @param length Value to shrink all limits off
     * @throws XBParsingException If limits are breached
     */
    private static void shrinkStatus(List<Integer> sizeLimits, int length) throws XBParsingException {
        for (int depthLevel = 0; depthLevel < sizeLimits.size(); depthLevel++) {
            Integer limit = sizeLimits.get(depthLevel);
            if (limit != null) {
                if (limit < length) {
                    throw new XBParsingException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }

                sizeLimits.set(depthLevel, limit - length);
            }
        }
    }

    @Override
    public void attachXBEventListener(XBEventListener eventListener) {
        this.listener = eventListener;
    }

    @Nonnull
    @Override
    public String toString() {
        String retValue;
        retValue = super.toString();
        return retValue;
    }
}
