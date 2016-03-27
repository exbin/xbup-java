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
package org.exbin.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.api.XBApplicationModuleRepository;
import org.exbin.xbup.plugin.XBModule;
import org.exbin.xbup.plugin.XBModuleHandler;
import org.exbin.xbup.plugin.XBModuleRecord;

/**
 * XBUP framework modules repository.
 *
 * @version 0.2.0 2016/03/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBDefaultApplicationModuleRepository implements XBApplicationModuleRepository {

    private static final String MODULE_FILE = "module.xb";
    private static final String MODULE_ID = "MODULE_ID";
    private final Map<String, XBModuleRecord> modules = new HashMap<>();
    private XBApplication application;

    public XBDefaultApplicationModuleRepository() {
    }

    @Override
    public void addModulesFrom(URI moduleClassURL) {
        // TODO pluginManager.addPluginsFrom(uri);
    }

    @Override
    public void addClassPathModules() {
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        for (String classpathEntry : classpathEntries) {
            addModulePlugin(new File(classpathEntry).toURI());
        }
    }

    @Override
    public void addModulesFromManifest(Class manifestClass) {
        try {
            URL moduleClassLocation = manifestClass.getProtectionDomain().getCodeSource().getLocation();
            URL manifestUrl = new URL("jar:" + moduleClassLocation.toExternalForm() + "!/META-INF/MANIFEST.MF");

            Manifest manifest = new Manifest(manifestUrl.openStream());
            String classPaths = manifest.getMainAttributes().getValue(new Attributes.Name("Class-Path"));
            String[] paths = classPaths.split(" ");
            String rootDirectory = new File(moduleClassLocation.toExternalForm()).getParent();
            for (String path : paths) {
                try {
                    addModulePlugin(new URI(rootDirectory + File.separator + path));
                } catch (URISyntaxException ex) {
                    // Ignore
                }
            }
        } catch (FileNotFoundException | MalformedURLException ex) {
            // Ignore
        } catch (IOException ex) {
            // Ignore
        }
    }

    /**
     * Attempts to load main library class if library URL contains valid module
     * declaration.
     *
     * @param libraryUrl library URL
     */
    private void addModulePlugin(URI libraryUrl) {
        URL moduleRecordUrl;
        InputStream moduleRecordStream = null;
        try {
            moduleRecordUrl = new URL("jar:" + libraryUrl.toURL().toExternalForm() + "!/META-INF/" + MODULE_FILE);
            moduleRecordStream = moduleRecordUrl.openStream();
        } catch (IOException ex) {
            // ignore
        }

        if (moduleRecordStream != null) {
            // TODO identify main class from module to load it, so that jar doesn't have to be scanned
            // TODO addPluginsFrom(libraryUrl);
        }
    }

    /**
     * Initializes all modules in order of their dependencies.
     */
    @Override
    public void initModules() {
//        // Process modules info
//        for (XBApplicationModule plugin : plugins) {
//            String canonicalName = plugin.getClass().getCanonicalName();
//            XBBasicApplicationModule module = new XBBasicApplicationModule(canonicalName, plugin);
//            URL moduleClassLocation = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
//            URL moduleRecordUrl;
//            InputStream moduleRecordStream = null;
//            try {
//                moduleRecordUrl = new URL("jar:" + moduleClassLocation.toExternalForm() + "!/META-INF/" + MODULE_FILE);
//                moduleRecordStream = moduleRecordUrl.openStream();
//            } catch (IOException ex) {
//                // ignore
//            }
//            if (moduleRecordStream != null) {
//                try {
//                    XBPullReader pullReader = new XBPullReader(moduleRecordStream);
//                    XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
//                    serial.process(module);
//                } catch (IOException ex) {
//                    // ignore
//                }
//            }
//            modules.put(canonicalName, module);
//        }

        // Process dependencies
        List<XBModuleRecord> unprocessedModules = new ArrayList<>(modules.values());
        int preRoundCount;
        int postRoundCount;
        do {
            preRoundCount = unprocessedModules.size();

            int moduleIndex = 0;
            while (moduleIndex < unprocessedModules.size()) {
                XBModuleRecord module = unprocessedModules.get(moduleIndex);
                // Process single module
                List<String> dependencyModuleIds = module.getDependencyModuleIds();
                boolean dependecySatisfied = true;
                for (String dependecyModuleId : dependencyModuleIds) {
                    XBModuleRecord dependecyModule = getModuleRecordById(dependecyModuleId);
                    if (dependecyModule == null || findModule(unprocessedModules, dependecyModuleId)) {
                        dependecySatisfied = false;
                        break;
                    }
                }

                if (dependecySatisfied) {
                    module.getModule().init((XBModuleHandler) application);
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
    public XBModuleRecord getModuleRecordById(String moduleId) {
        return modules.get(moduleId);
    }

    private boolean findModule(List<XBModuleRecord> modules, String moduleId) {
        for (XBModuleRecord module : modules) {
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
    public XBModule getModuleById(String moduleId) {
        XBModuleRecord moduleRecord = getModuleRecordById(moduleId);
        return moduleRecord == null ? null : moduleRecord.getModule();
    }

    /**
     * Gets list of modules.
     *
     * @return list of modules
     */
    @Override
    public List<XBModuleRecord> getModulesList() {
        return new ArrayList<>(modules.values());
    }

    @Override
    public <T extends XBModule> T getModuleByInterface(Class<T> interfaceClass) {
        try {
            Field declaredField = interfaceClass.getDeclaredField(MODULE_ID);
            if (declaredField != null) {
                Object moduleId = declaredField.get(null);
                if (moduleId instanceof String) {
                    return (T) getModuleById((String) moduleId);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(XBDefaultApplicationModuleRepository.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    void setApplication(XBApplication application) {
        this.application = application;
    }

    @Override
    public void addModulesFrom(URL moduleJarFileUrl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
