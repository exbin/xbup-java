/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.parser.token.pull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBParserState;
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
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.stream.FinishableStream;
import org.exbin.xbup.core.stream.XBFinishedStream;
import org.exbin.xbup.core.stream.XBResetableStream;
import org.exbin.xbup.core.stream.XBSkipableStream;
import org.exbin.xbup.core.ubnumber.type.UBENat32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 0 pull reader.
 *
 * @version 0.2.1 2017/05/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBPullReader implements XBPullProvider, XBResetableStream, XBSkipableStream, XBFinishedStream, Closeable {

    private XBParserState parserState;
    private XBParserMode parserMode = XBParserMode.FULL;

    private InputStream source;
    private FinishableStream dataWrapper;

    private final List<Integer> sizeLimits = new ArrayList<>();
    private int attributePartSizeValue;
    private Integer dataPartSizeValue;

    public XBPullReader() {
        resetParser();
    }

    public XBPullReader(InputStream inputStream) throws IOException {
        this();
        openStream(inputStream);
    }

    public XBPullReader(InputStream inputStream, XBParserMode parserMode) throws IOException {
        this();
        this.parserMode = parserMode;
        openStream(inputStream);
    }

    private void openStream(InputStream stream) throws IOException {
        source = stream;
        resetParser();
    }

    /**
     * Opens byte input stream.
     *
     * @param stream input stream
     * @throws java.io.IOException if input/output error
     */
    public void open(InputStream stream) throws IOException {
        openStream(stream);
    }

    private void resetParser() {
        dataWrapper = null;
        parserState = XBParserState.START;
        sizeLimits.clear();
        attributePartSizeValue = 0;
    }

    @Override
    public void resetXB() {
        resetParser();
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    /**
     * Shrinks limits accross all depths.
     *
     * @param length Value to shrink all limits off
     * @throws XBParsingException If limits are breached
     */
    private static void shrinkStatus(List<Integer> sizeLimits, int value) throws XBParsingException {
        for (int depthLevel = 0; depthLevel < sizeLimits.size(); depthLevel++) {
            Integer limit = sizeLimits.get(depthLevel);
            if (limit != null) {
                if (limit < value) {
                    throw new XBParsingException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }

                sizeLimits.set(depthLevel, limit - value);
            }
        }
    }

    /**
     * Returns current level of how deep in tree we are.
     *
     * @return tree level, 0 means root
     */
    protected int getLevel() {
        return sizeLimits.size();
    }

    /**
     * Indicates parsing completeness.
     *
     * @return true if complete
     * @throws java.io.IOException if input/output error
     */
    @Override
    public boolean isFinishedXB() throws IOException {
        return parserState == XBParserState.EOF;
    }

    @Override
    public void skipXB(long tokenCount) throws XBProcessingException, IOException {
        for (long i = 0; i < tokenCount; i++) {
            pullXBToken();
        }
    }

    @Override
    public void skipChildXB(long childBlocksCount) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void skipAttributesXB(long childBlocksCount) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Main pull reader parser processing method.
     *
     * @return next recognized event item in stream
     * @throws java.io.IOException if input/output error
     */
    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        if (dataWrapper != null) {
            dataWrapper.finish();
            shrinkStatus(sizeLimits, (int) dataWrapper.getLength());
            dataWrapper = null;
        }

        if (parserState == XBParserState.START) {
            if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
                XBHead.checkXBUPHead(source);
            }

            parserState = XBParserState.BLOCK_BEGIN;
        }

        switch (parserState) {
            case BLOCK_BEGIN: {
                UBNat32 attrPartSize = new UBNat32();
                int headSize = attrPartSize.fromStreamUB(source);
                shrinkStatus(sizeLimits, headSize);
                if (attrPartSize.getLong() == 0) {
                    // Process terminator
                    if (sizeLimits.isEmpty() || sizeLimits.get(sizeLimits.size() - 1) != null) {
                        throw new XBParsingException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
                    }

                    sizeLimits.remove(sizeLimits.size() - 1);
                    parserState = XBParserState.BLOCK_END;
                    // no break - Continue to process BLOCK_END
                } else {
                    attributePartSizeValue = attrPartSize.getInt();
                    shrinkStatus(sizeLimits, attributePartSizeValue);

                    UBENat32 dataPartSize = new UBENat32();
                    int dataPartSizeLength = dataPartSize.fromStreamUB(source);
                    dataPartSizeValue = dataPartSize.isInfinity() ? null : dataPartSize.getInt();

                    if (attributePartSizeValue == dataPartSizeLength) {
                        parserState = XBParserState.DATA_PART;
                    } else {
                        attributePartSizeValue -= dataPartSizeLength;
                        if (attributePartSizeValue > 0) {
                            parserState = XBParserState.ATTRIBUTE_PART;
                        } else {
                            if (dataPartSizeValue == null || dataPartSizeValue > 0) {
                                sizeLimits.add(dataPartSizeValue);
                                parserState = XBParserState.BLOCK_BEGIN;
                            } else {
                                parserState = XBParserState.BLOCK_END;
                            }
                        }
                    }

                    return XBBeginToken.create(dataPartSizeValue == null ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED);
                }
            }

            case TAIL_DATA:
            case BLOCK_END: {
                // must be after begin
                if (sizeLimits.isEmpty()) {
                    if (parserState == XBParserState.BLOCK_END) {
                        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL) {
                            parserState = XBParserState.TAIL_DATA;
                            TailDataInputStreamWrapper wrapper = new TailDataInputStreamWrapper(source);
                            if (wrapper.available() > 0) {
                                return XBDataToken.create(wrapper);
                            }
                        }
                    }

                    parserState = XBParserState.EOF;
                } else {
                    if (sizeLimits.get(sizeLimits.size() - 1) == null || sizeLimits.get(sizeLimits.size() - 1) > 0) {
                        parserState = XBParserState.BLOCK_BEGIN;
                    } else {
                        sizeLimits.remove(sizeLimits.size() - 1);
                    }
                }

                return XBEndToken.create();
            }

            case ATTRIBUTE_PART: {
                UBNat32 attribute = new UBNat32();
                int attributeLength = attribute.fromStreamUB(source);
                if (attributeLength > attributePartSizeValue) {
                    throw new XBParsingException("Attribute overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                }

                attributePartSizeValue -= attributeLength;
                if (attributePartSizeValue == 0) {
                    if (dataPartSizeValue == null || dataPartSizeValue > 0) {
                        sizeLimits.add(dataPartSizeValue);
                        parserState = XBParserState.BLOCK_BEGIN;
                    } else {
                        parserState = XBParserState.BLOCK_END;
                    }
                }

                return XBAttributeToken.create(attribute);
            }

            case DATA_PART: {
                dataWrapper = (dataPartSizeValue == null)
                        ? new TerminatedDataInputStreamWrapper(source)
                        : new FixedDataInputStreamWrapper(source, dataPartSizeValue);

                parserState = XBParserState.BLOCK_END;
                return XBDataToken.create((InputStream) dataWrapper);
            }

            case EOF:
                throw new XBParsingException("Reading After End", XBProcessingExceptionType.READING_AFTER_END);

            default:
                throw new XBParsingException("Unexpected pull item type", XBProcessingExceptionType.UNKNOWN);
        }
    }

    /**
     * Returns actual parsing mode.
     *
     * @return parse mode
     */
    public XBParserState getParseMode() {
        return parserState;
    }
}
