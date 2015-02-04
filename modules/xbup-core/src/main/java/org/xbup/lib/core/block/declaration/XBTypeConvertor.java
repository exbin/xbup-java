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

import org.xbup.lib.core.block.XBDBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;

/**
 * Interface for context related type conversions.
 *
 * @version 0.1.25 2015/02/04
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTypeConvertor {

    /**
     * Returns group representation for given group ID.
     *
     * @param groupId group identification
     * @return group representation
     */
    public XBGroup getGroupForId(int groupId);

    /**
     * Converts fixed block type to declared type.
     *
     * @param fixedType fixed block type
     * @return declared block type of null if no match found
     */
    public XBDeclBlockType getDeclBlockType(XBFBlockType fixedType);

    /**
     * Converts declared type to fixed block type.
     *
     * @param declType declared block type
     * @return fixed block type or null if no match found
     */
    public XBFixedBlockType getFixedBlockType(XBDBlockType declType);
    
    /**
     * Converts declared type to fixed block type.
     *
     * @param blockDecl block declaration
     * @param groupIdLimit maximum group ID to limit group search
     * @return fixed block type or null if no match found
     */
    public XBFixedBlockType getFixedBlockType(XBBlockDecl blockDecl, int groupIdLimit);
}
