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
     * @return block line editor
     */
    XBCXBlockLine findLineByPR(XBCBlockRev revision, long priority);

    /**
     * Finds line editor by ID.
     *
     * @param id id
     * @return line editor
     */
    XBCXBlockLine findById(long id);

    /**
     * Finds line plugin by ID.
     *
     * @param id id
     * @return line plugin
     */
    XBCXPlugLine findPlugLineById(long id);

    /**
     * Gets list of all line plugins.
     *
     * @param plugin plugin
     * @return line plugin
     */
    List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin);

    /**
     * Gets count of line editors for given plugin.
     *
     * @param plugin plugin
     * @return count of line editors
     */
    long getPlugLinesCount(XBCXPlugin plugin);

    /**
     * Gets line plugin for given index.
     *
     * @param plugin plugin
     * @param lineIndex line plugin index
     * @return line plugin
     */
    XBCXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex);

    /**
     * Gets count of all line editors.
     *
     * @return count of line editors
     */
    Long getAllPlugLinesCount();
}
