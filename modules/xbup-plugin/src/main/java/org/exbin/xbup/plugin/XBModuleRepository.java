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

import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * XBUP framework modules repository interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBModuleRepository {

    /**
     * Scans for all valid modules in give directory or specified jar file.
     *
     * @param moduleJarFileUri jar file URI
     */
    void addModulesFrom(URI moduleJarFileUri);

    /**
     * Scans for all valid modules in give directory or specified jar file.
     *
     * @param moduleJarFileUrl jar file URL
     */
    void addModulesFrom(URL moduleJarFileUrl);

    /**
     * Scans for all valid modules in jar libraries included using class path
     * expressions.
     *
     * Should include both command line parameters and manifest file.
     */
    void addClassPathModules();

    /**
     * Process given manifest file for modules.
     *
     * @param manifestClass manifest class
     */
    void addModulesFromManifest(Class manifestClass);

    /**
     * Process all jar files in given directory.
     *
     * @param pathUri path
     */
    void loadModulesFromPath(URI pathUri);

    /**
     * Process all jar files in given directory.
     *
     * @param pathUrl path
     */
    void addModulesFromPath(URL pathUrl);

    /**
     * Gets info about module.
     *
     * @param moduleId module identifier
     * @return application module record
     */
    @Nullable
    XBModuleRecord getModuleRecordById(String moduleId);

    /**
     * Gets module for specified identified.
     *
     * @param moduleId module identifier
     * @return application module
     * @throws IllegalArgumentException when module not found
     */
    @Nonnull
    XBModule getModuleById(String moduleId);

    /**
     * Gets module instance by module interface.
     *
     * @param <T> interface class
     * @param interfaceClass interface class
     * @return application module record
     * @throws IllegalArgumentException when module not found
     */
    @Nonnull
    <T extends XBModule> T getModuleByInterface(Class<T> interfaceClass);

    /**
     * Gets list of modules.
     *
     * @return list of modules
     */
    @Nonnull
    List<XBModuleRecord> getModulesList();

    /**
     * Processes all modules and initializes them in proper order.
     */
    void initModules();

    /**
     * Returns class loader with loaded plugins.
     *
     * @return context class loader
     */
    @Nonnull
    ClassLoader getContextClassLoader();
}
