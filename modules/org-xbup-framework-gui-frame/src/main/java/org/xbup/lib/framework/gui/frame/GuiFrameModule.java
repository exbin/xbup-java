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
package org.xbup.lib.framework.gui.frame;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.frame.api.XBApplicationFrameHandler;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.MenuPositionMode;

/**
 * Implementation of XBUP framework undo/redo module.
 *
 * @version 0.2.0 2016/01/08
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiFrameModule implements GuiFrameModuleApi {

    private XBApplication application;
    private XBApplicationFrame frame;

    public GuiFrameModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(MAIN_MENU_ID, MODULE_ID);
        menuModule.registerMenu(FILE_MENU_ID, MODULE_ID);
        menuModule.registerMenu(EDIT_MENU_ID, MODULE_ID);
        menuModule.registerMenu(VIEW_MENU_ID, MODULE_ID);
        menuModule.registerMenu(TOOLS_MENU_ID, MODULE_ID);
        menuModule.registerMenu(OPTIONS_MENU_ID, MODULE_ID);
        menuModule.registerMenu(HELP_MENU_ID, MODULE_ID);

        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, FILE_MENU_ID, "File", new MenuPosition(MenuPositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, EDIT_MENU_ID, "Edit", new MenuPosition(MenuPositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, VIEW_MENU_ID, "View", new MenuPosition(MenuPositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, TOOLS_MENU_ID, "Tools", new MenuPosition(MenuPositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, OPTIONS_MENU_ID, "Options", new MenuPosition(MenuPositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, HELP_MENU_ID, "Help", new MenuPosition(MenuPositionMode.TOP));
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    @Override
    public Frame getFrame() {
        XBApplicationFrameHandler frameHandler = getFrameHandler();
        return frameHandler.getFrame();
    }

    @Override
    public Action getExitAction() {
        Action exitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        exitAction.putValue(MODULE_ID, this);
        exitAction.putValue(Action.NAME, "Exit");
        exitAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.KeyEvent.ALT_DOWN_MASK));

        return exitAction;
    }

    @Override
    public XBApplicationFrameHandler getFrameHandler() {
        if (frame == null) {
            frame = new XBApplicationFrame();
            frame.loadMainMenu(application);
        }

        return frame;
    }
}
