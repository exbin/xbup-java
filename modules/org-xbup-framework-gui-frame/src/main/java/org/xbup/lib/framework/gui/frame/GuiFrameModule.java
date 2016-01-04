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
import javax.swing.JMenu;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.frame.api.XBApplicationFrameHandler;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;

/**
 * Implementation of XBUP framework undo/redo module.
 *
 * @version 0.2.0 2016/01/01
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
    public XBApplicationFrameHandler getFrameHandler() {
        if (frame == null) {
            frame = new XBApplicationFrame();
            GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
            JMenu mainMenu = menuModule.getMenu(GuiFrameModuleApi.MAIN_MENU_ID);
            frame.setMainMenu(mainMenu);
        }

        return frame;
    }
}
