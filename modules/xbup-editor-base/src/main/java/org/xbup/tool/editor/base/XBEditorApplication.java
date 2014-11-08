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
 * @version 0.1.23 2013/12/12
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

    /**
     * @return the firstCommand
     */
    public XBAppCommand getFirstCommand() {
        return firstCommand;
    }

    /**
     * @param aFirstCommand the firstCommand to set
     */
    public void setFirstCommand(XBAppCommand aFirstCommand) {
        firstCommand = aFirstCommand;
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
     * At startup create and show the main frame of the application.
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

    /**
     * @return the appMode
     */
    @Override
    public boolean isAppMode() {
        return appMode;
    }

    /**
     * @param appMode the appMode to set
     */
    public void setAppMode(boolean appMode) {
        this.appMode = appMode;
    }

    /**
     * @return the appBundle
     */
    @Override
    public ResourceBundle getAppBundle() {
        return appBundle;
    }

    /**
     * @param appBundle the appBundle to set
     */
    public void setAppBundle(ResourceBundle appBundle) {
        this.appBundle = appBundle;
        if (mainFrame != null) {
            mainFrame.setAppEditor(this);
        }
    }

    /**
     * @return the appPreferences
     */
    @Override
    public Preferences getAppPreferences() {
        return appPreferences;
    }

    /**
     * @param appPreferences the appPreferences to set
     */
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

    /**
     * @return the moduleRepository
     */
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
     * Add plugin to list of plugins.
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
