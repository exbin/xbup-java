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
package org.xbup.tool.editor.base.api;

import java.awt.Image;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Interface for application module management.
 *
 * @version 0.1 wr22.0 2013/03/25
 * @author XBUP Project (http://xbup.org)
 */
public interface XBEditorApp {

    /**
     * Get application bundle.
     *
     * @return the appBundle
     */
    public ResourceBundle getAppBundle();

    /**
     * Get application preferences.
     *
     * @return the appPreferences
     */
    public Preferences getAppPreferences();

    /**
     * Get modules repository.
     *
     * @return the moduleRepository
     */
    public ModuleRepository getModuleRepository();

    /**
     * Get application modeflag.
     *
     * @return the appMode
     */
    public boolean isAppMode();

    /**
     * Get preferences key value.
     *
     * @param key
     * @param def
     * @return value
     */
    public String preferencesGet(String key, String def);

    /**
     * Get application icon.
     *
     * @return application icon image
     */
    public Image getApplicationIcon();
}
