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
package org.xbup.lib.core.serial.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTConsumer;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBEditableAttribute;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.convert.XBTListenerToToken;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.basic.XBTBasicInputSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicSerializable;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBListConsistSerializable;
import org.xbup.lib.core.serial.sequence.XBListJoinSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequenceItem;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;

/**
 * XBUP level 2 serialization handler using parameter mapping to provider.
 *
 * @version 0.1.25 2015/06/28
 * @author ExBin Project (http://exbin.org)
 */
public class XBPProviderSerialHandler implements XBPInputSerialHandler, XBPSequenceSerialHandler, XBTTokenInputSerialHandler {

    private XBPSequencePullConsumer pullProvider;

    private boolean finished = false;
    private final List<XBParamType> paramTypes = new ArrayList<>();
    private XBParamType paramType = XBParamType.CONSIST;

    private static final String PUSH_NOT_ALLOWED_EXCEPTION = "Pushing data not allowed in pulling mode";
    private XBTBeginToken beginToken = null;

    public XBPProviderSerialHandler() {
    }

    public XBPProviderSerialHandler(XBOutput output) {
        performAttachXBOutput(output);
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeFromXB(this);
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(this);
        } else if (serial instanceof XBTChildSerializable) {
            ((XBTChildSerializable) serial).serializeFromXB(new XBTChildInputSerialHandlerImpl(this));
        } else if (serial instanceof XBTBasicSerializable) {
            ((XBTBasicSerializable) serial).serializeFromXB(new XBTBasicInputSerialHandlerImpl(this));
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        attachXBOutput(pullProvider);
    }

    public void attachXBOutput(XBTPullProvider pullProvider) {
        performAttachXBOutput(pullProvider);
    }

    private void performAttachXBOutput(XBOutput output) {
        if (output instanceof XBPSequencePullConsumer) {
            this.pullProvider = (XBPSequencePullConsumer) output;
        } else {
            this.pullProvider = new XBPSequencePullConsumer(output);
        }
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        if (paramType.isJoin()) {
            return XBBlockTerminationMode.SIZE_SPECIFIED;
        }

        XBTBeginToken token;
        if (beginToken != null) {
            token = beginToken;
            beginToken = null;
        } else {
            token = (XBTBeginToken) pullProvider.pullToken(XBTTokenType.BEGIN);
        }

        return token.getTerminationMode();
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        if (paramType.isJoin()) {
            return new XBFixedBlockType();
        }

        XBTTypeToken token = (XBTTypeToken) pullProvider.pullToken(XBTTokenType.TYPE);
        return token.getBlockType();
    }

    @Override
    public XBAttribute pullAttribute() throws XBProcessingException, IOException {
        XBTAttributeToken token = (XBTAttributeToken) pullProvider.pullToken(XBTTokenType.ATTRIBUTE);
        return token.getAttribute();
    }

    @Override
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        return (byte) pullAttribute().getNaturalInt();
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        return (short) pullAttribute().getNaturalInt();
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getNaturalInt();
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getNaturalLong();
    }

    @Override
    public InputStream pullData() throws XBProcessingException, IOException {
        XBTDataToken token = (XBTDataToken) pullProvider.pullToken(XBTTokenType.DATA);
        return token.getData();
    }

    @Override
    public boolean pullIfEmptyData() throws XBProcessingException, IOException {
        return pullProvider.pullIfEmpty();
    }

    @Override
    public boolean pullIfEmptyBlock() throws XBProcessingException, IOException {
        pullProvider.processAttributes();
        if (pullProvider.isFinishedNext()) {
            return true;
        }

        paramTypes.add(paramType);
        paramType = XBParamType.CONSIST;
        beginToken = new XBTBeginToken(pullBegin());
        if (pullIfEmptyData()) {
            pullEnd();
            beginToken = null;
            return true;
        }

        paramType = paramTypes.remove(paramTypes.size() - 1);
        return false;
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        if (paramType.isConsist()) {
            pullProvider.pullToken(XBTTokenType.END);
            pullProvider.pullRest();
        }

        if (paramTypes.isEmpty()) {
            paramType = null;
        } else {
            paramType = paramTypes.remove(paramTypes.size() - 1);
        }

        finished = true;
    }

    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        switch (tokenType) {
            case BEGIN: {
                return new XBTBeginToken(pullBegin());
            }
            case TYPE: {
                return new XBTTypeToken(pullType());
            }
            case ATTRIBUTE: {
                return new XBTAttributeToken(pullAttribute());
            }
            case DATA: {
                return new XBTDataToken(pullData());
            }
            case END: {
                pullEnd();
                return new XBTEndToken();
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public XBTToken pullToken() throws XBProcessingException, IOException {
        XBTToken token = pullProvider.pullToken();
        if (token.getTokenType() == XBTTokenType.END) {
            pullProvider.pullRest();
        }
        return token;
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
        XBAttribute attribute = pullAttribute();
        UBENatural listSize;
        if (attribute instanceof UBENatural) {
            listSize = (UBENatural) attribute;
        } else {
            listSize = new UBENat32();
            listSize.convertFromNatural(attribute.convertToNatural());
        }

        ((XBListConsistSerializable) serial).setSize(listSize);
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
        UBNatural listSize = pullAttribute().convertToNatural();
        ((XBListJoinSerializable) serial).setSize(listSize);
        int listItemCount = listSize.getInt();
        ((XBListJoinSerializable) serial).reset();
        for (int i = 0; i < listItemCount; i++) {
            paramTypes.add(paramType);
            paramType = XBParamType.JOIN;
            process(((XBListJoinSerializable) serial).next());
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
                pullJoin(item.getItem());
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
        XBBlockType type = pullType();
        if (blockType != null && blockType.getAsBasicType() != XBBasicBlockType.UNKNOWN_BLOCK) {
            if (type.getAsBasicType() != XBBasicBlockType.UNKNOWN_BLOCK) {
                if (!blockType.equals(type)) {
                    throw new XBProcessingException("Block type doesn't match", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                }
            }
        }
    }

    @Override
    public void matchType() throws XBProcessingException, IOException {
        matchType(XBFixedBlockType.UNKNOWN_BLOCK_TYPE);
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        pullEnd();
    }

    @Override
    public void attribute(XBEditableAttribute attributeValue) throws XBProcessingException, IOException {
        if (attributeValue instanceof UBNatural) {
            ((UBNatural) attributeValue).setValue(pullAttribute().getNaturalLong());
        } else {
            attributeValue.convertFromNatural(pullAttribute().convertToNatural());
        }
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

    public boolean isFinished() {
        return finished && paramTypes.isEmpty();
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
    public void putAttribute(XBAttribute attribute) throws XBProcessingException, IOException {
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
        public XBAttribute pullAttribute() throws XBProcessingException, IOException {
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

    private static class XBTBasicInputSerialHandlerImpl implements XBTBasicInputSerialHandler {

        private final XBPProviderSerialHandler handler;

        public XBTBasicInputSerialHandlerImpl(XBPProviderSerialHandler handler) {
            this.handler = handler;
        }

        @Override
        public void process(XBTConsumer consumer) {
            consumer.attachXBTProvider(new XBTProvider() {

                @Override
                public void produceXBT(XBTListener listener) throws XBProcessingException, IOException {
                    XBTToken token = handler.pullToken();
                    XBTListenerToToken.tokenToListener(token, listener);
                }
            });
        }
    }
}
