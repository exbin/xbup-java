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
package org.exbin.framework.gui.menu.api;

/**
 * Menu position.
 *
 * @version 0.2.0 2016/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class MenuPosition {

    private final PositionMode basicMode;
    private final NextToMode nextToMode;
    private final String groupId;

    public MenuPosition(PositionMode basicMode) {
        this.basicMode = basicMode;
        this.groupId = null;
        this.nextToMode = null;
    }

    public MenuPosition(String groupId) {
        this.basicMode = null;
        this.groupId = groupId;
        this.nextToMode = null;
    }

    public MenuPosition(NextToMode nextToMode, String groupId) {
        this.basicMode = null;
        this.groupId = groupId;
        this.nextToMode = nextToMode;
    }

    public PositionMode getBasicMode() {
        return basicMode;
    }

    public String getGroupId() {
        return groupId;
    }

    public NextToMode getNextToMode() {
        return nextToMode;
    }
}
