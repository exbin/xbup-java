/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.sequence.XBListJoinSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 local group definition.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBLRevisionDef implements XBRevisionDef, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private List<XBRevisionParam> revParams;

    public XBLRevisionDef() {
        revParams = new ArrayList<>();
    }

    public XBLRevisionDef(List<XBRevisionParam> revParams) {
        this.revParams = revParams;
    }

    @Override
    public List<XBRevisionParam> getRevParams() {
        return revParams;
    }

    public void setRevParams(List<XBRevisionParam> revParams) {
        this.revParams = revParams;
    }

    @Override
    public int getRevisionLimit(long revision) {
        if (revision > revParams.size()) {
            revision = revParams.size() - 1;
        }

        int limit = 0;
        for (int index = 0; index <= revision; index++) {
            limit += revParams.get(index).getParamCount();
        }

        return limit;
    }

    public void clear() {
        revParams.clear();
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.REVISION_DEFINITION));
        serial.listJoin(new XBListJoinSerializable() {

            private int position = 0;

            @Override
            public UBNatural getSize() {
                return new UBNat32(revParams.size());
            }

            @Override
            public void setSize(UBNatural count) {
                revParams.clear();
                int size = count.getInt();
                for (int i = 0; i < size; i++) {
                    revParams.add(new XBRevisionParam());
                }
            }

            @Override
            public void reset() {
                position = 0;
            }

            @Override
            public XBSerializable next() {
                XBRevisionParam revParam = revParams.get(position);
                position++;
                return revParam;
            }
        });
        serial.end();
    }

    private enum RecvProcessingState {

        START, BEGIN, TYPE, REV_PARAMS_SIZE, REV_PARAMS, END
    }

    @Override
    public void serializeRecvFromXB(XBTBasicInputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        clear();
        serializationHandler.process(new RecvSerialization());
    }

    private class RecvSerialization implements XBTListener, XBReceivingFinished {

        private RecvProcessingState processingState = RecvProcessingState.START;
        private int revParamsPos = 0;

        @Override
        public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (processingState != RecvProcessingState.START) {
                throw new XBProcessingException("Unexpected token: begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.BEGIN;
        }

        @Override
        public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
            if (processingState != RecvProcessingState.BEGIN) {
                throw new XBProcessingException("Unexpected token: type", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.TYPE;
        }

        @Override
        public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
            if (processingState == RecvProcessingState.TYPE) {
                int listSize = value.getNaturalInt();
                for (int i = 0; i < listSize; i++) {
                    revParams.add(null);
                }
                revParamsPos = 0;
                processingState = RecvProcessingState.REV_PARAMS_SIZE;
            } else if (processingState == RecvProcessingState.REV_PARAMS_SIZE) {
                revParams.set(revParamsPos, new XBRevisionParam());
                revParamsPos++;

                if (revParamsPos == revParams.size()) {
                    processingState = RecvProcessingState.REV_PARAMS;
                }
            } else {
                throw new XBProcessingException("Unexpected token: attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        @Override
        public void dataXBT(InputStream data) throws XBProcessingException, IOException {
            throw new XBProcessingException("Unexpected token: data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        @Override
        public void endXBT() throws XBProcessingException, IOException {
            if (processingState == RecvProcessingState.START || processingState == RecvProcessingState.BEGIN || processingState == RecvProcessingState.END) {
                throw new XBProcessingException("Unexpected token: end", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            finishRevParams();
            processingState = RecvProcessingState.END;
        }

        private void finishRevParams() {
            if (processingState == RecvProcessingState.REV_PARAMS_SIZE) {
                for (int i = revParamsPos; i < revParams.size(); i++) {
                    revParams.set(i, new XBRevisionParam());
                }
            }
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
