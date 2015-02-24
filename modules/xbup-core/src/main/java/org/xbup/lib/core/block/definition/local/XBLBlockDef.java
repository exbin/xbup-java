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
package org.xbup.lib.core.block.definition.local;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.block.definition.XBBlockParamConsist;
import org.xbup.lib.core.block.definition.XBBlockParamJoin;
import org.xbup.lib.core.block.definition.XBBlockParamListConsist;
import org.xbup.lib.core.block.definition.XBBlockParamListJoin;
import org.xbup.lib.core.block.definition.XBFormatParamConsist;
import org.xbup.lib.core.block.definition.XBFormatParamJoin;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.basic.XBReceivingFinished;
import org.xbup.lib.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicReceivingSerializable;
import org.xbup.lib.core.serial.param.XBPInputSerialHandler;
import org.xbup.lib.core.serial.param.XBPOutputSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBPSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.serial.sequence.XBListConsistSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;

/**
 * XBUP level 1 local block definition.
 *
 * @version 0.1.25 2015/02/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBLBlockDef implements XBBlockDef, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private List<XBBlockParam> blockParams = new ArrayList<>();
    private XBLRevisionDef revisionDef;

    public XBLBlockDef() {
    }

    public void setBlockParams(List<XBBlockParam> params) {
        this.blockParams = params;
    }

    @Override
    public List<XBBlockParam> getBlockParams() {
        return blockParams;
    }

    @Override
    public long getParamsCount() {
        return blockParams.size();
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return revisionDef;
    }

    public void setRevisionDef(XBLRevisionDef revisionDef) {
        this.revisionDef = revisionDef;
    }

    @Override
    public XBBlockParam getBlockParam(int paramIndex) {
        return blockParams.get(paramIndex);
    }

    public void clear() {
        blockParams.clear();
        revisionDef = null;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DEFINITION));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            revisionDef = new XBLRevisionDef();
            serial.join(revisionDef);
        } else {
            serial.join(revisionDef);
        }
        serial.listConsist(new XBListConsistSerializable() {

            private int position = 0;
            private boolean infinityMode = false;

            @Override
            public UBENatural getSize() {
                return new UBENat32(blockParams.size());
            }

            @Override
            public void setSize(UBENatural count) {
                if (count.isInfinity()) {
                    infinityMode = true;
                }

                blockParams.clear();
                if (!infinityMode) {
                    int size = count.getInt();
                    for (int i = 0; i < size; i++) {
                        blockParams.add(null);
                    }
                }
            }

            @Override
            public void reset() {
                position = 0;
            }

            @Override
            public XBSerializable next() {
                return new ParamSerializator(position);
            }

            class ParamSerializator implements XBPSerializable {

                private int position = 0;

                public ParamSerializator(int position) {
                    this.position = position;
                }

                @Override
                public void serializeFromXB(XBPInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    serializationHandler.begin();
                    XBBlockType type = serializationHandler.pullType();
                    XBBlockParam param = type.getAsBasicType() == XBBasicBlockType.BLOCK_CONSIST_PARAMETER
                            ? new XBBlockParamConsist() : type.getAsBasicType() == XBBasicBlockType.BLOCK_JOIN_PARAMETER
                                    ? new XBBlockParamJoin() : type.getAsBasicType() == XBBasicBlockType.BLOCK_LIST_CONSIST_PARAMETER ? new XBBlockParamListConsist()
                                            : type.getAsBasicType() == XBBasicBlockType.BLOCK_LIST_JOIN_PARAMETER ? new XBBlockParamListJoin()
                                                    : null;
                    if (param == null) {
                        throw new IllegalStateException("Illegal format parameter " + position);
                    }

                    if (infinityMode) {
                        blockParams.add(null);
                    }
                    blockParams.set(position, param);
                    serializationHandler.join(param);
                    serializationHandler.end();
                }

                @Override
                public void serializeToXB(XBPOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    serializationHandler.append(blockParams.get(position));
                }
            }
        });
        serial.end();
    }

    private enum RecvProcessingState {

        START, BEGIN, TYPE, REVISION_DEF_SIZE, REVISION_DEF, BLOCK_PARAM_SIZE, BLOCK_PARAM_BEGIN, BLOCK_PARAM, END
    }

    @Override
    public void serializeRecvFromXB(XBTBasicInputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        clear();
        serializationHandler.process(new RecvSerialization());
    }

    private class RecvSerialization implements XBTListener, XBReceivingFinished {

        private RecvProcessingState processingState = RecvProcessingState.START;
        private XBTListener activeListener = null;
        private XBTListener revisionDefListener = null;
        private int blockParamsPos = 0;
        private XBBlockTerminationMode blockParamTerminationMode;
        private int revisionDefPos = 0;
        private int revisionDefSize = 0;

        @Override
        public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (processingState == RecvProcessingState.BLOCK_PARAM_SIZE) {
                blockParamTerminationMode = terminationMode;
                blockParamsPos++;
                processingState = RecvProcessingState.BLOCK_PARAM_BEGIN;
            }

            if (activeListener != null) {
                activeListener.beginXBT(terminationMode);
                return;
            }

            if (processingState != RecvProcessingState.START) {
                throw new XBProcessingException("Unexpected token: begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.BEGIN;
        }

        @Override
        public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
            if (processingState == RecvProcessingState.BLOCK_PARAM_BEGIN) {
                XBTBasicReceivingSerializable param = type.getAsBasicType() == XBBasicBlockType.FORMAT_CONSIST_PARAMETER ? new XBFormatParamConsist()
                        : type.getAsBasicType() == XBBasicBlockType.FORMAT_CONSIST_PARAMETER ? new XBFormatParamJoin() : null;
                if (param == null) {
                    throw new IllegalStateException("Illegal format parameter " + blockParamsPos);
                }
                param.serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

                    @Override
                    public void process(XBTListener listener) {
                        activeListener = listener;
                    }
                });
                activeListener.beginXBT(blockParamTerminationMode);
                processingState = RecvProcessingState.BLOCK_PARAM;
            }

            if (activeListener != null) {
                activeListener.typeXBT(type);
                return;
            }

            if (processingState != RecvProcessingState.BEGIN) {
                throw new XBProcessingException("Unexpected token: type", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.TYPE;
        }

        @Override
        public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.attribXBT(value);
                return;
            }

            if (processingState == RecvProcessingState.TYPE) {
                revisionDefSize = value.getNaturalInt();
                revisionDefPos = 0;
                revisionDef = new XBLRevisionDef();
                revisionDef.serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

                    @Override
                    public void process(XBTListener listener) {
                        revisionDefListener = listener;
                    }
                });
                revisionDefListener.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                revisionDefListener.typeXBT(new XBFixedBlockType(XBBasicBlockType.REVISION_DEFINITION));
                revisionDefListener.attribXBT(value);
                if (revisionDefSize == 0) {
                    revisionDefListener.endXBT();
                    processingState = RecvProcessingState.REVISION_DEF;
                } else {
                    processingState = RecvProcessingState.REVISION_DEF_SIZE;
                }
            } else if (processingState == RecvProcessingState.REVISION_DEF_SIZE) {
                revisionDefListener.attribXBT(value);
                revisionDefPos++;
                if (revisionDefPos == revisionDefSize) {
                    revisionDefListener.endXBT();
                    processingState = RecvProcessingState.REVISION_DEF;
                }
            } else if (processingState == RecvProcessingState.REVISION_DEF) {
                int size = value.getNaturalInt();
                for (int i = 0; i < size; i++) {
                    blockParams.add(null);
                }
                blockParamsPos = 0;
                processingState = size == 0 ? RecvProcessingState.BLOCK_PARAM : RecvProcessingState.BLOCK_PARAM_SIZE;
            } else {
                throw new XBProcessingException("Unexpected token: attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        @Override
        public void dataXBT(InputStream data) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.dataXBT(data);
                return;
            }

            throw new XBProcessingException("Unexpected token: data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        @Override
        public void endXBT() throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.endXBT();
                if (((XBReceivingFinished) activeListener).isFinished()) {
                    if (blockParamsPos == blockParams.size()) {
                        processingState = RecvProcessingState.BLOCK_PARAM;
                    } else {
                        activeListener = null;
                    }
                }

                return;
            }

            if (processingState == RecvProcessingState.START || processingState == RecvProcessingState.BEGIN || processingState == RecvProcessingState.END) {
                throw new XBProcessingException("Unexpected token: end", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.END;
        }

        @Override
        public boolean isFinished() {
            return processingState == RecvProcessingState.END;
        }
    }

    @Override
    public void serializeRecvToXB(XBTBasicOutputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
