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
package org.xbup.tool.editor.module.online_help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.BasicMenuType;
import org.xbup.tool.editor.base.api.MenuPositionMode;
import org.xbup.tool.editor.base.api.ModuleManagement;

/**
 * JavaOnlineHelp module.
 *
 * @version 0.1.22 2013/03/10
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class OnlineHelpModule implements ApplicationModule {

    private URL helpUrl;
    private final String DIALOG_MENU_SUFIX = "...";
    private JMenuItem helpOnlineMenuItem;
    private ResourceBundle resourceBundle;

    /** Constructor */
    public OnlineHelpModule() {
        resourceBundle = ResourceBundle.getBundle("org/xbup/tool/xbeditor/module/onlinehelp/resources/OnlineHelpModule");
        try {
            helpUrl = new URL("http://xbup.org/");
        } catch (MalformedURLException ex) {
            Logger.getLogger(OnlineHelpModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "onlinehelp";
            }

            @Override
            public String getPluginName() {
                return "OnlineHelp";
            }

            @Override
            public String getPluginDescription() {
                return "Online help on project website";
            }

            @Override
            public String[] pluginDependency() {
                return null;
            }

            @Override
            public String[] pluginOptional() {
                return null;
            }
        };
    }

    @Override
    public void init(ModuleManagement management) {
        helpOnlineMenuItem = new JMenuItem(resourceBundle.getString("OnlineHelpModule.helpOnlineMenuItem.text")+DIALOG_MENU_SUFIX);
        helpOnlineMenuItem.setToolTipText(resourceBundle.getString("OnlineHelpModule.helpOnlineMenuItem.toolTipText"));
        helpOnlineMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BareBonesBrowserLaunch.openURL(helpUrl.toExternalForm());
            }
        });
        management.getMenuManagement().addMenuItem(helpOnlineMenuItem, BasicMenuType.HELP, MenuPositionMode.BEFORE_PANEL);
    }

    /**
     * @return the helpUrl
     */
    public URL getHelpUrl() {
        return helpUrl;
    }

    /**
     * @param helpUrl the helpUrl to set
     */
    public void setHelpUrl(URL helpUrl) {
        this.helpUrl = helpUrl;
    }


}
