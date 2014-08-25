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
package org.xbup.tool.editor.module.xbdoc_editor;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.editor.module.xbdoc_editor.panel.CatalogConnectionPanel;
import org.xbup.tool.editor.module.text_editor.panel.TextColorOptionsPanel;
import org.xbup.tool.editor.module.text_editor.panel.TextFontOptionsPanel;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.BasicMenuType;
import org.xbup.tool.editor.base.api.FileTypeManagement;
import org.xbup.tool.editor.base.api.MenuManagement;
import org.xbup.tool.editor.base.api.MenuPositionMode;
import org.xbup.tool.editor.base.api.ModuleManagement;
import org.xbup.tool.editor.base.api.OptionsManagement;
import org.xbup.tool.editor.base.api.StatusManagement;
import org.xbup.tool.editor.base.api.XBEditorApp;

/**
 * XB Document Editor Module.
 *
 * @version 0.1.22 2013/03/10
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class XBDocEditorModule implements ApplicationModule {

    private XBDocEditorFrame editorFrame;

    /** Constructor */
    public XBDocEditorModule() {
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "xbdoceditor";
            }

            @Override
            public String getPluginName() {
                return "XB Doc Editor";
            }

            @Override
            public String getPluginDescription() {
                return "Editor for XBUP-encoded documents";
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

        editorFrame = new XBDocEditorFrame();

        management.registerPanel(editorFrame.activePanel);

        editorFrame.setMainFrameManagement(management.getMainFrameManagement());

        // Register menus
        MenuManagement menuManagement = management.getMenuManagement();
        editorFrame.setMenuManagement(menuManagement);
        menuManagement.extendMenu(editorFrame.fileMenu, BasicMenuType.FILE, MenuPositionMode.PANEL);
        menuManagement.extendMenu(editorFrame.editMenu, BasicMenuType.EDIT, MenuPositionMode.PANEL);
        menuManagement.extendMenu(editorFrame.viewMenu, BasicMenuType.VIEW, MenuPositionMode.PANEL);
        menuManagement.extendMenu(editorFrame.optionsMenu, BasicMenuType.OPTIONS, MenuPositionMode.PANEL);
        menuManagement.extendMenu(editorFrame.toolsMenu, BasicMenuType.TOOLS, MenuPositionMode.PANEL);
        menuManagement.extendToolBar(editorFrame.getToolBar());
        menuManagement.insertMainPopupMenu(editorFrame.getPopupMenu(), 4);

        // Register file types
        FileTypeManagement fileTypeManagement = management.getFileTypeManagement();
        fileTypeManagement.addFileType(editorFrame.newXBFileType());

        // Register status panel
        StatusManagement statusManagement = management.getStatusManagement();
        statusManagement.addStatusPanel(editorFrame.getStatusPanel());

        // Register options panel
        OptionsManagement optionsManagement = management.getOptionsManagement();
        optionsManagement.addOptionsPanel(new CatalogConnectionPanel());
        optionsManagement.addOptionsPanel(new TextColorOptionsPanel(editorFrame));
        optionsManagement.addOptionsPanel(new TextFontOptionsPanel(editorFrame));
    }

    public void setEditorApp(XBEditorApp editorApp) {
        editorFrame.setEditorApp(editorApp);
    }

    public void setDevMode(boolean devMode) {
        editorFrame.setDevMode(devMode);
    }
}
