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
package org.xbup.lib.xbcommand.basic;

/**
 * Undo operation type enumeration
 *
 * @version 0.1 wr16.0 2008/09/14
 * @author XBUP Project (http://xbup.org)
 */
public enum XBBasicCommandType {
    /** Node added */
    NODE_ADD,
    /** Node deleted */
    NODE_DEL,
    /** Node modified */
    NODE_MOD,
    /** Count of attribte changed */
    NODE_RSZ_ATTR,
    /** Attribute modified */
    NODE_MOD_ATTR,
    /** Data podified */
    NODE_MOD_DATA,
    /** Data size changed */
    NODE_RSZ_DATA,
    /** Node swaped */
    NODE_SWAP
}
