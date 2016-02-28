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
package org.xbup.lib.core.block;

import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Block type defined as fixed group and block indexes.
 *
 * Class is supposed to be immutable.
 *
 * @version 0.1.25 2015/05/03
 * @author ExBin Project (http://exbin.org)
 */
public class XBFixedBlockType implements XBFBlockType {

    private final UBNatural groupID;
    private final UBNatural blockID;

    public static final XBFixedBlockType UNKNOWN_BLOCK_TYPE = new XBFixedBlockType();

    /**
     * Creates fixed block type UNKNOWN_BLOCK(0).
     */
    public XBFixedBlockType() {
        groupID = new UBNat32(0);
        blockID = new UBNat32(0);
    }

    public XBFixedBlockType(UBNatural groupID, UBNatural blockID) {
        this.groupID = groupID;
        this.blockID = blockID;
    }

    public XBFixedBlockType(long groupID, long blockID) {
        this.groupID = new UBNat32(groupID);
        this.blockID = new UBNat32(blockID);
    }

    public XBFixedBlockType(int groupID, int blockID) {
        this.groupID = new UBNat32(groupID);
        this.blockID = new UBNat32(blockID);
    }

    public XBFixedBlockType(XBBasicBlockType type) {
        groupID = new UBNat32(0);
        blockID = new UBNat32(type.ordinal());
    }

    @Override
    public XBBasicBlockType getAsBasicType() {
        if (groupID.getLong() == 0) {
            return XBBasicBlockType.valueOf((int) blockID.getLong());
        }

        return null;
    }

    @Override
    public UBNatural getGroupID() {
        return groupID;
    }

    @Override
    public UBNatural getBlockID() {
        return blockID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBFBlockType) {
            return ((XBFBlockType) obj).getGroupID().getLong() == 0 && groupID.getLong() == 0 && (((XBFBlockType) obj).getBlockID().getLong() == blockID.getLong());
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.groupID != null ? this.groupID.hashCode() : 0);
        hash = 29 * hash + (this.blockID != null ? this.blockID.hashCode() : 0);
        return hash;
    }

    public int getSizeUB() {
        return groupID.getSizeUB() + blockID.getSizeUB();
    }
}
