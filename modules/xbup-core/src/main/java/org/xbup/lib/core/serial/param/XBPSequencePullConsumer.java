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
import java.util.List;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.param.XBParamProcessingState;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.pull.XBTPullConsumer;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullPreLoader;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * Level 2 pull consumer performing block building using sequence operations.
 *
 * @version 0.1.24 2015/01/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSequencePullConsumer implements XBTPullConsumer {

    private XBTPullPreLoader pullProvider;
    private List<XBSerializable> childSequence = new ArrayList<>();
    private XBParamProcessingState processingState = XBParamProcessingState.START;

    public XBPSequencePullConsumer(XBTPullProvider pullProvider) {
        attachProvider(pullProvider);
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
                if (processingState != XBParamProcessingState.START) {
                    throw new XBProcessingException("Begin token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                XBTToken token = pullProvider.pullXBTToken();
                if (token.getTokenType() != XBTTokenType.BEGIN) {
                    throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                processingState = XBParamProcessingState.BEGIN;
                return token;
            }
            case TYPE: {
                if (processingState != XBParamProcessingState.BEGIN) {
                    throw new XBProcessingException("Begin token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                XBTToken token = pullProvider.pullXBTToken();
                if (token.getTokenType() != XBTTokenType.TYPE) {
                    throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                processingState = XBParamProcessingState.TYPE;
                return token;
            }
            case ATTRIBUTE: {
                if (processingState != XBParamProcessingState.BEGIN && processingState != XBParamProcessingState.ATTRIBUTES) {
                    throw new XBProcessingException("Begin token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
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
                    throw new XBProcessingException("Begin token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                XBTToken token = pullProvider.pullXBTToken();
                if (token.getTokenType() != XBTTokenType.DATA) {
                    throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                processingState = XBParamProcessingState.DATA;
                return token;
            }
            case END: {
                processingState = XBParamProcessingState.END;
                return new XBTEndToken();
            }
        }
        
        throw new IllegalStateException();
    }

    public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
        if (processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
            childSequence.add(child);
        } else {
            throw new XBProcessingException("Unexpected child event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    public boolean isFinished() {
        return processingState == XBParamProcessingState.END;
    }

    public List<XBSerializable> getChildSequence() {
        return childSequence;
    }

    public void resetSequence() {
        childSequence = new ArrayList<>();
        processingState = XBParamProcessingState.START;
    }
}
