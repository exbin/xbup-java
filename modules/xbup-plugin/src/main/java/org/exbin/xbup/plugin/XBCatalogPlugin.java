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
package org.exbin.xbup.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * XBUP Editor plugin - provides editing panel for XBUP data.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCatalogPlugin {

    /**
     * Gets catalog path of this plugin.
     *
     * @return Identification catalog path of this plugin file
     */
    @Nonnull
    String getPluginPath();

    /**
     * Gets count of line editors.
     *
     * @return count of line editor
     */
    long getLineEditorsCount();

    /**
     * Gets specific line editor.
     *
     * @param index line editor index
     * @return line editor
     */
    @Nullable
    XBLineEditor getLineEditor(long index);

    /**
     * Gets count of panel editors.
     *
     * @return count of panel editors
     */
    long getPanelEditorsCount();

    /**
     * Gets specific panel editor.
     *
     * @param index panel editor index
     * @return panel editor
     */
    @Nullable
    XBPanelEditor getPanelEditor(long index);

    /**
     * Gets count of transformations.
     *
     * @return transformation
     */
    long getTransformationCount();

    /**
     * Gets specific transformation.
     *
     * @param index transformation index
     * @return transformation
     */
    @Nullable
    XBTransformation getTransformation(long index);
}
