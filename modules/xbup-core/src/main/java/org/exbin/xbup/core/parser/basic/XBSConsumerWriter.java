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
package org.exbin.xbup.core.parser.basic;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.wrapper.FixedDataOutputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TerminatedDataOutputStreamWrapper;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.parser.token.convert.XBListenerToToken;
import org.exbin.xbup.core.parser.token.convert.XBTokenBuffer;
import org.exbin.xbup.core.stream.FinishableStream;
import org.exbin.xbup.core.ubnumber.type.UBENat32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * XBUP level 0 consumer writer with support for precomputed blocks.
 *
 * @version 0.1.25 2015/08/10
 * @author ExBin Project (http://exbin.org)
 */
public class XBSConsumerWriter implements Closeable, XBConsumer {

    private XBParserMode parserMode = XBParserMode.FULL;

    private OutputStream stream;
    private XBProvider provider;
    private XBListenerToToken tokenListener;

    public XBSConsumerWriter() {
        tokenListener = new XBListenerToToken();
    }

    public XBSConsumerWriter(OutputStream outputStream) throws IOException {
        this();
        openStream(outputStream);
    }

    public XBSConsumerWriter(OutputStream outputStream, XBParserMode parserMode) throws IOException {
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

    public void write() throws XBProcessingException, IOException {
        List<Integer> sizeLimits = new ArrayList<>();
        XBTokenBuffer tokenBuffer = new XBTokenBuffer();
        List<XBAttribute> attributeList = new ArrayList<>();
        XBBlockDataMode dataMode = null;
        int bufferedFromLevel = -1;
        int depthLevel = 0;
        // TODO UBNatural blockSize = null;

        // Write file head
        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
            XBHead.writeXBUPHead(stream);
        }

        XBToken token = pullToken();
        do {
            switch (token.getTokenType()) {
                case BEGIN: {
                    depthLevel++;
                    // TODO blockSize = null;
                    dataMode = null;
                    XBBlockTerminationMode terminationMode = ((XBBeginToken) token).getTerminationMode();
                    if (bufferedFromLevel >= 0) {
                        tokenBuffer.putXBToken(token);
                        sizeLimits.add(null);
                    } else {
                        if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            /* TODO if (token instanceof XBSBeginToken && ((XBSBeginToken) token).getBlockSize() != null) {
                                blockSize = ((XBSBeginToken) token).getBlockSize();
                                sizeLimits.add(blockSize.getInt());
                            } else { */
                                bufferedFromLevel = depthLevel;
                                tokenBuffer.putXBToken(token);
                                sizeLimits.add(null);
                            // }
                        } else {
                            sizeLimits.add(null);
                        }
                    }

                    token = pullToken();
                    switch (token.getTokenType()) {
                        case DATA: {
                            dataMode = XBBlockDataMode.DATA_BLOCK;
                            if (bufferedFromLevel >= 0) {
                                tokenBuffer.putXBToken(token);
                            } else {
                                OutputStream streamWrapper;
                                InputStream data = ((XBDataToken) token).getData();
                                if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                                    streamWrapper = new FixedDataOutputStreamWrapper(stream, 0);
                                    StreamUtils.copyInputStreamToOutputStream(data, streamWrapper);
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

                                    StreamUtils.copyInputStreamToOutputStream(data, streamWrapper);
                                    int dataSize = (int) ((FinishableStream) streamWrapper).finish();
                                    shrinkStatus(sizeLimits, dataSize);
                                }
                            }

                            token = pullToken();

                            if (depthLevel == 1 && token.getTokenType() == XBTokenType.DATA) {
                                if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL) {
                                    if (bufferedFromLevel >= 0) {
                                        tokenBuffer.putXBToken(XBEndToken.create());
                                        if (bufferedFromLevel == depthLevel) {
                                            tokenBuffer.write(stream);
                                            bufferedFromLevel = -1;
                                        }
                                    }

                                    depthLevel--;
                                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), stream);
                                    token = pullToken();
                                    if (token.getTokenType() != XBTokenType.END) {
                                        throw new XBParseException("End token was expected after tail data", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                    }
                                } else {
                                    throw new XBParseException("Tail data present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                }
                            }

                            if (token.getTokenType() != XBTokenType.END) {
                                throw new XBParseException("Data block must be followed by block end", XBProcessingExceptionType.UNEXPECTED_ORDER);
                            }

                            break;
                        }

                        case ATTRIBUTE: {
                            attributeList.clear();
                            int attributePartSizeValue = 0;
                            do {
                                XBAttribute attribute = ((XBAttributeToken) token).getAttribute();
                                dataMode = XBBlockDataMode.NODE_BLOCK;

                                if (bufferedFromLevel >= 0) {
                                    tokenBuffer.putXBToken(token);
                                } else {
                                    int attributePartSize = attribute.getSizeUB();
                                    shrinkStatus(sizeLimits, attributePartSize);
                                    attributePartSizeValue += attributePartSize;
                                    attributeList.add(attribute);
                                }

                                token = pullToken();
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
                                if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL) {
                                    if (bufferedFromLevel >= 0) {
                                        tokenBuffer.putXBToken(XBEndToken.create());
                                        if (bufferedFromLevel == depthLevel) {
                                            tokenBuffer.write(stream);
                                            bufferedFromLevel = -1;
                                        }
                                    } else {
                                        if (dataMode == XBBlockDataMode.NODE_BLOCK && sizeLimits.get(depthLevel - 1) == null) {
                                            stream.write(0);
                                        }
                                        decreaseStatus(sizeLimits);
                                    }

                                    depthLevel--;
                                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), stream);
                                    token = pullToken();
                                    if (token.getTokenType() != XBTokenType.END) {
                                        throw new XBParseException("End token was expected after tail data", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                    }
                                } else {
                                    throw new XBParseException("Tail data present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
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
                        if (dataMode == XBBlockDataMode.NODE_BLOCK && sizeLimits.get(depthLevel - 1) == null) {
                            stream.write(0);
                        }
                        decreaseStatus(sizeLimits);
                    }

                    dataMode = XBBlockDataMode.NODE_BLOCK;
                    depthLevel--;
                    if (depthLevel > 0) {
                        token = pullToken();
                    }

                    if (depthLevel == 1 && token.getTokenType() == XBTokenType.DATA) {
                        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL) {
                            if (bufferedFromLevel >= 0) {
                                tokenBuffer.putXBToken(XBEndToken.create());
                                if (bufferedFromLevel == depthLevel) {
                                    tokenBuffer.write(stream);
                                    bufferedFromLevel = -1;
                                }
                            } else {
                                if (dataMode == XBBlockDataMode.NODE_BLOCK && sizeLimits.get(depthLevel - 1) == null) {
                                    stream.write(0);
                                }
                                decreaseStatus(sizeLimits);
                            }

                            dataMode = XBBlockDataMode.NODE_BLOCK;
                            depthLevel--;
                            StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), stream);
                            token = pullToken();
                            if (token.getTokenType() != XBTokenType.END) {
                                throw new XBParseException("End token was expected after tail data", XBProcessingExceptionType.UNEXPECTED_ORDER);
                            }
                        } else {
                            throw new XBParseException("Tail data present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                        }
                    }

                    break;
                }

                default:
                    throw new XBParseException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        } while (depthLevel > 0);
    }

    private XBToken pullToken() throws XBProcessingException, IOException {
        tokenListener.setToken(null);
        provider.produceXB(tokenListener);
        if (tokenListener.getToken() == null) {
            throw new XBParseException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }

        return tokenListener.getToken();
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    /**
     * Shrinks limits accross all depths.
     *
     * @param sizeLimits block sizes
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
    public void attachXBProvider(XBProvider provider) {
        this.provider = provider;
    }
}
