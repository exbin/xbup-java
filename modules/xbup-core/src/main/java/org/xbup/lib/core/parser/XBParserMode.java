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
 * Enumeration of possible parsing modes.
 *
 * @version 0.1.23 2013/02/05
 * @author XBUP Project (http://xbup.org)
 */
public enum XBParserMode {

    /**
     * Parsing include both header and extended part / tail.
     */
    FULL,
    /**
     * Parsing only generate single block - skips both header and tail.
     */
    SINGLE_BLOCK,
    /**
     * Skip header.
     */
    SKIP_HEAD,
    /**
     * Skip extended part / tail.
     */
    SKIP_EXTENDED
}
