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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import org.xbup.lib.framework.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.api.XBModuleRepositoryUtils;

/**
 * Interface for XBUP framework menu module.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
public interface GuiMenuModuleApi extends XBApplicationModulePlugin {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(GuiMenuModuleApi.class);
    public static final String CLIPBOARD_ACTIONS_MENU_GROUP_ID = MODULE_ID + ".clipboardActionsMenuGroup";
    public static final String CLIPBOARD_ACTIONS_TOOL_BAR_GROUP_ID = MODULE_ID + ".clipboardActionsToolBarGroup";
    public static final String DIALOG_MENUITEM_EXT = "...";

    /**
     * Returns menu using given identificator.
     *
     * @param targetMenu target menu
     * @param menuId menu identificator
     */
    void buildMenu(JPopupMenu targetMenu, String menuId);

    /**
     * Returns menu using given identificator.
     *
     * @param targetMenuBar target menu bar
     * @param menuId menu identificator
     */
    void buildMenu(JMenuBar targetMenuBar, String menuId);

    /**
     * Registers menu associating it with given identificator.
     *
     * @param menuId menu identificator
     * @param pluginId plugin identificator
     */
    void registerMenu(String menuId, String pluginId);

    /**
     * Registers menu group for particular menu.
     *
     * @param menuId menu identificator
     * @param menuGroup menu group
     */
    void registerMenuGroup(String menuId, MenuGroup menuGroup);

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

    /**
     * Returns clipboard/editing actions.
     *
     * @return clipboard editing actions
     */
    ClipboardActionsApi getClipboardActions();

    /**
     * Registers menu clipboard actions.
     */
    void registerMenuClipboardActions();

    /**
     * Returns tool bar using given identificator.
     *
     * @param targetToolBar
     * @param toolBarId
     */
    void buildToolBar(JToolBar targetToolBar, String toolBarId);

    /**
     * Registers tool bar associating it with given identificator.
     *
     * @param toolBarId
     * @param pluginId
     */
    void registerToolBar(String toolBarId, String pluginId);

    /**
     * Registers tool bar group for particular tool bar.
     *
     * @param toolBarId
     * @param toolBarGroup
     */
    void registerToolBarGroup(String toolBarId, ToolBarGroup toolBarGroup);

    /**
     * Registers item as a child item for given tool bar.
     *
     * @param toolBarId
     * @param pluginId
     * @param action
     * @param position
     */
    void registerToolBarItem(String toolBarId, String pluginId, Action action, ToolBarPosition position);

    /**
     * Registers tool bar clipboard actions.
     */
    void registerToolBarClipboardActions();

    void registerClipboardMenuItems(String menuId, String moduleId, SeparationMode separationMode);

    /**
     * Registers clipboard handler for main clipboard actions.
     *
     * @param clipboardHandler
     */
    void registerClipboardHandler(ComponentClipboardHandler clipboardHandler);

    /**
     * Updates state of the clipboard actions.
     */
    public void updateClipboardActions();

    /**
     * Returns true if given menu group exists.
     *
     * @param menuId
     * @param groupId
     * @return true if group exists
     */
    public boolean menuGroupExists(String menuId, String groupId);
}
