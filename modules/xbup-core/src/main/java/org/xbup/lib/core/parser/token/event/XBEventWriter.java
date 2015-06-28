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
package org.xbup.lib.core.parser.token.event;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.basic.wrapper.FixedDataOutputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataOutputStreamWrapper;
import org.xbup.lib.core.parser.token.convert.XBTokenBuffer;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.stream.FinishableStream;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.StreamUtils;

/**
 * Basic XBUP level 0 event writer - listener.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBEventWriter implements Closeable, XBEventListener {

    private XBParserState parserState;
    private XBParserMode parserMode = XBParserMode.FULL;

    private final XBTokenBuffer tokenWriter = new XBTokenBuffer();
    private XBDataToken extendedArea = null;
    private OutputStream stream;
    private List<Integer> sizeLimits = new ArrayList<>();
    private int bufferedFromLevel = -1;
    private int level = 0;

    private XBBlockTerminationMode terminationMode;
    private XBBlockDataMode dataMode;
    private final List<XBAttribute> attributeList = new ArrayList<>();
    private int attributePartSizeValue = 0;

    public XBEventWriter() {
        sizeLimits = new ArrayList<>();
        level = 0;
        parserState = XBParserState.START;
    }

    public XBEventWriter(OutputStream outputStream) throws IOException {
        this();
        openStream(outputStream);
    }

    public XBEventWriter(OutputStream outputStream, XBParserMode parserMode) throws IOException {
        this();
        this.parserMode = parserMode;
        openStream(outputStream);
    }

    private void openStream(OutputStream outputStream) throws IOException {
        stream = outputStream;
    }

    public void open(OutputStream outputStream) throws IOException {
        openStream(outputStream);
    }

    @Override
    public void close() throws XBProcessingException, IOException {
        stream.close();
    }

    public void closeXB() throws XBProcessingException, IOException {
        if (parserState != XBParserState.EOF && parserState != XBParserState.EXTENDED_AREA) {
            throw new XBParseException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }

        close();
    }

    @Override
    public void putXBToken(XBToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                if (parserState == XBParserState.START) {
                    // Write file head
                    if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
                        XBHead.writeXBUPHead(stream);
                    }

                    parserState = XBParserState.BLOCK_BEGIN;
                } else if (parserState == XBParserState.ATTRIBUTE_PART) {
                    flushAttributes();
                    parserState = XBParserState.BLOCK_BEGIN;
                }

                if (parserState == XBParserState.BLOCK_BEGIN) {
                    level++;
                    this.terminationMode = ((XBBeginToken) token).getTerminationMode();
                    dataMode = null;
                    if (bufferedFromLevel >= 0) {
                        tokenWriter.putXBToken(token);
                        sizeLimits.add(null);
                    } else {
                        if (((XBBeginToken) token).getTerminationMode() == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            bufferedFromLevel = level;
                            tokenWriter.putXBToken(token);
                            sizeLimits.add(null);
                        } else {
                            sizeLimits.add(null);
                        }
                    }

                    attributeList.clear();
                    parserState = XBParserState.ATTRIBUTE_PART;
                } else {
                    throw new XBParseException("Unexpected begin of block", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }

            case ATTRIBUTE: {
                if (parserState == XBParserState.ATTRIBUTE_PART) {
                    dataMode = XBBlockDataMode.NODE_BLOCK;
                    if (bufferedFromLevel >= 0) {
                        tokenWriter.putXBToken(token);
                    } else {
                        int attributeSize = ((XBAttributeToken) token).getAttribute().getSizeUB();
                        shrinkStatus(sizeLimits, attributeSize);
                        attributePartSizeValue += attributeSize;
                        attributeList.add(((XBAttributeToken) token).getAttribute());
                    }
                } else {
                    throw new XBParseException("Unexpected attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }

            case DATA: {
                if (parserState == XBParserState.ATTRIBUTE_PART) {
                    if (level == 1 && dataMode == XBBlockDataMode.NODE_BLOCK) {
                        extendedArea = (XBDataToken) token;
                        parserState = XBParserState.EXTENDED_AREA;
                    } else {
                        dataMode = XBBlockDataMode.DATA_BLOCK;

                        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
                            throw new XBParseException("Unexpected data token after attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
                        }

                        if (bufferedFromLevel >= 0) {
                            tokenWriter.putXBToken(token);
                        } else {
                            OutputStream streamWrapper;
                            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                                // TODO determine size
                                streamWrapper = new FixedDataOutputStreamWrapper(stream, 0);
                            } else {
                                UBNat32 attributePartSize = new UBNat32(UBENat32.INFINITY_SIZE_UB);
                                attributePartSize.toStreamUB(stream);
                                UBENat32 dataPartSize = new UBENat32();
                                dataPartSize.setInfinity();
                                dataPartSize.toStreamUB(stream);
                                shrinkStatus(sizeLimits, UBENat32.INFINITY_SIZE_UB + 1);
                                streamWrapper = new TerminatedDataOutputStreamWrapper(stream);
                            }

                            StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), streamWrapper);

                            int dataPartSize = (int) ((FinishableStream) streamWrapper).finish();
                            shrinkStatus(sizeLimits, dataPartSize);
                        }

                        parserState = XBParserState.BLOCK_END;
                    }
                } else if (parserState == XBParserState.BLOCK_BEGIN) {
                    if (level == 1) {
                        extendedArea = (XBDataToken) token;
                        parserState = XBParserState.EXTENDED_AREA;
                    } else {
                        throw new XBParseException("Unexpected data token", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                } else if (parserState == XBParserState.BLOCK_END) {
                    if (level == 1 && dataMode == XBBlockDataMode.DATA_BLOCK) {
                        extendedArea = (XBDataToken) token;
                        parserState = XBParserState.EXTENDED_AREA;
                    } else {
                        throw new XBParseException("Unexpected data token", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                } else {
                    throw new XBParseException("Unexpected data token", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }

            case END: {
                switch (parserState) {
                    case ATTRIBUTE_PART: {
                        flushAttributes();
                    }

                    case EXTENDED_AREA:
                    case BLOCK_BEGIN:
                    case BLOCK_END: {
                        if (bufferedFromLevel >= 0) {
                            tokenWriter.putXBToken(new XBEndToken());
                            if (bufferedFromLevel == level) {
                                tokenWriter.write(stream);
                                bufferedFromLevel = -1;
                            }
                        }

                        // Write extended block if present
                        if (parserState == XBParserState.EXTENDED_AREA) {
                            if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                                StreamUtils.copyInputStreamToOutputStream(extendedArea.getData(), stream);
                            } else {
                                throw new XBParseException("Extended data block present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                            }
                        }

                        level--;
                        parserState = (level == 0) ? XBParserState.EOF : XBParserState.BLOCK_BEGIN;
                        break;
                    }

                    default:
                        throw new XBParseException("Unexpected end token", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
        }
    }

    private void flushAttributes() throws IOException {
        if (bufferedFromLevel == -1) {
            attributePartSizeValue += UBENat32.INFINITY_SIZE_UB;
            UBNat32 attributePartSize = new UBNat32(attributePartSizeValue);
            shrinkStatus(sizeLimits, attributePartSize.getSizeUB() + UBENat32.INFINITY_SIZE_UB + 1);
            attributePartSize.toStreamUB(stream);
            UBENat32 dataPartSize = new UBENat32();
            dataPartSize.setInfinity();
            dataPartSize.toStreamUB(stream);

            for (XBAttribute attribute : attributeList) {
                attribute.toStreamUB(stream);
            }

            stream.write(0);
            attributeList.clear();
        }
    }

    /**
     * Shrinks limits accross all depths.
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
}
