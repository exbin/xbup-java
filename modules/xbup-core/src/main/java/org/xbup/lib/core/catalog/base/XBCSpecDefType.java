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
package org.xbup.lib.core.catalog.base;

/**
 * Enumeration of spec definition types.
 *
 * @version 0.1.22 2012/01/11
 * @author XBUP Project (http://xbup.org)
 */
public enum XBCSpecDefType {

    /**
     * Adds given block as subblock or data block if unspecified.
     */
    CONS,
    /**
     * Append attributes and subblocks of given block or single attribute if
     * unspecified.
     */
    JOIN,
    /**
     * List of given block starting with count of blocks as UBENatural
     * (including infinity).
     */
    LIST_CONS,
    /**
     * Append finite list of attributes and subblocks of given block type or
     * list of attributes.
     */
    LIST_JOIN
    // TODO: Specifify parameter type for: If parameter not specified include single unspecified subblock
}
