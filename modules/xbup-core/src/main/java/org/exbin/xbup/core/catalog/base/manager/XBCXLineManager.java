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
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCBlockLine catalog manager.
 *
 * @version 0.1.21 2011/12/31
 * @author ExBin Project (http://exbin.org)
 * @param <T> block line editor entity
 */
public interface XBCXLineManager<T extends XBCXBlockLine> extends XBCManager<T>, XBCExtension {

    /**
     * Gets list of block line editors for block revision.
     *
     * @param revision revision
     * @return list of line editors
     */
    public List<XBCXBlockLine> getLines(XBCBlockRev revision);

    /**
     * Gets count of line editors for given revision.
     *
     * @param revision revision
     * @return count of line editors
     */
    public long getLinesCount(XBCBlockRev revision);

    /**
     * Gets line editor for given revision and priority.
     *
     * @param revision revision
     * @param priority priority
     * @return line editor
     */
    public XBCXBlockLine findLineByPR(XBCBlockRev revision, long priority);

    /**
     * Gets line editor by unique index.
     *
     * @param id unique index
     * @return line editor
     */
    public XBCXBlockLine findById(long id);

    /**
     * Finds plugin line by unique index.
     *
     * @param id plugin line id
     * @return plugin line
     */
    public XBCXPlugLine findPlugLineById(long id);

    /**
     * Gets list of line editors for plugin.
     *
     * @param plugin plugin
     * @return list of plugin lines
     */
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin);

    /**
     * Gets count of line editors for given plugin.
     *
     * @param plugin plugin
     * @return count of plugin lines
     */
    public long getPlugLinesCount(XBCXPlugin plugin);

    /**
     * Gets plugin line for given plugin and line index.
     *
     * @param plugin plugin
     * @param lineIndex line index
     * @return plugin lines
     */
    public XBCXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex);

    /**
     * Gets count of all plugin lines.
     *
     * @return count of plugin lines
     */
    public Long getAllPlugLinesCount();
}
