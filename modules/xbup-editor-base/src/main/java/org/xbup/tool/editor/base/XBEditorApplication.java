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
package org.xbup.tool.editor.base;

import java.awt.Image;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import org.xbup.tool.editor.module.frame.MainFrame;
import org.xbup.tool.editor.base.api.OptionsManagement;
import org.xbup.tool.editor.base.api.XBEditorApp;
import org.xbup.tool.editor.base.manager.BaseModuleRepository;

/**
 * Main class for XBEditors.
 *
 * @version 0.1.24 2015/01/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBEditorApplication implements XBEditorApp {

    private ResourceBundle appBundle;
    private Preferences appPreferences;
    private boolean appMode = false;
    private MainFrame mainFrame;

    private final BaseModuleRepository moduleRepository;
    private final List<URI> plugins;

    private XBAppCommand firstCommand;

    public XBEditorApplication() {
        plugins = new ArrayList<>();
        moduleRepository = new BaseModuleRepository();
    }

    public XBAppCommand getFirstCommand() {
        return firstCommand;
    }

    public void setFirstCommand(XBAppCommand firstCommand) {
        this.firstCommand = firstCommand;
    }

    public void prepare() {
        if (isAppMode()) {
            getModuleRepository().setMainFrame(mainFrame);
            getModuleRepository().processModules();
            getModuleRepository().loadPreferences(getAppPreferences());
        }
    }

    public void run() {
        if (isAppMode()) {
            mainFrame.setVisible(true);
            /*form.setSize(form.getWidth(), form.getHeight());
             form.setLocationByPlatform(true); */

            if (firstCommand != null) {
                firstCommand.execute();
            }
        }
    }

    /**
     * At startup creates and shows the main frame of the application.
     */
    public void startup() {
        prepare();
        run();
    }

    @Override
    public String preferencesGet(String key, String def) {
        if (getAppPreferences() == null) {
            return def;
        }

        return getAppPreferences().get(key, def);
    }

    @Override
    public boolean isAppMode() {
        return appMode;
    }

    public void setAppMode(boolean appMode) {
        this.appMode = appMode;
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
        if (mainFrame != null) {
            mainFrame.setAppEditor(this);
        }
    }

    @Override
    public Preferences getAppPreferences() {
        return appPreferences;
    }

    public void setAppPreferences(Preferences appPreferences) {
        this.appPreferences = appPreferences;

        // Switching language
        String localeLanguage = preferencesGet(OptionsManagement.PREFERENCES_LOCALE_LANGUAGE, Locale.US.getLanguage());
        String localeCountry = preferencesGet(OptionsManagement.PREFERENCES_LOCALE_COUNTRY, Locale.US.getCountry());
        String localeVariant = preferencesGet(OptionsManagement.PREFERENCES_LOCALE_VARIANT, Locale.US.getVariant());
        try {
            Locale locale = new Locale(localeLanguage, localeCountry, localeVariant);
            if (!locale.equals(Locale.ROOT)) {
                Locale.setDefault(locale);
            }
        } catch (SecurityException ex) {
            // Ignore it in java webstart
        }

        mainFrame = new MainFrame();
    }

    @Override
    public BaseModuleRepository getModuleRepository() {
        return moduleRepository;
    }

    public void loadFromFile(String string) {
        moduleRepository.openFile(string, null);
    }

    @Override
    public Image getApplicationIcon() {
        return new ImageIcon(getClass().getResource(getAppBundle().getString("Application.icon"))).getImage();
    }

    public interface XBAppCommand {

        public void execute();
    }

    /**
     * Adds plugin to the list of plugins.
     *
     * @param uri URI to plugin.
     */
    public void addPlugin(URI uri) {
        if (!plugins.add(uri)) {
            throw new RuntimeException("Unable to load plugin: " + uri.toString());
        }

        getModuleRepository().addPluginsFrom(uri);
    }
}
