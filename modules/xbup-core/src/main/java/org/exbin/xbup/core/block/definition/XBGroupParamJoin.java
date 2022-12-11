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
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.block.declaration.local.XBLGroupDecl;
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
 * XBUP level 1 group join parameter.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBGroupParamJoin implements XBGroupParam, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private XBGroupDecl groupDecl;

    public XBGroupParamJoin() {
        groupDecl = new XBLGroupDecl();
    }

    public XBGroupParamJoin(XBGroupDecl groupDecl) {
        this.groupDecl = groupDecl;
    }

    @Override
    public XBParamType getParamType() {
        return XBParamType.CONSIST;
    }

    public XBGroupDecl getGroupDecl() {
        return groupDecl;
    }

    public void setGroupDecl(XBGroupDecl groupDecl) {
        this.groupDecl = groupDecl;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.GROUP_JOIN_PARAMETER));
        serial.join(groupDecl);
        serial.end();
    }

    @Override
    public void serializeRecvFromXB(final XBTBasicInputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        ((XBTBasicReceivingSerializable) groupDecl).serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {
            @Override
            public void process(XBTListener listener) {
                serializationHandler.process(new XBTTypeReplacingFilter(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION), listener));
            }
        });
    }

    @Override
    public void serializeRecvToXB(final XBTBasicOutputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        ((XBTBasicReceivingSerializable) groupDecl).serializeRecvToXB(new XBTBasicOutputReceivingSerialHandler() {
            @Override
            public void process(XBTProvider provider) {
                serializationHandler.process(provider);
            }
        });
    }
}
