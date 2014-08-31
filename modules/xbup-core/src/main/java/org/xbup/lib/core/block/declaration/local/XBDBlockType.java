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
package org.xbup.lib.core.block.declaration.local;

import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;

/**
 * Block type defined with block declaration.
 *
 * @version 0.1.24 2014/08/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBDBlockType implements XBBlockType {

    private XBBlockDecl blockDecl;

    public XBDBlockType(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public XBBasicBlockType getAsBasicType() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }
}
