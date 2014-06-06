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
package org.xbup.tool.xbeditor.module.xbpictureeditor;

import javax.imageio.ImageIO;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.tool.xbeditorbase.base.api.ApplicationModule;
import org.xbup.tool.xbeditorbase.base.api.ApplicationModuleInfo;
import org.xbup.tool.xbeditorbase.base.api.BasicMenuType;
import org.xbup.tool.xbeditorbase.base.api.FileTypeManagement;
import org.xbup.tool.xbeditorbase.base.api.MenuManagement;
import org.xbup.tool.xbeditorbase.base.api.MenuPositionMode;
import org.xbup.tool.xbeditorbase.base.api.ModuleManagement;
import org.xbup.tool.xbeditorbase.base.api.StatusManagement;

/**
 * Picture Editor Module.
 *
 * @version 0.1 wr22.0 2013/03/10
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class XBPictureEditorModule implements ApplicationModule {

    private XBPictureEditorFrame editorFrame;

    /** Constructor */
    public XBPictureEditorModule() {
    }

    @Override
    public ApplicationModuleInfo getInfo() {
        return new ApplicationModuleInfo() {

            @Override
            public String getPluginId() {
                return "xbpictureeditor";
            }

            @Override
            public String getPluginName() {
                return "XB Picture Editor";
            }

            @Override
            public String getPluginDescription() {
                return "Editor for simple bitmap pictures";
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

        editorFrame = new XBPictureEditorFrame();

        management.registerPanel(editorFrame.activePanel);

        // Register menus
        MenuManagement menuManagement = management.getMenuManagement();
        menuManagement.extendMenu(editorFrame.fileMenu, BasicMenuType.FILE, MenuPositionMode.PANEL);
//        menuManagement.extendMenu(editorFrame.editMenu, BasicMenuType.EDIT, MenuPositionMode.PANEL);
        menuManagement.extendMenu(editorFrame.viewMenu, BasicMenuType.VIEW, MenuPositionMode.PANEL);
        menuManagement.extendMenu(editorFrame.optionsMenu, BasicMenuType.OPTIONS, MenuPositionMode.PANEL);
        menuManagement.extendMenu(editorFrame.toolsMenu, BasicMenuType.TOOLS, MenuPositionMode.PANEL);
        menuManagement.insertMenu(editorFrame.pictMenu, MenuPositionMode.PANEL);
        menuManagement.insertMainPopupMenu(editorFrame.getPopupMenu(), 0);

        // Register file types
        FileTypeManagement fileTypeManagement = management.getFileTypeManagement();
        fileTypeManagement.addFileType(editorFrame.newXBPFilesFilter());
        String[] formats = ImageIO.getReaderFormatNames();
        for (int i = 0; i < formats.length; i++) {
            String ext = formats[i];
            if (ext.toLowerCase().equals(ext)) {
               fileTypeManagement.addFileType(new PictureFileType(ext));
            }
        }

        // Register status panel
        StatusManagement statusManagement = management.getStatusManagement();
        statusManagement.addStatusPanel(editorFrame.getStatusPanel());
    }
}
