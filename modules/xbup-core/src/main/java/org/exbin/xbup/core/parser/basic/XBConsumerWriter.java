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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
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
 * XBUP level 0 consumer writer.
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
public class XBConsumerWriter implements Closeable, XBConsumer {

    @Nonnull
    private XBParserMode parserMode = XBParserMode.FULL;

    private OutputStream stream;
    private XBProvider provider;
    private XBListenerToToken tokenListener;

    public XBConsumerWriter() {
        tokenListener = new XBListenerToToken();
    }

    public XBConsumerWriter(OutputStream outputStream) throws IOException {
        this();
        openStream(outputStream);
    }

    public XBConsumerWriter(OutputStream outputStream, XBParserMode parserMode) throws IOException {
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
        XBTokenBuffer tokenBuffer = new XBTokenBuffer();
        List<XBAttribute> attributeList = new ArrayList<>();
        XBBlockDataMode dataMode = null;
        int bufferedFromLevel = -1;
        int depthLevel = 0;

        // Write file head
        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
            XBHead.writeXBUPHead(stream);
        }

        XBToken token = pullToken();
        do {
            switch (token.getTokenType()) {
                case BEGIN: {
                    depthLevel++;
                    dataMode = null;
                    XBBlockTerminationMode terminationMode = ((XBBeginToken) token).getTerminationMode();
                    if (bufferedFromLevel >= 0) {
                        tokenBuffer.putXBToken(token);
                    } else {
                        if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            bufferedFromLevel = depthLevel;
                            tokenBuffer.putXBToken(token);
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
                                } else {
                                    UBNat32 attributePartSize = new UBNat32(UBENat32.INFINITY_SIZE_UB);
                                    attributePartSize.toStreamUB(stream);
                                    UBENat32 dataPartSize = new UBENat32();
                                    dataPartSize.setInfinity();
                                    dataPartSize.toStreamUB(stream);
                                    streamWrapper = new TerminatedDataOutputStreamWrapper(stream);

                                    StreamUtils.copyInputStreamToOutputStream(data, streamWrapper);
                                    int dataSize = (int) ((FinishableStream) streamWrapper).finish();
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
                                    attributePartSizeValue += attributePartSize;
                                    attributeList.add(attribute);
                                }

                                token = pullToken();
                            } while (token.getTokenType() == XBTokenType.ATTRIBUTE);

                            if (bufferedFromLevel < 0) {
                                attributePartSizeValue += UBENat32.INFINITY_SIZE_UB;
                                UBNat32 attributePartSize = new UBNat32(attributePartSizeValue);
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
                                        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
                                            stream.write(0);
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
                        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
                            stream.write(0);
                        }
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
                                stream.write(0);
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

    @Override
    public void attachXBProvider(XBProvider provider) {
        this.provider = provider;
    }
}
