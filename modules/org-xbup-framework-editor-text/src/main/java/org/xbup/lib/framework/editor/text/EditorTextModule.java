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
package org.xbup.lib.framework.editor.text;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.tool.editor.module.text_editor.panel.TextPanel;

/**
 * XBUP editor module.
 *
 * @version 0.2.0 2016/01/09
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorTextModule implements XBApplicationModulePlugin {

    public static final String XBT_FILE_TYPE = "XBTextEditor.XBTFileType";
    public static final String TXT_FILE_TYPE = "XBTextEditor.TXTFileType";

    private XBApplication application;
    private XBEditorProvider editorProvider;

    public EditorTextModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        // Register file types
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);
        fileModule.addFileType(new XBTFileType());
        fileModule.addFileType(new TXTFileType());
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    public XBEditorProvider getEditorProvider() {
        if (editorProvider == null) {
            editorProvider = new TextPanel();
        }

        return editorProvider;
    }

//        editorFrame = new XBTextEditorFrame();
//        editorFrame.setMainFrameManagement(management.getMainFrameManagement());
//        management.registerPanel(editorFrame.getActivePanel());
//        editorFrame.setMenuManagement(management.getMenuManagement());
//
//        // Register status panel
//        StatusManagement statusManagement = management.getStatusManagement();
//        statusManagement.addStatusPanel(editorFrame.getStatusPanel());
//
//        // Register options panel
//        OptionsManagement optionsManagement = management.getOptionsManagement();
//        optionsManagement.addOptionsPanel(new TextColorOptionsPanel(editorFrame));
//        optionsManagement.addOptionsPanel(new TextFontOptionsPanel(editorFrame));
//        optionsManagement.addOptionsPanel(new TextEncodingOptionsPanel(editorFrame));
//        optionsManagement.extendAppearanceOptionsPanel(new TextAppearanceOptionsPanel(editorFrame));
    public class XBTFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                if (extension.length() < 3) {
                    return false;
                }
                return "xbt".contains(extension.substring(0, 3));
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "XBUP Text Files (*.xbt*)";
        }

        @Override
        public String getFileTypeId() {
            return XBT_FILE_TYPE;
        }
    }

    public FileType newXBTFileType() {
        return new XBTFileType();
    }

    public class TXTFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                return "txt".equals(extension);
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Text Files (*.txt)";
        }

        @Override
        public String getFileTypeId() {
            return TXT_FILE_TYPE;
        }
    }

    /**
     * Gets the extension part of file name.
     *
     * @param file Source file
     * @return extension part of file name
     */
    public static String getExtension(File file) {
        String ext = null;
        String str = file.getName();
        int i = str.lastIndexOf('.');

        if (i > 0 && i < str.length() - 1) {
            ext = str.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
