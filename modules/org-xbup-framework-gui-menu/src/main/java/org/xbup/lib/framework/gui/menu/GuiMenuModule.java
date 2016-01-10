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
package org.xbup.lib.framework.gui.menu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.MenuPositionMode;

/**
 * Implementation of XBUP framework menu module.
 *
 * @version 0.2.0 2016/01/10
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiMenuModule implements GuiMenuModuleApi {

    public static final String CLIPBOARD_ACTIONS_MENU_GROUP_ID = MODULE_ID + ".clipboardActionsGroup";
    private XBApplication application;
    private ClipboardActions clipboardActions = null;
    private MenuHandler menuHandler = null;

    /**
     * Menu records: menu id -> menu definition.
     */
    private Map<String, MenuDefinition> menus = new HashMap<>();

    /**
     * Menu group records: menu id -> menu group.
     */
    private Map<String, List<MenuGroup>> menuGroups = new HashMap<>();

    /**
     * Menu modified flags.
     */
    private Set<String> menuModified = new HashSet<>();

    /**
     * Map of plugins usage per menu id.
     */
    private Map<String, String> pluginsUsage = new HashMap<>();

    public GuiMenuModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    @Override
    public ClipboardActions getClipboardActions() {
        if (clipboardActions == null) {
            clipboardActions = new ClipboardActions();
            clipboardActions.init();
        }

        return clipboardActions;
    }

    private MenuHandler getMenuHandler() {
        if (menuHandler == null) {
            menuHandler = new MenuHandler();
        }

        return menuHandler;
    }

    @Override
    public void buildMenu(JPopupMenu targetMenu, String menuId) {
        getMenuHandler().buildMenu(targetMenu, menuId);
    }

    @Override
    public void buildMenu(JMenuBar targetMenuBar, String menuId) {
        getMenuHandler().buildMenu(targetMenuBar, menuId);
    }

    @Override
    public void registerMenu(String menuId, String pluginId) {
        getMenuHandler().registerMenu(menuId, pluginId);
    }

    @Override
    public void registerMenuGroup(String menuId, MenuGroup menuGroup) {
        getMenuHandler().registerMenuGroup(menuId, menuGroup);
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, JMenu item, MenuPosition position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, JMenuItem item, MenuPosition position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, Action action, MenuPosition position) {
        getMenuHandler().registerMenuItem(menuId, pluginId, action, position);
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, String subMenuId, String subMenuName, MenuPosition position) {
        getMenuHandler().registerMenuItem(menuId, pluginId, subMenuId, subMenuName, position);
    }

    @Override
    public void registerClipboardActions() {
        getClipboardActions();
        registerMenuGroup(GuiFrameModuleApi.EDIT_MENU_ID, new MenuGroup(CLIPBOARD_ACTIONS_MENU_GROUP_ID, new MenuPosition(MenuPositionMode.TOP)));
        registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, clipboardActions.getCutAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, clipboardActions.getCopyAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, clipboardActions.getPasteAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, clipboardActions.getDeleteAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, clipboardActions.getSelectAllAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
    }
}
