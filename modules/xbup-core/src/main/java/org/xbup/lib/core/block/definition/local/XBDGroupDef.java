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
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 group definition.
 *
 * @version 0.1.24 2014/12/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBDGroupDef implements XBSerializable, XBGroupDef {

    private List<XBGroupParam> groups = new ArrayList<>();
    private XBDRevisionDef revisionDef;

    public XBDGroupDef() {
    }

    public XBDGroupDef(XBBlockDecl block) {
        groups.add(new XBGroupParamConsist(block));
    }

    @Override
    public List<XBGroupParam> getGroupParams() {
        return groups;
    }

    public void setGroups(List<XBGroupParam> groups) {
        this.groups = groups;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return revisionDef;
    }

    @Override
    public XBBlockDecl getBlockDecl(int blockId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
