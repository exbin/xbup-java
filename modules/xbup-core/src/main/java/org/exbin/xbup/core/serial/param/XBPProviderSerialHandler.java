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
import java.util.LinkedList;
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
import org.exbin.xbup.core.parser.basic.convert.XBTProducerToProvider;
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
import org.exbin.xbup.core.parser.token.convert.XBTListenerToToken;
import org.exbin.xbup.core.parser.token.event.XBTEventProducer;
import org.exbin.xbup.core.parser.token.event.convert.XBTEventProducerToProducer;
import org.exbin.xbup.core.parser.token.pull.XBTPullConsumer;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullPreLoader;
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
 * @version 0.2.1 2020/09/11
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

    private void extractSerial(XBSerializable serial, XBPProvider provider) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeFromXB(
                    provider instanceof XBPInputSerialHandler ? (XBPInputSerialHandler) provider : new SerialHandlerWrapper(provider)
            );
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(
                    provider instanceof XBPSequenceSerialHandler ? (XBPSequenceSerialHandler) provider : new SerialHandlerWrapper(provider)
            );
        } else if (serial instanceof XBTChildSerializable) {
            ((XBTChildSerializable) serial).serializeFromXB(
                    provider instanceof XBTChildInputSerialHandler ? (XBTChildInputSerialHandler) provider : new SerialHandlerWrapper(provider)
            );
        } else if (serial instanceof XBTBasicSerializable) {
            ((XBTBasicSerializable) serial).serializeFromXB(
                    provider instanceof XBTBasicInputSerialHandler ? (XBTBasicInputSerialHandler) provider : new SerialHandlerWrapper(provider)
            );
        } else {
            throw new UnsupportedOperationException("Serialization method " + (serial == null ? "null" : serial.getClass().getCanonicalName()) + " not supported.");
        }
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        extractSerial(serial, this);
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

    public static class XBPSequencePullConsumer implements XBTPullConsumer {

        private XBTPullPreLoader pullProvider;
        private final List<List<XBTAttributeToken>> attributeSequences = new ArrayList<>();
        private List<XBTAttributeToken> attributeSequence = new LinkedList<>();
        private XBParamProcessingState processingState = XBParamProcessingState.START;
        private boolean emptyNodeMode = false;

        public XBPSequencePullConsumer(XBOutput output) {
            if (output instanceof XBTPullProvider) {
                attachProvider((XBTPullProvider) output);
            } else if (output instanceof XBTProvider) {
                attachProvider(new XBTProviderToPullProvider((XBTProvider) output));
            } else if (output instanceof XBTEventProducer) {
                attachProvider(new XBTProviderToPullProvider(new XBTProducerToProvider(new XBTEventProducerToProducer((XBTEventProducer) output))));
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        @Override
        public void attachXBTPullProvider(XBTPullProvider pullProvider) {
            attachProvider(pullProvider);
        }

        private void attachProvider(XBTPullProvider pullProvider) {
            if (pullProvider instanceof XBTPullPreLoader) {
                this.pullProvider = (XBTPullPreLoader) pullProvider;
            } else {
                this.pullProvider = new XBTPullPreLoader(pullProvider);
            }
        }

        @Nonnull
        public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
            switch (tokenType) {
                case BEGIN: {
                    if (processingState == XBParamProcessingState.DATA || processingState == XBParamProcessingState.BEGIN) {
                        throw new XBProcessingException("Begin token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    if (processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
                        processAttributes();
                        attributeSequences.add(attributeSequence);
                        attributeSequence = new LinkedList<>();
                    }

                    XBTToken token = pullProvider.pullXBTToken();
                    processingState = XBParamProcessingState.BEGIN;
                    if (token.getTokenType() == XBTTokenType.END) {
                        emptyNodeMode = true;
                        return XBTBeginToken.create(XBBlockTerminationMode.SIZE_SPECIFIED);
                    } else if (token.getTokenType() != XBTTokenType.BEGIN) {
                        throw new XBProcessingException("Unexpected token type " + token.getTokenType(), XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    return token;
                }
                case TYPE: {
                    if (processingState != XBParamProcessingState.BEGIN && !emptyNodeMode) {
                        throw new XBProcessingException("Type token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    XBTToken token = pullProvider.pullXBTToken();
                    if (token.getTokenType() != XBTTokenType.TYPE) {
                        throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                    processingState = XBParamProcessingState.TYPE;
                    return token;
                }
                case ATTRIBUTE: {
                    if (processingState == XBParamProcessingState.DATA || processingState == XBParamProcessingState.START || emptyNodeMode) {
                        throw new XBProcessingException("Attribute token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    if (pullProvider.getNextTokenType() != XBTTokenType.ATTRIBUTE) {
                        processingState = XBParamProcessingState.ATTRIBUTES;
                        if (!attributeSequence.isEmpty()) {
                            return attributeSequence.remove(0);
                        } else {
                            return XBTAttributeToken.createZeroToken();
                        }
                    }

                    XBTToken token = pullProvider.pullXBTToken();
                    if (token.getTokenType() != XBTTokenType.ATTRIBUTE) {
                        throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                    processingState = XBParamProcessingState.ATTRIBUTES;
                    return token;
                }
                case DATA: {
                    if (processingState != XBParamProcessingState.BEGIN) {
                        throw new XBProcessingException("Data token out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    processingState = XBParamProcessingState.DATA;
                    if (emptyNodeMode) {
                        return XBTDataToken.createEmptyToken();
                    }

                    XBTToken token = pullProvider.pullXBTToken();
                    if (token.getTokenType() != XBTTokenType.DATA) {
                        throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                    return token;
                }
                case END: {
                    if (processingState == XBParamProcessingState.BEGIN) {
                        throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    processingState = XBParamProcessingState.END;
                    if (emptyNodeMode) {
                        emptyNodeMode = false;
                    } else if (!attributeSequences.isEmpty()) {
                        attributeSequence = attributeSequences.remove(attributeSequences.size() - 1);
                    }

                    return XBTEndToken.create();
                }
            }

            throw new IllegalStateException();
        }

        /**
         * Pulls single token for preserving minimal form.
         *
         * @throws XBProcessingException if not matching
         * @throws IOException if input/output exception occurs
         * @return token
         */
        @Nonnull
        public XBTToken pullToken() throws XBProcessingException, IOException {
            if (emptyNodeMode) {
                return pullToken(processingState == XBParamProcessingState.DATA ? XBTTokenType.END : XBTTokenType.DATA);
            } else {
                return pullToken(pullProvider.getNextTokenType());
            }
        }

        public boolean pullIfEmpty() throws XBProcessingException, IOException {
            if (processingState != XBParamProcessingState.BEGIN) {
                throw new XBProcessingException("Empty data token test out of order", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            if (emptyNodeMode) {
                processingState = XBParamProcessingState.DATA;
                return true;
            }

            XBTToken nextToken = pullProvider.getNextToken();
            if (nextToken != null && nextToken.getTokenType() == XBTTokenType.DATA) {
                if (((XBTDataToken) nextToken).isEmpty()) {
                    pullProvider.pullXBTToken();
                    processingState = XBParamProcessingState.DATA;
                    return true;
                }
            }

            return false;
        }

        public void processAttributes() throws XBProcessingException, IOException {
            while (pullProvider.getNextTokenType() == XBTTokenType.ATTRIBUTE) {
                attributeSequence.add((XBTAttributeToken) pullProvider.pullXBTToken());
            }
        }

        /**
         * Returns true if current block was processed.
         *
         * @return true if block processed
         */
        public boolean isFinished() {
            return processingState == XBParamProcessingState.END;
        }

        /**
         * Returns true if block will be finished with next token.
         *
         * @return true if block will be finished next
         */
        public boolean isFinishedNext() {
            return pullProvider.getNextTokenType() == XBTTokenType.END;
        }

        @Nonnull
        public List<XBTAttributeToken> getAttributeSequence() {
            return attributeSequence;
        }

        public void resetSequence() {
            attributeSequence = new LinkedList<>();
            processingState = XBParamProcessingState.START;
        }

        public void pullRest() throws XBProcessingException, IOException {
            pullProvider.skipAttributes();
            pullProvider.skipChildren();
            XBTToken token = pullProvider.pullXBTToken();
            if (token.getTokenType() != XBTTokenType.END) {
                throw new XBProcessingException("End token was expected, but " + token.getTokenType().name() + " token was received", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static class SerialHandlerWrapper implements XBTChildInputSerialHandler, XBTBasicInputSerialHandler, XBPInputSerialHandler, XBPSequenceSerialHandler {

        private final XBPProvider provider;

        public SerialHandlerWrapper(XBPProvider provider) {
            this.provider = provider;
        }

        @Nonnull
        @Override
        public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
            return provider.pullBegin();
        }

        @Nonnull
        @Override
        public XBBlockType pullType() throws XBProcessingException, IOException {
            return provider.pullType();
        }

        @Nonnull
        @Override
        public XBAttribute pullAttribute() throws XBProcessingException, IOException {
            return provider.pullAttribute();
        }

        @Override
        public byte pullByteAttribute() throws XBProcessingException, IOException {
            return provider.pullByteAttribute();
        }

        @Override
        public short pullShortAttribute() throws XBProcessingException, IOException {
            return provider.pullShortAttribute();
        }

        @Override
        public int pullIntAttribute() throws XBProcessingException, IOException {
            return provider.pullIntAttribute();
        }

        @Override
        public long pullLongAttribute() throws XBProcessingException, IOException {
            return provider.pullLongAttribute();
        }

        @Override
        public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
            provider.pullConsist(child);
        }

        @Override
        public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException {
            provider.pullAppend(serial);
        }

        @Nonnull
        @Override
        public InputStream pullData() throws XBProcessingException, IOException {
            return provider.pullData();
        }

        @Override
        public void pullEnd() throws XBProcessingException, IOException {
            provider.pullEnd();
        }

        @Override
        public void attachXBTPullProvider(XBTPullProvider provider) {
            throw new IllegalStateException();
        }

        @Override
        public void process(XBTConsumer consumer) {
            consumer.attachXBTProvider((XBTListener listener) -> {
                XBTToken token = provider.pullToken();
                XBTListenerToToken.tokenToListener(token, listener);
            });
        }

        @Nonnull
        @Override
        public XBSerializationMode getSerializationMode() {
            return XBSerializationMode.PUSH;
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
            pullType();
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

        @Override
        public boolean pullIfEmptyData() throws XBProcessingException, IOException {
            return provider.pullIfEmptyData();
        }

        @Override
        public boolean pullIfEmptyBlock() throws XBProcessingException, IOException {
            return provider.pullIfEmptyBlock();
        }

        @Nonnull
        @Override
        public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
            return provider.pullToken(tokenType);
        }

        @Nonnull
        @Override
        public XBTToken pullToken() throws XBProcessingException, IOException {
            return provider.pullToken();
        }

        @Override
        public void pullConsist(XBSerializable child) throws XBProcessingException, IOException {
            provider.pullConsist(child);
        }

        @Override
        public void pullJoin(XBSerializable serial) throws XBProcessingException, IOException {
            provider.pullJoin(serial);
        }

        @Override
        public void pullListConsist(XBSerializable child) throws XBProcessingException, IOException {
            provider.pullListConsist(child);
        }

        @Override
        public void pullListJoin(XBSerializable serial) throws XBProcessingException, IOException {
            provider.pullListJoin(serial);
        }

        @Override
        public void pullItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
            provider.pullItem(item);
        }
    }
}
