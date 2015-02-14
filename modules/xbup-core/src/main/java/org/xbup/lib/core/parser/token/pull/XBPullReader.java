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
package org.xbup.lib.core.parser.token.pull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.basic.wrapper.ExtendedAreaInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.xbup.lib.core.stream.FinishableStream;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.stream.XBFinishedStream;
import org.xbup.lib.core.stream.XBResetableStream;
import org.xbup.lib.core.stream.XBSkipableStream;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 0 pull reader.
 *
 * @version 0.1.25 2015/02/14
 * @author XBUP Project (http://xbup.org)
 */
public class XBPullReader implements XBPullProvider, XBResetableStream, XBSkipableStream, XBFinishedStream, Closeable {

    private XBParserState parserState;
    private XBParserMode parserMode = XBParserMode.FULL;

    private InputStream source;
    private FinishableStream dataWrapper;

    private List<Integer> sizeLimits;
    private int attrPartSizeValue;
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
     * @param stream
     * @throws IOException
     */
    public void open(InputStream stream) throws IOException {
        openStream(stream);
    }

    @Override
    public String toString() {
        String retValue;
        retValue = super.toString();
        return retValue;
    }

    private void resetParser() {
        dataWrapper = null;
        parserState = XBParserState.START;
        sizeLimits = new ArrayList<>();
        attrPartSizeValue = 0;
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
     * @throws XBParseException If limits are breached
     */
    private void shrinkStatus(int value) throws XBParseException {
        for (int depth = 0; depth < sizeLimits.size(); depth++) {
            Integer limit = sizeLimits.get(depth);
            if (limit != null) {
                if (limit > 0) {
                    if (limit < value) {
                        throw new XBParseException("Block Overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
                    }

                    sizeLimits.set(depth, limit - value);
                }
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
     * @throws IOException
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
     * @throws java.io.IOException
     */
    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        if (dataWrapper != null) {
            dataWrapper.finish();
            shrinkStatus((int) dataWrapper.getLength());
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
                shrinkStatus(headSize);
                if (attrPartSize.getLong() == 0) {
                    // Process terminator
                    if (sizeLimits.isEmpty() || sizeLimits.get(sizeLimits.size() - 1) != null) {
                        throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
                    }

                    parserState = XBParserState.BLOCK_END;
                    // no break - Continue to process BLOCK_END
                } else {
                    attrPartSizeValue = attrPartSize.getInt();
                    shrinkStatus(attrPartSizeValue);

                    UBENat32 dataPartSize = new UBENat32();
                    int dataPartSizeLength = dataPartSize.fromStreamUB(source);
                    dataPartSizeValue = dataPartSize.isInfinity() ? null : dataPartSize.getInt();

                    if (attrPartSizeValue == dataPartSizeLength) {
                        parserState = XBParserState.DATA_PART;
                    } else {
                        attrPartSizeValue -= dataPartSizeLength;
                        if (attrPartSizeValue > 0) {
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

                    return new XBBeginToken(dataPartSizeValue == null ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED);
                }
            }

            case EXTENDED_AREA:
            case BLOCK_END: {
                // must be after begin
                if (sizeLimits.isEmpty()) {
                    if (parserState == XBParserState.BLOCK_END) {
                        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                            parserState = XBParserState.EXTENDED_AREA;
                            ExtendedAreaInputStreamWrapper wrapper = new ExtendedAreaInputStreamWrapper(source);
                            if (wrapper.available() > 0) {
                                return new XBDataToken(wrapper);
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

                return new XBEndToken();
            }

            case ATTRIBUTE_PART: {
                UBNat32 attribute = new UBNat32();
                int attributeLength = attribute.fromStreamUB(source);
                if (attributeLength > attrPartSizeValue) {
                    throw new XBParseException("Attribute Overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                }

                attrPartSizeValue -= attributeLength;
                if (attrPartSizeValue == 0) {
                    if (dataPartSizeValue == null || dataPartSizeValue > 0) {
                        sizeLimits.add(dataPartSizeValue);
                        parserState = XBParserState.BLOCK_BEGIN;
                    } else {
                        parserState = XBParserState.BLOCK_END;
                    }
                }

                return new XBAttributeToken(attribute);
            }

            case DATA_PART: {
                dataWrapper = (dataPartSizeValue == null)
                        ? new TerminatedDataInputStreamWrapper(source)
                        : new FixedDataInputStreamWrapper(source, dataPartSizeValue);

                parserState = XBParserState.BLOCK_END;
                return new XBDataToken((InputStream) dataWrapper);
            }

            case EOF:
                throw new XBParseException("Reading After End", XBProcessingExceptionType.READING_AFTER_END);

            default:
                throw new XBParseException("Unexpected pull item type", XBProcessingExceptionType.UNKNOWN);
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
