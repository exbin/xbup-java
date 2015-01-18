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
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.FileTypeManagement;
import org.xbup.tool.editor.base.api.ModuleManagement;
import org.xbup.tool.editor.base.api.OptionsManagement;
import org.xbup.tool.editor.base.api.StatusManagement;
import org.xbup.tool.editor.base.api.XBEditorApp;
import org.xbup.tool.editor.module.text_editor.panel.TextColorOptionsPanel;
import org.xbup.tool.editor.module.text_editor.panel.TextFontOptionsPanel;
import org.xbup.tool.editor.module.xbdoc_editor.panel.CatalogConnectionPanel;

/**
 * XB Document Editor Module.
 *
 * @version 0.1.24 2014/11/23
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class XBDocEditorModule implements ApplicationModule {

    private XBDocEditorFrame editorFrame;

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
                return "XB Document Editor";
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
        management.registerPanel(editorFrame.getActivePanel());
        editorFrame.setMainFrameManagement(management.getMainFrameManagement());
        editorFrame.setMenuManagement(management.getMenuManagement());

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

    public void postWindowOpened() {
        editorFrame.postWindowOpened();
    }
}
