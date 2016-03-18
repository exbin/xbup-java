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
 * Tool bar position.
 *
 * @version 0.2.0 2016/01/11
 * @author ExBin Project (http://exbin.org)
 */
public class ToolBarPosition {

    private final PositionMode basicMode;
    private final String groupId;

    public ToolBarPosition(PositionMode basicMode) {
        this.basicMode = basicMode;
        groupId = null;
    }

    public ToolBarPosition(String groupId) {
        basicMode = null;
        this.groupId = groupId;
    }

    public PositionMode getBasicMode() {
        return basicMode;
    }

    public String getGroupId() {
        return groupId;
    }
}
