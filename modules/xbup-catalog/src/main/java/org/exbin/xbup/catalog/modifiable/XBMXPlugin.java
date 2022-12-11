/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.catalog.modifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for catalog plugin entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMXPlugin extends XBCXPlugin, XBMBase {

    /**
     * Sets node which is owner of this plugin.
     *
     * @param owner owner node
     */
    void setOwner(XBCNode owner);

    /**
     * Sets plugin index.
     *
     * @param pluginIndex plugin index
     */
    void setPluginIndex(long pluginIndex);

    /**
     * Sets plugin file.
     *
     * @param file file
     */
    void setPluginFile(XBCXFile file);
}
