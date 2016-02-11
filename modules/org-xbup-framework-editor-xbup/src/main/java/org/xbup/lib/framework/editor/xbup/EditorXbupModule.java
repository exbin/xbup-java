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
package org.xbup.lib.framework.editor.xbup;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.framework.editor.xbup.panel.XBDocStatusPanel;
import org.xbup.lib.framework.editor.xbup.panel.XBDocumentPanel;
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
import org.xbup.lib.framework.service_manager.XBFileType;

/**
 * XBUP editor module.
 *
 * @version 0.2.0 2016/02/11
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorXbupModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(EditorXbupModule.class);
    public static final String XB_FILE_TYPE = "XBEditor.XBFileType";

    private static final String EDIT_ITEM_MENU_GROUP_ID = MODULE_ID + ".editItemMenuGroup";
    private static final String EDIT_ITEM_TOOL_BAR_GROUP_ID = MODULE_ID + ".editItemToolBarGroup";
    private static final String VIEW_MODE_MENU_GROUP_ID = MODULE_ID + ".viewModeMenuGroup";
    public static final String DOC_STATUS_BAR_ID = "docStatusBar";
    public static final String SAMPLE_FILE_SUBMENU_ID = MODULE_ID + ".sampleFileSubMenu";

    private XBApplication application;
    private XBEditorProvider editorProvider;
    private XBACatalog catalog;
    private XBDocStatusPanel docStatusPanel;

    private DocEditingHandler docEditingHandler;
    private ViewModeHandler viewModeHandler;
    private SampleFilesHandler sampleFilesHandler;
    private CatalogBrowserHandler catalogBrowserHandler;
    private PropertiesHandler propertiesHandler;
    private PropertyPanelHandler propertyPanelHandler;

    private boolean devMode;

    public EditorXbupModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    public XBEditorProvider getEditorProvider() {
        if (editorProvider == null) {
            editorProvider = new XBDocumentPanel(catalog);
        }

        return editorProvider;
    }

    public void registerFileTypes() {
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);
        fileModule.addFileType(new XBFileType());
    }

    private DocEditingHandler getDocEditingHandler() {
        if (docEditingHandler == null) {
            docEditingHandler = new DocEditingHandler(application, editorProvider);
            docEditingHandler.init();
        }

        return docEditingHandler;
    }

    private ViewModeHandler getViewModeHandler() {
        if (viewModeHandler == null) {
            viewModeHandler = new ViewModeHandler(application, editorProvider);
            viewModeHandler.init();
        }

        return viewModeHandler;
    }

    private SampleFilesHandler getSampleFilesHandler() {
        if (sampleFilesHandler == null) {
            sampleFilesHandler = new SampleFilesHandler(application, editorProvider);
            sampleFilesHandler.init();
        }

        return sampleFilesHandler;
    }
    
    private CatalogBrowserHandler getCatalogBrowserHandler() {
        if (catalogBrowserHandler == null) {
            catalogBrowserHandler = new CatalogBrowserHandler(application, editorProvider);
            catalogBrowserHandler.init();
        }

        return catalogBrowserHandler;
    }
    
    private PropertiesHandler getPropertiesHandler() {
        if (propertiesHandler == null) {
            propertiesHandler = new PropertiesHandler(application, editorProvider);
            propertiesHandler.init();
        }

        return propertiesHandler;
    }
    
    private PropertyPanelHandler getPropertyPanelHandler() {
        if (propertyPanelHandler == null) {
            propertyPanelHandler = new PropertyPanelHandler(application, editorProvider);
            propertyPanelHandler.init();
        }

        return propertyPanelHandler;
    }
    
    public void registerDocEditingMenuActions() {
        getDocEditingHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.EDIT_MENU_ID, new MenuGroup(EDIT_ITEM_MENU_GROUP_ID, new MenuPosition(PositionMode.BOTTOM), SeparationMode.AROUND));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, docEditingHandler.getAddItemAction(), new MenuPosition(EDIT_ITEM_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, MODULE_ID, docEditingHandler.getModifyItemAction(), new MenuPosition(EDIT_ITEM_MENU_GROUP_ID));
    }

    public void registerDocEditingToolBarActions() {
        getDocEditingHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerToolBarGroup(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, new ToolBarGroup(EDIT_ITEM_TOOL_BAR_GROUP_ID, new ToolBarPosition(PositionMode.BOTTOM), SeparationMode.AROUND));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, docEditingHandler.getAddItemAction(), new ToolBarPosition(EDIT_ITEM_TOOL_BAR_GROUP_ID));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, docEditingHandler.getModifyItemAction(), new ToolBarPosition(EDIT_ITEM_TOOL_BAR_GROUP_ID));
    }

    public void registerViewModeMenu() {
        getViewModeHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.VIEW_MENU_ID, new MenuGroup(VIEW_MODE_MENU_GROUP_ID, new MenuPosition(PositionMode.MIDDLE), SeparationMode.AROUND));
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, viewModeHandler.getTreeModeAction(), new MenuPosition(VIEW_MODE_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, viewModeHandler.getTextModeAction(), new MenuPosition(VIEW_MODE_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, viewModeHandler.getHexModeAction(), new MenuPosition(VIEW_MODE_MENU_GROUP_ID));
    }

    public void registerStatusBar() {
        docStatusPanel = new XBDocStatusPanel();
        GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, DOC_STATUS_BAR_ID, docStatusPanel);
        frameModule.switchStatusBar(DOC_STATUS_BAR_ID);
        // ((XBDocumentPanel) getEditorProvider()).registerTextStatus(docStatusPanel);
    }

    public void registerSampleFilesSubMenuActions() {
        getSampleFilesHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenu(SAMPLE_FILE_SUBMENU_ID, MODULE_ID);
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, SAMPLE_FILE_SUBMENU_ID, "Open Sample File", new MenuPosition(PositionMode.BOTTOM));
        menuModule.registerMenuItem(SAMPLE_FILE_SUBMENU_ID, MODULE_ID, sampleFilesHandler.getSampleHtmlFileAction(), new MenuPosition(PositionMode.TOP));
        menuModule.registerMenuItem(SAMPLE_FILE_SUBMENU_ID, MODULE_ID, sampleFilesHandler.getSamplePictureFileAction(), new MenuPosition(PositionMode.TOP));
    }

    public void registerCatalogBrowserMenu() {
        getCatalogBrowserHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.TOOLS_MENU_ID, MODULE_ID, catalogBrowserHandler.getCatalogBrowserAction(), new MenuPosition(PositionMode.TOP));
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void registerOptionsPanels() {
        GuiOptionsModuleApi optionsModule = application.getModuleRepository().getModuleByInterface(GuiOptionsModuleApi.class);
        // TODO
    }

    public void registerPropertiesMenuAction() {
        getPropertiesHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.FILE_MENU_ID, MODULE_ID, propertiesHandler.getPropertiesAction(), new MenuPosition(PositionMode.BOTTOM));
    }
    
    public void registerPropertyPanelMenuAction() {
        getPropertyPanelHandler();
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.VIEW_MENU_ID, MODULE_ID, propertyPanelHandler.getViewPropertyPanelAction(), new MenuPosition(PositionMode.MIDDLE));
    }
    
    /**
     * FileFilter for *.xb* files.
     */
    public class XBFileFilter extends FileFilter implements FileType {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            String extension = getExtension(file);
            if (extension != null) {
                if (extension.length() >= 2) {
                    return extension.substring(0, 2).equals("xb");
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "All XB Files (*.xb*)";
        }

        @Override
        public String getFileTypeId() {
            return XB_FILE_TYPE;
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
