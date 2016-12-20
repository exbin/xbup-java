/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.parser.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBParserState;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TailDataInputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import org.exbin.xbup.core.stream.FinishableStream;
import org.exbin.xbup.core.ubnumber.type.UBENat32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Basic XBUP level 0 reader - provider.
 *
 * @version 0.1.25 2015/07/28
 * @author ExBin Project (http://exbin.org)
 */
public class XBProviderReader implements XBProvider {

    private XBParserState parserState;
    private XBParserMode parserMode = XBParserMode.FULL;

    private InputStream source;
    private FinishableStream dataWrapper;

    private final List<Integer> sizeLimits = new ArrayList<>();
    private int attributePartSizeValue;
    private Integer dataPartSizeValue;

    public XBProviderReader() {
        resetParser();
    }

    public XBProviderReader(InputStream inputStream) throws IOException {
        this();
        openStream(inputStream);
    }

    public XBProviderReader(InputStream inputStream, XBParserMode parserMode) throws IOException {
        this();
        this.parserMode = parserMode;
        openStream(inputStream);
    }

    private void openStream(InputStream stream) throws IOException {
        source = stream;
        reset();
    }

    private void resetParser() {
        dataWrapper = null;
        parserState = XBParserState.START;
        sizeLimits.clear();
        attributePartSizeValue = 0;
    }

    /**
     * Opens input byte-stream.
     *
     * @param stream input stream
     * @throws java.io.IOException if input/output error
     */
    public void open(InputStream stream) throws IOException {
        source = stream;
        reset();
    }

    /**
     * Resets input stream and parser state.
     *
     * @throws java.io.IOException if input/output error
     */
    public void reset() throws IOException {
        resetParser();
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
     * @param value Value to shrink all limits off
     * @throws XBParseException If limits are breached
     */
    private static void shrinkStatus(List<Integer> sizeLimits, int value) throws XBParseException {
        for (int depthLevel = 0; depthLevel < sizeLimits.size(); depthLevel++) {
            Integer limit = sizeLimits.get(depthLevel);
            if (limit != null) {
                if (limit < value) {
                    throw new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
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
     */
    public boolean isFinished() {
        return parserState == XBParserState.EOF;
    }

    /**
     * Returns current parsing mode.
     *
     * @return parse mode
     */
    public XBParserState getParserState() {
        return parserState;
    }

    @Override
    public String toString() {
        String retValue;
        retValue = super.toString();
        return retValue;
    }

    @Override
    public void produceXB(XBListener listener) throws XBProcessingException, IOException {
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
                        throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
                    }

                    sizeLimits.remove(sizeLimits.size() - 1);
                    parserState = XBParserState.BLOCK_END;
                    // nobreak - Continue to process BLOCK_END
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

                    listener.beginXB(dataPartSizeValue == null ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED);
                    break;
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
                                listener.dataXB(wrapper);
                                break;
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

                listener.endXB();
                break;
            }

            case ATTRIBUTE_PART: {
                UBNat32 attribute = new UBNat32();
                int attributeLength = attribute.fromStreamUB(source);
                if (attributeLength > attributePartSizeValue) {
                    throw new XBParseException("Attribute overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
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

                listener.attribXB(attribute);
                break;
            }

            case DATA_PART: {
                dataWrapper = (dataPartSizeValue == null)
                        ? new TerminatedDataInputStreamWrapper(source)
                        : new FixedDataInputStreamWrapper(source, dataPartSizeValue);

                parserState = XBParserState.BLOCK_END;
                listener.dataXB((InputStream) dataWrapper);
                break;
            }

            case EOF:
                throw new XBParseException("Reading After End", XBProcessingExceptionType.READING_AFTER_END);

            default:
                throw new XBParseException("Unexpected pull item type", XBProcessingExceptionType.UNKNOWN);
        }
    }
}
