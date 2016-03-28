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
package org.exbin.framework.api;

import java.awt.Image;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import org.exbin.xbup.plugin.XBModuleHandler;

/**
 * Interface for application module management.
 *
 * @version 0.2.0 2016/03/28
 * @author ExBin Project (http://exbin.org)
 */
public interface XBApplication extends XBModuleHandler {

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
    public XBApplicationModuleRepository getModuleRepository();

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
