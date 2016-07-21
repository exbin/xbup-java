/*
 * Copyright (C) ExBin Project
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
    public List<XBCXBlockPane> getPanes(XBCBlockRev revision);

    /**
     * Gets count of panel editors for given revision.
     *
     * @param revision revision
     * @return count of panel editors
     */
    public long getPanesCount(XBCBlockRev revision);

    /**
     * Gets panel editor for given revision and priority
     *
     * @param revision revision
     * @param priority priority
     * @return panel editor
     */
    public XBCXBlockPane findPaneByPR(XBCBlockRev revision, long priority);

    /**
     * Gets panel editor by unique index.
     *
     * @param id unique index
     * @return panel editor
     */
    public XBCXBlockPane findById(long id);

    /**
     * Gets plugin panel by unique index.
     *
     * @param id unique index
     * @return panel editor
     */
    public XBCXPlugPane findPlugPaneById(long id);

    /**
     * Gets list of all plugin panels.
     *
     * @param plugin plugin
     * @return plugin panels
     */
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin);

    /**
     * Gets count of panel editors for given plugin.
     *
     * @param plugin plugin
     * @return count of plugin panels
     */
    public long getPlugPanesCount(XBCXPlugin plugin);

    /**
     * Gets plugin panel editor for plugin.
     *
     * @param plugin plugin
     * @param pane panel editor index
     * @return plugin panel
     */
    public XBCXPlugPane getPlugPane(XBCXPlugin plugin, long pane);

    /**
     * Gets count of all plugin panels.
     *
     * @return count of plugin panels
     */
    public Long getAllPlugPanesCount();
}
