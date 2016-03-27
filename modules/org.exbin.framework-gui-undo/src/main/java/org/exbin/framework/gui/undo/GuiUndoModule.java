/*
 * Copyright (C) ExBin Project
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
package org.exbin.framework.gui.undo;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.menu.api.MenuGroup;
import org.exbin.framework.gui.menu.api.MenuPosition;
import org.exbin.framework.gui.menu.api.PositionMode;
import org.exbin.framework.gui.menu.api.SeparationMode;
import org.exbin.framework.gui.menu.api.ToolBarGroup;
import org.exbin.framework.gui.menu.api.ToolBarPosition;
import org.exbin.framework.gui.undo.api.GuiUndoModuleApi;
import org.exbin.framework.gui.undo.api.UndoActions;
import org.exbin.framework.gui.undo.api.UndoUpdateListener;
import org.exbin.framework.gui.undo.dialog.UndoManagerDialog;
import org.exbin.framework.gui.undo.dialog.UndoManagerModel;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.operation.undo.XBUndoUpdateListener;
import org.exbin.framework.gui.undo.api.UndoActionsHandler;
import org.exbin.xbup.plugin.XBModuleHandler;

/**
 * Implementation of XBUP framework undo/redo module.
 *
 * @version 0.2.0 2016/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class GuiUndoModule implements GuiUndoModuleApi {

    private static final String UNDO_MENU_GROUP_ID = MODULE_ID + ".undoMenuGroup";
    private static final String UNDO_TOOL_BAR_GROUP_ID = MODULE_ID + ".undoToolBarGroup";

    private XBApplication application;
    private final UndoManagerModel undoModel = new UndoManagerModel();
    private XBUndoHandler undoHandler;

    private BasicUndoActions defaultUndoActions = null;

    public GuiUndoModule() {
    }

    @Override
    public void init(XBModuleHandler moduleHandler) {
        this.application = (XBApplication) moduleHandler;

        defaultUndoActions = new BasicUndoActions();
        undoModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                updateUndoStatus();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
            }
        });
    }

    @Override
    public void unregisterModule(String moduleId) {
    }

    public void actionEditUndo() {
        try {
            undoHandler.performUndo();
        } catch (Exception ex) {
            Logger.getLogger(GuiUndoModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actionEditRedo() {
        try {
            undoHandler.performRedo();
        } catch (Exception ex) {
            Logger.getLogger(GuiUndoModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setUndoHandler(final XBUndoHandler undoHandler) {
        this.undoHandler = undoHandler;
        defaultUndoActions.setUndoActionsHandler(new UndoActionsHandler() {
            @Override
            public Boolean canUndo() {
                return undoHandler.canUndo();
            }

            @Override
            public Boolean canRedo() {
                return undoHandler.canRedo();
            }

            @Override
            public void performUndo() {
                actionEditUndo();
            }

            @Override
            public void performRedo() {
                actionEditRedo();
            }

            @Override
            public void performUndoManager() {
                openUndoManager();
            }

            @Override
            public void setUndoUpdateListener(UndoUpdateListener undoUpdateListener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        undoModel.setUndoHandler(undoHandler);
        undoHandler.addUndoUpdateListener(new XBUndoUpdateListener() {
            @Override
            public void undoChanged() {
                updateUndoStatus();
            }
        });
    }

    @Override
    public void updateUndoStatus() {
        defaultUndoActions.updateUndoActions();
    }

    @Override
    public void registerMainMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.EDIT_MENU_ID, new MenuGroup(UNDO_MENU_GROUP_ID, new MenuPosition(PositionMode.TOP), SeparationMode.BELOW));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, defaultUndoActions.getUndoAction(), new MenuPosition(UNDO_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, defaultUndoActions.getRedoAction(), new MenuPosition(UNDO_MENU_GROUP_ID));
    }

    @Override
    public void registerUndoManagerInMainMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, defaultUndoActions.getUndoManagerAction(), new MenuPosition(UNDO_MENU_GROUP_ID));
    }

    @Override
    public void registerMainToolBar() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerToolBarGroup(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, new ToolBarGroup(UNDO_TOOL_BAR_GROUP_ID, new ToolBarPosition(PositionMode.TOP), SeparationMode.AROUND));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, defaultUndoActions.getUndoAction(), new ToolBarPosition(UNDO_TOOL_BAR_GROUP_ID));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, defaultUndoActions.getRedoAction(), new ToolBarPosition(UNDO_TOOL_BAR_GROUP_ID));
    }

    @Override
    public XBUndoHandler getUndoHandler() {
        return undoHandler;
    }

    @Override
    public void openUndoManager() {
        GuiFrameModuleApi frameModule = GuiUndoModule.this.application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
        UndoManagerDialog dialog = new UndoManagerDialog(frameModule.getFrame(), true, undoModel);
        dialog.setVisible(true);
    }

    @Override
    public UndoActions createUndoActions(UndoActionsHandler undoActionsHandler) {
        BasicUndoActions undoActions = new BasicUndoActions();
        undoActions.setUndoActionsHandler(undoActionsHandler);
        return undoActions;
    }
}
