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

import java.io.IOException;
import org.xbup.lib.framework.gui.api.XBModuleRepository;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;
import net.xeoh.plugins.base.util.uri.ClassURI;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModule;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;

/**
 * XBUP framework modules repository.
 *
 * @version 0.2.0 2016/01/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBDefaultModuleRepository implements XBModuleRepository {

    private static final String MODULE_FILE = "module.xb";
    private static final String MODULE_ID = "MODULE_ID";
    private final PluginManager pluginManager;
    private final Map<String, XBApplicationModule> modules = new HashMap<>();
    private XBApplication application;

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

    @Override
    public void addClassPathPlugins() {
        pluginManager.addPluginsFrom(ClassURI.CLASSPATH);
    }

    /**
     * Initializes all modules in order of their dependencies.
     */
    @Override
    public void initModules() {
        PluginManagerUtil pmu = new PluginManagerUtil(pluginManager);
        final Collection<XBApplicationModulePlugin> plugins = pmu.getPlugins(XBApplicationModulePlugin.class);

        // Process modules info
        for (XBApplicationModulePlugin plugin : plugins) {
            String canonicalName = plugin.getClass().getCanonicalName();
            XBBasicApplicationModule module = new XBBasicApplicationModule(canonicalName, plugin);
            URL moduleClassLocation = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
            URL resource = plugin.getClass().getClassLoader().getResource("META-INF/MANIFEST.MF");
            URL moduleRecordUrl;
            InputStream moduleRecordStream = null;
            try {
                moduleRecordUrl = new URL("jar:" + moduleClassLocation.toExternalForm() + "!/META-INF/" + MODULE_FILE);
                moduleRecordStream = moduleRecordUrl.openStream();
            } catch (IOException ex) {
                // ignore
            }
            if (moduleRecordStream != null) {
                try {
                    XBPullReader pullReader = new XBPullReader(moduleRecordStream);
                    XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
                    serial.process(module);
                } catch (IOException ex) {
                    // ignore
                }
            }
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
                    XBApplicationModule dependecyModule = getModuleRecordById(dependecyModuleId);
                    if (dependecyModule == null || findModule(unprocessedModules, dependecyModuleId)) {
                        dependecySatisfied = false;
                        break;
                    }
                }

                if (dependecySatisfied) {
                    module.getPlugin().init(application);
                    unprocessedModules.remove(moduleIndex);
                } else {
                    moduleIndex++;
                }
            }

            postRoundCount = unprocessedModules.size();
        } while (postRoundCount > 0 && postRoundCount < preRoundCount);

        if (postRoundCount > 0) {
            throw new IllegalStateException("Circular dependency detected");
        }
    }

    /**
     * Gets info about module.
     *
     * @param moduleId module identifier
     * @return application module record
     */
    @Override
    public XBApplicationModule getModuleRecordById(String moduleId) {
        return modules.get(moduleId);
    }

    private boolean findModule(List<XBApplicationModule> modules, String moduleId) {
        for (XBApplicationModule module : modules) {
            if (moduleId.equals(module.getModuleId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets info about module.
     *
     * @param moduleId module identifier
     * @return application module record
     */
    @Override
    public XBApplicationModulePlugin getModuleById(String moduleId) {
        XBApplicationModule moduleRecord = getModuleRecordById(moduleId);
        return moduleRecord == null ? null : moduleRecord.getPlugin();
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

    @Override
    public <T extends XBApplicationModulePlugin> T getModuleByInterface(Class<T> interfaceClass) {
        try {
            Field declaredField = interfaceClass.getDeclaredField(MODULE_ID);
            if (declaredField != null) {
                Object moduleId = declaredField.get(null);
                if (moduleId instanceof String) {
                    return (T) getModuleById((String) moduleId);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(XBDefaultModuleRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    void setApplication(XBApplication application) {
        this.application = application;
    }
}
