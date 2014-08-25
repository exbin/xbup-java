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

import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Context-based block type with cached block declaration.
 *
 * @version 0.1.20 2010/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBContextBlockType implements XBDeclBlockType {

    private XBBlockDecl cachedBlockDecl;
    private XBContext context;
    private int blockId;
    private int groupId;

    /**
     * Creates a new instance of XBDBlockType.
     * 
     * @param groupId group identification number
     * @param blockId block identification number
     */
    public XBContextBlockType(int groupId, int blockId) {
        this.blockId = blockId;
        this.groupId = groupId;
        this.context = null;
        resetCache();
    }

    public XBContextBlockType(int groupId, int blockId, XBContext context) {
        this.blockId = blockId;
        this.groupId = groupId;
        this.context = context;
        resetCache();
    }

    public XBContextBlockType(XBBlockDecl blockDecl, XBContext context) {
        cachedBlockDecl = blockDecl;
        XBBlockType blockType = context.getBlockType(blockDecl);
        this.blockId = blockType.getBlockID().getInt();
        this.groupId = blockType.getGroupID().getInt();
        this.context = context;
    }

    /** Returns 0 - Claims to be unknown */
    @Override
    public UBNatural getGroupID() {
        return new UBNat32(groupId);
    }

    /** Returns 0 - Claims to be unknown */
    @Override
    public UBNatural getBlockID() {
        return new UBNat32(blockId);
    }

    /**
     * @return the blockDecl
     */
    @Override
    public XBBlockDecl getBlockDecl() {
        return cachedBlockDecl;
    }

    /**
     * @param blockDecl the blockDecl to set
     */
    public void setBlockDecl(XBBlockDecl blockDecl) {
        cachedBlockDecl = blockDecl;
        if (getContext() == null) {
            blockId = 0;
            groupId = 0;
        } else {
            XBBlockType blockType = getContext().getBlockType(blockDecl);
            this.blockId = blockType.getBlockID().getInt();
            this.groupId = blockType.getGroupID().getInt();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBContextBlockType) {
            return ((XBContextBlockType) obj).getBlockDecl().equals(getBlockDecl()); // && (((XBDBlockType) obj).revision).equals(revision);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 1;
/*        int hash = 5;
        hash = 19 * hash + (this.blockDecl != null ? this.blockDecl.hashCode() : 0);
        hash = 19 * hash + (this.revision != null ? this.revision.hashCode() : 0);
        return hash; */
    }

    /**
     * @return the context
     */
    public XBContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(XBContext context) {
        if (this.context != context) {
            this.context = context;
            resetCache();
        } else {
            this.context = context;
        }
    }

    private void resetCache() {
        if (context != null) {
            cachedBlockDecl = context.getBlockType(groupId, blockId);
        } else {
            cachedBlockDecl = null;
        }
    }
}
