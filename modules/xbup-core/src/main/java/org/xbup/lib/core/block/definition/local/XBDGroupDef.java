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

import org.xbup.lib.core.block.definition.XBGroupDef;
import java.util.List;
import org.xbup.lib.core.block.definition.XBGroupParam;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 group definition.
 *
 * @version 0.1.24 2014/11/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBDGroupDef implements XBSerializable, XBGroupDef {

    private List<XBGroupParam> groups;
    private XBDRevisionDef revisionDef;

    public XBDGroupDef() {
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
}
