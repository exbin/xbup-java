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

import java.util.List;
import org.xbup.lib.core.block.declaration.local.XBDGroupDecl;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 group definition.
 *
 * @version 0.1.21 2011/12/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBGroupDef implements XBSerializable {

    private UBNatural consistSkip = new UBNat32(0);
    private UBNatural joinCount = new UBNat32(0);
    private List<XBDGroupDecl> groups;

    public XBGroupDef() {
    }

    public UBNatural getConsistSkip() {
        return consistSkip;
    }

    public void setConsistSkip(UBNatural consistSkip) {
        this.consistSkip = consistSkip;
    }

    public UBNatural getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(UBNatural joinCount) {
        this.joinCount = joinCount;
    }

    public List<XBDGroupDecl> getGroups() {
        return groups;
    }

    public void setGroups(List<XBDGroupDecl> groups) {
        this.groups = groups;
    }
}
