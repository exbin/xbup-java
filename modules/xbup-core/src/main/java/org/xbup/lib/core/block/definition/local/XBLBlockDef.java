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

import java.util.List;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 local block definition.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBLBlockDef implements XBBlockDef, XBSerializable {

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
}
