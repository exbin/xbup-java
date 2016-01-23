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
package org.xbup.lib.framework.gui.file;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.frame.api.ApplicationExitListener;
import org.xbup.lib.framework.gui.frame.api.ApplicationFrameHandler;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.menu.api.ToolBarGroup;
import org.xbup.lib.framework.gui.menu.api.ToolBarPosition;

/**
 * Implementation of XBUP framework file module.
 *
 * @version 0.2.0 2016/01/12
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiFileModule implements GuiFileModuleApi {

    private static final String FILE_MENU_GROUP_ID = MODULE_ID + ".fileMenuGroup";
    private static final String FILE_TOOL_BAR_GROUP_ID = MODULE_ID + ".fileToolBarGroup";

    private FileHandlingActions fileHandlingActions = null;
    private XBApplication application;

    public GuiFileModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    @Override
    public FileHandlingActions getFileHandlingActions() {
        if (fileHandlingActions == null) {
            fileHandlingActions = new FileHandlingActions();
            fileHandlingActions.init(application);
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
    public void loadFromFile(String filename) {
        getFileHandlingActions().loadFromFile(filename);
    }
}
