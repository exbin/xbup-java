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
package org.xbup.tool.editor.module.text_editor;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.editor.module.text_editor.panel.TextAppearanceOptionsPanel;
import org.xbup.tool.editor.module.text_editor.panel.TextColorOptionsPanel;
import org.xbup.tool.editor.module.text_editor.panel.TextEncodingOptionsPanel;
import org.xbup.tool.editor.module.text_editor.panel.TextFontOptionsPanel;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.FileTypeManagement;
import org.xbup.tool.editor.base.api.ModuleManagement;
import org.xbup.tool.editor.base.api.OptionsManagement;
import org.xbup.tool.editor.base.api.StatusManagement;

/**
 * XB Text Editor Module.
 *
 * @version 0.1.24 2015/01/30
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class XBTextEditorModule implements ApplicationModule {

    private XBTextEditorFrame editorFrame;

    public XBTextEditorModule() {
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "xbtexteditor";
            }

            @Override
            public String getPluginName() {
                return "XB Text Editor";
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
        editorFrame = new XBTextEditorFrame();
        editorFrame.setMainFrameManagement(management.getMainFrameManagement());
        management.registerPanel(editorFrame.getActivePanel());
        editorFrame.setMenuManagement(management.getMenuManagement());

        // Register file types
        FileTypeManagement fileTypeManagement = management.getFileTypeManagement();
        fileTypeManagement.addFileType(editorFrame.newXBTFileType());
        fileTypeManagement.addFileType(editorFrame.newTXTFileType());

        // Register status panel
        StatusManagement statusManagement = management.getStatusManagement();
        statusManagement.addStatusPanel(editorFrame.getStatusPanel());

        // Register options panel
        OptionsManagement optionsManagement = management.getOptionsManagement();
        optionsManagement.addOptionsPanel(new TextColorOptionsPanel(editorFrame));
        optionsManagement.addOptionsPanel(new TextFontOptionsPanel(editorFrame));
        optionsManagement.addOptionsPanel(new TextEncodingOptionsPanel(editorFrame));
        optionsManagement.extendAppearanceOptionsPanel(new TextAppearanceOptionsPanel(editorFrame));
    }
}
