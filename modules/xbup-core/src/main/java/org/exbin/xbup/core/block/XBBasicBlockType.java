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
package org.exbin.xbup.core.block;

import java.util.HashMap;
import java.util.Map;

/**
 * XBUP basic specification block types enumeration.
 *
 * @version 0.1.24 2014/12/05
 * @author ExBin Project (http://exbin.org)
 */
public enum XBBasicBlockType {

    /**
     * Should be data block, but has attributes.
     */
    UNKNOWN_BLOCK(0),
    /**
     * Block and group types declaration.
     */
    DECLARATION(1),
    /**
     * Format declaration.
     */
    FORMAT_DECLARATION(2),
    /**
     * Definition of format.
     */
    FORMAT_DEFINITION(3),
    /**
     * Format join parameter.
     */
    FORMAT_JOIN_PARAMETER(4),
    /**
     * Format consist parameter.
     */
    FORMAT_CONSIST_PARAMETER(5),
    /**
     * Group declaration.
     */
    GROUP_DECLARATION(6),
    /**
     * Definition of group.
     */
    GROUP_DEFINITION(7),
    /**
     * Group join parameter.
     */
    GROUP_JOIN_PARAMETER(8),
    /**
     * Group consist parameter.
     */
    GROUP_CONSIST_PARAMETER(9),
    /**
     * Block declaration.
     */
    BLOCK_DECLARATION(10),
    /**
     * Definition of block.
     */
    BLOCK_DEFINITION(11),
    /**
     * Block join parameter.
     */
    BLOCK_JOIN_PARAMETER(12),
    /**
     * Block consist parameter.
     */
    BLOCK_CONSIST_PARAMETER(13),
    /**
     * Block list join parameter.
     */
    BLOCK_LIST_JOIN_PARAMETER(14),
    /**
     * Block list consist parameter.
     */
    BLOCK_LIST_CONSIST_PARAMETER(15),
    /**
     * Definition of revision.
     */
    REVISION_DEFINITION(16),
    /**
     * Revision parameter.
     */
    REVISION_PARAMETER(17);

    private final int blockId;

    private static final Map<Integer, XBBasicBlockType> map = new HashMap<>();

    static {
        for (XBBasicBlockType blockType : XBBasicBlockType.values()) {
            map.put(blockType.blockId, blockType);
        }
    }

    private XBBasicBlockType(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockId() {
        return blockId;
    }

    public static XBBasicBlockType valueOf(int blockId) {
        return map.get(blockId);
    }

    private static final int size = values().length;

    public static int getSize() {
        return size;
    }
}
