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
 * Record of sub/child menu contribution.
 *
 * @version 0.2.0 2016/01/09
 * @author ExBin Project (http://exbin.org)
 */
public class SubMenuContribution implements MenuContribution {

    private final String menuId;
    private final String name;
    private final MenuPosition position;

    public SubMenuContribution(String menuId, String name, MenuPosition position) {
        this.menuId = menuId;
        this.name = name;
        this.position = position;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    @Override
    public MenuPosition getMenuPosition() {
        return position;
    }
}
