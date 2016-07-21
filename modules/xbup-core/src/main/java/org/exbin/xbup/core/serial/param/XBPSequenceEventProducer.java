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
package org.exbin.xbup.core.serial.param;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.convert.XBTConsumerToListener;
import org.exbin.xbup.core.parser.param.XBParamProcessingState;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.XBTEventProducer;
import org.exbin.xbup.core.parser.token.event.convert.XBTCompactingEventFilter;
import org.exbin.xbup.core.parser.token.event.convert.XBTListenerToEventListener;
import org.exbin.xbup.core.parser.token.pull.XBTPullConsumer;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullConsumerToConsumer;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.stream.XBInput;

/**
 * Level 2 event producer performing block building using sequence operations.
 *
 * @version 0.1.25 2015/03/05
 * @author ExBin Project (http://exbin.org)
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
                        while (!childSequences.isEmpty()) {
                            List<XBSerializable> nextSequence = childSequences.get(childSequences.size() - 1);
                            if (!nextSequence.isEmpty()) {
                                nextChild = nextSequence.remove(0);
                                break;
                            } else {
                                putEnd();
                                childSequences.remove(childSequences.size() - 1);
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
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    private void putEnd() throws XBProcessingException, IOException {
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
