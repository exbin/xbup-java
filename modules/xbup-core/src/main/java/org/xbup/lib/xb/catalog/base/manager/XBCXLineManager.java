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
package org.xbup.lib.xb.catalog.base.manager;

import java.util.List;
import org.xbup.lib.xb.catalog.base.XBCBlockRev;
import org.xbup.lib.xb.catalog.base.XBCXBlockLine;
import org.xbup.lib.xb.catalog.base.XBCXPlugLine;
import org.xbup.lib.xb.catalog.base.XBCXPlugin;
import org.xbup.lib.xb.catalog.base.XBCExtension;

/**
 * Interface for XBCBlockLine catalog manager.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 * @param <T> block line editor entity
 */
public interface XBCXLineManager<T extends XBCXBlockLine> extends XBCManager<T>, XBCExtension {

    /**
     * Get list of block line editors for block revision.
     *
     * @param revision
     * @return list of line editors
     */
    public List<XBCXBlockLine> getLines(XBCBlockRev revision);

    /**
     * Get count of line editors for given revision.
     *
     * @param revision
     * @return count of line editors
     */
    public long getLinesCount(XBCBlockRev revision);

    /**
     * Get line editor for given revision and priority.
     *
     * @param revision
     * @param priority
     * @return line editor
     */
    public XBCXBlockLine findLineByPR(XBCBlockRev revision, long priority);

    /**
     * Get line editor by unique index.
     *
     * @param id unique index
     * @return line editor
     */
    public XBCXBlockLine findById(long id);

    /**
     * Find plugin lines by unique index.
     *
     * @param id
     * @return plugin lines
     */
    public XBCXPlugLine findPlugLineById(long id);

    /**
     * Get count of all line editors.
     *
     * @return count of line editors
     */
    public Long getAllLinesCount();

    /**
     * Get list of line editors for plugin.
     *
     * @param plugin
     * @return list of plugin lines
     */
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin);

    /**
     * Get count of line editors for given plugin.
     *
     * @param plugin
     * @return count of plugin lines
     */
    public long getPlugLinesCount(XBCXPlugin plugin);

    /**
     * Get plugin line for given plugin and line index.
     *
     * @param plugin
     * @param lineIndex
     * @return plugin lines
     */
    public XBCXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex);

    /**
     * Get count of all plugin lines.
     *
     * @return count of plugin lines
     */
    public Long getAllPlugLinesCount();
}
