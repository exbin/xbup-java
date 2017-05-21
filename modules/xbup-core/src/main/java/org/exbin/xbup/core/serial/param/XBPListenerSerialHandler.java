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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
 * @version 0.1.25 2015/05/03
 * @author ExBin Project (http://exbin.org)
 */
public class XBPListenerSerialHandler implements XBPOutputSerialHandler, XBPSequenceSerialHandler, XBTTokenOutputSerialHandler {

    private XBPSequenceEventProducer eventListener;

    private XBPSequencingListener sequencingListener = null;
    private final List<XBParamProcessingState> processingStates = new ArrayList<>();
    private XBParamProcessingState processingState = XBParamProcessingState.START;
    private XBParamType paramType = XBParamType.CONSIST;

    private static final String PULL_NOT_ALLOWED_EXCEPTION = "Pulling data not allowed in pushing mode";

    public XBPListenerSerialHandler() {
    }

    public XBPListenerSerialHandler(XBInput input) {
        performAttachXBInput(input);
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeToXB(this);
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(this);
        } else if (serial instanceof XBTChildSerializable) {
            ((XBTChildSerializable) serial).serializeToXB(new XBTChildOutputSerialHandlerImpl(this));
        } else if (serial instanceof XBTBasicSerializable) {
            ((XBTBasicSerializable) serial).serializeToXB(new XBTBasicOutputSerialHandlerImpl(this));
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
        if (sequencingListener != null) {
            sequencingListener.putBegin(terminationMode);
            return;
        }

        switch (processingState) {
            case START: {
                if (paramType.isConsist()) {
                    eventListener.putToken(XBTBeginToken.create(terminationMode));
                }
                processingState = XBParamProcessingState.BEGIN;
                break;
            }

            case TYPE:
            case ATTRIBUTES:
            case CHILDREN: {
                sequencingListener = new XBPSequencingListener();
                sequencingListener.putBegin(terminationMode);
                break;
            }
            default: {
                throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putType(type);
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN) {
            if (paramType.isConsist()) {
                eventListener.putToken(XBTTypeToken.create(type));
            }
            processingState = XBParamProcessingState.TYPE;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(XBAttribute attribute) throws XBSerialException, XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attribute);
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN || processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
            eventListener.putToken(XBTAttributeToken.create(attribute));
            processingState = XBParamProcessingState.ATTRIBUTES;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putData(data);
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN) {
            eventListener.putToken(XBTDataToken.create(data));
            processingState = XBParamProcessingState.DATA;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putEnd();
            checkSequencingListener();
            return;
        }

        if (processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES || processingState == XBParamProcessingState.DATA) {
            if (paramType.isConsist()) {
                eventListener.putToken(XBTEndToken.create());
            }
            processingState = XBParamProcessingState.END;
            if (processingStates.isEmpty()) {
                XBSerializable nextChild = eventListener.getNextChild();
                if (nextChild != null) {
                    processingState = XBParamProcessingState.START;
                    paramType = XBParamType.CONSIST;
                    process(nextChild);
                }
            } else {
                processingState = processingStates.remove(processingStates.size() - 1);
                paramType = processingStates.isEmpty() ? XBParamType.CONSIST : XBParamType.JOIN;
            }
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putToken(token);
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
        if (sequencingListener != null) {
            sequencingListener.putConsist(serial);
            return;
        }

        eventListener.putChild(serial == null ? XBTEmptyBlock.getEmptyBlock() : serial);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putJoin(serial);
            return;
        }

        processingStates.add(processingState);
        processingState = XBParamProcessingState.START;
        paramType = XBParamType.JOIN;
        process(serial);
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putListConsist(serial);
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
        if (sequencingListener != null) {
            sequencingListener.putListJoin(serial);
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
        if (sequencingListener != null) {
            sequencingListener.putItem(item);
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

    @Override
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        process(serial);
    }

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

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

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

    private void checkSequencingListener() throws IOException, XBProcessingException {
        if (sequencingListener.isFinished()) {
            eventListener.putChild(sequencingListener.getSequenceSerial());
            sequencingListener = null;

        }
    }

    private static class XBTChildOutputSerialHandlerImpl implements XBTChildOutputSerialHandler {

        private final XBPListenerSerialHandler handler;

        public XBTChildOutputSerialHandlerImpl(XBPListenerSerialHandler handler) {
            this.handler = handler;
        }

        @Override
        public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            handler.putBegin(terminationMode);
        }

        @Override
        public void putType(XBBlockType type) throws XBProcessingException, IOException {
            handler.putType(type);
        }

        @Override
        public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
            handler.putAttribute(attribute);
        }

        @Override
        public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
            handler.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
            handler.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
            handler.putAttribute(attributeValue);
        }

        @Override
        public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
            handler.putAttribute(attributeValue);
        }

        @Override
        public void putChild(XBSerializable child) throws XBProcessingException, IOException {
            handler.putConsist(child);
        }

        @Override
        public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
            handler.process(serial);
        }

        @Override
        public void putData(InputStream data) throws XBProcessingException, IOException {
            handler.putData(data);
        }

        @Override
        public void putEnd() throws XBProcessingException, IOException {
            handler.putEnd();
        }

        @Override
        public void attachXBTEventListener(XBTEventListener listener) {
            throw new IllegalStateException();
        }
    }

    private static class XBTBasicOutputSerialHandlerImpl implements XBTBasicOutputSerialHandler {

        private final XBPListenerSerialHandler handler;

        public XBTBasicOutputSerialHandlerImpl(XBPListenerSerialHandler handler) {
            this.handler = handler;
        }

        @Override
        public void process(XBTProducer producer) {
            producer.attachXBTListener(new XBTListener() {

                @Override
                public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                    handler.putBegin(terminationMode);
                }

                @Override
                public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
                    handler.putType(blockType);
                }

                @Override
                public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException {
                    handler.putAttribute(attribute);
                }

                @Override
                public void dataXBT(InputStream data) throws XBProcessingException, IOException {
                    handler.putData(data);
                }

                @Override
                public void endXBT() throws XBProcessingException, IOException {
                    handler.putEnd();
                }
            });
        }
    }
}
