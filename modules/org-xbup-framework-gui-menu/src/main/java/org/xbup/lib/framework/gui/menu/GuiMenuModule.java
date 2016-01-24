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

import org.xbup.lib.framework.gui.menu.api.ComponentClipboardHandler;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.menu.api.ToolBarGroup;
import org.xbup.lib.framework.gui.menu.api.ToolBarPosition;

/**
 * Implementation of XBUP framework menu module.
 *
 * @version 0.2.0 2016/01/23
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiMenuModule implements GuiMenuModuleApi {

    private XBApplication application;
    private ClipboardActions clipboardActions = null;
    private MenuHandler menuHandler = null;
    private ToolBarHandler toolBarHandler = null;

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

    private ToolBarHandler getToolBarHandler() {
        if (toolBarHandler == null) {
            toolBarHandler = new ToolBarHandler();
        }

        return toolBarHandler;
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
    public void registerMenuItem(String menuId, String pluginId, JMenu menu, MenuPosition position) {
        getMenuHandler().registerMenuItem(menuId, pluginId, menu, position);
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
    public void registerClipboardMenuItems(String menuId, String moduleId, SeparationMode separationMode) {
        getClipboardActions();
        registerMenuGroup(menuId, new MenuGroup(CLIPBOARD_ACTIONS_MENU_GROUP_ID, new MenuPosition(PositionMode.TOP), separationMode));
        registerMenuItem(menuId, moduleId, clipboardActions.getCutAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(menuId, moduleId, clipboardActions.getCopyAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(menuId, moduleId, clipboardActions.getPasteAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(menuId, moduleId, clipboardActions.getDeleteAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
        registerMenuItem(menuId, moduleId, clipboardActions.getSelectAllAction(), new MenuPosition(CLIPBOARD_ACTIONS_MENU_GROUP_ID));
    }

    @Override
    public void buildToolBar(JToolBar targetToolBar, String toolBarId) {
        getToolBarHandler().buildToolBar(targetToolBar, toolBarId);
    }

    @Override
    public void registerToolBar(String toolBarId, String pluginId) {
        getToolBarHandler().registerToolBar(toolBarId, pluginId);
    }

    @Override
    public void registerToolBarGroup(String toolBarId, ToolBarGroup toolBarGroup) {
        getToolBarHandler().registerToolBarGroup(toolBarId, toolBarGroup);
    }

    @Override
    public void registerToolBarItem(String toolBarId, String pluginId, Action action, ToolBarPosition position) {
        getToolBarHandler().registerToolBarItem(toolBarId, pluginId, action, position);
    }

    @Override
    public void registerMenuClipboardActions() {
        registerClipboardMenuItems(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, SeparationMode.NONE);
    }

    @Override
    public void registerToolBarClipboardActions() {
        getClipboardActions();
        registerToolBarGroup(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, new ToolBarGroup(CLIPBOARD_ACTIONS_TOOL_BAR_GROUP_ID, new ToolBarPosition(PositionMode.TOP)));
        registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, clipboardActions.getCutAction(), new ToolBarPosition(CLIPBOARD_ACTIONS_TOOL_BAR_GROUP_ID));
        registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, clipboardActions.getCopyAction(), new ToolBarPosition(CLIPBOARD_ACTIONS_TOOL_BAR_GROUP_ID));
        registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, clipboardActions.getPasteAction(), new ToolBarPosition(CLIPBOARD_ACTIONS_TOOL_BAR_GROUP_ID));
        registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, clipboardActions.getDeleteAction(), new ToolBarPosition(CLIPBOARD_ACTIONS_TOOL_BAR_GROUP_ID));
    }

    @Override
    public void registerClipboardHandler(ComponentClipboardHandler clipboardHandler) {
        getClipboardActions().setClipboardHandler(clipboardHandler);
    }
    
    @Override
    public void updateClipboardActions() {
        getClipboardActions().updateClipboardActions();
    }
}
