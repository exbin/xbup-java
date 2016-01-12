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
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.options.api.GuiOptionsModuleApi;
import org.xbup.lib.framework.gui.options.api.OptionsPanel;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Implementation of XBUP framework file module.
 *
 * @version 0.2.0 2016/01/12
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiOptionsModule implements GuiOptionsModuleApi {

    private XBApplication application;
    private Action optionsAction;
    private OptionsDialog optionsDialog;

    public GuiOptionsModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    private OptionsDialog getOptionsDialog() {
        if (optionsDialog == null) {
            GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
            optionsDialog = new OptionsDialog(frameModule.getFrame(), true, frameModule.getFrameHandler());
        }
        
        return optionsDialog;
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

                    getOptionsDialog();
                    optionsDialog.setAppEditor(application);
                    optionsDialog.setPreferences(preferences);
                    optionsDialog.setVisible(true);
                }
            };
            optionsAction.putValue("Name", "Options");
            optionsAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        }
        
        return optionsAction;
    }

    @Override
    public void addOptionsPanel(OptionsPanel optionsPanel) {
        getOptionsDialog().addOptionsPanel(optionsPanel);
    }

    @Override
    public Preferences getPreferences() {
        return null; // TODO getOptionsDialog().getPreferences();
    }

    @Override
    public void extendMainOptionsPanel(OptionsPanel panel) {
        getOptionsDialog().extendMainOptionsPanel(panel);
    }

    @Override
    public void extendAppearanceOptionsPanel(OptionsPanel panel) {
        getOptionsDialog().extendAppearanceOptionsPanel(panel);
    }
}
