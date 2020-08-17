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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCXBlockUi items service.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 * @param <T> block UI editor entity
 */
@ParametersAreNonnullByDefault
public interface XBCXUiService<T extends XBCXBlockUi> extends XBCService<T>, XBCExtension {

    /**
     * Gets list of all block UI editors.
     *
     * @param revision revision
     * @return block UI editors
     */
    @Nonnull
    List<XBCXBlockUi> getUis(XBCBlockRev revision);

    /**
     * Gets list of all block UI editors.
     *
     * @param revision revision
     * @param type type
     * @return block UI editors
     */
    @Nonnull
    List<XBCXBlockUi> getUis(XBCBlockRev revision, XBPlugUiType type);

    /**
     * Gets count of UI editors for given revision.
     *
     * @param revision revision
     * @return count of UI editors
     */
    long getUisCount(XBCBlockRev revision);

    /**
     * Gets count of UI editors for given revision and type.
     *
     * @param revision revision
     * @param type type
     * @return count of UI editors
     */
    long getUisCount(XBCBlockRev revision, XBPlugUiType type);

    /**
     * Gets UI editor for given revision and priority.
     *
     * @param revision revision
     * @param type type
     * @param priority priority
     * @return block UI editor
     */
    XBCXBlockUi findUiByPR(XBCBlockRev revision, XBPlugUiType type, long priority);

    /**
     * Finds UI editor by ID.
     *
     * @param id id
     * @return UI editor
     */
    XBCXBlockUi findById(long id);

    /**
     * Finds UI plugin by ID.
     *
     * @param id id
     * @return UI plugin
     */
    XBCXPlugUi findPlugUiById(long id);

    /**
     * Finds UI plugin type by ID.
     *
     * @param id id
     * @return UI plugin type
     */
    XBCXPlugUiType findTypeById(int id);

    /**
     * Gets list of all UI plugins.
     *
     * @param plugin plugin
     * @return UI plugin
     */
    @Nonnull
    List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin);

    /**
     * Gets list of UI plugins of given type.
     *
     * @param plugin plugin
     * @param type type
     * @return UI plugin
     */
    @Nonnull
    List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin, XBPlugUiType type);

    /**
     * Gets count of UI editors for given plugin.
     *
     * @param plugin plugin
     * @return count of UI editors
     */
    long getPlugUisCount(XBCXPlugin plugin);

    /**
     * Gets count of UI editors of given type for given plugin.
     *
     * @param plugin plugin
     * @param type type
     * @return count of UI editors
     */
    long getPlugUisCount(XBCXPlugin plugin, XBPlugUiType type);

    /**
     * Gets UI plugin for given index.
     *
     * @param plugin plugin
     * @param type type
     * @param methodIndex UI plugin index
     * @return UI plugin
     */
    XBCXPlugUi getPlugUi(XBCXPlugin plugin, XBPlugUiType type, long methodIndex);

    /**
     * Gets count of all UI editors.
     *
     * @return count of UI editors
     */
    long getAllPlugUisCount();
}
