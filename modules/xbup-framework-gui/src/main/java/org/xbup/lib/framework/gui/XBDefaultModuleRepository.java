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

import org.xbup.lib.framework.gui.api.XBModuleRepository;
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
import org.xbup.lib.framework.gui.api.XBApplicationModule;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;

/**
 * XBUP framework modules repository.
 *
 * @version 0.2.0 2015/12/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBDefaultModuleRepository implements XBModuleRepository {

    private static final String MODULE_FILE_EXT = ".xb";
    private final PluginManager pluginManager;
    private final Map<String, XBApplicationModule> modules = new HashMap<>();

    public XBDefaultModuleRepository() {
        pluginManager = PluginManagerFactory.createPluginManager();
    }

    /**
     * Scans for all valid modules in give directory or specified jar file.
     *
     * @param uri
     */
    @Override
    public void addPluginsFrom(URI uri) {
        pluginManager.addPluginsFrom(uri);
    }

    /**
     * Processes all modules and initializes them in proper order.
     */
    @Override
    public void processModules() {
        PluginManagerUtil pmu = new PluginManagerUtil(pluginManager);
        final Collection<XBApplicationModulePlugin> plugins = pmu.getPlugins(XBApplicationModulePlugin.class);

        // Process modules info
        for (XBApplicationModulePlugin plugin : plugins) {
            String canonicalName = plugin.getClass().getCanonicalName();
            XBBasicApplicationModule module = new XBBasicApplicationModule(canonicalName, plugin);
            InputStream moduleFile = plugin.getClass().getResourceAsStream(canonicalName + MODULE_FILE_EXT);
            // TODO XBPSequencePullConsumer consumer = new XBPSequencePullConsumer(new XBTPunew XBTPull)

            modules.put(canonicalName, module);
        }

        // Process dependencies
        List<XBApplicationModule> unprocessedModules = new ArrayList<>(modules.values());
        int preRoundCount;
        int postRoundCount;
        do {
            preRoundCount = unprocessedModules.size();

            int moduleIndex = 0;
            while (moduleIndex < unprocessedModules.size()) {
                XBApplicationModule module = unprocessedModules.get(moduleIndex);
                // Process single module
                List<String> dependencyModuleIds = module.getDependencyModuleIds();
                boolean dependecySatisfied = true;
                for (String dependecyModuleId : dependencyModuleIds) {
                    XBApplicationModule dependecyModule = getModuleById(dependecyModuleId);
                    if (dependecyModule == null) {
                        dependecySatisfied = false;
                        break;
                    }
                }

                if (dependecySatisfied) {
                    // TODO init module

                    unprocessedModules.remove(moduleIndex);
                } else {
                    moduleIndex++;
                }
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
    @Override
    public XBApplicationModule getModuleById(String moduleId) {
        return modules.get(moduleId);
    }

    /**
     * Gets plugin manager.
     *
     * @return the pluginManager
     */
    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Gets list of modules.
     *
     * @return list of modules
     */
    @Override
    public List<XBApplicationModule> getModulesList() {
        return new ArrayList<>(modules.values());
    }
}
