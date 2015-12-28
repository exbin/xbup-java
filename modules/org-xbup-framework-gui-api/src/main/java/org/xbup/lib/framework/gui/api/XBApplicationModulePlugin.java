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
package org.xbup.lib.framework.gui.api;

import net.xeoh.plugins.base.Plugin;

/**
 * Interface for application's module.
 *
 * @version 0.2.0 2015/12/09
 * @author XBUP Project (http://xbup.org)
 */
public interface XBApplicationModulePlugin extends Plugin {

    /**
     * Initializes plugin.
     *
     * @param application framework application
     */
    public void init(XBApplication application);

    /**
     * Unregisters all functionality registered by given plugin.
     *
     * @param pluginId plugin identification code
     */
    public void unregisterPlugin(String pluginId);
}
