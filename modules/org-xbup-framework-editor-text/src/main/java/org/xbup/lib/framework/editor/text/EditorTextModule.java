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

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.filechooser.FileFilter;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.menu.api.ToolBarGroup;
import org.xbup.lib.framework.gui.menu.api.ToolBarPosition;
import org.xbup.lib.framework.gui.options.api.GuiOptionsModuleApi;
import org.xbup.lib.framework.editor.text.panel.TextColorOptionsPanel;
import org.xbup.lib.framework.editor.text.panel.TextEncodingOptionsPanel;
import org.xbup.lib.framework.editor.text.panel.TextFontOptionsPanel;
import org.xbup.lib.framework.editor.text.panel.TextPanel;
import org.xbup.lib.framework.editor.text.panel.TextStatusPanel;
import org.xbup.lib.framework.editor.text.panel.TextColorPanelApi;
import org.xbup.lib.framework.editor.text.panel.TextEncodingPanelApi;
import org.xbup.lib.framework.editor.text.panel.TextFontPanelApi;

/**
 * XBUP text editor module.
 *
 * @version 0.2.0 2016/01/23
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorTextModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(EditorTextModule.class);

    private static final String EDIT_FIND_MENU_GROUP_ID = MODULE_ID + ".editFindMenuGroup";
    private static final String EDIT_FIND_TOOL_BAR_GROUP_ID = MODULE_ID + ".editFindToolBarGroup";

    public static final String XBT_FILE_TYPE = "XBTextEditor.XBTFileType";
    public static final String TXT_FILE_TYPE = "XBTextEditor.TXTFileType";

    public static final String TEXT_STATUS_BAR_ID = "textStatusBar";

    private XBApplication application;
    private XBEditorProvider editorProvider;
    private TextStatusPanel textStatusPanel;

    private FindReplaceHandler findReplaceHandler;
    private ToolsOptionsHandler toolsOptionsHandler;
    private EncodingsHandler encodingsHandler;
    private WordWrappingHandler wordWrappingHandler;
    private GoToLineHandler goToLineHandler;

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

    public void registerStatusBar() {
        textStatusPanel = new TextStatusPanel();
        GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, TEXT_STATUS_BAR_ID, textStatusPanel);
        frameModule.switchStatusBar(TEXT_STATUS_BAR_ID);
        ((TextPanel) getEditorProvider()).registerTextStatus(textStatusPanel);
    }

    public void registerOptionsMenuPanels() {
        getEncodingsHandler();
        JMenu toolsEncodingMenu = encodingsHandler.getToolsEncodingMenu();
        encodingsHandler.encodingsRebuild();

        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, encodingsHandler.getToolsEncodingMenu(), new MenuPosition(PositionMode.TOP_LAST));
    }

    public void registerOptionsPanels() {
        GuiOptionsModuleApi optionsModule = application.getModuleRepository().getModuleByInterface(GuiOptionsModuleApi.class);
        TextColorPanelApi textColorPanelFrame = new TextColorPanelApi() {
            @Override
            public Color[] getCurrentTextColors() {
                return ((TextPanel) getEditorProvider()).getCurrentColors();
            }

            @Override
            public Color[] getDefaultTextColors() {
                return ((TextPanel) getEditorProvider()).getDefaultColors();
            }

            @Override
            public void setCurrentTextColors(Color[] colors) {
                ((TextPanel) getEditorProvider()).setCurrentColors(colors);
            }
        };

        optionsModule.addOptionsPanel(new TextColorOptionsPanel(textColorPanelFrame));

        TextFontPanelApi textFontPanelFrame = new TextFontPanelApi() {
            @Override
            public Font getCurrentFont() {
                return ((TextPanel) getEditorProvider()).getCurrentFont();
            }

            @Override
            public Font getDefaultFont() {
                return ((TextPanel) getEditorProvider()).getDefaultFont();
            }

            @Override
            public void setCurrentFont(Font font) {
                ((TextPanel) getEditorProvider()).setCurrentFont(font);
            }
        };

        optionsModule.addOptionsPanel(new TextFontOptionsPanel(textFontPanelFrame));

        TextEncodingPanelApi textEncodingPanelFrame = new TextEncodingPanelApi() {
            @Override
            public List<String> getEncodings() {
                return getEncodingsHandler().getEncodings();
            }

            @Override
            public String getSelectedEncoding() {
                return ((TextPanel) getEditorProvider()).getCharset().name();
            }

            @Override
            public void setEncodings(List<String> encodings) {
                getEncodingsHandler().setEncodings(encodings);
                getEncodingsHandler().encodingsRebuild();
            }

            @Override
            public void setSelectedEncoding(String encoding) {
                if (encoding != null) {
                    ((TextPanel) getEditorProvider()).setCharset(Charset.forName(encoding));
                }
            }
        };

        optionsModule.addOptionsPanel(new TextEncodingOptionsPanel(textEncodingPanelFrame));
//        optionsModule.extendAppearanceOptionsPanel(new TextAppearanceOptionsPanel(textColorFrame));

    }

    public void registerWordWrapping() {
        getWordWrappingHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, wordWrappingHandler.getViewWordWrapAction(), new MenuPosition(PositionMode.BOTTOM));
    }

    public void registerGoToLine() {
        getGoToLineHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, goToLineHandler.getGoToLineAction(), new MenuPosition(PositionMode.BOTTOM));
    }

    public TextStatusPanel getTextStatusPanel() {
        return textStatusPanel;
    }

    private FindReplaceHandler getFindReplaceHandler() {
        if (findReplaceHandler == null) {
            findReplaceHandler = new FindReplaceHandler(application, (TextPanel) getEditorProvider());
            findReplaceHandler.init();
        }

        return findReplaceHandler;
    }

    private ToolsOptionsHandler getToolsOptionsHandler() {
        if (toolsOptionsHandler == null) {
            toolsOptionsHandler = new ToolsOptionsHandler(application, (TextPanel) getEditorProvider());
            toolsOptionsHandler.init();
        }

        return toolsOptionsHandler;
    }

    private EncodingsHandler getEncodingsHandler() {
        if (encodingsHandler == null) {
            encodingsHandler = new EncodingsHandler(application, (TextPanel) getEditorProvider(), this);
            encodingsHandler.init();
        }

        return encodingsHandler;
    }

    private WordWrappingHandler getWordWrappingHandler() {
        if (wordWrappingHandler == null) {
            wordWrappingHandler = new WordWrappingHandler(application, (TextPanel) getEditorProvider());
            wordWrappingHandler.init();
        }

        return wordWrappingHandler;
    }

    private GoToLineHandler getGoToLineHandler() {
        if (goToLineHandler == null) {
            goToLineHandler = new GoToLineHandler(application, (TextPanel) getEditorProvider());
            goToLineHandler.init();
        }

        return goToLineHandler;
    }

    public void registerEditFindMenuActions() {
        getFindReplaceHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.EDIT_MENU_ID, new MenuGroup(EDIT_FIND_MENU_GROUP_ID, new MenuPosition(PositionMode.MIDDLE), SeparationMode.AROUND));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, findReplaceHandler.getEditFindAction(), new MenuPosition(EDIT_FIND_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, findReplaceHandler.getEditFindAgainAction(), new MenuPosition(EDIT_FIND_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, findReplaceHandler.getEditReplaceAction(), new MenuPosition(EDIT_FIND_MENU_GROUP_ID));
    }

    public void registerEditFindToolBarActions() {
        getFindReplaceHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerToolBarGroup(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, new ToolBarGroup(EDIT_FIND_TOOL_BAR_GROUP_ID, new ToolBarPosition(PositionMode.MIDDLE), SeparationMode.AROUND));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, findReplaceHandler.getEditFindAction(), new ToolBarPosition(EDIT_FIND_TOOL_BAR_GROUP_ID));
    }

    public void registerToolsOptionsMenuActions() {
        getToolsOptionsHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, toolsOptionsHandler.getToolsSetFontAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, toolsOptionsHandler.getToolsSetColorAction(), new MenuPosition(PositionMode.TOP));
    }

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
