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
package org.xbup.tool.editor.base.api;

/**
 * Interface for application's module information.
 *
 * @version 0.1.21 2011/06/15
 * @author XBUP Project (http://xbup.org)
 */
public interface ApplicationModuleInfo {

    /**
     * Gets plugin ID.
     *
     * This should be unique.
     *
     * @return plugin ID
     */
    public String getPluginId();

    /**
     * Gets plugin name.
     *
     * @return plugin name
     */
    public String getPluginName();

    /**
     * Gets plugin description.
     *
     * @return plugin description
     */
    public String getPluginDescription();

    /**
     * Gets list of modules this module depends on / require for it's proper
     * work.
     *
     * @return list of modules
     */
    public String[] pluginDependency();

    /**
     * Gets list of modules this module can optionally use.
     *
     * @return list of modules
     */
    public String[] pluginOptional();
}
