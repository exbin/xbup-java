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
package org.exbin.framework.gui.options;

import java.util.prefs.Preferences;
import org.exbin.framework.gui.options.api.OptionsPanel;

/**
 * Interface for application options panels management.
 *
 * @version 0.2.0 2015/11/02
 * @author ExBin Project (http://exbin.org)
 */
public interface OptionsManagement {

    public static final String PREFERENCES_LOOK_AND_FEEL = "lookAndFeel";
    public static final String PREFERENCES_LOCALE_LANGUAGE = "locale.language";
    public static final String PREFERENCES_LOCALE_COUNTRY = "locale.country";
    public static final String PREFERENCES_LOCALE_VARIANT = "locale.variant";

    /**
     * Adds options panel.
     *
     * @param optionsPanel
     */
    public void addOptionsPanel(OptionsPanel optionsPanel);

    /**
     * Extends main options panel.
     *
     * @param optionsPanel
     */
    public void extendMainOptionsPanel(OptionsPanel optionsPanel);

    /**
     * Extends appearance options panel.
     *
     * @param optionsPanel
     */
    public void extendAppearanceOptionsPanel(OptionsPanel optionsPanel);

    /**
     * Gets preferences.
     *
     * @return prefereces.
     */
    public Preferences getPreferences();
}
