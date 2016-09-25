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
package org.exbin.xbup.core.parser;

/**
 * Enumeration of possible parsing modes.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public enum XBParserMode {

    /**
     * Parsing includes both header and tail data parts.
     */
    FULL,
    /**
     * Parsing only generates single block - skips both header and tail.
     */
    SINGLE_BLOCK,
    /**
     * Skips header.
     */
    SKIP_HEAD,
    /**
     * Skips tail.
     */
    SKIP_TAIL
}
