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
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Interface for catalog plugin editor entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMXPlugUi extends XBCXPlugUi, XBMBase {

    /**
     * Sets attached plugin.
     *
     * @param plugin plugin
     */
    void setPlugin(XBCXPlugin plugin);

    /**
     * Sets UI type.
     *
     * @param uiType UI type
     */
    void setUiType(XBCXPlugUiType uiType);

    /**
     * Sets index of the method from plugin.
     *
     * @param methodIndex method index
     */
    void setMethodIndex(long methodIndex);

    /**
     * Sets name.
     *
     * @param name name
     */
    void setName(String name);
}
