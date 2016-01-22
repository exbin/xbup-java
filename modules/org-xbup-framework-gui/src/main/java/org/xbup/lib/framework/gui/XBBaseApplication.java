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

import org.xbup.lib.framework.gui.api.XBApplication;
import java.awt.Image;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import org.xbup.lib.framework.gui.api.XBModuleRepository;

/**
 * Application interface class.
 *
 * @version 0.2.0 2015/12/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBBaseApplication implements XBApplication {

    public static final String PREFERENCES_LOOK_AND_FEEL = "lookAndFeel";
    public static final String PREFERENCES_LOCALE_LANGUAGE = "locale.language";
    public static final String PREFERENCES_LOCALE_COUNTRY = "locale.country";
    public static final String PREFERENCES_LOCALE_VARIANT = "locale.variant";

    private ResourceBundle appBundle;
    private Preferences appPreferences;

    private final XBDefaultModuleRepository moduleRepository;
    private final List<URI> plugins;

    public XBBaseApplication() {
        plugins = new ArrayList<>();
        moduleRepository = new XBDefaultModuleRepository();
    }

    /**
     * At init creates and shows the main frame of the application.
     */
    public void init() {
        moduleRepository.setApplication(this);
    }

    @Override
    public String preferencesGet(String key, String def) {
        if (getAppPreferences() == null) {
            return def;
        }

        return getAppPreferences().get(key, def);
    }

    @Override
    public ResourceBundle getAppBundle() {
        return appBundle;
    }

    /**
     * Sets application resource bundle handler.
     *
     * @param appBundle application resource bundle
     * @param bundleName this is workaround for getBaseBundleName method
     * available only in Java 1.8
     */
    public void setAppBundle(ResourceBundle appBundle, String bundleName) {
        if (!Locale.getDefault().equals(appBundle.getLocale())) {
            appBundle = ResourceBundle.getBundle(bundleName);
            // appBundle = ResourceBundle.getBundle(appBundle.getBaseBundleName());
        }

        this.appBundle = appBundle;
    }

    @Override
    public Preferences getAppPreferences() {
        return appPreferences;
    }

    public void setAppPreferences(Preferences appPreferences) {
        this.appPreferences = appPreferences;

        // Switching language
        String localeLanguage = preferencesGet(PREFERENCES_LOCALE_LANGUAGE, Locale.US.getLanguage());
        String localeCountry = preferencesGet(PREFERENCES_LOCALE_COUNTRY, Locale.US.getCountry());
        String localeVariant = preferencesGet(PREFERENCES_LOCALE_VARIANT, Locale.US.getVariant());
        try {
            Locale locale = new Locale(localeLanguage, localeCountry, localeVariant);
            if (!locale.equals(Locale.ROOT)) {
                Locale.setDefault(locale);
            }
        } catch (SecurityException ex) {
            // Ignore it in java webstart
        }
    }

    /**
     * Adds plugin to the list of plugins.
     *
     * @param uri URI to plugin.
     */
    public void loadPlugin(URI uri) {
        if (!plugins.add(uri)) {
            throw new RuntimeException("Unable to load plugin: " + uri.toString());
        }

        getModuleRepository().addPluginsFrom(uri);
    }

    public void loadPlugin(String jarFilePath) {
        try {
            loadPlugin(new URI(jarFilePath));
        } catch (URISyntaxException ex) {
            // ignore
        }
    }

    public void loadClassPathPlugins(Class targetClass) {
        try {
            Manifest manifest = new Manifest(targetClass.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
            Attributes classPaths = manifest.getAttributes("Class-Path");
            Collection<Object> values = classPaths.values();
            for (Object classPath : values) {
                if (classPath instanceof String) {
                    XBBaseApplication.this.loadPlugin((String) classPath);
                }
            }
        } catch (IOException ex) {
            // ignore
        }
    }

    @Override
    public Image getApplicationIcon() {
        return new ImageIcon(getClass().getResource(getAppBundle().getString("Application.icon"))).getImage();
    }

    @Override
    public XBModuleRepository getModuleRepository() {
        return moduleRepository;
    }
}
