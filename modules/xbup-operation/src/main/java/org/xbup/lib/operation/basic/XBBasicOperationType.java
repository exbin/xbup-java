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
package org.xbup.lib.operation.basic;

/**
 * Document operation type enumeration.
 *
 * @version 0.1.25 2015/04/25
 * @author XBUP Project (http://xbup.org)
 */
public enum XBBasicOperationType {

    /**
     * Node added.
     */
    ADD_NODE("Add node"),
    /**
     * Node deleted.
     */
    DELETE_NODE("Delete node"),
    /**
     * Node modified.
     */
    MODIFY_NODE("Modify node"),
    /**
     * Attribute modified.
     */
    MODIFY_ATTRIBUTE("Modify attribute"),
    /**
     * Data modified.
     */
    MODIFY_DATA("Modify data"),
    /**
     * Node swaped.
     */
    MOVE_NODE("Swap node");

    private final String caption;

    private XBBasicOperationType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
