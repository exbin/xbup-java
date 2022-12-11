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
package org.exbin.xbup.core.catalog.base;

import javax.annotation.Nonnull;

/**
 * Interface for catalog plugin editor entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBCXPlugUi extends XBCBase {

    /**
     * Gets attached plugin.
     *
     * @return plugin
     */
    @Nonnull
    XBCXPlugin getPlugin();

    /**
     * Gets UI type.
     *
     * @return UI type
     */
    @Nonnull
    XBCXPlugUiType getUiType();

    /**
     * Gets index of the method from plugin.
     *
     * @return method index
     */
    long getMethodIndex();

    /**
     * Gets name.
     *
     * @return name
     */
    @Nonnull
    String getName();
}
