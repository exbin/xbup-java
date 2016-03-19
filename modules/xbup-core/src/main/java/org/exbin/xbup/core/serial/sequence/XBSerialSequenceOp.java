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
package org.exbin.xbup.core.serial.sequence;

/**
 * XBUP level 1 serialization sequence operation types enumeration.
 *
 * @version 0.1.24 2015/01/21
 * @author ExBin Project (http://exbin.org)
 */
public enum XBSerialSequenceOp {

    /**
     * XBTToken operation.
     */
    TOKEN,
    /**
     * Join block sequence operation.
     */
    JOIN,
    /**
     * Consist of subblock sequence operation.
     */
    CONSIST,
    /**
     * Join list of blocks sequence operation.
     */
    LIST_JOIN,
    /**
     * Consist of list of subblocks sequence operation.
     */
    LIST_CONSIST
}
