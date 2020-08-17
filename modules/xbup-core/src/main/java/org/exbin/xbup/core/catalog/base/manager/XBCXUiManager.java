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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for XBCBlockUi catalog manager.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 * @param <T> UI editor entity
 */
@ParametersAreNonnullByDefault
public interface XBCXUiManager<T extends XBCXBlockUi> extends XBCManager<T>, XBCExtension {

    /**
     * Gets list of all UI editors for given revision.
     *
     * @param revision revision
     * @return UI editors
     */
    @Nonnull
    List<XBCXBlockUi> getUis(XBCBlockRev revision);

    /**
     * Gets list of all UI editors for given revision.
     *
     * @param revision revision
     * @param type type
     * @return UI editors
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
     * Gets count of UI editors for given revision.
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
     * @return UI editor
     */
    XBCXBlockUi findUiByPR(XBCBlockRev revision, XBPlugUiType type, long priority);

    /**
     * Gets UI editor by unique index.
     *
     * @param id unique index
     * @return UI editor
     */
    XBCXBlockUi findById(long id);

    /**
     * Gets plugin UI by unique index.
     *
     * @param id unique index
     * @return UI editor
     */
    XBCXPlugUi findPlugUiById(long id);

    /**
     * Gets plugin UI type by unique index.
     *
     * @param id unique index
     * @return UI type
     */
    XBCXPlugUiType findTypeById(long id);

    /**
     * Gets list of all plugin UIs.
     *
     * @param plugin plugin
     * @return plugin UIs
     */
    @Nonnull
    List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin);

    /**
     * Gets list of all plugin UIs of given type.
     *
     * @param plugin plugin
     * @param type type
     * @return plugin UIs
     */
    @Nonnull
    List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin, XBPlugUiType type);

    /**
     * Gets count of UI editors for given plugin.
     *
     * @param plugin plugin
     * @return count of plugin UIs
     */
    long getPlugUisCount(XBCXPlugin plugin);

    /**
     * Gets count of UI editors for given plugin.
     *
     * @param plugin plugin
     * @param type type
     * @return count of plugin UIs
     */
    long getPlugUisCount(XBCXPlugin plugin, XBPlugUiType type);

    /**
     * Gets plugin UI editor for plugin.
     *
     * @param plugin plugin
     * @param type type
     * @param methodIndex UI editor index
     * @return plugin UI
     */
    @Nullable
    XBCXPlugUi getPlugUi(XBCXPlugin plugin, XBPlugUiType type, long methodIndex);

    /**
     * Gets count of all plugin UIs.
     *
     * @return count of plugin UIs
     */
    long getAllPlugUisCount();
}
