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
package org.xbup.lib.framework.gui.options.api;

import java.util.prefs.Preferences;
import javax.swing.Action;
import org.xbup.lib.framework.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.api.XBModuleRepositoryUtils;

/**
 * Interface for XBUP framework file module.
 *
 * @version 0.2.0 2016/01/12
 * @author ExBin Project (http://exbin.org)
 */
public interface GuiOptionsModuleApi extends XBApplicationModulePlugin {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(GuiOptionsModuleApi.class);
    public static String TOOLS_OPTIONS_MENU_GROUP_ID = MODULE_ID + ".toolsOptionsMenuGroup";

    Action getOptionsAction();

    /**
     * Adds options panel.
     *
     * @param optionsPanel
     */
    void addOptionsPanel(OptionsPanel optionsPanel);

    /**
     * Extends main options panel.
     *
     * @param optionsPanel
     */
    void extendMainOptionsPanel(OptionsPanel optionsPanel);

    /**
     * Extends appearance options panel.
     *
     * @param optionsPanel
     */
    void extendAppearanceOptionsPanel(OptionsPanel optionsPanel);

    /**
     * Gets preferences.
     *
     * @return prefereces.
     */
    Preferences getPreferences();

    /**
     * Registers options menu action in default position.
     */
    public void registerMenuAction();
}
