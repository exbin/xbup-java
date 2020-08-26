/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.catalog.base.manager;

import java.io.InputStream;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCXPlugin catalog manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCXPlugManager extends XBCManager<XBCXPlugin>, XBCExtension {

    /**
     * Gets count of all plugins.
     *
     * @return count of plugins
     */
    Long getAllPluginCount();

    /**
     * Returns path of XBIndexes for given plugin.
     *
     * @param plugin plugin
     * @return xbindex path
     */
    Long[] getPluginXBPath(XBCXPlugin plugin);

    /**
     * Finds plugin by unique index.
     *
     * @param id plugin id
     * @return plugin
     */
    XBCXPlugin findById(long id);

    /**
     * Gets plugin by node and order index.
     *
     * @param node node
     * @param index order index
     * @return plugin
     */
    XBCXPlugin findPlugin(XBCNode node, Long index);

    /**
     * Returns access to content of plugin as data stream.
     *
     * @param plugin plugin
     * @return input stream
     */
    InputStream getPlugin(XBCXPlugin plugin);

    /**
     * Finds all plugins for given node.
     *
     * @param node parent node of the plugin
     * @return list of plugins or empty list if no such file exist
     */
    @Nonnull
    List<XBCXPlugin> findPluginsForNode(XBCNode node);
}
