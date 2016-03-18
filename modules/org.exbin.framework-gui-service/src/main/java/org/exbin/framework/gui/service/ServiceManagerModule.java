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
package org.exbin.framework.gui.service;

import java.awt.Component;
import java.util.prefs.Preferences;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.api.XBApplicationModulePlugin;
import org.exbin.framework.api.XBModuleRepositoryUtils;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.menu.api.MenuManagement;
import org.exbin.framework.gui.menu.api.PositionMode;
import org.exbin.framework.gui.service.dialog.ConnectionDialog;
import org.exbin.framework.gui.service.panel.ServiceManagerPanel;
import org.xbup.lib.framework.gui.utils.WindowUtils;

/**
 * XBUP service manager module.
 *
 * @version 0.2.0 2016/02/02
 * @author ExBin Project (http://exbin.org)
 */
@PluginImplementation
public class ServiceManagerModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(ServiceManagerModule.class);

    private XBApplication application;
    private ServiceManagerPanel servicePanel;
    private Preferences preferences;

    public ServiceManagerModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        // Register file types
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);
//        fileModule.addFileType(new XBTFileType());
//        fileModule.addFileType(new TXTFileType());
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    public void openConnectionDialog() {
        ConnectionDialog loginDialog = new ConnectionDialog(WindowUtils.getFrame(servicePanel), true);
        loginDialog.setLocationRelativeTo(loginDialog.getParent());
        loginDialog.loadConnectionList(preferences);
        loginDialog.setVisible(true);
        loginDialog.saveConnectionList(preferences);
        getServicePanel().setService(loginDialog.getService());
    }

    public ServiceManagerPanel getServicePanel() {
        if (servicePanel == null) {
            servicePanel = new ServiceManagerPanel();
            servicePanel.setMenuManagement(new MenuManagement() {
                @Override
                public void extendMenu(JMenu menu, Integer pluginId, String menuId, PositionMode positionMode) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void addMenuItem(Component menuItem, Integer pluginId, String menuId, PositionMode mode) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void insertMenu(JMenu menu, Integer pluginId, PositionMode positionMode) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void extendToolBar(JToolBar toolBar) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void insertMainPopupMenu(JPopupMenu popupMenu, int position) {
                    // Temporary
                    GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
                    menuModule.fillPopupMenu(popupMenu, position);
                }
            });
        }

        return servicePanel;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
