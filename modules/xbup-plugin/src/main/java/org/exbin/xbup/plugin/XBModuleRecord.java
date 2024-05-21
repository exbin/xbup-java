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
package org.exbin.xbup.plugin;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * Interface for record about single module.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBModuleRecord {

    /**
     * Returns module ID.
     *
     * @return module ID
     */
    @Nonnull
    String getModuleId();

    /**
     * Returns module name.
     *
     * @return module name
     */
    @Nonnull
    String getName();

    /**
     * Returns module description.
     *
     * @return description text
     */
    @Nonnull
    String getDescription();

    /**
     * Returns list of required dependency modules.
     *
     * @return list of dependecy module identifiers
     */
    @Nonnull
    List<String> getDependencyModuleIds();

    /**
     * Returns list of optional modules.
     *
     * @return list of module identifiers
     */
    @Nonnull
    List<String> getOptionalModuleIds();

    /**
     * Returns instance of the module.
     *
     * @return module instance
     */
    @Nonnull
    XBModule getModule();
}
