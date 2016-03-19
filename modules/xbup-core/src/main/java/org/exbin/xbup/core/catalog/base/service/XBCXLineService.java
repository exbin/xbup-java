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
package org.exbin.xbup.core.catalog.base.service;

import java.util.List;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCXBlockLine items service.
 *
 * @version 0.1.24 2014/10/19
 * @author ExBin Project (http://exbin.org)
 * @param <T> block line editor entity
 */
public interface XBCXLineService<T extends XBCXBlockLine> extends XBCService<T>, XBCExtension {

    /**
     * Gets list of all block line editors.
     *
     * @param revision revision
     * @return block line editors
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
     * @return block line editor
     */
    public XBCXBlockLine findLineByPR(XBCBlockRev revision, long priority);

    /**
     * Finds line editor by ID.
     *
     * @param id id
     * @return line editor
     */
    public XBCXBlockLine findById(long id);

    /**
     * Finds line plugin by ID.
     *
     * @param id id
     * @return line plugin
     */
    public XBCXPlugLine findPlugLineById(long id);

    /**
     * Gets list of all line plugins.
     *
     * @param plugin plugin
     * @return line plugin
     */
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin);

    /**
     * Gets count of line editors for given plugin.
     *
     * @param plugin plugin
     * @return count of line editors
     */
    public long getPlugLinesCount(XBCXPlugin plugin);

    /**
     * Gets line plugin for given index.
     *
     * @param plugin plugin
     * @param lineIndex line plugin index
     * @return line plugin
     */
    public XBCXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex);

    /**
     * Gets count of all line editors.
     *
     * @return count of line editors
     */
    public Long getAllPlugLinesCount();
}
