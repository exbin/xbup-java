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

import java.util.List;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCXBlockPane items service.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 * @param <T> block panel editor entity
 */
public interface XBCXPaneService<T extends XBCXBlockPane> extends XBCService<T>, XBCExtension {

    /**
     * Gets list of all binds for given revision.
     *
     * @param revision revision
     * @return list of block panel editors
     */
    List<XBCXBlockPane> getPanes(XBCBlockRev revision);

    /**
     * Gets count of panel editors for given revision.
     *
     * @param revision revision
     * @return count of panel editors
     */
    long getPanesCount(XBCBlockRev revision);

    /**
     * Gets panel editor for given revision and priority.
     *
     * @param revision revision
     * @param priority priority
     * @return block panel editor
     */
    XBCXBlockPane findPaneByPR(XBCBlockRev revision, long priority);

    /**
     * Finds block panel editor by ID.
     *
     * @param id id
     * @return block panel editor
     */
    XBCXBlockPane findById(long id);

    /**
     * Finds panel editor plugin by ID.
     *
     * @param id id
     * @return panel editor plugin
     */
    XBCXPlugPane findPlugPaneById(long id);

    /**
     * Gets list of all plugin editor panels for given plugin.
     *
     * @param plugin plugin
     * @return list of plugin editor panels
     */
    List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin);

    /**
     * Gets count of panel editors for given plugin.
     *
     * @param plugin plugin
     * @return count of plugin editor panels
     */
    long getPlugPanesCount(XBCXPlugin plugin);

    /**
     * Gets editor panel plugin for given plugin and order index.
     *
     * @param plugin plugin
     * @param pane editor panel order index
     * @return editor panel plugin
     */
    XBCXPlugPane getPlugPane(XBCXPlugin plugin, long pane);

    /**
     * Gets count of editor panel plugins.
     *
     * @return count of editor panel plugins
     */
    Long getAllPlugPanesCount();
}
