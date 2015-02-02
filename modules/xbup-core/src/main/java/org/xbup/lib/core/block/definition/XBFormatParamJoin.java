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
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;

/**
 * XBUP level 1 format join parameter.
 *
 * @version 0.1.25 2015/02/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBFormatParamJoin implements XBFormatParam, XBPSequenceSerializable {

    private XBFormatDecl formatDecl;

    public XBFormatParamJoin() {
        formatDecl = new XBLFormatDecl();
    }
    
    public XBFormatParamJoin(XBFormatDecl formatDecl) {
        this.formatDecl = formatDecl;
    }

    @Override
    public XBParamType getParamType() {
        return XBParamType.CONSIST;
    }

    public XBFormatDecl getFormatDecl() {
        return formatDecl;
    }

    public void setFormatDecl(XBFormatDecl formatDecl) {
        this.formatDecl = formatDecl;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.FORMAT_JOIN_PARAMETER));
        serial.join(formatDecl);
        serial.end();
    }
}
