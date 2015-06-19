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
package org.xbup.lib.core.block.definition;

import java.io.IOException;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.basic.convert.XBTTypeReplacingFilter;
import org.xbup.lib.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicReceivingSerializable;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;

/**
 * XBUP level 1 group consist parameter.
 *
 * @version 0.1.25 2015/02/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBGroupParamConsist implements XBGroupParam, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private XBBlockDecl blockDecl;

    public XBGroupParamConsist() {
        blockDecl = new XBLBlockDecl();
    }

    public XBGroupParamConsist(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public XBParamType getParamType() {
        return XBParamType.CONSIST;
    }

    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.GROUP_CONSIST_PARAMETER));
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
