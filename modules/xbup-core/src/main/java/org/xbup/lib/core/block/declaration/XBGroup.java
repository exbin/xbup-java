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
package org.xbup.lib.core.block.declaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Group structure as a list of block declarations.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBGroup {

    private List<XBBlockDecl> blocks;

    public XBGroup() {
        blocks = new ArrayList<>();
    }

    public XBGroup(List<XBBlockDecl> blocks) {
        this.blocks = blocks;
    }

    public List<XBBlockDecl> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<XBBlockDecl> blockDecl) {
        this.blocks = blockDecl;
    }

    public XBBlockDecl getBlockForId(int blockId) {
        return blockId < blocks.size() ? blocks.get(blockId) : null;
    }

    public void setBlockForId(XBBlockDecl blockDecl, int blockId) {
        if (blockId > blocks.size()) {
            for (int i = 0; i < blockId - blocks.size(); i++) {
                blocks.add(null);
            }
        }

        blocks.set(blockId, blockDecl);
    }
}
