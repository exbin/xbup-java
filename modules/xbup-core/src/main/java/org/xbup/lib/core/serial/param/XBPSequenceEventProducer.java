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
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.convert.XBTConsumerToListener;
import org.xbup.lib.core.parser.param.XBParamProcessingState;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.XBTEventProducer;
import org.xbup.lib.core.parser.token.event.convert.XBTCompactingEventFilter;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.parser.token.pull.XBTPullConsumer;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullConsumerToConsumer;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.stream.XBInput;

/**
 * Level 2 event producer performing block building using sequence operations.
 *
 * @version 0.1.25 2015/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSequenceEventProducer implements XBTEventProducer {

    private XBTCompactingEventFilter eventListener;
    private List<XBSerializable> childSequence = new ArrayList<>();
    private final List<List<XBSerializable>> childSequences = new ArrayList<>();
    private XBParamProcessingState processingState = XBParamProcessingState.START;
    private XBSerializable nextChild;

    public XBPSequenceEventProducer() {
    }

    public XBPSequenceEventProducer(XBTEventListener eventListener) {
        attachListener(eventListener);
    }

    public XBPSequenceEventProducer(XBInput input) {
        if (input instanceof XBTEventListener) {
            attachListener((XBTEventListener) input);
        } else if (input instanceof XBTListener) {
            attachListener(new XBTListenerToEventListener((XBTListener) input));
        } else if (input instanceof XBTPullConsumer) {
            attachListener(new XBTListenerToEventListener(new XBTConsumerToListener(new XBTPullConsumerToConsumer((XBTPullConsumer) input))));
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        attachListener(eventListener);
    }

    private void attachListener(XBTEventListener eventListener) {
        if (eventListener instanceof XBTCompactingEventFilter) {
            this.eventListener = (XBTCompactingEventFilter) eventListener;
        } else {
            this.eventListener = new XBTCompactingEventFilter(eventListener);
        }
    }

    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                if (processingState != XBParamProcessingState.BEGIN && processingState != XBParamProcessingState.DATA) {
                    eventListener.putXBTToken(token);
                    processingState = XBParamProcessingState.BEGIN;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
            case TYPE: {
                if (processingState == XBParamProcessingState.BEGIN) {
                    eventListener.putXBTToken(token);
                    processingState = XBParamProcessingState.TYPE;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
            case ATTRIBUTE: {
                if (processingState == XBParamProcessingState.BEGIN || processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
                    eventListener.putXBTToken(token);
                    processingState = XBParamProcessingState.ATTRIBUTES;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
            case DATA: {
                if (processingState == XBParamProcessingState.BEGIN) {
                    eventListener.putXBTToken(token);
                    processingState = XBParamProcessingState.DATA;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
            case END: {
                if (processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES || processingState == XBParamProcessingState.DATA) {
                    processingState = XBParamProcessingState.END;

                    if (childSequence.isEmpty()) {
                        nextChild = null;
                        putEnd();
                        if (!childSequences.isEmpty()) {
                            while (!childSequences.isEmpty()) {
                                childSequence = childSequences.remove(childSequences.size() - 1);
                                if (!childSequence.isEmpty()) {
                                    nextChild = childSequence.remove(0);
                                    break;
                                } else {
                                    putEnd();
                                    if (childSequences.isEmpty()) {
                                        putEnd();
                                    }
                                }
                            }
                        }
                    } else {
                        nextChild = childSequence.remove(0);
                        childSequences.add(childSequence);
                        childSequence = new ArrayList<>();
                        processingState = XBParamProcessingState.START;
                    }
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
        }
    }

    /**
     * Passes end token to source listener.
     *
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putEnd() throws XBProcessingException, IOException {
        eventListener.putXBTToken(new XBTEndToken());
    }

    public void putChild(XBSerializable child) throws XBProcessingException, IOException {
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

    public XBSerializable getNextChild() {
        return nextChild;
    }
}
