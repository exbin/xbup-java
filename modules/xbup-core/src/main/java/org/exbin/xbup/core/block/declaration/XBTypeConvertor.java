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
package org.exbin.xbup.core.block.declaration;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBDBlockType;
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;

/**
 * Interface for context related type conversions.
 *
 * @version 0.1.25 2015/02/04
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTypeConvertor {

    /**
     * Returns group representation for given group ID.
     *
     * @param groupId group identification
     * @return group representation
     */
    @Nullable
    XBGroup getGroupForId(int groupId);

    /**
     * Converts fixed block type to declared type.
     *
     * @param fixedType fixed block type
     * @return declared block type of null if no match found
     */
    @Nullable
    XBDeclBlockType getDeclBlockType(XBFBlockType fixedType);

    /**
     * Converts declared type to fixed block type.
     *
     * @param declType declared block type
     * @return fixed block type or null if no match found
     */
    @Nullable
    XBFixedBlockType getFixedBlockType(XBDBlockType declType);
    
    /**
     * Converts declared type to fixed block type.
     *
     * @param blockDecl block declaration
     * @param groupIdLimit maximum group ID to limit group search
     * @return fixed block type or null if no match found
     */
    @Nullable
    XBFixedBlockType getFixedBlockType(XBBlockDecl blockDecl, int groupIdLimit);
}
