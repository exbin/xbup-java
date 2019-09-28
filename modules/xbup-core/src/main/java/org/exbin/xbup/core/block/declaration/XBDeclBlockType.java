/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.block.declaration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBDBlockType;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;

/**
 * Block type defined using block declaration.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDeclBlockType implements XBDBlockType {

    @Nonnull
    private XBBlockDecl blockDecl;

    public XBDeclBlockType(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    public XBDeclBlockType(long[] blockRevisionCatalogPath) {
        this.blockDecl = new XBLBlockDecl(blockRevisionCatalogPath);
    }

    public XBDeclBlockType(Long[] blockRevisionCatalogPath) {
        this.blockDecl = new XBLBlockDecl(blockRevisionCatalogPath);
    }

    @Nullable
    @Override
    public XBBasicBlockType getAsBasicType() {
        return null;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof XBDeclBlockType) {
            return ((XBDeclBlockType) obj).blockDecl.equals(blockDecl);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return blockDecl.hashCode();
    }

    @Nonnull
    @Override
    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }
}
