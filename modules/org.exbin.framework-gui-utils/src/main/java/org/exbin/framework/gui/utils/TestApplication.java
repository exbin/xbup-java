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
package org.exbin.framework.gui.utils;

import java.awt.Image;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import net.xeoh.plugins.base.PluginManager;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.api.XBApplicationModule;
import org.exbin.framework.api.XBApplicationModulePlugin;
import org.exbin.framework.api.XBModuleRepository;

/**
 * Some simple static methods usable for windows and dialogs.
 *
 * @version 0.2.0 2016/03/21
 * @author ExBin Project (http://exbin.org)
 */
public class TestApplication implements XBApplication {

    private final Map<String, XBApplicationModulePlugin> modules = new HashMap<>();

    public TestApplication() {
    }

    public void addModule(String moduleId, XBApplicationModulePlugin module) {
        modules.put(moduleId, module);
    }

    @Override
    public ResourceBundle getAppBundle() {
        return emptyBundle;
    }

    @Override
    public Preferences getAppPreferences() {
        return Preferences.systemRoot();
    }

    @Override
    public XBModuleRepository getModuleRepository() {
        return new XBModuleRepository() {
            private static final String MODULE_ID = "MODULE_ID";

            @Override
            public void addPluginsFrom(URI uri) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addClassPathPlugins() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addPluginsFromManifest(Class sourceClass) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public XBApplicationModule getModuleRecordById(String moduleId) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public XBApplicationModulePlugin getModuleById(String moduleId) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public <T extends XBApplicationModulePlugin> T getModuleByInterface(Class<T> interfaceClass) {
                try {
                    Field declaredField = interfaceClass.getDeclaredField(MODULE_ID);
                    if (declaredField != null) {
                        Object interfaceModuleId = declaredField.get(null);
                        if (interfaceModuleId instanceof String) {
                            return (T) modules.get((String) interfaceModuleId);
                        }
                    }

                    return null;
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(TestApplication.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
            }

            @Override
            public List<XBApplicationModule> getModulesList() {
                return new ArrayList<>();
            }

            @Override
            public PluginManager getPluginManager() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void initModules() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    public String preferencesGet(String key, String def) {
        return null;
    }

    @Override
    public Image getApplicationIcon() {
        return null;
    }
    ResourceBundle emptyBundle = new ResourceBundle() {

        @Override
        protected Object handleGetObject(String key) {
            return "";
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.emptyEnumeration();
        }
    };
}
