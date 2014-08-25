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
package org.xbup.lib.core.block.declaration;

import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Block type defined with block declaration.
 *
 * @version 0.1.20 2010/09/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBDBlockType implements XBDeclBlockType {

    private XBBlockDecl blockDecl;

    public XBDBlockType(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    /** Returns 0 - Claims to be unknown */
    @Override
    public UBNatural getGroupID() {
        return new UBNat32();
    }

    /** Returns 0 - Claims to be unknown */
    @Override
    public UBNatural getBlockID() {
        return new UBNat32();
    }

    /**
     * @return the blockDecl
     */
    @Override
    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    /**
     * @param blockDecl the blockDecl to set
     */
    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBDBlockType) {
            return ((XBDBlockType) obj).blockDecl.equals(blockDecl);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return blockDecl.hashCode();
    }
}
