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
package org.exbin.xbup.core.block;

import javax.annotation.Nullable;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Block type defined as fixed group and block indexes.
 *
 * Class is supposed to be immutable.
 *
 * @author ExBin Project (https://exbin.org)
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
    public boolean equals(@Nullable Object obj) {
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
