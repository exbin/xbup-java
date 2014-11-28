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

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;

/**
 * Representation of current declaration context for block types.
 *
 * @version 0.1.24 2014/11/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBContext {

    private XBContext parent = null;
    private int startFrom = 1;
    private List<XBGroup> groups = new ArrayList<>();

    public XBContext() {
    }

    public XBGroup getGroupForId(int groupId) {
        if (groupId < startFrom) {
            return parent != null ? parent.getGroupForId(groupId) : null;
        }

        if (groupId - startFrom < groups.size()) {
            return groups.get(groupId - startFrom);
        }

        return null;
    }

    public XBBlockDecl getBlockDecl(XBFBlockType blockType) {
        XBGroup group = getGroupForId(blockType.getGroupID().getInt());
        return group != null ? group.getBlockForId(blockType.getBlockID().getInt()) : null;
    }

    /**
     * Traverses thru all groups and check if block declaration matches to any
     * block type declared in current context.
     *
     * @param blockDecl block declaration
     * @return fixed block type or null if no match
     */
    public XBFixedBlockType getFixedBlockType(XBBlockDecl blockDecl) {
        return getFixedBlockType(blockDecl, startFrom + groups.size() - 1);
    }

    private XBFixedBlockType getFixedBlockType(XBBlockDecl blockDecl, int groupIdLimit) {
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

        return startFrom > 0 ? parent.getFixedBlockType(blockDecl, startFrom - 1) : null;
    }

    public XBContext getParent() {
        return parent;
    }

    public void setParent(XBContext parent) {
        this.parent = parent;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    public List<XBGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<XBGroup> groups) {
        this.groups = groups;
    }
}
