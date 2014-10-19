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
package org.xbup.lib.core.catalog.base.service;

import java.io.InputStream;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXPlugin items service.
 *
 * @version 0.1.24 2014/10/19
 * @author XBUP Project (http://xbup.org)
 * @param <T> plugin entity
 */
public interface XBCXPlugService<T extends XBCXPlugin> extends XBCService<T>, XBCExtension {

    /**
     * Get count of all plugins.
     *
     * @return count of plugins
     */
    public Long getAllPluginCount();

    /**
     * Returns path of XBIndexes for given plugin.
     *
     * @param plugin plugin
     * @return catalog path
     */
    public Long[] getPluginXBPath(XBCXPlugin plugin);

    /**
     * Find plugin by ID.
     *
     * @param id id
     * @return plugin
     */
    public XBCXPlugin findById(long id);

    /**
     * Find plugin for given node and order index.
     *
     * @param node node
     * @param index order index
     * @return plugin
     */
    public XBCXPlugin findPlugin(XBCNode node, Long index);

    /**
     * Get data content for given plugin.
     *
     * @param plugin plugin
     * @return data
     */
    public InputStream getPlugin(XBCXPlugin plugin);
}
