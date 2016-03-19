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
package org.exbin.xbup.core.serial.child;

/**
 * Enumeration of possible child serialization states.
 *
 * @version 0.1.24 2014/08/23
 * @author ExBin Project (http://exbin.org)
 */
public enum XBChildSerialState {

    /**
     * Start of serialization of the given child.
     */
    BLOCK_BEGIN,
    /**
     * Inside or at the end of attribute part.
     */
    ATTRIBUTE_PART,
    /**
     * Type processing.
     */
    TYPE,
    /**
     * After first and before last attribute.
     */
    ATTRIBUTES,
    /**
     * Inside or at the end of data part.
     */
    DATA,
    /**
     * Children data processed.
     */
    CHILDREN,
    /**
     * End of block.
     */
    BLOCK_END,
    /**
     * End of serialization of the given child.
     */
    EOF,
}
