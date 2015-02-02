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
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.block.definition.XBBlockParamConsist;
import org.xbup.lib.core.block.definition.XBBlockParamJoin;
import org.xbup.lib.core.block.definition.XBBlockParamListConsist;
import org.xbup.lib.core.block.definition.XBBlockParamListJoin;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
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
 * @version 0.1.25 2015/02/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBLBlockDef implements XBBlockDef, XBPSequenceSerializable {

    private List<XBBlockParam> blockParams;
    private XBLRevisionDef revisionDef;

    public XBLBlockDef() {
    }

    public void setParams(List<XBBlockParam> params) {
        this.blockParams = params;
    }

    @Override
    public List<XBBlockParam> getBlockParams() {
        return blockParams;
    }

    @Override
    public long getParamCount() {
        return blockParams.size();
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return revisionDef;
    }

    @Override
    public XBBlockParam getBlockParam(int paramIndex) {
        return blockParams.get(paramIndex);
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

            @Override
            public UBENatural getSize() {
                return new UBENat32(blockParams.size());
            }

            @Override
            public void setSize(UBENatural count) {
                if (count.isInfinity()) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                blockParams.clear();
                int size = count.getInt();
                for (int i = 0; i < size; i++) {
                    blockParams.add(null);
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
                            ? new XBBlockParamConsist() : type.getAsBasicType() == XBBasicBlockType.BLOCK_CONSIST_PARAMETER
                                    ? new XBBlockParamJoin() : type.getAsBasicType() == XBBasicBlockType.BLOCK_LIST_CONSIST_PARAMETER ? new XBBlockParamListConsist()
                                            : type.getAsBasicType() == XBBasicBlockType.BLOCK_LIST_JOIN_PARAMETER ? new XBBlockParamListJoin()
                                                    : null;
                    if (param == null) {
                        throw new IllegalStateException("Illegal format parameter " + position);
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
}
