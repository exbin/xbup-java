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

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;

/**
 * Interface for XBUP framework menu module.
 *
 * @version 0.2.0 2016/01/04
 * @author XBUP Project (http://xbup.org)
 */
public interface GuiMenuModuleApi extends XBApplicationModulePlugin {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(GuiMenuModuleApi.class);

    /**
     * Returns menu using given identificator.
     *
     * @param menuId menu identificator
     * @return menu or null if such menu is not available
     */
    JMenu getMenu(String menuId);

    /**
     * Registers menu associating it with given identificator.
     *
     * @param menuId menu identificator
     * @param pluginId plugin identificator
     */
    void registerMenu(String menuId, String pluginId);

    /**
     * Registers menu as a child item for given menu.
     *
     * @param menuId
     * @param pluginId
     * @param item
     * @param position menu position
     */
    void registerMenuItem(String menuId, String pluginId, JMenu item, MenuPosition position);

    /**
     * Registers menu item as a child item for given menu.
     *
     * @param menuId
     * @param pluginId
     * @param item
     * @param position menu position
     */
    void registerMenuItem(String menuId, String pluginId, JMenuItem item, MenuPosition position);

    /**
     * Registers menu item as a child item for given menu.
     *
     * @param menuId
     * @param pluginId
     * @param action
     * @param position menu position
     */
    void registerMenuItem(String menuId, String pluginId, Action action, MenuPosition position);

    /**
     * Registers menu item as a child item for given menu.
     *
     * @param menuId
     * @param pluginId
     * @param subMenuId
     * @param subMenuName
     * @param position menu position
     */
    void registerMenuItem(String menuId, String pluginId, String subMenuId, String subMenuName, MenuPosition position);
}
