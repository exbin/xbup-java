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

import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.core.catalog.XBACatalog;
import static org.xbup.lib.framework.editor.text.EditorTextModule.TEXT_STATUS_BAR_ID;
import org.xbup.lib.framework.editor.text.panel.TextPanel;
import org.xbup.lib.framework.editor.xbup.panel.XBDocStatusPanel;
import org.xbup.lib.framework.editor.xbup.panel.XBDocumentPanel;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.api.XBApplicationModulePlugin;
import org.xbup.lib.framework.gui.api.XBModuleRepositoryUtils;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.GuiFileModuleApi;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.menu.api.ToolBarGroup;
import org.xbup.lib.framework.gui.menu.api.ToolBarPosition;

/**
 * XBUP editor module.
 *
 * @version 0.2.0 2016/02/08
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class EditorXbupModule implements XBApplicationModulePlugin {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(EditorXbupModule.class);
    
    private static final String EDIT_ITEM_MENU_GROUP_ID = MODULE_ID + ".editItemMenuGroup";
    private static final String EDIT_ITEM_TOOL_BAR_GROUP_ID = MODULE_ID + ".editItemToolBarGroup";
    public static final String DOC_STATUS_BAR_ID = "docStatusBar";


    private XBApplication application;
    private XBEditorProvider editorProvider;
    private XBACatalog catalog;
    private XBDocStatusPanel docStatusPanel;

    private DocEditingHandler docEditingHandler;

    public EditorXbupModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;

        // Register file types
        GuiFileModuleApi fileModule = application.getModuleRepository().getModuleByInterface(GuiFileModuleApi.class);
//        fileModule.addFileType(new XBTFileType());
//        fileModule.addFileType(new TXTFileType());
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBEditorProvider getEditorProvider() {
        if (editorProvider == null) {
            editorProvider = new XBDocumentPanel(catalog);
        }

        return editorProvider;
    }

    private DocEditingHandler getDocEditingHandler() {
        if (docEditingHandler == null) {
            docEditingHandler = new DocEditingHandler(application, editorProvider);
            docEditingHandler.init();
        }

        return docEditingHandler;
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
    
    public void registerStatusBar() {
        docStatusPanel = new XBDocStatusPanel();
        GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, DOC_STATUS_BAR_ID, docStatusPanel);
        frameModule.switchStatusBar(DOC_STATUS_BAR_ID);
        // ((XBDocumentPanel) getEditorProvider()).registerTextStatus(docStatusPanel);
    }
}
