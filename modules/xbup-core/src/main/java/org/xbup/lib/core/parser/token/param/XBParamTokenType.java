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
package org.xbup.lib.core.parser.token.param;

/**
 * Enumeration of XBUP level 1 parameter token types.
 *
 * @version 0.1.23 2014/02/06
 * @author XBUP Project (http://xbup.org)
 */
public enum XBParamTokenType {
    /** Begin of parameter. */
    BEGIN,
    /** Block parameter. */
    BLOCK,
    /** List of blocks parameter. */
    LIST,
    /** End of parameter. */
    END
}
