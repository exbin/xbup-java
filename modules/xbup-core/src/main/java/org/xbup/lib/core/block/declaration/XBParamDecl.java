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

/**
 * XBUP level 1 parameter definition.
 * 
 * Parameter is in one of four modes: Join, Consist, Join List, Consist List.
 * Without declaration (blockDecl) it means single atribute for join and data
 * block for consist mode and their lists respectively.
 *
 * @version 0.1 wr20.0 2010/12/18
 * @author XBUP Project (http://xbup.org)
 */
public interface XBParamDecl {

    /**
     * Get block declaration.
     *
     * @return block declaration
     */
    public XBBlockDecl getBlockDecl();

    /**
     * Get flag whether parameter is list of items.
     *
     * @return true if parameter is list
     */
    public boolean isListFlag();

    /**
     * Get flag whether parameter is joined.
     *
     * @return true if parameter is joined
     */
    public boolean isJoinFlag();

    /**
     * Get type of this parameter.
     *
     * @return type of this parameter
     */
    public XBParamType getParamType();

    /**
     * Get new instance of the same declaration, but as list.
     *
     * @return parameter declaration
     */
    public XBParamDecl convertToNonList();
}
