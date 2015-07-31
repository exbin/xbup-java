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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserMode;
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
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.StreamUtils;

/**
 * XBUP level 0 pull writer.
 *
 * @version 0.1.25 2015/07/22
 * @author XBUP Project (http://xbup.org)
 */
public class XBPullWriter implements Closeable, XBPullConsumer {

    private XBParserMode parserMode = XBParserMode.FULL;

    private OutputStream stream;
    private XBPullProvider pullProvider;

    public XBPullWriter() throws IOException {
    }

    public XBPullWriter(OutputStream outputStream) throws IOException {
        this();
        openStream(outputStream);
    }

    public XBPullWriter(OutputStream outputStream, XBParserMode parserMode) throws IOException {
        this();
        this.parserMode = parserMode;
        openStream(outputStream);
    }

    private void openStream(OutputStream stream) throws IOException {
        this.stream = stream;
    }

    /**
     * Opens byte output stream.
     *
     * @param stream
     * @throws IOException
     */
    public void open(OutputStream stream) throws IOException {
        openStream(stream);
    }

    public void write() throws XBProcessingException, IOException {
        List<Integer> sizeLimits = new ArrayList<>();
        XBTokenBuffer tokenBuffer = new XBTokenBuffer();
        List<XBAttribute> attributeList = new ArrayList<>();
        int bufferedFromLevel = -1;
        int depthLevel = 0;

        // Write file head
        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
            XBHead.writeXBUPHead(stream);
        }

        XBToken token = pullProvider.pullXBToken();
        do {
            switch (token.getTokenType()) {
                case BEGIN: {
                    depthLevel++;
                    XBBlockTerminationMode terminationMode = ((XBBeginToken) token).getTerminationMode();
                    if (bufferedFromLevel >= 0) {
                        tokenBuffer.putXBToken(token);
                        sizeLimits.add(null);
                    } else {
                        if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            bufferedFromLevel = depthLevel;
                            tokenBuffer.putXBToken(token);
                            sizeLimits.add(null);
                        } else {
                            sizeLimits.add(null);
                        }
                    }

                    token = pullProvider.pullXBToken();
                    switch (token.getTokenType()) {
                        case DATA: {
                            if (bufferedFromLevel >= 0) {
                                tokenBuffer.putXBToken(token);
                            } else {
                                OutputStream streamWrapper;
                                if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                                    streamWrapper = new FixedDataOutputStreamWrapper(stream, 0);
                                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), streamWrapper);
                                    int dataSize = (int) ((FinishableStream) streamWrapper).finish();
                                    shrinkStatus(sizeLimits, dataSize);
                                } else {
                                    UBNat32 attributePartSize = new UBNat32(UBENat32.INFINITY_SIZE_UB);
                                    attributePartSize.toStreamUB(stream);
                                    UBENat32 dataPartSize = new UBENat32();
                                    dataPartSize.setInfinity();
                                    dataPartSize.toStreamUB(stream);
                                    shrinkStatus(sizeLimits, UBENat32.INFINITY_SIZE_UB + 1);
                                    streamWrapper = new TerminatedDataOutputStreamWrapper(stream);

                                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), streamWrapper);
                                    int dataSize = (int) ((FinishableStream) streamWrapper).finish();
                                    shrinkStatus(sizeLimits, dataSize);
                                }
                            }

                            token = pullProvider.pullXBToken();

                            if (depthLevel == 1 && token.getTokenType() == XBTokenType.DATA) {
                                if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                                    if (bufferedFromLevel >= 0) {
                                        tokenBuffer.putXBToken(new XBEndToken());
                                        if (bufferedFromLevel == depthLevel) {
                                            tokenBuffer.write(stream);
                                            bufferedFromLevel = -1;
                                        }
                                    }

                                    depthLevel--;
                                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), stream);
                                    token = pullProvider.pullXBToken();
                                    if (token.getTokenType() != XBTokenType.END) {
                                        throw new XBParseException("End token was expected after extended area", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                    }
                                } else {
                                    throw new XBParseException("Extended data block present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                }
                            }

                            if (token.getTokenType() != XBTokenType.END) {
                                throw new XBParseException("Data block must be followed by block end", XBProcessingExceptionType.UNEXPECTED_ORDER);
                            } else {
                                if (bufferedFromLevel < 0) {
                                    depthLevel--;
                                    decreaseStatus(sizeLimits);
                                    if (depthLevel > 0) {
                                        token = pullProvider.pullXBToken();
                                    }
                                }
                            }

                            break;
                        }

                        case ATTRIBUTE: {
                            attributeList.clear();
                            int attributePartSizeValue = 0;
                            do {
                                XBAttribute attribute = ((XBAttributeToken) token).getAttribute();

                                if (bufferedFromLevel >= 0) {
                                    tokenBuffer.putXBToken(token);
                                } else {
                                    int attributeSize = attribute.getSizeUB();
                                    shrinkStatus(sizeLimits, attributeSize);
                                    attributePartSizeValue += attributeSize;
                                    attributeList.add(attribute);
                                }

                                token = pullProvider.pullXBToken();
                            } while (token.getTokenType() == XBTokenType.ATTRIBUTE);

                            if (bufferedFromLevel < 0) {
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

                                attributeList.clear();
                            }

                            if (depthLevel == 1 && token.getTokenType() == XBTokenType.DATA) {
                                if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                                    if (bufferedFromLevel >= 0) {
                                        tokenBuffer.putXBToken(new XBEndToken());
                                        if (bufferedFromLevel == depthLevel) {
                                            tokenBuffer.write(stream);
                                            bufferedFromLevel = -1;
                                        }
                                    } else {
                                        if (sizeLimits.get(depthLevel - 1) == null) {
                                            stream.write(0);
                                        }
                                        decreaseStatus(sizeLimits);
                                    }

                                    depthLevel--;
                                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), stream);
                                    token = pullProvider.pullXBToken();
                                    if (token.getTokenType() != XBTokenType.END) {
                                        throw new XBParseException("End token was expected after extended area", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                    }
                                } else {
                                    throw new XBParseException("Extended data block present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                }
                            }

                            break;
                        }
                        default:
                            throw new XBParseException("Missing at least one attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    break;
                }

                case END: {
                    if (bufferedFromLevel >= 0) {
                        tokenBuffer.putXBToken(token);
                        if (bufferedFromLevel == depthLevel) {
                            tokenBuffer.write(stream);
                            bufferedFromLevel = -1;
                        }
                    } else {
                        if (sizeLimits.get(depthLevel - 1) == null) {
                            stream.write(0);
                        }
                        decreaseStatus(sizeLimits);
                    }

                    depthLevel--;
                    if (depthLevel > 0) {
                        token = pullProvider.pullXBToken();
                    }

                    if (depthLevel == 1 && token.getTokenType() == XBTokenType.DATA) {
                        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                            if (bufferedFromLevel >= 0) {
                                tokenBuffer.putXBToken(new XBEndToken());
                                if (bufferedFromLevel == depthLevel) {
                                    tokenBuffer.write(stream);
                                    bufferedFromLevel = -1;
                                }
                            } else {
                                if (sizeLimits.get(depthLevel - 1) == null) {
                                    stream.write(0);
                                }
                                decreaseStatus(sizeLimits);
                            }

                            depthLevel--;
                            StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), stream);
                            token = pullProvider.pullXBToken();
                            if (token.getTokenType() != XBTokenType.END) {
                                throw new XBParseException("End token was expected after extended area", XBProcessingExceptionType.UNEXPECTED_ORDER);
                            }
                        } else {
                            throw new XBParseException("Extended data block present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                        }
                    }

                    break;
                }

                default:
                    throw new XBParseException("Must begin with NodeBegin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        } while (depthLevel > 0);
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    /**
     * Shrinks limits accross all depths.
     *
     * @param value Value to shrink all limits off
     * @throws XBParseException If limits are breached
     */
    private static void shrinkStatus(List<Integer> sizeLimits, int value) throws XBParseException {
        for (int depth = 0; depth < sizeLimits.size(); depth++) {
            Integer limit = sizeLimits.get(depth);
            if (limit != null) {
                if (limit < value) {
                    throw new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }

                sizeLimits.set(depth, limit - value);
            }
        }
    }

    /**
     * Decreases size limits by removing status on last level.
     *
     * @param sizeLimits block sizes
     */
    private static void decreaseStatus(List<Integer> sizeLimits) {
        Integer levelValue = sizeLimits.remove(sizeLimits.size() - 1);
        if (levelValue != null && levelValue != 0) {
            throw new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
        }
    }

    @Override
    public void attachXBPullProvider(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }
}
