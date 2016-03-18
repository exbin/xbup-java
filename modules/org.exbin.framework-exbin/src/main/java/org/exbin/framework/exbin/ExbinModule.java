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
package org.exbin.framework.exbin;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.api.XBApplicationModulePlugin;
import org.exbin.framework.api.XBModuleRepositoryUtils;
import org.xbup.lib.client.XBCatalogNetServiceClient;
import org.exbin.framework.exbin.dialog.LoginDialog;
import org.exbin.framework.exbin.panel.ExbinMainPanel;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.utils.WindowUtils;

/**
 * XBUP service manager module.
 *
 * @version 0.2.0 2016/02/16
 * @author ExBin Project (http://exbin.org)
 */
@PluginImplementation
public class ExbinModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(ExbinModule.class);

    private XBApplication application;
    private ExbinMainPanel mainPanel;
    private Preferences preferences;

    public ExbinModule() {
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
        final LoginDialog loginDialog = new LoginDialog(WindowUtils.getFrame(mainPanel), true);
        loginDialog.setLocationRelativeTo(loginDialog.getParent());
        loginDialog.loadConnectionList(preferences);
        loginDialog.setConnectionListener(new LoginDialog.ConnectionListener() {
            @Override
            public boolean connect() {
                String connectionString = loginDialog.getSelectedConnection();
                String connectionHost;
                int connectionPort = 22594; // is 0x5842 (XB)
                int pos = connectionString.indexOf(":");
                if (pos >= 0) {
                    connectionHost = connectionString.substring(0, pos);
                    connectionPort = Integer.valueOf(connectionString.substring(pos + 1));
                } else {
                    connectionHost = connectionString;
                }

                loginDialog.setConnectionStatus(Color.ORANGE, "Connecting", "Connecting to server " + connectionHost + ":" + connectionPort);

                XBCatalogNetServiceClient service = new XBCatalogNetServiceClient(connectionHost, connectionPort);
                try {
                    service.ping();
                    loginDialog.setConnectionStatus(Color.GREEN, "Connected", null);
                    return true;
                } catch (Exception ex) {
                    Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
                    loginDialog.setConnectionStatus(Color.RED, "Failed", null);
                }
                
                return false;
            }
        });
        loginDialog.setVisible(true);
        loginDialog.saveConnectionList(preferences);
        getExbinMainPanel().setService(loginDialog.getService());
    }

    public ExbinMainPanel getExbinMainPanel() {
        if (mainPanel == null) {
            mainPanel = new ExbinMainPanel();
        }

        return mainPanel;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
