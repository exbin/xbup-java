/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block.declaration;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Group structure as a list of block declarations.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBGroup {

    @Nonnull
    private List<XBBlockDecl> blockDecls;

    public XBGroup() {
        blockDecls = new ArrayList<>();
    }

    public XBGroup(List<XBBlockDecl> blockDecls) {
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
    public void setBlocks(List<XBBlockDecl> blockDecls) {
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
    public void setBlockForId(XBBlockDecl blockDecl, int blockId) {
        if (blockId > blockDecls.size()) {
            for (int i = 0; i < blockId - blockDecls.size(); i++) {
                blockDecls.add(null);
            }
        }

        blockDecls.set(blockId, blockDecl);
    }
}
