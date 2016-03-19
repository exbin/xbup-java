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
package org.exbin.xbup.core.block.definition.local;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.definition.XBFormatParamConsist;
import org.exbin.xbup.core.block.definition.XBFormatParamJoin;
import org.exbin.xbup.core.block.definition.XBGroupDef;
import org.exbin.xbup.core.block.definition.XBGroupParam;
import org.exbin.xbup.core.block.definition.XBGroupParamConsist;
import org.exbin.xbup.core.block.definition.XBGroupParamJoin;
import org.exbin.xbup.core.block.definition.XBRevisionDef;
import org.exbin.xbup.core.block.definition.XBRevisionParam;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.basic.XBReceivingFinished;
import org.exbin.xbup.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicReceivingSerializable;
import org.exbin.xbup.core.serial.param.XBPInputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPOutputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBPSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.serial.sequence.XBListConsistSerializable;
import org.exbin.xbup.core.ubnumber.UBENatural;
import org.exbin.xbup.core.ubnumber.type.UBENat32;

/**
 * XBUP level 1 local group definition.
 *
 * @version 0.1.25 2015/02/26
 * @author ExBin Project (http://exbin.org)
 */
public class XBLGroupDef implements XBGroupDef, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private List<XBGroupParam> groupParams = new ArrayList<>();
    private XBLRevisionDef revisionDef;

    public XBLGroupDef() {
    }

    public XBLGroupDef(XBBlockDecl block) {
        groupParams.add(new XBGroupParamConsist(block));
        revisionDef = new XBLRevisionDef();
        revisionDef.getRevParams().add(new XBRevisionParam(1));
    }

    @Override
    public List<XBGroupParam> getGroupParams() {
        return groupParams;
    }

    public void setGroupParams(List<XBGroupParam> groups) {
        this.groupParams = groups;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return revisionDef;
    }

    public void setRevisionDef(XBLRevisionDef revisionDef) {
        this.revisionDef = revisionDef;
    }

    @Override
    public XBGroupParam getGroupParam(int paramIndex) {
        return paramIndex < groupParams.size() ? groupParams.get(paramIndex) : null;
    }

    public void provideRevision() {
        revisionDef = new XBLRevisionDef();
        XBRevisionParam revisionParam = new XBRevisionParam();
        revisionParam.setParamCount(groupParams.size());
        revisionDef.getRevParams().add(revisionParam);
    }

    public void clear() {
        groupParams.clear();
        revisionDef = null;
    }

    @Override
    public long getParamsCount() {
        return groupParams.size();
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.GROUP_DEFINITION));
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
                return new UBENat32(groupParams.size());
            }

            @Override
            public void setSize(UBENatural count) {
                if (count.isInfinity()) {
                    infinityMode = true;
                }

                groupParams.clear();
                if (!infinityMode) {
                    int size = count.getInt();
                    for (int i = 0; i < size; i++) {
                        groupParams.add(null);
                    }
                }
            }

            @Override
            public void reset() {
                position = 0;
            }

            @Override
            public XBSerializable next() {
                return new ParamSerializator(position++);
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
                    XBGroupParam param = type.getAsBasicType() == XBBasicBlockType.GROUP_CONSIST_PARAMETER ? new XBGroupParamConsist()
                            : type.getAsBasicType() == XBBasicBlockType.GROUP_CONSIST_PARAMETER ? new XBGroupParamJoin() : null;
                    if (param == null) {
                        throw new IllegalStateException("Illegal format parameter " + position);
                    }

                    if (infinityMode) {
                        groupParams.add(null);
                    }
                    groupParams.set(position, param);
                    serializationHandler.join(param);
                    serializationHandler.end();
                }

                @Override
                public void serializeToXB(XBPOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    serializationHandler.append(groupParams.get(position));
                }
            }
        });
        serial.end();
    }

    private enum RecvProcessingState {

        START, BEGIN, TYPE, REVISION_DEF_SIZE, REVISION_DEF, GROUP_PARAM_SIZE, GROUP_PARAM_BEGIN, GROUP_PARAM, END
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
        private int groupParamsPos = 0;
        private XBBlockTerminationMode groupParamTerminationMode;
        private int revisionDefPos = 0;
        private int revisionDefSize = 0;

        @Override
        public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (processingState == RecvProcessingState.GROUP_PARAM_SIZE) {
                groupParamTerminationMode = terminationMode;
                groupParamsPos++;
                processingState = RecvProcessingState.GROUP_PARAM_BEGIN;
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
            if (processingState == RecvProcessingState.GROUP_PARAM_BEGIN) {
                XBTBasicReceivingSerializable param = type.getAsBasicType() == XBBasicBlockType.FORMAT_CONSIST_PARAMETER ? new XBFormatParamConsist()
                        : type.getAsBasicType() == XBBasicBlockType.FORMAT_CONSIST_PARAMETER ? new XBFormatParamJoin() : null;
                if (param == null) {
                    throw new IllegalStateException("Illegal format parameter " + groupParamsPos);
                }
                param.serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

                    @Override
                    public void process(XBTListener listener) {
                        activeListener = listener;
                    }
                });
                activeListener.beginXBT(groupParamTerminationMode);
                processingState = RecvProcessingState.GROUP_PARAM;
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
                    groupParams.add(null);
                }
                groupParamsPos = 0;
                processingState = size == 0 ? RecvProcessingState.GROUP_PARAM : RecvProcessingState.GROUP_PARAM_SIZE;
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
                    if (groupParamsPos == groupParams.size()) {
                        processingState = RecvProcessingState.GROUP_PARAM;
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
