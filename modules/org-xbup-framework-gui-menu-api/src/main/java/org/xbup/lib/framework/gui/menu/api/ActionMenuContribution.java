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
package org.xbup.lib.framework.gui.menu.api;

import javax.swing.Action;

/**
 * Record for action as menu item contribution.
 *
 * @version 0.2.0 2016/01/09
 * @author ExBin Project (http://exbin.org)
 */
public class ActionMenuContribution implements MenuContribution {

    private final Action action;
    private final MenuPosition position;

    public ActionMenuContribution(Action action, MenuPosition position) {
        this.action = action;
        this.position = position;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public MenuPosition getMenuPosition() {
        return position;
    }
}
