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
 * Menu group definition.
 *
 * @version 0.2.0 2016/01/09
 * @author ExBin Project (http://exbin.org)
 */
public class MenuGroup {

    private final String groupId;
    private final MenuPosition position;
    private final SeparationMode separationMode;

    public MenuGroup(String groupId, MenuPosition position) {
        this.groupId = groupId;
        this.position = position;
        separationMode = SeparationMode.NONE;
    }

    public MenuGroup(String groupId, MenuPosition position, SeparationMode separationMode) {
        this.groupId = groupId;
        this.position = position;
        this.separationMode = separationMode;
    }

    public String getGroupId() {
        return groupId;
    }

    public MenuPosition getPosition() {
        return position;
    }

    public SeparationMode getSeparationMode() {
        return separationMode;
    }
}
