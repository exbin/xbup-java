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
package org.xbup.tool.editor.base.api;

import java.awt.Component;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

/**
 * Interface for application's menus.
 *
 * @version 0.1.22 2013/03/01
 * @author XBUP Project (http://xbup.org)
 */
public interface MenuManagement {

    /**
     * Add all items from given menu instance into menu manager.
     *
     * @param menu
     * @param positionMode
     * @param menuType
     */
    public void extendMenu(JMenu menu, BasicMenuType menuType, MenuPositionMode positionMode);

    /**
     * Add given menu component into menu manager.
     *
     * @param menuItem
     * @param menuType
     * @param mode
     */
    public void addMenuItem(Component menuItem, BasicMenuType menuType, MenuPositionMode mode);

    /**
     * Insert menu into menubar into main menu manager.
     *
     * @param menu
     * @param positionMode
     */
    public void insertMenu(JMenu menu, MenuPositionMode positionMode);

    /**
     * Add all items from given toolbar instance into menu manager.
     *
     * @param toolBar
     */
    public void extendToolBar(JToolBar toolBar);

    /**
     * Copy and insert main popup menu into given popup menu.
     *
     * @param popupMenu
     * @param position
     */
    public void insertMainPopupMenu(JPopupMenu popupMenu, int position);
}
