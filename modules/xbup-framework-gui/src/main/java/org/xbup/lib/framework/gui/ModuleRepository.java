/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.framework.gui;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

/**
 * XBUP framework modules repository.
 *
 * @version 0.2.0 2015/11/17
 * @author XBUP Project (http://xbup.org)
 */
public class ModuleRepository {

    private static final String MODULE_FILE_EXT = ".xb";
    private final PluginManager pluginManager;
    private final Map<String, ApplicationModule> modules = new HashMap<>();

    public ModuleRepository() {
        pluginManager = PluginManagerFactory.createPluginManager();
    }

    /**
     * Scans for all valid modules in give directory or specified jar file.
     *
     * @param uri
     */
    public void addPluginsFrom(URI uri) {
        pluginManager.addPluginsFrom(uri);
    }

    /**
     * Processes all modules and initializes them in proper order.
     */
    public void processModules() {
        PluginManagerUtil pmu = new PluginManagerUtil(pluginManager);
        final Collection<ApplicationModulePlugin> plugins = pmu.getPlugins(ApplicationModulePlugin.class);

        // Process modules info
        for (ApplicationModulePlugin plugin : plugins) {
            String canonicalName = plugin.getClass().getCanonicalName();
            InputStream moduleFile = plugin.getClass().getResourceAsStream(canonicalName + MODULE_FILE_EXT);

            // TODO
            //modules.put(getActiveModule(), applicationModule);
        }
        
        // Process dependencies
        List<ApplicationModule> unprocessedModules = new ArrayList<>(modules.values());
        int preRoundCount;
        int postRoundCount;
        do {
            preRoundCount = unprocessedModules.size();
            
            for (ApplicationModule module : unprocessedModules) {
                // Process single module
                List<String> dependencyModuleIds = module.getDependencyModuleIds();
                // TODO
            }
            
            postRoundCount = unprocessedModules.size();
        } while (postRoundCount > 0 && postRoundCount < preRoundCount);
        
        if (postRoundCount > 0) {
            // TODO throw Circular dependency detected
        }
    }

    /**
     * Gets info about module.
     *
     * @param moduleId module identifier
     * @return application module record
     */
    public ApplicationModule getModuleById(String moduleId) {
        return modules.get(moduleId);
    }

    /**
     * Gets plugin manager.
     *
     * @return the pluginManager
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Gets list of modules.
     *
     * @return list of modules
     */
    public List<ApplicationModule> getModulesList() {
        return new ArrayList<>(modules.values());
    }
}
