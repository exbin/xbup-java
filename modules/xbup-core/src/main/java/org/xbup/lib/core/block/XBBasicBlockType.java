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
package org.xbup.lib.core.block;

/**
 * XBUP basic specification block types enumeration.
 *
 * @version 0.1.23 2014/02/07
 * @author XBUP Project (http://xbup.org)
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
     * Group declaration.
     */
    GROUP_DECLARATION(3),
    /**
     * Block declaration.
     */
    BLOCK_DECLARATION(4),
    /**
     * Definition of format.
     */
    FORMAT_DEFINITION(5),
    /**
     * Definition of group.
     */
    GROUP_DEFINITION(6),
    /**
     * Definition of block.
     */
    BLOCK_DEFINITION(7),
    /**
     * Definition of list.
     */
    LIST_DEFINITION(8),
    /**
     * Definition of revision.
     */
    REVISION_DEFINITION(9),
    /**
     * Link to Format Declaration in catalog.
     */
    FORMAT_DECLARATION_LINK(10),
    /**
     * Link to Group Declaration in catalog.
     */
    GROUP_DECLARATION_LINK(11),
    /**
     * Link to Block Declaration in catalog.
     */
    BLOCK_DECLARATION_LINK(12),
    /**
     * Link to Format Definition in catalog.
     */
    FORMAT_DEFINITION_LINK(13),
    /**
     * Link to Group Definition in catalog.
     */
    GROUP_DEFINITION_LINK(14),
    /**
     * Link to Block Definition in catalog.
     */
    BLOCK_DEFINITION_LINK(15);

    private final int blockId;

    private XBBasicBlockType(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockId() {
        return blockId;
    }
}
