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

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.menu.api.MenuGroup;
import org.exbin.framework.gui.menu.api.MenuPosition;
import org.exbin.framework.gui.menu.api.PositionMode;
import org.exbin.framework.gui.menu.api.SeparationMode;
import org.exbin.framework.gui.options.api.GuiOptionsModuleApi;
import org.exbin.framework.gui.options.api.OptionsPanel;
import org.exbin.framework.gui.options.dialog.OptionsDialog;
import org.exbin.framework.gui.utils.ActionUtils;
import org.exbin.xbup.plugin.XBModuleHandler;

/**
 * Implementation of XBUP framework file module.
 *
 * @version 0.2.0 2016/01/22
 * @author ExBin Project (http://exbin.org)
 */
public class GuiOptionsModule implements GuiOptionsModuleApi {

    private XBApplication application;
    private ResourceBundle resourceBundle;

    private Action optionsAction;
    private OptionsDialog optionsDialog;

    public GuiOptionsModule() {
    }

    @Override
    public void init(XBModuleHandler moduleHandler) {
        this.application = (XBApplication) moduleHandler;
        resourceBundle = ActionUtils.getResourceBundleByClass(GuiOptionsModule.class);
    }

    @Override
    public void unregisterModule(String moduleId) {
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
                    getOptionsDialog();
                    optionsDialog.setAppEditor(application);
                    optionsDialog.setPreferences(application.getAppPreferences());
                    optionsDialog.setVisible(true);
                }
            };

            ActionUtils.setupAction(optionsAction, resourceBundle, "optionsAction");
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

    @Override
    public void registerMenuAction() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.TOOLS_MENU_ID, new MenuGroup(TOOLS_OPTIONS_MENU_GROUP_ID, new MenuPosition(PositionMode.BOTTOM_LAST), SeparationMode.AROUND));
        getOptionsAction();
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, optionsAction, new MenuPosition(TOOLS_OPTIONS_MENU_GROUP_ID));
    }
}
