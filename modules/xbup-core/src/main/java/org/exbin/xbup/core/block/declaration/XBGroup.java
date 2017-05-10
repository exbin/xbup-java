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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Group structure as a list of block declarations.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public class XBGroup {

    @Nonnull
    private List<XBBlockDecl> blockDecls;

    public XBGroup() {
        blockDecls = new ArrayList<>();
    }

    public XBGroup(@Nonnull List<XBBlockDecl> blockDecls) {
        this.blockDecls = blockDecls;
    }

    /**
     * Returns list of block declarations belonging to this group.
     *
     * @return list of block declarations
     */
    @Nonnull
    public List<XBBlockDecl> getBlocks() {
        return blockDecls;
    }

    /**
     * Sets list of block declarations belonging to this group.
     *
     * @param blockDecls list of block declarations
     */
    public void setBlocks(@Nonnull List<XBBlockDecl> blockDecls) {
        this.blockDecls = blockDecls;
    }

    /**
     * Returns block declaration for given block Id.
     *
     * @param blockId block Id
     * @return block declaration or null if not present
     */
    @Nullable
    public XBBlockDecl getBlockForId(int blockId) {
        return blockId < blockDecls.size() ? blockDecls.get(blockId) : null;
    }

    /**
     * Sets block declaration with given block Id.
     *
     * @param blockDecl block declaration
     * @param blockId block Id
     */
    public void setBlockForId(@Nonnull XBBlockDecl blockDecl, int blockId) {
        if (blockId > blockDecls.size()) {
            for (int i = 0; i < blockId - blockDecls.size(); i++) {
                blockDecls.add(null);
            }
        }

        blockDecls.set(blockId, blockDecl);
    }
}
