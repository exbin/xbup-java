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
package org.exbin.xbup.core.serial.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProducer;
import org.exbin.xbup.core.parser.basic.convert.XBTConsumerToListener;
import org.exbin.xbup.core.parser.param.XBParamProcessingState;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBEditableAttribute;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.convert.XBTCompactingEventFilter;
import org.exbin.xbup.core.parser.token.event.convert.XBTListenerToEventListener;
import org.exbin.xbup.core.parser.token.pull.XBTPullConsumer;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullConsumerToConsumer;
import org.exbin.xbup.core.serial.XBSerialException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.basic.XBTBasicOutputSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicSerializable;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.serial.sequence.XBListConsistSerializable;
import org.exbin.xbup.core.serial.sequence.XBListJoinSerializable;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceItem;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceOp;
import org.exbin.xbup.core.serial.token.XBTTokenOutputSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.ubnumber.UBENatural;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization handler using parameter mapping to listener.
 *
 * @version 0.2.1 2020/09/03
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPListenerSerialHandler implements XBPOutputSerialHandler, XBPSequenceSerialHandler, XBTTokenOutputSerialHandler {

    private XBTCompactingEventFilter eventListener;

    private XBParamProcessingState processingState = XBParamProcessingState.START;
    private final List<ProcessingState> processingStates = new ArrayList<>();
    private final List<JoinState> joinStates = new ArrayList<>();
    private int subDepth = 0;

    private static final String PULL_NOT_ALLOWED_EXCEPTION = "Pulling data not allowed in pushing mode";

    private XBTChildOutputSerialHandlerImpl childOutputSerialHandler = null;
    private XBTBasicOutputSerialHandlerImpl basicOutputSerialHandler = null;

    public XBPListenerSerialHandler() {
        processingStates.add(new ProcessingState());
    }

    public XBPListenerSerialHandler(XBInput input) {
        this();
        performAttachXBInput(input);
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeToXB(this);
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(this);
        } else if (serial instanceof XBTChildSerializable) {
            if (childOutputSerialHandler == null) {
                childOutputSerialHandler = new XBTChildOutputSerialHandlerImpl();
            }
            ((XBTChildSerializable) serial).serializeToXB(childOutputSerialHandler);
        } else if (serial instanceof XBTBasicSerializable) {
            if (basicOutputSerialHandler == null) {
                basicOutputSerialHandler = new XBTBasicOutputSerialHandlerImpl();
            }
            ((XBTBasicSerializable) serial).serializeToXB(basicOutputSerialHandler);
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        attachXBInput(listener);
    }

    public void attachXBInput(XBInput input) {
        performAttachXBInput(input);
    }

    private void performAttachXBInput(XBInput input) {
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

    private void attachListener(XBTEventListener eventListener) {
        if (eventListener instanceof XBTCompactingEventFilter) {
            this.eventListener = (XBTCompactingEventFilter) eventListener;
        } else {
            this.eventListener = new XBTCompactingEventFilter(eventListener);
        }
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting()) {
            if (subDepth == 0 && !joinStates.isEmpty()) {
                JoinState joinState = joinStates.get(joinStates.size() - 1);
                if (joinState == JoinState.START) {
                    joinStates.set(joinStates.size() - 1, JoinState.BEGIN_SKIPPED);
                    return;
                }
            }

            subDepth++;
            currentState.serialSequence.putBegin(terminationMode);
            return;
        } else if (currentState.isPassing()) {
            eventListener.putXBTToken(XBTBeginToken.create(terminationMode));
            return;
        }

        switch (processingState) {
            case START: {
                eventListener.putXBTToken(XBTBeginToken.create(terminationMode));
                processingState = XBParamProcessingState.BEGIN;
                break;
            }

            case TYPE:
            case ATTRIBUTES:
            case CHILDREN: {
                if (currentState.mode == Mode.BEGIN) {
                    currentState.mode = Mode.COLLECTING;
                    subDepth++;
                    currentState.serialSequence.putBegin(terminationMode);
                } else {
                    eventListener.putXBTToken(XBTBeginToken.create(terminationMode));
                    processingState = XBParamProcessingState.CHILDREN;
                }
                break;
            }
            default: {
                throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting()) {
            if (subDepth > 0) {
                currentState.serialSequence.putType(type);
                return;
            }

            if (!joinStates.isEmpty()) {
                JoinState joinState = joinStates.get(joinStates.size() - 1);
                if (joinState == JoinState.BEGIN_SKIPPED) {
                    joinStates.set(joinStates.size() - 1, JoinState.TYPE_SKIPPED);
                    return;
                }
            }
        } else if (currentState.isPassing()) {
            eventListener.putXBTToken(XBTTypeToken.create(type));
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN) {
            eventListener.putXBTToken(XBTTypeToken.create(type));
            processingState = XBParamProcessingState.TYPE;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(XBAttribute attribute) throws XBSerialException, XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            currentState.serialSequence.putAttribute(attribute);
            return;
        } else if (currentState.isPassing()) {
            eventListener.putXBTToken(XBTAttributeToken.create(attribute));
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN || processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
            eventListener.putXBTToken(XBTAttributeToken.create(attribute));
            processingState = XBParamProcessingState.ATTRIBUTES;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            currentState.serialSequence.putData(data);
            return;
        } else if (currentState.isPassing()) {
            eventListener.putXBTToken(XBTDataToken.create(data));
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN) {
            eventListener.putXBTToken(XBTDataToken.create(data));
            processingState = XBParamProcessingState.DATA;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting()) {
            if (subDepth > 0) {
                currentState.serialSequence.putEnd();
                subDepth--;
                return;
            }

            if (!joinStates.isEmpty()) {
                JoinState joinState = joinStates.get(joinStates.size() - 1);
                if (joinState == JoinState.TYPE_SKIPPED) {
                    joinStates.remove(joinStates.size() - 1);
                    return;
                }
            }

            if (processingStates.size() > 1) {
                processingState = XBParamProcessingState.END;
                return;
            }

            // TODO optimize for currentState.isInOrder
            // if (currentState.isInOrder) {
            //     currentState.mode = Mode.PASSING;
            // } else { }
            while (!processingStates.isEmpty()) {
                ProcessingState topState = getCurrentState();
                ProcessingState superState = processingStates.size() > 1 ? processingStates.get(processingStates.size() - 2) : null;
                boolean pass = false;
                if (topState.serialSequence.isEmpty()) {
                    eventListener.putXBTToken(XBTEndToken.create());
                    if (superState != null && !superState.serialSequence.isEmpty()) {
                        topState.mode = Mode.BEGIN;
                        topState.serialSequence = new XBPSerialSequence();
                        topState.isInOrder = false;
                        pass = true;
                    } else {
                        processingStates.remove(processingStates.size() - 1);
                    }
                } else {
                    ProcessingState nextState = new ProcessingState();
                    processingStates.add(nextState);
                    pass = true;
                }

                if (pass) {
                    processingState = XBParamProcessingState.START;
                    while (!topState.serialSequence.isEmpty() && processingState != XBParamProcessingState.END) {
                        putItem(topState.serialSequence.pullItem());
                    }
                }
            }
        } else if (currentState.isPassing()) {
            eventListener.putXBTToken(XBTEndToken.create());
            return;
        }

        if (processingState != XBParamProcessingState.BEGIN && processingState != XBParamProcessingState.START) {
            eventListener.putXBTToken(XBTEndToken.create());
            processingState = XBParamProcessingState.END;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            if (token.getTokenType() == XBTTokenType.END) {
                putEnd();
                return;
            }
            currentState.serialSequence.putToken(token);
            return;
        }

        switch (token.getTokenType()) {
            case BEGIN: {
                putBegin(((XBTBeginToken) token).getTerminationMode());
                break;
            }
            case TYPE: {
                putType(((XBTTypeToken) token).getBlockType());
                break;
            }
            case ATTRIBUTE: {
                putAttribute(((XBTAttributeToken) token).getAttribute());
                break;
            }
            case DATA: {
                putData(((XBTDataToken) token).getData());
                break;
            }
            case END: {
                putEnd();
                break;
            }
        }
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting()) {
            currentState.serialSequence.putConsist(serial);
            return;
        } else if (currentState.isPassing()) {
            process(serial);
            return;
        }

        currentState.mode = Mode.COLLECTING;
        currentState.serialSequence.putConsist(serial);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting()) {
            if (subDepth > 0) {
                // TODO optimize
                currentState.isInOrder = false;
                currentState.serialSequence.putJoin(serial);
            } else {
                joinStates.add(JoinState.START);
                process(serial);
            }
            return;
        } else if (currentState.isPassing()) {
            process(serial);
            return;
        }

        currentState.mode = Mode.COLLECTING;
        if (subDepth > 0) {
            // TODO optimize
            currentState.isInOrder = false;
            currentState.serialSequence.putJoin(serial);
        } else {
            joinStates.add(JoinState.START);
            process(serial);
        }
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting()) {
            if (subDepth > 0) {
                currentState.serialSequence.putListConsist(serial);
                return;
            } else {
                XBListConsistSerializable list = (XBListConsistSerializable) serial;
                UBENatural count = list.getSize();
                putAttribute(count.convertToNatural());

                list.reset();
                int listSize = count.getInt();
                for (int i = 0; i < listSize; i++) {
                    currentState.serialSequence.putConsist(list.next());
                }
                return;
            }
        } else if (currentState.isPassing()) {
            XBListConsistSerializable list = (XBListConsistSerializable) serial;
            UBENatural count = list.getSize();
            eventListener.putXBTToken(XBTAttributeToken.create(count.convertToNatural()));

            currentState.mode = Mode.COLLECTING;
            list.reset();
            int listSize = count.getInt();
            for (int i = 0; i < listSize; i++) {
                process(list.next());
            }

            return;
        }

        XBListConsistSerializable list = (XBListConsistSerializable) serial;
        UBENatural count = list.getSize();
        putAttribute(count.convertToNatural());

        currentState.mode = Mode.COLLECTING;
        list.reset();
        int listSize = count.getInt();
        for (int i = 0; i < listSize; i++) {
            currentState.serialSequence.putConsist(list.next());
        }
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            currentState.serialSequence.putListJoin(serial);
            return;
        }

        XBListJoinSerializable list = (XBListJoinSerializable) serial;
        UBNatural count = list.getSize();
        putAttribute(count);

        list.reset();
        int listSize = count.getInt();
        for (int i = 0; i < listSize; i++) {
            putJoin(list.next());
        }
    }

    @Override
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (currentState.isCollecting() && subDepth > 0) {
            if (item.getSequenceOp() == XBSerialSequenceOp.TOKEN && ((XBPTokenWrapper) item.getItem()).getToken().getTokenType() == XBTTokenType.END) {
                putEnd();
                return;
            } else if (item.getSequenceOp() == XBSerialSequenceOp.JOIN || item.getSequenceOp() == XBSerialSequenceOp.LIST_JOIN) {
                currentState.isInOrder = false;
            }
            currentState.serialSequence.putItem(item);
            return;
        }

        switch (item.getSequenceOp()) {
            case TOKEN: {
                putToken(((XBPTokenWrapper) item.getItem()).getToken());
                break;
            }

            case CONSIST: {
                putConsist(item.getItem());
                break;
            }

            case JOIN: {
                putJoin(item.getItem());
                break;
            }

            case LIST_CONSIST: {
                putListConsist(item.getItem());
                break;
            }

            case LIST_JOIN: {
                putListJoin(item.getItem());
                break;
            }

            default:
                throw new IllegalStateException();
        }
    }

    @Nonnull
    private ProcessingState getCurrentState() {
        return processingStates.get(processingStates.size() - 1);
    }

    @Override
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        process(serial);
    }

    @Nonnull
    @Override
    public XBSerializationMode getSerializationMode() {
        return XBSerializationMode.PUSH;
    }

    @Override
    public void begin() throws XBProcessingException, IOException {
        putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
    }

    @Override
    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException {
        putType(blockType);
    }

    @Override
    public void matchType() throws XBProcessingException, IOException {
        matchType(XBFixedBlockType.UNKNOWN_BLOCK_TYPE);
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        putEnd();
    }

    @Override
    public void attribute(XBEditableAttribute attributeValue) throws XBProcessingException, IOException {
        putAttribute(attributeValue);
    }

    @Override
    public void consist(XBSerializable serial) throws XBProcessingException, IOException {
        putConsist(serial);
    }

    @Override
    public void join(XBSerializable serial) throws XBProcessingException, IOException {
        putJoin(serial);
    }

    @Override
    public void listConsist(XBSerializable serial) throws XBProcessingException, IOException {
        putListConsist(serial);
    }

    @Override
    public void listJoin(XBSerializable serial) throws XBProcessingException, IOException {
        putListJoin(serial);
    }

    @Override
    public void append(XBSerializable serial) throws XBProcessingException, IOException {
        putAppend(serial);
    }

    @Nonnull
    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Nonnull
    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Nonnull
    @Override
    public UBNatural pullAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Nonnull
    @Override
    public InputStream pullData() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public boolean pullIfEmptyData() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public boolean pullIfEmptyBlock() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Nonnull
    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Nonnull
    @Override
    public XBTToken pullToken() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullConsist(XBSerializable child) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullListConsist(XBSerializable child) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @ParametersAreNonnullByDefault
    public static class XBPSerialSequence implements XBPListener {

        private final List<XBSerialSequenceItem> items = new ArrayList<>();
        private int depth = 0;

        public XBPSerialSequence() {
        }

        private void checkStarted() {
            if (depth == 0) {
                throw new XBProcessingException("Unexpected serialization event when sequencing", XBProcessingExceptionType.WRITING_AFTER_END);
            }
        }

        @Override
        public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            depth++;
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTBeginToken.create(terminationMode))));
        }

        @Override
        public void putType(XBBlockType type) throws XBProcessingException, IOException {
            checkStarted();
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTTypeToken.create(type))));
        }

        @Override
        public void putAttribute(XBAttribute attribute) throws XBProcessingException, IOException {
            checkStarted();
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTAttributeToken.create(attribute))));
        }

        @Override
        public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
            checkStarted();
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
            checkStarted();
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
            checkStarted();
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
            checkStarted();
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putData(InputStream data) throws XBProcessingException, IOException {
            checkStarted();
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTDataToken.create(data))));
        }

        @Override
        public void putEnd() throws XBProcessingException, IOException {
            checkStarted();
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTEndToken.create())));
            depth--;
        }

        @Override
        public void putToken(XBTToken token) throws XBProcessingException, IOException {
            putItem(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(token)));
        }

        @Override
        public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.CONSIST, serial));
        }

        @Override
        public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.JOIN, serial));
        }

        @Override
        public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_CONSIST, serial));
        }

        @Override
        public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_JOIN, serial));
        }

        @Override
        public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
            if (item.getSequenceOp() == XBSerialSequenceOp.TOKEN) {
                switch (((XBPTokenWrapper) item.getItem()).getToken().getTokenType()) {
                    case BEGIN: {
                        depth++;
                        break;
                    }
                    case END: {
                        checkStarted();
                        depth--;
                        break;
                    }
                    default: {
                        checkStarted();
                    }
                }

                items.add(item);
            } else {
                checkStarted();
                items.add(item);
            }
        }

        @Override
        public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
            throw new IllegalStateException("Append is not allowed on sequencing");
        }

        public XBSerialSequenceItem pullItem() {
            return items.remove(0);
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }

        public int getDepth() {
            return depth;
        }

        public boolean isClosed() {
            return depth == 0;
        }
    }

    @ParametersAreNonnullByDefault
    private class XBTChildOutputSerialHandlerImpl implements XBTChildOutputSerialHandler {

        public XBTChildOutputSerialHandlerImpl() {
        }

        @Override
        public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putBegin(terminationMode);
        }

        @Override
        public void putType(XBBlockType type) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putType(type);
        }

        @Override
        public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putAttribute(attribute);
        }

        @Override
        public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putAttribute(attributeValue);
        }

        @Override
        public void putChild(XBSerializable child) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putConsist(child);
        }

        @Override
        public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.process(serial);
        }

        @Override
        public void putData(InputStream data) throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putData(data);
        }

        @Override
        public void putEnd() throws XBProcessingException, IOException {
            XBPListenerSerialHandler.this.putEnd();
        }

        @Override
        public void attachXBTEventListener(XBTEventListener listener) {
            throw new IllegalStateException();
        }
    }

    @ParametersAreNonnullByDefault
    private class XBTBasicOutputSerialHandlerImpl implements XBTBasicOutputSerialHandler {

        public XBTBasicOutputSerialHandlerImpl() {
        }

        @Override
        public void process(XBTProducer producer) {
            producer.attachXBTListener(new XBTListener() {

                @Override
                public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                    putBegin(terminationMode);
                }

                @Override
                public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
                    putType(blockType);
                }

                @Override
                public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException {
                    putAttribute(attribute);
                }

                @Override
                public void dataXBT(InputStream data) throws XBProcessingException, IOException {
                    putData(data);
                }

                @Override
                public void endXBT() throws XBProcessingException, IOException {
                    putEnd();
                }
            });
        }
    }

    private static class ProcessingState {

        Mode mode = Mode.BEGIN;
        XBPSerialSequence serialSequence = new XBPSerialSequence();
        // TODO optimize
        boolean isInOrder = false;

        private boolean isCollecting() {
            return mode == Mode.COLLECTING;
        }

        private boolean isPassing() {
            return mode == Mode.PASSING;
        }
    }

    private enum JoinState {
        START, BEGIN_SKIPPED, TYPE_SKIPPED
    }

    private enum Mode {
        BEGIN, COLLECTING, PASSING
    }
}
