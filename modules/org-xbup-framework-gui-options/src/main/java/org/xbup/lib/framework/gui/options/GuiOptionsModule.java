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
package org.xbup.lib.framework.gui.options;

import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.options.api.GuiOptionsModuleApi;

/**
 * Implementation of XBUP framework file module.
 *
 * @version 0.2.0 2016/01/09
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiOptionsModule implements GuiOptionsModuleApi {

    private XBApplication application;
    private Action optionsAction;

    public GuiOptionsModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    @Override
    public Action getOptionsAction() {
        if (optionsAction == null) {
            optionsAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Temporary fixed preferences
                    Preferences preferences;
                    try {
                        preferences = Preferences.userNodeForPackage(GuiOptionsModule.class);
                    } catch (SecurityException ex) {
                        preferences = null;
                    }

                    GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
                    OptionsDialog optionsDialog = new OptionsDialog(frameModule.getFrame(), true, frameModule.getFrameHandler());
                    optionsDialog.setAppEditor(application);
                    optionsDialog.setPreferences(preferences);
                    optionsDialog.setVisible(true);
                }
            };
            optionsAction.putValue("Name", "Options...");
        }
        
        return optionsAction;
    }
}
