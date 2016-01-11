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
package org.xbup.lib.framework.gui.menu.api;

/**
 * Enumeration of menu position modes using build-in groups.
 *
 * @version 0.2.0 2015/12/16
 * @author XBUP Project (http://xbup.org)
 */
public enum PositionMode {

    /**
     * Top position.
     */
    TOP,
    /**
     * End of the top position.
     */
    TOP_LAST,
    /**
     * Default: Normal position in the middle section.
     */
    MIDDLE,
    /**
     * Normal position at the end of the middle section.
     */
    MIDDLE_LAST,
    /**
     * Bottom position.
     */
    BOTTOM,
    /**
     * End of the bottom position.
     */
    BOTTOM_LAST
}
