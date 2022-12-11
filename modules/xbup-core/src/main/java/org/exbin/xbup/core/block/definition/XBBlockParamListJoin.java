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
package org.exbin.xbup.core.block.definition;

import java.io.IOException;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProvider;
import org.exbin.xbup.core.parser.basic.convert.XBTTypeReplacingFilter;
import org.exbin.xbup.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicReceivingSerializable;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;

/**
 * XBUP level 1 block list join parameter.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBBlockParamListJoin implements XBBlockParam, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private XBBlockDecl blockDecl;

    public XBBlockParamListJoin() {
        this(null);
    }

    public XBBlockParamListJoin(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public XBParamType getParamType() {
        return XBParamType.LIST_JOIN;
    }

    @Override
    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_LIST_JOIN_PARAMETER));
        serial.join(blockDecl);
        serial.end();
    }

    @Override
    public void serializeRecvFromXB(final XBTBasicInputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        ((XBTBasicReceivingSerializable) blockDecl).serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {
            @Override
            public void process(XBTListener listener) {
                serializationHandler.process(new XBTTypeReplacingFilter(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION), listener));
            }
        });
    }

    @Override
    public void serializeRecvToXB(final XBTBasicOutputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        ((XBTBasicReceivingSerializable) blockDecl).serializeRecvToXB(new XBTBasicOutputReceivingSerialHandler() {
            @Override
            public void process(XBTProvider provider) {
                serializationHandler.process(provider);
            }
        });
    }
}
