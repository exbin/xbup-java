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
package org.xbup.lib.core.block;

/**
 * Block data mode enumeration.
 *
 * Standard block can be either data block or node block.
 *
 * @version 0.1.23 2014/02/18
 * @author ExBin Project (http://exbin.org)
 */
public enum XBBlockDataMode {

    /**
     * Block is node block.
     *
     * It must have at least one attribute and can have child blocks.
     */
    NODE_BLOCK,
    /**
     * Block is data block.
     *
     * It can only contain data blob.
     */
    DATA_BLOCK
}
