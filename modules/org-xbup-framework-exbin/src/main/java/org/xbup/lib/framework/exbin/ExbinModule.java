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
package org.xbup.lib.framework.exbin;

import java.util.prefs.Preferences;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.utils.WindowUtils;
import org.xbup.lib.framework.exbin.dialog.LoginDialog;
import org.xbup.lib.framework.exbin.panel.ExbinMainPanel;

/**
 * XBUP service manager module.
 *
 * @version 0.2.0 2016/02/16
 * @author XBUP Project (http://xbup.org)
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
        LoginDialog loginDialog = new LoginDialog(WindowUtils.getFrame(mainPanel), true);
        loginDialog.setLocationRelativeTo(loginDialog.getParent());
        loginDialog.loadConnectionList(preferences);
        loginDialog.setVisible(true);
        loginDialog.saveConnectionList(preferences);
        exbinMainPanel().setService(loginDialog.getService());
    }

    public ExbinMainPanel exbinMainPanel() {
        if (mainPanel == null) {
            mainPanel = new ExbinMainPanel();
        }

        return mainPanel;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
