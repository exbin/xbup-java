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
package org.exbin.xbup.core.parser.token.event;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBParserState;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.parser.basic.wrapper.FixedDataOutputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TerminatedDataOutputStreamWrapper;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.convert.XBTokenBuffer;
import org.exbin.xbup.core.stream.FinishableStream;
import org.exbin.xbup.core.ubnumber.type.UBENat32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Basic XBUP level 0 event writer - listener.
 *
 * @version 0.1.25 2015/08/08
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBEventWriter implements Closeable, XBEventListener {

    private OutputStream stream;
    private XBParserState parserState = XBParserState.START;
    private XBParserMode parserMode = XBParserMode.FULL;

    private final XBTokenBuffer tokenBuffer = new XBTokenBuffer();
    private int bufferedFromLevel = -1;
    private int depthLevel = 0;

    private XBBlockTerminationMode terminationMode;
    private XBBlockDataMode dataMode;
    private final List<XBAttribute> attributeList = new ArrayList<>();
    private int attributePartSizeValue = 0;

    public XBEventWriter() {
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
        if (parserState != XBParserState.EOF && parserState != XBParserState.TAIL_DATA) {
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
                } else if (parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.BLOCK_END) {
                    if (parserState == XBParserState.ATTRIBUTE_PART) {
                        flushAttributes();
                    }
                    parserState = XBParserState.BLOCK_BEGIN;
                }

                if (parserState == XBParserState.BLOCK_BEGIN) {
                    depthLevel++;
                    terminationMode = ((XBBeginToken) token).getTerminationMode();
                    attributePartSizeValue = 0;
                    dataMode = null;
                    if (bufferedFromLevel >= 0) {
                        tokenBuffer.putXBToken(token);
                    } else {
                        if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            bufferedFromLevel = depthLevel;
                            tokenBuffer.putXBToken(token);
                        }
                    }

                    attributeList.clear();
                } else {
                    throw new XBParseException("Unexpected begin of block", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }

            case ATTRIBUTE: {
                if (parserState == XBParserState.BLOCK_BEGIN) {
                    parserState = XBParserState.ATTRIBUTE_PART;
                }
                if (parserState == XBParserState.ATTRIBUTE_PART) {
                    dataMode = XBBlockDataMode.NODE_BLOCK;
                    if (bufferedFromLevel >= 0) {
                        tokenBuffer.putXBToken(token);
                    } else {
                        int attributeSize = ((XBAttributeToken) token).getAttribute().getSizeUB();
                        attributePartSizeValue += attributeSize;
                        attributeList.add(((XBAttributeToken) token).getAttribute());
                    }
                } else {
                    throw new XBParseException("Unexpected attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }

            case DATA: {
                if (depthLevel == 1 && (parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.DATA_PART || parserState == XBParserState.BLOCK_END)) {
                    if (parserMode == XBParserMode.SINGLE_BLOCK || parserMode == XBParserMode.SKIP_TAIL) {
                        throw new XBParseException("Tail data present when not expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                    putXBToken(XBEndToken.create());
                    parserState = XBParserState.TAIL_DATA;
                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), stream);
                } else {
                    if (parserState != XBParserState.BLOCK_BEGIN) {
                        throw new XBParseException("Unexpected data token", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    dataMode = XBBlockDataMode.DATA_BLOCK;
                    if (bufferedFromLevel >= 0) {
                        tokenBuffer.putXBToken(token);
                    } else {
                        OutputStream streamWrapper;
                        if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            streamWrapper = new FixedDataOutputStreamWrapper(stream, 0);
                            StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), streamWrapper);
                            int dataSize = (int) ((FinishableStream) streamWrapper).finish();
                        } else {
                            UBNat32 attributePartSize = new UBNat32(UBENat32.INFINITY_SIZE_UB);
                            attributePartSize.toStreamUB(stream);
                            UBENat32 dataPartSize = new UBENat32();
                            dataPartSize.setInfinity();
                            dataPartSize.toStreamUB(stream);
                            streamWrapper = new TerminatedDataOutputStreamWrapper(stream);

                            StreamUtils.copyInputStreamToOutputStream(((XBDataToken) token).getData(), streamWrapper);
                            int dataSize = (int) ((FinishableStream) streamWrapper).finish();
                        }
                    }
                    parserState = XBParserState.DATA_PART;
                }

                break;
            }

            case END: {
                switch (parserState) {
                    case TAIL_DATA: {
                        parserState = XBParserState.EOF;
                        break;
                    }
                    case ATTRIBUTE_PART: {
                        flushAttributes();
                        // Continue to next case intended
                    }

                    case BLOCK_END:
                    case DATA_PART: {
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

                        dataMode = XBBlockDataMode.NODE_BLOCK;
                        depthLevel--;
                        parserState = (depthLevel == 0) ? XBParserState.EOF : XBParserState.BLOCK_END;
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
            attributePartSize.toStreamUB(stream);
            UBENat32 dataPartSize = new UBENat32();
            dataPartSize.setInfinity();
            dataPartSize.toStreamUB(stream);

            for (XBAttribute attribute : attributeList) {
                attribute.toStreamUB(stream);
            }

            attributeList.clear();
        }
    }
}
