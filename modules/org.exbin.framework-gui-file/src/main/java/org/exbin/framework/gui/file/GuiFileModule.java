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
package org.exbin.framework.gui.file;

import javax.swing.JMenu;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.gui.file.api.FileType;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.frame.api.ApplicationExitListener;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.menu.api.MenuGroup;
import org.exbin.framework.gui.menu.api.MenuPosition;
import org.exbin.framework.gui.menu.api.NextToMode;
import org.exbin.framework.gui.menu.api.PositionMode;
import org.exbin.framework.gui.menu.api.ToolBarGroup;
import org.exbin.framework.gui.menu.api.ToolBarPosition;
import org.exbin.xbup.plugin.XBModuleHandler;

/**
 * Implementation of XBUP framework file module.
 *
 * @version 0.2.0 2016/02/03
 * @author ExBin Project (http://exbin.org)
 */
public class GuiFileModule implements GuiFileModuleApi {

    private static final String FILE_MENU_GROUP_ID = MODULE_ID + ".fileMenuGroup";
    private static final String FILE_TOOL_BAR_GROUP_ID = MODULE_ID + ".fileToolBarGroup";

    private FileHandlingActions fileHandlingActions = null;
    private XBApplication application;

    public GuiFileModule() {
    }

    @Override
    public void init(XBModuleHandler moduleHandler) {
        this.application = (XBApplication) moduleHandler;
    }

    @Override
    public void unregisterModule(String moduleId) {
    }

    @Override
    public FileHandlingActions getFileHandlingActions() {
        if (fileHandlingActions == null) {
            fileHandlingActions = new FileHandlingActions();
            fileHandlingActions.init(application);
            fileHandlingActions.setPreferences(application.getAppPreferences());
        }

        return fileHandlingActions;
    }

    @Override
    public void addFileType(FileType fileType) {
        FileHandlingActions handle = getFileHandlingActions();
        handle.addFileType(fileType);
    }

    @Override
    public void registerMenuFileHandlingActions() {
        getFileHandlingActions();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.FILE_MENU_ID, new MenuGroup(FILE_MENU_GROUP_ID, new MenuPosition(PositionMode.TOP)));
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, fileHandlingActions.getNewFileAction(), new MenuPosition(FILE_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, fileHandlingActions.getOpenFileAction(), new MenuPosition(FILE_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, fileHandlingActions.getSaveFileAction(), new MenuPosition(FILE_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, fileHandlingActions.getSaveAsFileAction(), new MenuPosition(FILE_MENU_GROUP_ID));
    }

    @Override
    public void registerToolBarFileHandlingActions() {
        getFileHandlingActions();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerToolBarGroup(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, new ToolBarGroup(FILE_TOOL_BAR_GROUP_ID, new ToolBarPosition(PositionMode.TOP)));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, fileHandlingActions.getNewFileAction(), new ToolBarPosition(FILE_TOOL_BAR_GROUP_ID));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, fileHandlingActions.getOpenFileAction(), new ToolBarPosition(FILE_TOOL_BAR_GROUP_ID));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, fileHandlingActions.getSaveFileAction(), new ToolBarPosition(FILE_TOOL_BAR_GROUP_ID));
    }

    @Override
    public void registerCloseListener() {
        getFileHandlingActions();
        GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
        frameModule.addExitListener(new ApplicationExitListener() {
            @Override
            public boolean processExit(ApplicationFrameHandler frameHandler) {
                return fileHandlingActions.releaseFile();
            }
        });
    }

    @Override
    public void registerLastOpenedMenuActions() {
        getFileHandlingActions();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        JMenu recentFileMenu = fileHandlingActions.getOpenRecentMenu();
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, recentFileMenu, new MenuPosition(NextToMode.AFTER, "Open..."));
    }

    @Override
    public void loadFromFile(String filename) {
        getFileHandlingActions().loadFromFile(filename);
    }
}
