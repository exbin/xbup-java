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
package org.exbin.xbup.core.catalog.base.service;

import java.io.InputStream;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCXPlugin items service.
 *
 * @version 0.1.24 2014/10/19
 * @author ExBin Project (http://exbin.org)
 * @param <T> plugin entity
 */
public interface XBCXPlugService<T extends XBCXPlugin> extends XBCService<T>, XBCExtension {

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
     * @return catalog path
     */
    Long[] getPluginXBPath(XBCXPlugin plugin);

    /**
     * Finds plugin by ID.
     *
     * @param id id
     * @return plugin
     */
    XBCXPlugin findById(long id);

    /**
     * Finds plugin for given node and order index.
     *
     * @param node node
     * @param index order index
     * @return plugin
     */
    XBCXPlugin findPlugin(XBCNode node, Long index);

    /**
     * Gets data content for given plugin.
     *
     * @param plugin plugin
     * @return data
     */
    InputStream getPlugin(XBCXPlugin plugin);
}
