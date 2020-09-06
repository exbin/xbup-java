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
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
 * @version 0.2.1 2020/09/06
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPListenerSerialHandler implements XBPOutputSerialHandler, XBPSequenceSerialHandler, XBTTokenOutputSerialHandler {

    private XBTCompactingEventFilter eventListener;

    private XBParamProcessingState processingState = XBParamProcessingState.START;
    private final List<Processing> processings = new ArrayList<>();
    private int subDepth = 0;

    private static final String PULL_NOT_ALLOWED_EXCEPTION = "Pulling data not allowed in pushing mode";

    public XBPListenerSerialHandler() {
        processings.add(new Processing());
    }

    public XBPListenerSerialHandler(XBInput input) {
        this();
        performAttachXBInput(input);
    }

    private void processItem(XBSerialSequenceOp op, @Nullable XBTTokenType tokenType, @Nullable XBSerialSequenceItem item, @Nullable XBTToken token) throws XBProcessingException, IOException {
        boolean loop;
        do {
            Processing currentState = getCurrentState();
            if (currentState.isCollecting()) {
                if (subDepth > 0) {
                    currentState.childSequence.putItem(item != null ? item : new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(token)));
                    if (op == XBSerialSequenceOp.TOKEN) {
                        switch (tokenType) {
                            case BEGIN: {
                                subDepth++;
                                break;
                            }
                            case END: {
                                subDepth--;
                                break;
                            }
                        }
                    }
                } else {
                    switch (op) {
                        case CONSIST: {
                            currentState.childSequence.putItem(item);
                            break;
                        }
                        case JOIN: {
                            XBPSerialSequence seq = new XBPSerialSequence();
                            extractItem(Objects.requireNonNull(item).getItem(), seq);
                            seq.dropLevel();
                            currentState.extractionSequence.insertAtBegining(seq);
                            break;
                        }
                        case LIST_CONSIST: {
                            XBPSerialSequence seq = new XBPSerialSequence();
                            XBListConsistSerializable list = (XBListConsistSerializable) Objects.requireNonNull(item).getItem();
                            extractConsistList(list, seq);
                            currentState.extractionSequence.insertAtBegining(seq);
                            break;
                        }
                        case LIST_JOIN: {
                            XBPSerialSequence seq = new XBPSerialSequence();
                            XBListJoinSerializable list = (XBListJoinSerializable) Objects.requireNonNull(item).getItem();
                            extractJoinList(list, seq);
                            currentState.extractionSequence.insertAtBegining(seq);
                            break;
                        }
                        case TOKEN: {
                            switch (tokenType) {
                                case BEGIN: {
                                    subDepth++;
                                    currentState.childSequence.putItem(item != null ? item : new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(token)));
                                    break;
                                }
                                case TYPE: {
                                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                }
                                case ATTRIBUTE: {
                                    eventListener.putXBTToken(convertToToken(item, token));
                                    break;
                                }
                                case DATA: {
                                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                                }
                                case END: {
                                    currentState.mode = Mode.END;
                                    break;
                                }
                                default:
                                    throw new IllegalStateException("Unexpected token type " + tokenType.name());
                            }
                            break;
                        }
                        default:
                            throw new XBProcessingException("Unexpected operation order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                }
            } else if (currentState.isPassing()) {
                eventListener.putXBTToken(convertToToken(item, token));
            } else {
                switch (op) {
                    case TOKEN: {
                        processToken(tokenType, item, token);
                        break;
                    }
                    default: {
                        currentState.mode = Mode.COLLECTING;
                        currentState.childSequence.putItem(Objects.requireNonNull(item));
                    }
                }
            }

            loop = false;
            if (!currentState.extractionSequence.isEmpty()) {
                item = currentState.extractionSequence.pullItem();
                token = null;
                op = item.getSequenceOp();
                tokenType = (item.getSequenceOp() == XBSerialSequenceOp.TOKEN) ? ((XBPTokenWrapper) item.getItem()).getToken().getTokenType() : null;
                loop = true;
            } else if (currentState.mode == Mode.END) {
                while (currentState.childSequence.isEmpty() && currentState.mode == Mode.END) {
                    processings.remove(processings.size() - 1);
                    eventListener.putXBTToken(XBTEndToken.create());
                    if (processings.isEmpty()) {
                        currentState = null;
                        break;
                    }
                    currentState = getCurrentState();
                }

                if (currentState != null && !currentState.childSequence.isEmpty()) {
                    Processing nextState = new Processing();
                    processings.add(nextState);

                    XBSerialSequenceItem nextItem = currentState.childSequence.pullItem();
                    XBSerialSequenceOp nextOp = nextItem.getSequenceOp();
                    switch (nextOp) {
                        case CONSIST: {
                            extractItem(nextItem.getItem(), nextState.extractionSequence);
                            break;
                        }
                        case TOKEN: {
                            nextState.extractionSequence.putItem(nextItem);
                            break;
                        }
                        default:
                            throw new XBProcessingException("Unexpected operation order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    while (!currentState.childSequence.isEmpty() && !nextState.extractionSequence.isClosed()) {
                        nextState.extractionSequence.putItem(currentState.childSequence.pullItem());
                    }

                    processingState = XBParamProcessingState.START;
                    item = nextState.extractionSequence.pullItem();
                    token = null;
                    op = item.getSequenceOp();
                    tokenType = (item.getSequenceOp() == XBSerialSequenceOp.TOKEN) ? ((XBPTokenWrapper) item.getItem()).getToken().getTokenType() : null;
                    loop = true;
                }
            }
        } while (loop);
    }

    private void processToken(XBTTokenType tokenType, @Nullable XBSerialSequenceItem item, @Nullable XBTToken token) throws XBProcessingException, IOException {
        Processing currentState = getCurrentState();
        switch (tokenType) {
            case BEGIN: {
                switch (processingState) {
                    case START: {
                        eventListener.putXBTToken(convertToToken(item, token));
                        processingState = XBParamProcessingState.BEGIN;
                        break;
                    }

                    case TYPE:
                    case ATTRIBUTES:
                    case CHILDREN: {
                        if (currentState.mode == Mode.BEGIN) {
                            currentState.mode = Mode.COLLECTING;
                            subDepth++;
                            currentState.childSequence.putItem(convertToSequenceItem(item, token));
                        } else {
                            eventListener.putXBTToken(convertToToken(item, token));
                            processingState = XBParamProcessingState.CHILDREN;
                        }
                        break;
                    }
                    default:
                        throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
            case TYPE: {
                if (processingState == XBParamProcessingState.BEGIN) {
                    eventListener.putXBTToken(convertToToken(item, token));
                    processingState = XBParamProcessingState.TYPE;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
            case ATTRIBUTE: {
                if (processingState == XBParamProcessingState.BEGIN || processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
                    eventListener.putXBTToken(convertToToken(item, token));
                    processingState = XBParamProcessingState.ATTRIBUTES;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                break;
            }
            case DATA: {
                if (processingState == XBParamProcessingState.BEGIN) {
                    eventListener.putXBTToken(convertToToken(item, token));
                    processingState = XBParamProcessingState.DATA;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                break;
            }
            case END: {
                if (processingState != XBParamProcessingState.BEGIN && processingState != XBParamProcessingState.START) {
                    processingState = XBParamProcessingState.END;
                    currentState.mode = Mode.END;
                } else {
                    throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                break;
            }
            default:
                throw new IllegalStateException("Unexpected token type " + tokenType.name());
        }
    }

    private void extractItem(XBSerializable serial, XBPListener listener) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeToXB(
                    listener instanceof XBPOutputSerialHandler ? (XBPOutputSerialHandler) listener : new SerialHandlerWrapper(listener)
            );
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(
                    listener instanceof XBPSequenceSerialHandler ? (XBPSequenceSerialHandler) listener : new SerialHandlerWrapper(listener)
            );
        } else if (serial instanceof XBTChildSerializable) {
            ((XBTChildSerializable) serial).serializeToXB(
                    listener instanceof XBTChildOutputSerialHandler ? (XBTChildOutputSerialHandler) listener : new SerialHandlerWrapper(listener)
            );
        } else if (serial instanceof XBTBasicSerializable) {
            ((XBTBasicSerializable) serial).serializeToXB(
                    listener instanceof XBTBasicOutputSerialHandler ? (XBTBasicOutputSerialHandler) listener : new SerialHandlerWrapper(listener)
            );
        } else {
            throw new UnsupportedOperationException("Serialization method " + (serial == null ? "null" : serial.getClass().getCanonicalName()) + " not supported.");
        }
    }

    private void extractConsistList(XBListConsistSerializable list, XBPListener listener) throws IOException, XBProcessingException {
        UBENatural count = list.getSize();
        listener.putAttribute(count.convertToNatural());

        list.reset();
        int listSize = count.getInt();
        for (int i = 0; i < listSize; i++) {
            listener.putConsist(list.next());
        }
    }

    private void extractJoinList(XBListJoinSerializable list, XBPListener listener) throws IOException, XBProcessingException {
        UBNatural count = list.getSize();
        listener.putAttribute(count.convertToNatural());

        list.reset();
        int listSize = count.getInt();
        for (int i = 0; i < listSize; i++) {
            listener.putJoin(list.next());
        }
    }

    @Nonnull
    private XBTToken convertToToken(@Nullable XBSerialSequenceItem item, @Nullable XBTToken token) {
        return item != null ? ((XBPTokenWrapper) item.getItem()).getToken() : Objects.requireNonNull(token);
    }

    @Nonnull
    private XBSerialSequenceItem convertToSequenceItem(@Nullable XBSerialSequenceItem item, @Nullable XBTToken token) {
        return item != null ? item : new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(token));
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        extractItem(serial, this);
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
        processItem(XBSerialSequenceOp.TOKEN, XBTTokenType.BEGIN, null, XBTBeginToken.create(terminationMode));
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.TOKEN, XBTTokenType.TYPE, null, XBTTypeToken.create(type));
    }

    @Override
    public void putAttribute(XBAttribute attribute) throws XBSerialException, XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.TOKEN, XBTTokenType.ATTRIBUTE, null, XBTAttributeToken.create(attribute));
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.TOKEN, XBTTokenType.DATA, null, XBTDataToken.create(data));
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.TOKEN, XBTTokenType.END, null, XBTEndToken.create());
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.TOKEN, token.getTokenType(), null, token);
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.CONSIST, null, new XBSerialSequenceItem(XBSerialSequenceOp.CONSIST, serial), null);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.JOIN, null, new XBSerialSequenceItem(XBSerialSequenceOp.JOIN, serial), null);
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.LIST_CONSIST, null, new XBSerialSequenceItem(XBSerialSequenceOp.LIST_CONSIST, serial), null);
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.LIST_JOIN, null, new XBSerialSequenceItem(XBSerialSequenceOp.LIST_JOIN, serial), null);
    }

    @Override
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        processItem(item.getSequenceOp(), null, item, null);
    }

    @Nonnull
    private Processing getCurrentState() {
        return processings.get(processings.size() - 1);
    }

    @Override
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        processItem(XBSerialSequenceOp.JOIN, null, new XBSerialSequenceItem(XBSerialSequenceOp.JOIN, serial), null);
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
        private boolean isInOrder = false; // TODO optimalizations later
        private boolean childPresent = false;

        public XBPSerialSequence() {
        }

        @Override
        public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            depth++;
            childPresent = false;
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTBeginToken.create(terminationMode))));
        }

        @Override
        public void putType(XBBlockType type) throws XBProcessingException, IOException {
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTTypeToken.create(type))));
        }

        @Override
        public void putAttribute(XBAttribute attribute) throws XBProcessingException, IOException {
            if (childPresent) {
                isInOrder = false;
            }
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTAttributeToken.create(attribute))));
        }

        @Override
        public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
            if (childPresent) {
                isInOrder = false;
            }
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
            if (childPresent) {
                isInOrder = false;
            }
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
            if (childPresent) {
                isInOrder = false;
            }
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
            if (childPresent) {
                isInOrder = false;
            }
            putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putData(InputStream data) throws XBProcessingException, IOException {
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTDataToken.create(data))));
        }

        @Override
        public void putEnd() throws XBProcessingException, IOException {
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(XBTEndToken.create())));
            depth--;
            childPresent = true;
        }

        @Override
        public void putToken(XBTToken token) throws XBProcessingException, IOException {
            putItem(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(token)));
        }

        @Override
        public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
            childPresent = true;
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.CONSIST, serial));
        }

        @Override
        public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
            childPresent = true;
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.JOIN, serial));
        }

        @Override
        public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
            childPresent = true;
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_CONSIST, serial));
        }

        @Override
        public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
            childPresent = true;
            items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_JOIN, serial));
        }

        @Override
        public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
            if (item.getSequenceOp() == XBSerialSequenceOp.TOKEN) {
                switch (((XBPTokenWrapper) item.getItem()).getToken().getTokenType()) {
                    case BEGIN: {
                        childPresent = false;
                        depth++;
                        break;
                    }
                    case END: {
                        depth--;
                        childPresent = true;
                        break;
                    }
                }

                items.add(item);
            } else {
                items.add(item);
            }
        }

        @Override
        public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
            throw new IllegalStateException("Append is not allowed on sequencing");
        }

        @Nonnull
        public XBSerialSequenceItem pullItem() {
            return items.remove(0);
        }

        public void insertAtBegining(XBPSerialSequence sequence) {
            items.addAll(0, sequence.items);
        }

        public void insertAtBegining(XBSerialSequenceItem item) {
            items.add(0, item);
        }

        public void insertAtEnd(XBPSerialSequence sequence) {
            items.addAll(sequence.items);
        }

        public void dropLevel() {
            XBSerialSequenceItem beginItem = items.remove(0);
            if (beginItem == null || beginItem.getSequenceOp() != XBSerialSequenceOp.TOKEN || ((XBPTokenWrapper) beginItem.getItem()).getToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBProcessingException("Missing begin token");
            }
            XBSerialSequenceItem typeItem = items.remove(0);
            if (typeItem == null || typeItem.getSequenceOp() != XBSerialSequenceOp.TOKEN || ((XBPTokenWrapper) typeItem.getItem()).getToken().getTokenType() != XBTTokenType.TYPE) {
                throw new XBProcessingException("Missing type token");
            }
            XBSerialSequenceItem endItem = items.remove(items.size() - 1);
            if (endItem == null || endItem.getSequenceOp() != XBSerialSequenceOp.TOKEN || ((XBPTokenWrapper) endItem.getItem()).getToken().getTokenType() != XBTTokenType.END) {
                throw new XBProcessingException("Missing end token");
            }
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

        public boolean isIsInOrder() {
            return isInOrder;
        }
    }

    @ParametersAreNonnullByDefault
    private static class SerialHandlerWrapper implements XBTBasicOutputSerialHandler, XBTChildOutputSerialHandler, XBPOutputSerialHandler, XBPSequenceSerialHandler {

        private final XBPListener listener;

        public SerialHandlerWrapper(XBPListener listener) {
            this.listener = listener;
        }

        @Override
        public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            listener.putBegin(terminationMode);
        }

        @Override
        public void putType(XBBlockType type) throws XBProcessingException, IOException {
            listener.putType(type);
        }

        @Override
        public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
            listener.putAttribute(attribute);
        }

        @Override
        public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
            listener.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
            listener.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
            listener.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
            listener.putAttribute(attributeValue);
        }

        @Override
        public void putChild(XBSerializable child) throws XBProcessingException, IOException {
            listener.putConsist(child);
        }

        @Override
        public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putAppend(serial);
        }

        @Override
        public void putData(InputStream data) throws XBProcessingException, IOException {
            listener.putData(data);
        }

        @Override
        public void putEnd() throws XBProcessingException, IOException {
            listener.putEnd();
        }

        @Override
        public void attachXBTEventListener(XBTEventListener listener) {
            throw new IllegalStateException();
        }

        @Override
        public XBSerializationMode getSerializationMode() {
            return XBSerializationMode.PUSH;
        }

        @Override
        public void begin() throws XBProcessingException, IOException {
            listener.putBegin(XBBlockTerminationMode.TERMINATED_BY_ZERO);
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
            listener.putEnd();
        }

        @Override
        public void attribute(XBEditableAttribute attributeValue) throws XBProcessingException, IOException {
            listener.putAttribute(attributeValue);
        }

        @Override
        public void consist(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putConsist(serial);
        }

        @Override
        public void join(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putJoin(serial);
        }

        @Override
        public void listConsist(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putListConsist(serial);
        }

        @Override
        public void listJoin(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putListJoin(serial);
        }

        @Override
        public void append(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putAppend(serial);
        }

        @Override
        public void putAttribute(XBAttribute attribute) throws XBProcessingException, IOException {
            listener.putAttribute(attribute);
        }

        @Override
        public void putToken(XBTToken token) throws XBProcessingException, IOException {
            listener.putToken(token);
        }

        @Override
        public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putConsist(serial);
        }

        @Override
        public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putJoin(serial);
        }

        @Override
        public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putListConsist(serial);
        }

        @Override
        public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
            listener.putListJoin(serial);
        }

        @Override
        public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
            listener.putItem(item);
        }

        @Override
        public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
            throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
        }

        @Override
        public XBBlockType pullType() throws XBProcessingException, IOException {
            throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
        }

        @Override
        public XBAttribute pullAttribute() throws XBProcessingException, IOException {
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

        @Override
        public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
            throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
        }

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

        @Override
        public void process(XBTProducer producer) {
            producer.attachXBTListener(new XBTListener() {

                @Override
                public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                    listener.putBegin(terminationMode);
                }

                @Override
                public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
                    listener.putType(blockType);
                }

                @Override
                public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException {
                    listener.putAttribute(attribute);
                }

                @Override
                public void dataXBT(InputStream data) throws XBProcessingException, IOException {
                    listener.putData(data);
                }

                @Override
                public void endXBT() throws XBProcessingException, IOException {
                    listener.putEnd();
                }
            });
        }
    }

    private static class Processing {

        Mode mode = Mode.BEGIN;
        XBPSerialSequence childSequence = new XBPSerialSequence();
        XBPSerialSequence extractionSequence = new XBPSerialSequence();

        private boolean isCollecting() {
            return mode == Mode.COLLECTING;
        }

        private boolean isPassing() {
            return mode == Mode.PASSING;
        }
    }

    private enum Mode {
        BEGIN, COLLECTING, PASSING, END
    }
}
