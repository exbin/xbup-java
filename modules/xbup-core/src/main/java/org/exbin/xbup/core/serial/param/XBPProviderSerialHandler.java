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
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTConsumer;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProvider;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBEditableAttribute;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.convert.XBTListenerToToken;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.basic.XBTBasicInputSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicSerializable;
import org.exbin.xbup.core.serial.child.XBTChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.serial.sequence.XBListConsistSerializable;
import org.exbin.xbup.core.serial.sequence.XBListJoinSerializable;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceItem;
import org.exbin.xbup.core.serial.token.XBTTokenInputSerialHandler;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.ubnumber.UBENatural;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBENat32;

/**
 * XBUP level 2 serialization handler using parameter mapping to provider.
 *
 * @version 0.2.1 2020/09/01
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
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

    @Nonnull
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

    @Nonnull
    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        if (paramType.isJoin()) {
            return new XBFixedBlockType();
        }

        XBTTypeToken token = (XBTTypeToken) pullProvider.pullToken(XBTTokenType.TYPE);
        return token.getBlockType();
    }

    @Nonnull
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

    @Nonnull
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
        beginToken = XBTBeginToken.create(pullBegin());
        if (pullIfEmptyData()) {
            pullEnd();
            beginToken = null;
            return true;
        }

        paramType = paramTypes.remove(paramTypes.size() - 1);
        return false;
    }

    public boolean isFinishedNext() {
        return pullProvider.isFinishedNext();
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        if (paramType.isConsist()) {
            pullProvider.pullToken(XBTTokenType.END);
            pullProvider.pullRest();
        }

        if (paramTypes.isEmpty()) {
            paramType = XBParamType.CONSIST;
        } else {
            paramType = paramTypes.remove(paramTypes.size() - 1);
        }

        finished = true;
    }

    @Nonnull
    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        switch (tokenType) {
            case BEGIN: {
                return XBTBeginToken.create(pullBegin());
            }
            case TYPE: {
                return XBTTypeToken.create(pullType());
            }
            case ATTRIBUTE: {
                return XBTAttributeToken.create(pullAttribute());
            }
            case DATA: {
                return XBTDataToken.create(pullData());
            }
            case END: {
                pullEnd();
                return XBTEndToken.create();
            }
        }

        throw new IllegalStateException();
    }

    @Nonnull
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

    @Nonnull
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

    @ParametersAreNonnullByDefault
    private static class XBTChildInputSerialHandlerImpl implements XBTChildInputSerialHandler {

        private final XBPProviderSerialHandler handler;

        public XBTChildInputSerialHandlerImpl(XBPProviderSerialHandler handler) {
            this.handler = handler;
        }

        @Nonnull
        @Override
        public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
            return handler.pullBegin();
        }

        @Nonnull
        @Override
        public XBBlockType pullType() throws XBProcessingException, IOException {
            return handler.pullType();
        }

        @Nonnull
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

        @Nonnull
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

    @ParametersAreNonnullByDefault
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
