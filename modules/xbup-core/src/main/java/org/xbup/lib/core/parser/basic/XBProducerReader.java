/*
 * Copyright (C) XBUP Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.core.parser.basic;

import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.wrapper.ExtendedAreaInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.InputStreamWrapper;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Basic XBUP level 0 reader - producer.
 *
 * @version 0.1.24 2014/10/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBProducerReader implements XBProducer {

    private XBParserMode parserMode = XBParserMode.FULL;

    private InputStream source;
    private XBListener listener;

    public XBProducerReader() {
    }

    public XBProducerReader(InputStream inputStream) throws IOException {
        this();
        openStream(inputStream);
    }

    public XBProducerReader(InputStream inputStream, XBParserMode parserMode) throws IOException {
        this();
        this.parserMode = parserMode;
        openStream(inputStream);
    }

    private void openStream(InputStream stream) throws IOException {
        source = stream;
        reset();
    }

    /**
     * Open input byte-stream.
     *
     * @param stream input stream
     * @throws java.io.IOException
     */
    public void open(InputStream stream) throws IOException {
        source = stream;
        reset();
    }

    /**
     * Process all events and send them to target.
     *
     * @throws XBProcessingException
     * @throws java.io.IOException
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
     * Read single node and all its child nodes.
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
            int headSize = attributePartSize.fromStreamUB(source);
            shrinkStatus(sizeLimits, headSize);
            if (attributePartSize.getLong() == 0) {
                // Process terminator
                if (sizeLimits.isEmpty() || sizeLimits.get(sizeLimits.size() - 1) != null) {
                    throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
                }

                sizeLimits.remove(sizeLimits.size() - 1);
                listener.endXB();
            } else {
                // Process regular block
                int attrPartSizeValue = attributePartSize.getInt();
                shrinkStatus(sizeLimits, attrPartSizeValue);

                UBENat32 dataPartSize = new UBENat32();
                int dataPartSizeLength = dataPartSize.fromStreamUB(source);
                Integer dataPartSizeValue = dataPartSize.isInfinity() ? null : dataPartSize.getInt();

                listener.beginXB(dataPartSizeValue == null ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED);

                if (attrPartSizeValue == dataPartSizeLength) {
                    // Process data block
                    InputStreamWrapper dataWrapper = (dataPartSizeValue == null)
                            ? new TerminatedDataInputStreamWrapper(source)
                            : new FixedDataInputStreamWrapper(source, dataPartSizeValue);
                    listener.dataXB((InputStream) dataWrapper);
                    dataWrapper.finish();
                    shrinkStatus(sizeLimits, dataWrapper.getLength());

                    listener.endXB();
                } else {
                    // Process standard block
                    sizeLimits.add(dataPartSizeValue);

                    // Process attributes
                    attrPartSizeValue -= dataPartSizeLength;
                    while (attrPartSizeValue > 0) {
                        UBNat32 attribute = new UBNat32();
                        int attributeLength = attribute.fromStreamUB(source);
                        if (attributeLength > attrPartSizeValue) {
                            throw new XBParseException("Attribute Overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                        }

                        attrPartSizeValue -= attributeLength;

                        listener.attribXB(attribute);
                    }
                }
            }

            // Enclose all completed blocks
            while (sizeLimits.size() > 0 && sizeLimits.get(sizeLimits.size() - 1) != null && sizeLimits.get(sizeLimits.size() - 1) == 0) {
                sizeLimits.remove(sizeLimits.size() - 1);

                if (sizeLimits.isEmpty()) {
                    // Process extended area
                    if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                        listener.dataXB(new ExtendedAreaInputStreamWrapper(source));
                    }
                }

                listener.endXB();
            }
        } while (!sizeLimits.isEmpty());
    }

    /**
     * Reset input stream and parser state.
     *
     * @throws java.io.IOException
     */
    public void reset() throws IOException {
    }

    /**
     * Close input stream.
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        source.close();
    }

    /**
     * Method to shrink limits accross all depths.
     *
     * @param value Value to shrink all limits off
     * @throws XBParseException If limits are breached
     */
    private void shrinkStatus(List<Integer> sizeLimits, int value) throws XBParseException {
        for (int depth = 0; depth < sizeLimits.size(); depth++) {
            Integer limit = sizeLimits.get(depth);
            if (limit != null) {
                if (limit < value) {
                    throw new XBParseException("Block Overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }

                sizeLimits.set(depth, limit - value);
            }
        }
    }

    @Override
    public void attachXBListener(XBListener eventListener) {
        this.listener = eventListener;
    }

    @Override
    public String toString() {
        String retValue;
        retValue = super.toString();
        return retValue;
    }
}
