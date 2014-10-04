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

import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBDBlockType;

/**
 * Block type defined with block declaration.
 *
 * @version 0.1.24 2014/10/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBDeclBlockType implements XBDBlockType {

    private XBBlockDecl blockDecl;

    public XBDeclBlockType(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public XBBasicBlockType getAsBasicType() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBDeclBlockType) {
            return ((XBDeclBlockType) obj).blockDecl.equals(blockDecl);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return blockDecl.hashCode();
    }

    @Override
    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }
}
