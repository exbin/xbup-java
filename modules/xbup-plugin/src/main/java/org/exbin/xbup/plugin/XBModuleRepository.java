/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.xbup.plugin;

import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * XBUP framework modules repository interface.
 *
 * @version 0.2.0 2016/03/27
 * @author ExBin Project (http://exbin.org)
 */
public interface XBModuleRepository {

    /**
     * Scans for all valid modules in give directory or specified jar file.
     *
     * @param moduleJarFileUri
     */
    void addModulesFrom(URI moduleJarFileUri);

    /**
     * Scans for all valid modules in give directory or specified jar file.
     *
     * @param moduleJarFileUrl
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
     * @param manifestClass
     */
    public void addModulesFromManifest(Class manifestClass);

    /**
     * Gets info about module.
     *
     * @param moduleId module identifier
     * @return application module record
     */
    XBModuleRecord getModuleRecordById(String moduleId);

    /**
     * Gets module for specified identified.
     *
     * @param moduleId module identifier
     * @return application module
     */
    XBModule getModuleById(String moduleId);

    /**
     * Gets module instance by module interface.
     *
     * @param <T> interface class
     * @param interfaceClass interface class
     * @return application module record
     */
    <T extends XBModule> T getModuleByInterface(Class<T> interfaceClass);

    /**
     * Gets list of modules.
     *
     * @return list of modules
     */
    List<XBModuleRecord> getModulesList();

    /**
     * Processes all modules and initializes them in proper order.
     */
    void initModules();
}
