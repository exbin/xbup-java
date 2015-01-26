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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.param.XBParamProcessingState;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.XBPReadSerialHandler;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBListConsistSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequenceItem;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;

/**
 * XBUP level 2 serialization handler using parameter mapping to provider.
 *
 * @version 0.1.24 2015/01/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBPProviderSerialHandler implements XBPInputSerialHandler, XBPSequenceSerialHandler, XBTTokenInputSerialHandler {

    private XBPSequencePullConsumer pullProvider;
    private XBPReadSerialHandler childHandler = null;

    private XBParamProcessingState processingState = XBParamProcessingState.START;
    private final List<XBParamType> paramTypes = new ArrayList<>();
    private XBParamType paramType = XBParamType.CONSIST;

    private static final String PUSH_NOT_ALLOWED_EXCEPTION = "Pushing data not allowed in pulling mode";

    public XBPProviderSerialHandler() {
    }

    public XBPProviderSerialHandler(XBPReadSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeFromXB(this);
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(this);
        } else if (serial instanceof XBTChildSerializable) {
            ((XBTChildSerializable) serial).serializeFromXB(new XBTChildInputSerialHandlerImpl(this));
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        if (pullProvider instanceof XBPSequencePullConsumer) {
            this.pullProvider = (XBPSequencePullConsumer) pullProvider;
        } else {
            this.pullProvider = new XBPSequencePullConsumer(pullProvider);
        }
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        processingState = XBParamProcessingState.BEGIN;
        if (paramType.isJoin()) {
            return XBBlockTerminationMode.SIZE_SPECIFIED;
        }

        XBTBeginToken token = (XBTBeginToken) pullProvider.pullToken(XBTTokenType.BEGIN);
        return token.getTerminationMode();
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        processingState = XBParamProcessingState.TYPE;
        if (paramType.isJoin()) {
            return new XBFixedBlockType();
        }

        XBTTypeToken token = (XBTTypeToken) pullProvider.pullToken(XBTTokenType.TYPE);
        return token.getBlockType();
    }

    @Override
    public UBNatural pullAttribute() throws XBProcessingException, IOException {
        XBTAttributeToken token = (XBTAttributeToken) pullProvider.pullToken(XBTTokenType.ATTRIBUTE);
        processingState = XBParamProcessingState.ATTRIBUTES;
        return token.getAttribute();
    }

    @Override
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        return (byte) pullAttribute().getInt();
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        return (short) pullAttribute().getInt();
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getInt();
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getLong();
    }

    @Override
    public InputStream pullData() throws XBProcessingException, IOException {
        XBTDataToken token = (XBTDataToken) pullProvider.pullToken(XBTTokenType.DATA);
        processingState = XBParamProcessingState.DATA;
        return token.getData();
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        processingState = XBParamProcessingState.END;
        if (paramType.isConsist()) {
            pullProvider.pullToken(XBTTokenType.END);
        }

        if (paramTypes.isEmpty()) {
            paramType = null;
        } else {
            paramType = paramTypes.remove(paramTypes.size() - 1);
        }
    }

    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullConsist(XBSerializable serial) throws XBProcessingException, IOException {
        paramTypes.add(paramType);
        paramType = XBParamType.CONSIST;
        process(serial);
    }

    @Override
    public void pullJoin(XBSerializable serial) throws XBProcessingException, IOException {
        paramTypes.add(paramType);
        paramType = XBParamType.JOIN;
        process(serial);
    }

    @Override
    public void pullListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        UBENat32 listSize = new UBENat32();
        listSize.convertFromNatural(pullAttribute());
        int listItemCount = listSize.getInt();
        ((XBListConsistSerializable) serial).reset();
        for (int i = 0; i < listItemCount; i++) {
            paramTypes.add(paramType);
            paramType = XBParamType.CONSIST;
            process(((XBListConsistSerializable) serial).next());
        }
    }

    @Override
    public void pullListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        UBNatural listSize = pullAttribute();
        int listItemCount = listSize.getInt();
        ((XBListConsistSerializable) serial).reset();
        for (int i = 0; i < listItemCount; i++) {
            paramTypes.add(paramType);
            paramType = XBParamType.JOIN;
            process(((XBListConsistSerializable) serial).next());
        }
    }

    @Override
    public void pullItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        switch (item.getSequenceOp()) {
            case TOKEN: {
                pullToken(((XBPTokenWrapper) item.getItem()).getToken().getTokenType());
                break;
            }
            case CONSIST: {
                pullConsist(item.getItem());
                break;
            }
            case JOIN: {
                pullConsist(item.getItem());
                break;
            }
            case LIST_CONSIST: {
                pullListConsist(item.getItem());
                break;
            }
            case LIST_JOIN: {
                pullListJoin(item.getItem());
                break;
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException {
        process(serial);
    }

    @Override
    public XBSerializationMode getSerializationMode() {
        return XBSerializationMode.PULL;
    }

    @Override
    public void begin() throws XBProcessingException, IOException {
        pullBegin();
    }

    @Override
    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException {
        pullType();
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        pullEnd();
    }

    @Override
    public void attribute(UBNatural attributeValue) throws XBProcessingException, IOException {
        attributeValue.setValue(pullAttribute().getLong());
    }

    @Override
    public void consist(XBSerializable serial) throws XBProcessingException, IOException {
        pullConsist(serial);
    }

    @Override
    public void join(XBSerializable serial) throws XBProcessingException, IOException {
        pullJoin(serial);
    }

    @Override
    public void listConsist(XBSerializable serial) throws XBProcessingException, IOException {
        pullListConsist(serial);
    }

    @Override
    public void listJoin(XBSerializable serial) throws XBProcessingException, IOException {
        pullListJoin(serial);
    }

    @Override
    public void append(XBSerializable serial) throws XBProcessingException, IOException {
        pullAppend(serial);
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    private static class XBTChildInputSerialHandlerImpl implements XBTChildInputSerialHandler {

        private final XBPProviderSerialHandler handler;

        public XBTChildInputSerialHandlerImpl(XBPProviderSerialHandler handler) {
            this.handler = handler;
        }

        @Override
        public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
            return handler.pullBegin();
        }

        @Override
        public XBBlockType pullType() throws XBProcessingException, IOException {
            return handler.pullType();
        }

        @Override
        public UBNatural pullAttribute() throws XBProcessingException, IOException {
            return handler.pullAttribute();
        }

        @Override
        public byte pullByteAttribute() throws XBProcessingException, IOException {
            return handler.pullByteAttribute();
        }

        @Override
        public short pullShortAttribute() throws XBProcessingException, IOException {
            return handler.pullShortAttribute();
        }

        @Override
        public int pullIntAttribute() throws XBProcessingException, IOException {
            return handler.pullIntAttribute();
        }

        @Override
        public long pullLongAttribute() throws XBProcessingException, IOException {
            return handler.pullLongAttribute();
        }

        @Override
        public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
            handler.pullConsist(child);
        }

        @Override
        public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException {
            handler.process(serial);
        }

        @Override
        public InputStream pullData() throws XBProcessingException, IOException {
            return handler.pullData();
        }

        @Override
        public void pullEnd() throws XBProcessingException, IOException {
            handler.pullEnd();
        }

        @Override
        public void attachXBTPullProvider(XBTPullProvider provider) {
            throw new IllegalStateException();
        }
    }
}
