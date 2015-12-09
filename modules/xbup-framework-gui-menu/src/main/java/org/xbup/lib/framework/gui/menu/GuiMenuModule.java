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

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;

/**
 * Implementation of XBUP framework menu module.
 *
 * @version 0.2.0 2015/12/07
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiMenuModule implements GuiMenuModuleApi {

    private XBApplication application;

    public GuiMenuModule(XBApplication application) {
    }

    @Override
    public void init(XBApplication application) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
