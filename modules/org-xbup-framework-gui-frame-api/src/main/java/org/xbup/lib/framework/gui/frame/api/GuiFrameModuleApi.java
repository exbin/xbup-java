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
package org.xbup.lib.framework.gui.frame.api;

import java.awt.Frame;
import javax.swing.Action;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;

/**
 * Interface for XBUP framework frame module.
 *
 * @version 0.2.0 2016/01/01
 * @author XBUP Project (http://xbup.org)
 */
public interface GuiFrameModuleApi extends XBApplicationModulePlugin {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(GuiFrameModuleApi.class);
    public static String MAIN_MENU_ID = MODULE_ID + ".main";
    public static String FILE_MENU_ID = MAIN_MENU_ID + "/File";
    public static String EDIT_MENU_ID = MAIN_MENU_ID + "/Edit";
    public static String VIEW_MENU_ID = MAIN_MENU_ID + "/View";
    public static String TOOLS_MENU_ID = MAIN_MENU_ID + "/Tools";
    public static String OPTIONS_MENU_ID = MAIN_MENU_ID + "/Options";
    public static String HELP_MENU_ID = MAIN_MENU_ID + "/Help";

    /**
     * Returns frame instance.
     *
     * @return frame
     */
    Frame getFrame();

    /**
     * Returns frame handler.
     *
     * @return frame handler
     */
    XBApplicationFrameHandler getFrameHandler();
    
    Action getExitAction();
}
