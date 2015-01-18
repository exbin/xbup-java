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
package org.xbup.tool.editor.module.wave_editor;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.editor.module.wave_editor.panel.AudioDevicesPanel;
import org.xbup.tool.editor.module.wave_editor.panel.WaveColorOptionsPanel;
import org.xbup.tool.editor.base.api.ApplicationModule;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.FileTypeManagement;
import org.xbup.tool.editor.base.api.ModuleManagement;
import org.xbup.tool.editor.base.api.OptionsManagement;
import org.xbup.tool.editor.base.api.StatusManagement;

/**
 * XB Wave Editor Module.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class XBWaveEditorModule implements ApplicationModule {

    XBWaveEditorFrame editorFrame;

    public XBWaveEditorModule() {
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "xbwaveeditor";
            }

            @Override
            public String getPluginName() {
                return "XB Wave Editor";
            }

            @Override
            public String getPluginDescription() {
                return "Simple editor for audio waves";
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
        editorFrame = new XBWaveEditorFrame();
        management.registerPanel(editorFrame.getActivePanel());
        editorFrame.setMenuManagement(management.getMenuManagement());

        // Register file types
        FileTypeManagement fileTypeManagement = management.getFileTypeManagement();
        fileTypeManagement.addFileType(editorFrame.newXBSFileType());
        String[] formats = new String[3];
        formats[0] = "wav";
        formats[1] = "aiff";
        formats[2] = "au";
        for (int i = 0; i < formats.length; i++) {
            String ext = formats[i];
            if (ext.toLowerCase().equals(ext)) {
                fileTypeManagement.addFileType(new AudioFileType(ext));
            }
        }

        // Register status panel
        StatusManagement statusManagement = management.getStatusManagement();
        statusManagement.addStatusPanel(editorFrame.getStatusPanel());

        // Register options panel
        OptionsManagement optionsManagement = management.getOptionsManagement();
        optionsManagement.addOptionsPanel(new AudioDevicesPanel());
        optionsManagement.addOptionsPanel(new WaveColorOptionsPanel(editorFrame));
    }
}
