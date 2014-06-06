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
package org.xbup.lib.xb.parser.basic;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.XBParserMode;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.parser.basic.wrapper.FixedDataOutputStreamWrapper;
import org.xbup.lib.xb.parser.basic.wrapper.OutputStreamWrapper;
import org.xbup.lib.xb.parser.basic.wrapper.TerminatedDataOutputStreamWrapper;
import org.xbup.lib.xb.parser.token.convert.XBTokenWriter;
import org.xbup.lib.xb.parser.token.XBAttributeToken;
import org.xbup.lib.xb.parser.token.XBBeginToken;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.XBDataToken;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.parser.token.XBTokenType;
import org.xbup.lib.xb.parser.token.convert.XBListenerToToken;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBENat32;
import org.xbup.lib.xb.ubnumber.type.UBNat32;
import org.xbup.lib.xb.util.CopyStreamUtils;

/**
 * XBUP level 0 consumer writer.
 *
 * @version 0.1 wr23.0 2014/02/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBConsumerWriter implements Closeable, XBConsumer {

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
    
    /** Open input byte-stream. */
    private void openStream(OutputStream outputStream) throws IOException {
        stream = outputStream;
    }

    public void open(OutputStream outputStream) throws IOException {
        openStream(outputStream);
    }
    
    public void write() throws XBProcessingException, IOException {
        List<Integer> sizeLimits = new ArrayList<>();
        XBTokenWriter tokenWriter = new XBTokenWriter();
        XBDataToken extendedArea = null;
        List<UBNatural> attributeList = new ArrayList<>();
        int bufferedFromLevel = -1;
        int level = 0;

        // Write file head
        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
            XBHead.writeXBUPHead(stream);
        }
        
        XBToken token = pullToken();
        do {
            switch (token.getTokenType()) {
                case BEGIN: {
                    level++;
                    XBBlockTerminationMode terminationMode = ((XBBeginToken) token).getTerminationMode();
                    if (bufferedFromLevel >= 0) {
                        tokenWriter.putXBToken(token);
                        sizeLimits.add(null);
                    } else {
                        if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            bufferedFromLevel = level;
                            tokenWriter.putXBToken(token);
                            sizeLimits.add(null);
                        } else {
                            sizeLimits.add(null);
                        }
                    }

                    token = pullToken();
                    switch (token.getTokenType()) {
                        case DATA: {
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

                                CopyStreamUtils.copyInputStreamToOutputStream(((XBDataToken)token).getData(), streamWrapper);

                                int dataPartSize = ((OutputStreamWrapper) streamWrapper).finish();
                                shrinkStatus(sizeLimits, dataPartSize);
                            }

                            token = pullToken();
                            if (level == 1 && token.getTokenType() == XBTokenType.DATA) {
                                extendedArea = (XBDataToken) token;
                                token = pullToken();
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
                                UBNatural attribute = ((XBAttributeToken) token).getAttribute();

                                if (bufferedFromLevel >= 0) {
                                    tokenWriter.putXBToken(token);
                                } else {
                                    int attributeSize = attribute.getSizeUB();
                                    shrinkStatus(sizeLimits, attributeSize);
                                    attributePartSizeValue += attributeSize;
                                    attributeList.add(attribute);
                                }

                                token = pullToken();
                            } while (token.getTokenType() == XBTokenType.ATTRIBUTE);

                            if (token.getTokenType() == XBTokenType.DATA) {
                                if (level == 1) {
                                    extendedArea = (XBDataToken) token;
                                    token = pullToken();
                                } else {
                                    throw new XBParseException("Unexpected DATA token after attribute token(s)", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                }
                            }
                            
                            if (bufferedFromLevel < 0) {
                                attributePartSizeValue += UBENat32.INFINITY_SIZE_UB;
                                UBNat32 attributePartSize = new UBNat32(attributePartSizeValue);
                                shrinkStatus(sizeLimits, attributePartSize.getSizeUB() + UBENat32.INFINITY_SIZE_UB + 1);
                                attributePartSize.toStreamUB(stream);
                                UBENat32 dataPartSize = new UBENat32();
                                dataPartSize.setInfinity();
                                dataPartSize.toStreamUB(stream);
                                
                                for (UBNatural attribute : attributeList) {
                                    attribute.toStreamUB(stream);
                                }

                                stream.write(0);
                                attributeList.clear();
                            }

                            break;
                        }

                        default: throw new XBParseException("Missing at least one atribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    break;
                }

                case END: {
                    if (bufferedFromLevel >= 0) {
                        tokenWriter.putXBToken(token);
                        if (bufferedFromLevel == level) {
                            tokenWriter.write(stream);
                            bufferedFromLevel = -1;
                        }
                    }

                    level--;
                    if (level > 0) {
                        token = pullToken();

                        if (level == 1 && token.getTokenType() == XBTokenType.DATA) {
                            extendedArea = (XBDataToken) token;
                            token = pullToken();
                        }
                    }

                    break;
                }

                default: throw new XBParseException("Must begin with NodeBegin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        } while (level > 0);
        
        // Write extended block if present
        if (extendedArea != null) {
            if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                CopyStreamUtils.copyInputStreamToOutputStream(extendedArea.getData(), stream);
            } else {
                throw new XBParseException("Extended data block present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
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
    public void attachXBProvider(XBProvider provider) {
        this.provider = provider;
    }
}
