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
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.ApplicationExitListener;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.frame.api.ApplicationFrameHandler;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Implementation of XBUP framework undo/redo module.
 *
 * @version 0.2.0 2016/01/10
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiFrameModule implements GuiFrameModuleApi {

    public static final String FILE_EXIT_GROUP_ID = MODULE_ID + ".exit";
    public static final String VIEW_BARS_GROUP_ID = MODULE_ID + ".view";

    private XBApplication application;
    private ResourceBundle resourceBundle;
    private XBApplicationFrame frame;
    private ApplicationExitHandler exitHandler = null;
    private StatusBarHandler statusBarHandler = null;

    public GuiFrameModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        resourceBundle = ActionUtils.getResourceBundleByClass(this.getClass());
        initMainMenu();
        initMainToolBar();
    }

    private void initMainMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(MAIN_MENU_ID, MODULE_ID);
        menuModule.registerMenu(FILE_MENU_ID, MODULE_ID);
        menuModule.registerMenu(EDIT_MENU_ID, MODULE_ID);
        menuModule.registerMenu(VIEW_MENU_ID, MODULE_ID);
        menuModule.registerMenu(TOOLS_MENU_ID, MODULE_ID);
        menuModule.registerMenu(OPTIONS_MENU_ID, MODULE_ID);
        menuModule.registerMenu(HELP_MENU_ID, MODULE_ID);

        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, FILE_MENU_ID, resourceBundle.getString("fileMenu.text"), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, EDIT_MENU_ID, resourceBundle.getString("editMenu.text"), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, VIEW_MENU_ID, resourceBundle.getString("viewMenu.text"), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, TOOLS_MENU_ID, resourceBundle.getString("toolsMenu.text"), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, OPTIONS_MENU_ID, resourceBundle.getString("optionsMenu.text"), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(MAIN_MENU_ID, MODULE_ID, HELP_MENU_ID, resourceBundle.getString("helpMenu.text"), new MenuPosition(PositionMode.TOP));
    }

    private void initMainToolBar() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerToolBar(MAIN_TOOL_BAR_ID, MODULE_ID);
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    @Override
    public Frame getFrame() {
        return getFrameHandler().getFrame();
    }

    @Override
    public Action getExitAction() {
        Action exitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (exitHandler != null) {
                    exitHandler.executeExit(getFrameHandler());
                } else {
                    System.exit(0);
                }
            }
        };
        ActionUtils.setupAction(exitAction, resourceBundle, "exitAction");
        exitAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.KeyEvent.ALT_DOWN_MASK));

        return exitAction;
    }

    @Override
    public void registerExitAction() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        String appClosingActionsGroup = "ApplicationClosingActionsGroup";
        menuModule.registerMenuGroup(GuiFrameModuleApi.FILE_MENU_ID, new MenuGroup(appClosingActionsGroup, new MenuPosition(PositionMode.BOTTOM_LAST), SeparationMode.ABOVE));
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, getExitAction(), new MenuPosition(appClosingActionsGroup));
    }

    @Override
    public ApplicationFrameHandler getFrameHandler() {
        if (frame == null) {
            frame = new XBApplicationFrame();
            frame.setApplication(application);
            frame.loadMainMenu(application);
            frame.loadMainToolBar(application);
        }

        return frame;
    }

    @Override
    public void addExitListener(ApplicationExitListener listener) {
        getExitHandler().addListener(listener);
    }

    @Override
    public void removeExitListener(ApplicationExitListener listener) {
        getExitHandler().removeListener(listener);
    }

    private ApplicationExitHandler getExitHandler() {
        if (exitHandler == null) {
            exitHandler = new ApplicationExitHandler();
        }

        return exitHandler;
    }

    private StatusBarHandler getStatusBarHandler() {
        if (statusBarHandler == null) {
            statusBarHandler = new StatusBarHandler(frame);
        }
        
        return statusBarHandler;
    }

    @Override
    public void registerStatusBar(String moduleId, String statusBarId, JPanel panel) {
        getStatusBarHandler().registerStatusBar(moduleId, statusBarId, panel);
    }
    
    @Override
    public void switchStatusBar(String statusBarId) {
        getStatusBarHandler().switchStatusBar(statusBarId);
    }

    @Override
    public Action getViewToolBarAction() {
        final Action viewToolBarAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof JMenuItem) {
                    frame.setToolBarVisible(((JMenuItem) source).isSelected());
                }
            }
        };
        ActionUtils.setupAction(viewToolBarAction, resourceBundle, "viewToolBarAction");
        viewToolBarAction.putValue(Action.SELECTED_KEY, true);
        viewToolBarAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.CHECK);
        return viewToolBarAction;
    }

    @Override
    public Action getViewToolBarCaptionsAction() {
        final Action viewToolBarCaptionsAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof JMenuItem) {
                    frame.setToolBarCaptionsVisible(((JMenuItem) source).isSelected());
                }
            }
        };
        ActionUtils.setupAction(viewToolBarCaptionsAction, resourceBundle, "viewToolBarCaptionsAction");
        viewToolBarCaptionsAction.putValue(Action.SELECTED_KEY, true);
        viewToolBarCaptionsAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.CHECK);
        return viewToolBarCaptionsAction;
    }

    @Override
    public Action getViewStatusBarAction() {
        final Action viewStatusBarAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof JMenuItem) {
                    frame.setStatusBarVisible(((JMenuItem) source).isSelected());
                }
            }
        };
        ActionUtils.setupAction(viewStatusBarAction, resourceBundle, "viewStatusBarAction");
        viewStatusBarAction.putValue(Action.SELECTED_KEY, true);
        viewStatusBarAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.CHECK);
        return viewStatusBarAction;
    }

    @Override
    public void registerBarsVisibilityActions() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.VIEW_MENU_ID, new MenuGroup(VIEW_BARS_GROUP_ID, new MenuPosition(PositionMode.TOP), SeparationMode.BELOW));
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, getViewToolBarAction(), new MenuPosition(VIEW_BARS_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, getViewToolBarCaptionsAction(), new MenuPosition(VIEW_BARS_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, getViewStatusBarAction(), new MenuPosition(VIEW_BARS_GROUP_ID));
    }
}
