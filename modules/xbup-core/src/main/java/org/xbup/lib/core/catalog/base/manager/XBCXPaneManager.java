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
package org.xbup.lib.core.catalog.base.manager;

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXBlockPane;
import org.xbup.lib.core.catalog.base.XBCXPlugPane;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCBlockPane catalog manager.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 * @param <T> panel editor entity
 */
public interface XBCXPaneManager<T extends XBCXBlockPane> extends XBCManager<T>, XBCExtension {

    /**
     * Get list of all panel editors for given revision.
     *
     * @param revision
     * @return panel editors
     */
    public List<XBCXBlockPane> getPanes(XBCBlockRev revision);

    /**
     * Get count of panel editors for given revision.
     *
     * @param revision
     * @return count of panel editors
     */
    public long getPanesCount(XBCBlockRev revision);

    /**
     * Get panel editor for given revision and priority
     *
     * @param revision
     * @param priority
     * @return panel editor
     */
    public XBCXBlockPane findPaneByPR(XBCBlockRev revision, long priority);

    /**
     * Get panel editor by unique index.
     *
     * @param id unique index
     * @return panel editor
     */
    public XBCXBlockPane findById(long id);

    /**
     * Get plugin panel by unique index.
     *
     * @param id unique index
     * @return panel editor
     */
    public XBCXPlugPane findPlugPaneById(long id);

    /**
     * Get count of all panel editors.
     *
     * @return count of panel editors
     */
    public Long getAllPanesCount();

    /**
     * Get list of all plugin panels.
     *
     * @param plugin
     * @return plugin panels
     */
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin);

    /**
     * Get count of panel editors for given plugin.
     *
     * @param plugin
     * @return count of plugin panels
     */
    public long getPlugPanesCount(XBCXPlugin plugin);

    /**
     * Get plugin panel editor for plugin.
     *
     * @param plugin
     * @param pane
     * @return plugin panel
     */
    public XBCXPlugPane getPlugPane(XBCXPlugin plugin, long pane);

    /**
     * Get count of all plugin panels.
     *
     * @return count of plugin panels
     */
    public Long getAllPlugPanesCount();
}
