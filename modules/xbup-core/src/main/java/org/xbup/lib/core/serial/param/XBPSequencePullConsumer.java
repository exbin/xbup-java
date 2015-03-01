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
package org.xbup.lib.core.serial.param;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.basic.convert.XBTProducerToProvider;
import org.xbup.lib.core.parser.param.XBParamProcessingState;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEmptyDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTZeroAttributeToken;
import org.xbup.lib.core.parser.token.event.XBTEventProducer;
import org.xbup.lib.core.parser.token.event.convert.XBTEventProducerToProducer;
import org.xbup.lib.core.parser.token.pull.XBTPullConsumer;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullPreLoader;
import org.xbup.lib.core.stream.XBOutput;

/**
 * Level 2 pull consumer performing block building using sequence operations.
 *
 * @version 0.1.25 2015/03/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSequencePullConsumer implements XBTPullConsumer {

    private XBTPullPreLoader pullProvider;
    private final List<List<XBTAttributeToken>> attributeSequences = new ArrayList<>();
    private List<XBTAttributeToken> attributeSequence = new LinkedList<>();
    private XBParamProcessingState processingState = XBParamProcessingState.START;
    private boolean emptyNodeMode = false;

    public XBPSequencePullConsumer(XBTPullProvider pullProvider) {
        attachProvider(pullProvider);
    }

    public XBPSequencePullConsumer(XBOutput output) {
        if (output instanceof XBTPullProvider) {
            attachProvider((XBTPullProvider) output);
        } else if (output instanceof XBTProvider) {
            attachProvider(new XBTProviderToPullProvider((XBTProvider) output));
        } else if (output instanceof XBTEventProducer) {
            attachProvider(new XBTProviderToPullProvider(new XBTProducerToProvider(new XBTEventProducerToProducer((XBTEventProducer) output))));
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        attachProvider(pullProvider);
    }

    private void attachProvider(XBTPullProvider pullProvider) {
        if (pullProvider instanceof XBTPullPreLoader) {
            this.pullProvider = (XBTPullPreLoader) pullProvider;
        } else {
            this.pullProvider = new XBTPullPreLoader(pullProvider);
        }
    }

    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        switch (tokenType) {
            case BEGIN: {
                if (processingState == XBParamProcessingState.DATA || processingState == XBParamProcessingState.BEGIN) {
                    throw new XBProcessingException("Begin token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                if (processingState == XBParamProcessingState.ATTRIBUTES) {
                    processAttributes();
                    attributeSequences.add(attributeSequence);
                    attributeSequence = new LinkedList<>();
                }

                XBTToken token = pullProvider.pullXBTToken();
                processingState = XBParamProcessingState.BEGIN;
                if (token.getTokenType() == XBTTokenType.END) {
                    emptyNodeMode = true;
                    return new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED);
                } else if (token.getTokenType() != XBTTokenType.BEGIN) {
                    throw new XBProcessingException("Unexpected token type " + token.getTokenType(), XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                return token;
            }
            case TYPE: {
                if (processingState != XBParamProcessingState.BEGIN && !emptyNodeMode) {
                    throw new XBProcessingException("Type token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                XBTToken token = pullProvider.pullXBTToken();
                if (token.getTokenType() != XBTTokenType.TYPE) {
                    throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                processingState = XBParamProcessingState.TYPE;
                return token;
            }
            case ATTRIBUTE: {
                if (processingState == XBParamProcessingState.DATA || processingState == XBParamProcessingState.START || emptyNodeMode) {
                    throw new XBProcessingException("Attribute token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                if (pullProvider.getNextTokenType() != XBTTokenType.ATTRIBUTE) {
                    processingState = XBParamProcessingState.ATTRIBUTES;
                    if (!attributeSequence.isEmpty()) {
                        return attributeSequence.remove(0);
                    } else {
                        return new XBTZeroAttributeToken();
                    }
                }

                XBTToken token = pullProvider.pullXBTToken();
                if (token.getTokenType() != XBTTokenType.ATTRIBUTE) {
                    throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                processingState = XBParamProcessingState.ATTRIBUTES;
                return token;
            }
            case DATA: {
                if (processingState != XBParamProcessingState.BEGIN) {
                    throw new XBProcessingException("Data token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                processingState = XBParamProcessingState.DATA;
                if (emptyNodeMode) {
                    return new XBTEmptyDataToken();
                }

                XBTToken token = pullProvider.pullXBTToken();
                if (token.getTokenType() != XBTTokenType.DATA) {
                    throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                return token;
            }
            case END: {
                if (processingState == XBParamProcessingState.BEGIN) {
                    throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                processingState = XBParamProcessingState.END;
                if (emptyNodeMode) {
                    emptyNodeMode = false;
                } else if (!attributeSequences.isEmpty()) {
                    attributeSequence = attributeSequences.remove(attributeSequences.size() - 1);
                }

                return new XBTEndToken();
            }
        }

        throw new IllegalStateException();
    }

    public boolean pullIfEmpty() throws XBProcessingException, IOException {
        if (processingState != XBParamProcessingState.BEGIN) {
            throw new XBProcessingException("Empty data token test out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        processingState = XBParamProcessingState.BEGIN;
        XBTToken nextToken = pullProvider.getNextToken();
        if (nextToken != null && nextToken.getTokenType() == XBTTokenType.DATA) {
            if (((XBTDataToken) nextToken).isEmpty()) {
                pullProvider.pullXBTToken();
                processingState = XBParamProcessingState.DATA;
                return true;
            }
        }

        return false;
    }

    public void processAttributes() throws XBProcessingException, IOException {
        while (pullProvider.getNextTokenType() == XBTTokenType.ATTRIBUTE) {
            attributeSequence.add((XBTAttributeToken) pullProvider.pullXBTToken());
        }
    }

    /**
     * Returns true if current block was processed.
     *
     * @return true if block processed
     */
    public boolean isFinished() {
        return processingState == XBParamProcessingState.END;
    }

    /**
     * Returns true if block will be finished with next token.
     *
     * @return true if block will be finished next
     */
    public boolean isFinishedNext() {
        return pullProvider.getNextTokenType() == XBTTokenType.END;
    }

    public List<XBTAttributeToken> getAttributeSequence() {
        return attributeSequence;
    }

    public void resetSequence() {
        attributeSequence = new LinkedList<>();
        processingState = XBParamProcessingState.START;
    }

    public void pullRest() throws XBProcessingException, IOException {
        pullProvider.skipAttributes();
        pullProvider.skipChildren();
        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.END) {
            throw new XBProcessingException("End token was expected, but " + token.getTokenType().name() + " token was received", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }
}
