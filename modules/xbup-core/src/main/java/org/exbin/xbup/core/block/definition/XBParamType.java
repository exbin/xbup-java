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
package org.exbin.xbup.core.block.definition;

/**
 * XBUP level 1 parameter type enumeration.
 *
 * @version 0.1.25 2015/02/06
 * @author ExBin Project (http://exbin.org)
 */
public enum XBParamType {

    /**
     * Consist parameter.
     *
     * Appends given block as subblock or any block if unspecified.
     */
    CONSIST,
    /**
     * Join parameter.
     *
     * Appends attributes and subblocks of given block or single attribute if
     * unspecified.
     */
    JOIN,
    /**
     * List consist parameter.
     *
     * Appends list of subblocks of given block type or any block. List size is
     * included first as UBENatural attribute (including infinity).
     */
    LIST_CONSIST,
    /**
     * List join parameter.
     *
     * Appends list of attributes and subblocks of given block type or plain
     * attributes. List size is included first as UBNatural attribute.
     */
    LIST_JOIN;

    public boolean isList() {
        return this == LIST_CONSIST || this == LIST_JOIN;
    }

    public boolean isConsist() {
        return this == CONSIST || this == LIST_CONSIST;
    }

    public boolean isJoin() {
        return this == JOIN || this == LIST_JOIN;
    }
}
