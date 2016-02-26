/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xbup.lib.framework.api;

import java.net.URI;
import java.util.List;
import net.xeoh.plugins.base.PluginManager;

/**
 * XBUP framework modules repository interface.
 *
 * @version 0.2.0 2016/01/25
 * @author XBUP Project (http://xbup.org)
 */
public interface XBModuleRepository {

    /**
     * Scans for all valid modules in give directory or specified jar file.
     *
     * @param uri
     */
    void addPluginsFrom(URI uri);

    /**
     * Scans for all valid modules in jar libraries included using class path
     * expressions.
     *
     * Should include both command line parameters and manifest file.
     */
    void addClassPathPlugins();

    /**
     * Process given manifest file for plugins.
     *
     * @param sourceClass
     */
    public void addPluginsFromManifest(Class sourceClass);

    /**
     * Gets info about module.
     *
     * @param moduleId module identifier
     * @return application module record
     */
    XBApplicationModule getModuleRecordById(String moduleId);

    /**
     * Gets module for specified identified.
     *
     * @param moduleId module identifier
     * @return application module
     */
    XBApplicationModulePlugin getModuleById(String moduleId);

    /**
     * Gets module instance by module interface.
     *
     * @param <T> interface class
     * @param interfaceClass interface class
     * @return application module record
     */
    <T extends XBApplicationModulePlugin> T getModuleByInterface(Class<T> interfaceClass);

    /**
     * Gets list of modules.
     *
     * @return list of modules
     */
    List<XBApplicationModule> getModulesList();

    /**
     * Gets plugin manager.
     *
     * @return the pluginManager
     */
    PluginManager getPluginManager();

    /**
     * Processes all modules and initializes them in proper order.
     */
    void initModules();
}
