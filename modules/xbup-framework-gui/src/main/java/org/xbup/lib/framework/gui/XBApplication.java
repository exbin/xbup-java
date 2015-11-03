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

import java.awt.Image;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Interface for application module management.
 *
 * @version 0.2.0 2015/10/28
 * @author XBUP Project (http://xbup.org)
 */
public interface XBApplication {

    /**
     * Gets application bundle.
     *
     * @return the appBundle
     */
    public ResourceBundle getAppBundle();

    /**
     * Gets application preferences.
     *
     * @return the appPreferences
     */
    public Preferences getAppPreferences();

    /**
     * Gets modules repository.
     *
     * @return the moduleRepository
     */
    public ModuleRepository getModuleRepository();

    /**
     * Gets application modeflag.
     *
     * @return the appMode
     */
    public boolean isAppMode();

    /**
     * Gets preferences key value.
     *
     * @param key
     * @param def
     * @return value
     */
    public String preferencesGet(String key, String def);

    /**
     * Gets application icon.
     *
     * @return application icon image
     */
    public Image getApplicationIcon();
}