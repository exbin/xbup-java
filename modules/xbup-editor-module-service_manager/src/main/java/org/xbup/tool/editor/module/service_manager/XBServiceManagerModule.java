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
package org.xbup.tool.editor.module.service_manager;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.editor.module.service_manager.panel.ServiceManagerPanel;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.MenuManagement;
import org.xbup.tool.editor.base.api.MenuPositionMode;
import org.xbup.tool.editor.base.api.ModuleManagement;
import org.xbup.tool.editor.base.api.StatusManagement;

/**
 * XBUP Service Manager Module.
 *
 * @version 0.1.24 2013/09/26
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class XBServiceManagerModule implements ApplicationModule {

    private XBServiceManagerFrame editorFrame;

    public XBServiceManagerModule() {
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "xbservicemanager";
            }

            @Override
            public String getPluginName() {
                return "XB Service Manager";
            }

            @Override
            public String getPluginDescription() {
                return "Simple manager for XBUP service";
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
        editorFrame = new XBServiceManagerFrame();
        ServiceManagerPanel activePanel = editorFrame.getActivePanel();
        management.registerPanel(activePanel);
        activePanel.setMenuManagement(management.getMenuManagement());

        // Register menus
        MenuManagement menuManagement = management.getMenuManagement();
        menuManagement.insertMenu(editorFrame.serviceMenu, MenuPositionMode.PANEL);

        // Register status panel
        StatusManagement statusManagement = management.getStatusManagement();
        statusManagement.addStatusPanel(editorFrame.getStatusPanel());

        editorFrame.setMainFrameManagement(management.getMainFrameManagement());
    }

    public XBServiceManagerFrame getEditorFrame() {
        return editorFrame;
    }
}
