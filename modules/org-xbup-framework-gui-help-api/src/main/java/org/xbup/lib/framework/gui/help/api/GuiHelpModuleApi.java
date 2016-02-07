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
package org.xbup.lib.framework.gui.help.api;

import java.net.URL;
import javax.swing.Action;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;

/**
 * Interface for XBUP framework help module.
 *
 * @version 0.2.0 2016/02/07
 * @author XBUP Project (http://xbup.org)
 */
public interface GuiHelpModuleApi extends XBApplicationModulePlugin {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(GuiHelpModuleApi.class);

    /**
     * Registers help action to main frame menu.
     */
    void registerMainMenu();
    
    /**
     * Registers online help action to main frame menu.
     */
    void registerOnlineHelpMenu();
    
    /**
     * Returns help action.
     * 
     * @return help action
     */
    Action getHelpAction();

    /**
     * Returns online help action.
     * 
     * @return online help action
     */
    Action getOnlineHelpAction();
    
    void setHelpUrl(URL helpUrl);
}
