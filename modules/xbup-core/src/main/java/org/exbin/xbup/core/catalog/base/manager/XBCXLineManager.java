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
    List<XBCXBlockLine> getLines(XBCBlockRev revision);

    /**
     * Gets count of line editors for given revision.
     *
     * @param revision revision
     * @return count of line editors
     */
    long getLinesCount(XBCBlockRev revision);

    /**
     * Gets line editor for given revision and priority.
     *
     * @param revision revision
     * @param priority priority
     * @return line editor
     */
    XBCXBlockLine findLineByPR(XBCBlockRev revision, long priority);

    /**
     * Gets line editor by unique index.
     *
     * @param id unique index
     * @return line editor
     */
    XBCXBlockLine findById(long id);

    /**
     * Finds plugin line by unique index.
     *
     * @param id plugin line id
     * @return plugin line
     */
    XBCXPlugLine findPlugLineById(long id);

    /**
     * Gets list of line editors for plugin.
     *
     * @param plugin plugin
     * @return list of plugin lines
     */
    List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin);

    /**
     * Gets count of line editors for given plugin.
     *
     * @param plugin plugin
     * @return count of plugin lines
     */
    long getPlugLinesCount(XBCXPlugin plugin);

    /**
     * Gets plugin line for given plugin and line index.
     *
     * @param plugin plugin
     * @param lineIndex line index
     * @return plugin lines
     */
    XBCXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex);

    /**
     * Gets count of all plugin lines.
     *
     * @return count of plugin lines
     */
    Long getAllPlugLinesCount();
}
