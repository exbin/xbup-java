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

import org.xbup.lib.core.serial.sequence.XBSerialSequenceItem;
import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.core.parser.param.XBParamProcessingState;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.XBPWriteSerialHandler;
import org.xbup.lib.core.serial.XBSerialException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBListConsistSerializable;
import org.xbup.lib.core.serial.sequence.XBListJoinSerializable;
import org.xbup.lib.core.serial.token.XBTTokenOutputSerialHandler;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization handler using parameter mapping to listener.
 *
 * @version 0.1.24 2015/01/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBPListenerSerialHandler implements XBPOutputSerialHandler, XBPSequenceSerialHandler, XBTTokenOutputSerialHandler {

    private XBPWriteSerialHandler childHandler = null;
    private XBPSequenceEventProducer eventListener;

    private XBPSequencingListener sequencingListener = null;
    private XBParamProcessingState processingState = XBParamProcessingState.START;
    private XBParamType paramType = XBParamType.CONSIST;

    private static final String PULL_NOT_ALLOWED_EXCEPTION = "Pulling data not allowed in pushing mode";

    public XBPListenerSerialHandler() {
    }

    public XBPListenerSerialHandler(XBPWriteSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeToXB(this);
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(this);
        } else if (serial instanceof XBTChildSerializable) {
            ((XBTChildSerializable) serial).serializeToXB(new XBTChildOutputSerialHandlerImpl(this));
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        if (eventListener instanceof XBPSequenceEventProducer) {
            this.eventListener = (XBPSequenceEventProducer) eventListener;
        } else {
            this.eventListener = new XBPSequenceEventProducer(listener);
        }
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putBegin(terminationMode);
            return;
        }

        if (processingState == XBParamProcessingState.START) {
            if (paramType.isConsist()) {
                eventListener.putToken(new XBTBeginToken(terminationMode));
            }
            processingState = XBParamProcessingState.BEGIN;
        } else {
            sequencingListener = new XBPSequencingListener();
            sequencingListener.putBegin(terminationMode);
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
                eventListener.putToken(new XBTTypeToken(type));
            }
            processingState = XBParamProcessingState.TYPE;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBSerialException, XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attribute);
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN || processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
            eventListener.putToken(new XBTAttributeToken(attribute));
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
            eventListener.putToken(new XBTDataToken(data));
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
                eventListener.putToken(new XBTEndToken());
            }
            processingState = XBParamProcessingState.END;
            XBSerializable nextChild = eventListener.getNextChild();
            if (nextChild != null) {
                processingState = XBParamProcessingState.START;
                process(nextChild);
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

        paramType = XBParamType.CONSIST;
        eventListener.putChild(serial == null ? XBTEmptyBlock.getEmptyBlock() : serial);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putJoin(serial);
            return;
        }

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
    public void end() throws XBProcessingException, IOException {
        putEnd();
    }

    @Override
    public void attribute(UBNatural attributeValue) throws XBProcessingException, IOException {
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
    public void pullEnd() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
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

    private class XBTChildOutputSerialHandlerImpl implements XBTChildOutputSerialHandler {

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
}
