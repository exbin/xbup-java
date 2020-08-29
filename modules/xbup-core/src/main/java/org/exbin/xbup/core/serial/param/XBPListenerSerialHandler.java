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
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProducer;
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
import org.exbin.xbup.core.serial.XBSerialException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.basic.XBTBasicOutputSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicSerializable;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.serial.sequence.XBListConsistSerializable;
import org.exbin.xbup.core.serial.sequence.XBListJoinSerializable;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceItem;
import org.exbin.xbup.core.serial.token.XBTTokenOutputSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.ubnumber.UBENatural;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization handler using parameter mapping to listener.
 *
 * @version 0.2.1 2020/08/29
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPListenerSerialHandler implements XBPOutputSerialHandler, XBPSequenceSerialHandler, XBTTokenOutputSerialHandler {

    private XBPSequenceEventProducer eventListener;

    private final List<ProcessingState> processingStates = new ArrayList<>();
    private int joinDepth = 0;
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
        if (eventListener instanceof XBPSequenceEventProducer) {
            this.eventListener = (XBPSequenceEventProducer) eventListener;
        } else {
            this.eventListener = new XBPSequenceEventProducer(input);
        }
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putBegin(terminationMode);
            return;
        }

        switch (currentState.processingState) {
            case START: {
                eventListener.putToken(XBTBeginToken.create(terminationMode));
                currentState.processingState = XBParamProcessingState.BEGIN;
                break;
            }

            case TYPE:
            case ATTRIBUTES:
            case CHILDREN: {
                subDepth++;
                currentState.serialSequence.putBegin(terminationMode);
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
        if (subDepth > 0) {
            currentState.serialSequence.putType(type);
            return;
        }

        if (currentState.processingState == XBParamProcessingState.BEGIN) {
            eventListener.putToken(XBTTypeToken.create(type));
            currentState.processingState = XBParamProcessingState.TYPE;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(XBAttribute attribute) throws XBSerialException, XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putAttribute(attribute);
            return;
        }

        if (currentState.processingState == XBParamProcessingState.BEGIN || currentState.processingState == XBParamProcessingState.TYPE || currentState.processingState == XBParamProcessingState.ATTRIBUTES) {
            eventListener.putToken(XBTAttributeToken.create(attribute));
            currentState.processingState = XBParamProcessingState.ATTRIBUTES;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putData(data);
            return;
        }

        if (currentState.processingState == XBParamProcessingState.BEGIN) {
            eventListener.putToken(XBTDataToken.create(data));
            currentState.processingState = XBParamProcessingState.DATA;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putEnd();
            subDepth--;
            return;
        } else if (joinDepth > 0) {
            joinDepth--;
            return;
        }

        if (currentState.processingState == XBParamProcessingState.TYPE || currentState.processingState == XBParamProcessingState.ATTRIBUTES || currentState.processingState == XBParamProcessingState.DATA) {
            eventListener.putToken(XBTEndToken.create());
            currentState.processingState = XBParamProcessingState.END;
            if (processingStates.isEmpty()) {
                XBSerializable nextChild = eventListener.getNextChild();
                if (nextChild != null) {
                    currentState.processingState = XBParamProcessingState.START;
                    process(nextChild);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
//                currentState.processingState = processingStates.remove(processingStates.size() - 1);
//                paramType = processingStates.isEmpty() ? XBParamType.CONSIST : XBParamType.JOIN;
            }
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putToken(token);
            checkSequencingListener();
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
        if (subDepth > 0) {
            currentState.serialSequence.putConsist(serial);
            return;
        }

        eventListener.putChild(serial == null ? XBTEmptyBlock.getEmptyBlock() : serial);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putJoin(serial);
            return;
        }

        throw new UnsupportedOperationException("Not supported yet.");
//        processingStates.add(processingState);
//        processingState = XBParamProcessingState.START;
//        paramType = XBParamType.JOIN;
//        process(serial);
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
            currentState.serialSequence.putListConsist(serial);
            return;
        }

        XBListConsistSerializable list = (XBListConsistSerializable) serial;
        UBENatural count = list.getSize();
        putAttribute(count.convertToNatural());
        list.reset();
        int listSize = count.getInt();
        for (int i = 0; i < listSize; i++) {
            putConsist(list.next());
        }
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        ProcessingState currentState = getCurrentState();
        if (subDepth > 0) {
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
        if (subDepth > 0) {
            currentState.serialSequence.putItem(item);
            checkSequencingListener();
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

    private void checkSequencingListener() throws IOException, XBProcessingException {
        throw new UnsupportedOperationException("Not supported yet.");
//        if (serialSequence.isFinished()) {
//            eventListener.putChild(serialSequence.getSequenceSerial());
//            serialSequence = null;
//        }
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

        XBParamProcessingState processingState = XBParamProcessingState.START;
        XBPSerialSequence serialSequence = null;
    }
}
