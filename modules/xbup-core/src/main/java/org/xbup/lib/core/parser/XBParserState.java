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
package org.xbup.lib.core.parser;

/**
 * Enumeration of possible parsing states.
 *
 * @version 0.1.24 2014/09/30
 * @author XBUP Project (http://xbup.org)
 */
public enum XBParserState {

    /**
     * Start of file (before Head).
     */
    START,
    /**
     * Start of block or block expected.
     */
    BLOCK_BEGIN,
    /**
     * Inside or at the end of block type.
     */
    BLOCK_TYPE,
    /**
     * Inside or at the end of attribute part.
     */
    ATTRIBUTE_PART,
    /**
     * Inside or at the end of data part.
     */
    DATA_PART,
    /**
     * Inside or at the end of children part.
     */
    CHILDREN_PART,
    /**
     * Inside extended part.
     */
    EXTENDED_AREA,
    /**
     * End of block or end of multiple blocks.
     */
    BLOCK_END,
    /**
     * End of parsing.
     */
    EOF
}
