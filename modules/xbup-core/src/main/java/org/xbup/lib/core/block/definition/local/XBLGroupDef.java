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

import java.util.ArrayList;
import org.xbup.lib.core.block.definition.XBGroupDef;
import java.util.List;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.definition.XBGroupParam;
import org.xbup.lib.core.block.definition.XBGroupParamConsist;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.block.definition.XBRevisionParam;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 local group definition.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBLGroupDef implements XBGroupDef, XBSerializable {

    private List<XBGroupParam> groupParams = new ArrayList<>();
    private XBLRevisionDef revisionDef;

    public XBLGroupDef() {
    }

    public XBLGroupDef(XBBlockDecl block) {
        groupParams.add(new XBGroupParamConsist(block));
    }

    @Override
    public List<XBGroupParam> getGroupParams() {
        return groupParams;
    }

    public void setGroups(List<XBGroupParam> groups) {
        this.groupParams = groups;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return revisionDef;
    }

    @Override
    public XBBlockDecl getBlockDecl(int blockId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBGroupParam getGroupParam(int paramIndex) {
        return groupParams.get(paramIndex);
    }

    public void provideRevision() {
        revisionDef = new XBLRevisionDef();
        XBRevisionParam revisionParam = new XBRevisionParam();
        revisionParam.setLimit(groupParams.size());
        revisionDef.getRevParams().add(revisionParam);
    }
}
