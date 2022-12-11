/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.core.block.XBDBlockType;
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;

/**
 * Representation of current declaration context for block types.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBContext implements XBTypeConvertor {

    private XBTypeConvertor parent = null;
    private int startFrom = 1;
    private List<XBGroup> groups = new ArrayList<>();

    public XBContext() {
    }

    @Nullable
    @Override
    public XBGroup getGroupForId(int groupId) {
        if (groupId < startFrom) {
            return parent != null ? parent.getGroupForId(groupId) : null;
        }

        if (groupId - startFrom < groups.size()) {
            return groups.get(groupId - startFrom);
        }

        return null;
    }

    @Nullable
    @Override
    public XBDeclBlockType getDeclBlockType(XBFBlockType blockType) {
        XBGroup group = getGroupForId(blockType.getGroupID().getInt());
        return group != null ? new XBDeclBlockType(group.getBlockForId(blockType.getBlockID().getInt())) : null;
    }

    /**
     * Traverses thru all groups and check if block declaration matches to any
     * block type declared in current context.
     *
     * @param declType block declaration
     * @return fixed block type or null if no match
     */
    @Nullable
    @Override
    public XBFixedBlockType getFixedBlockType(XBDBlockType declType) {
        return getFixedBlockType(declType.getBlockDecl(), startFrom + groups.size() - 1);
    }

    @Nullable
    @Override
    public XBFixedBlockType getFixedBlockType(XBBlockDecl blockDecl, int groupIdLimit) {
        for (int groupIndex = 0; groupIndex <= groupIdLimit - startFrom; groupIndex++) {
            XBGroup group = groups.get(groupIndex);
            List<XBBlockDecl> blocks = group.getBlocks();
            for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
                XBBlockDecl groupBlockDecl = blocks.get(blockIndex);
                if (groupBlockDecl.equals(blockDecl)) {
                    return new XBFixedBlockType(groupIndex + startFrom, blockIndex);
                }
            }
        }

        return startFrom > 0 && parent != null ? parent.getFixedBlockType(blockDecl, startFrom - 1) : null;
    }

    @Nullable
    public XBTypeConvertor getParent() {
        return parent;
    }

    public void setParent(@Nullable XBTypeConvertor parent) {
        this.parent = parent;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @Nonnull
    public List<XBGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<XBGroup> groups) {
        this.groups = groups;
    }

    public int getGroupsCount() {
        return groups.size();
    }
}
