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

/**
 * Component viewers catalog plugin interface.
 *
 * @version 0.2.1 2020/07/27
 * @author ExBin Project (http://exbin.org)
 */
public interface XBComponentViewerCatalogPlugin {

    /**
     * Gets count of component viewers.
     *
     * @return count of panel editors
     */
    long getComponentViewersCount();

    /**
     * Gets specific component viewer.
     *
     * @param index panel editor index
     * @return panel editor
     */
    @Nonnull
    XBComponentViewer getComponentViewer(long index);
}
