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

import java.util.List;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCBlockPane catalog manager.
 *
 * @version 0.1.21 2011/12/31
 * @author ExBin Project (http://exbin.org)
 * @param <T> panel editor entity
 */
public interface XBCXPaneManager<T extends XBCXBlockPane> extends XBCManager<T>, XBCExtension {

    /**
     * Gets list of all panel editors for given revision.
     *
     * @param revision revision
     * @return panel editors
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
     * Gets panel editor for given revision and priority
     *
     * @param revision revision
     * @param priority priority
     * @return panel editor
     */
    XBCXBlockPane findPaneByPR(XBCBlockRev revision, long priority);

    /**
     * Gets panel editor by unique index.
     *
     * @param id unique index
     * @return panel editor
     */
    XBCXBlockPane findById(long id);

    /**
     * Gets plugin panel by unique index.
     *
     * @param id unique index
     * @return panel editor
     */
    XBCXPlugPane findPlugPaneById(long id);

    /**
     * Gets list of all plugin panels.
     *
     * @param plugin plugin
     * @return plugin panels
     */
    List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin);

    /**
     * Gets count of panel editors for given plugin.
     *
     * @param plugin plugin
     * @return count of plugin panels
     */
    long getPlugPanesCount(XBCXPlugin plugin);

    /**
     * Gets plugin panel editor for plugin.
     *
     * @param plugin plugin
     * @param pane panel editor index
     * @return plugin panel
     */
    XBCXPlugPane getPlugPane(XBCXPlugin plugin, long pane);

    /**
     * Gets count of all plugin panels.
     *
     * @return count of plugin panels
     */
    Long getAllPlugPanesCount();
}
