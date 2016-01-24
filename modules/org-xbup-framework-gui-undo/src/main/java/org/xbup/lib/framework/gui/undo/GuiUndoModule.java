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
package org.xbup.lib.framework.gui.undo;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.menu.api.ToolBarGroup;
import org.xbup.lib.framework.gui.menu.api.ToolBarPosition;
import org.xbup.lib.framework.gui.undo.api.GuiUndoModuleApi;
import org.xbup.lib.framework.gui.undo.dialog.UndoManagerDialog;
import org.xbup.lib.framework.gui.undo.dialog.UndoManagerModel;
import org.xbup.lib.framework.gui.utils.ActionUtils;
import org.xbup.lib.operation.undo.UndoUpdateListener;
import org.xbup.lib.operation.undo.XBUndoHandler;

/**
 * Implementation of XBUP framework undo/redo module.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiUndoModule implements GuiUndoModuleApi {

    private static final String UNDO_MENU_GROUP_ID = MODULE_ID + ".undoMenuGroup";
    private static final String UNDO_TOOL_BAR_GROUP_ID = MODULE_ID + ".undoToolBarGroup";

    private XBApplication application;
    private final UndoManagerModel undoModel = new UndoManagerModel();
    private XBUndoHandler undoHandler;

    private final java.util.ResourceBundle bundle = ActionUtils.getResourceBundleByClass(GuiUndoModule.class);
    private int metaMask;
    private Action undoAction;
    private Action redoAction;
    private Action undoManagerAction;

    public GuiUndoModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        undoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditUndo();
            }
        };
        ActionUtils.setupAction(undoAction, bundle, "editUndoAction");
        undoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, metaMask));
        undoAction.setEnabled(false);

        redoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditRedo();
            }
        };
        ActionUtils.setupAction(redoAction, bundle, "editRedoAction");
        redoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | metaMask));
        redoAction.setEnabled(false);

        undoManagerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiFrameModuleApi frameModule = GuiUndoModule.this.application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
                UndoManagerDialog dialog = new UndoManagerDialog(frameModule.getFrame(), true, undoModel);
                dialog.setVisible(true);
            }
        };
        undoManagerAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
        ActionUtils.setupAction(undoManagerAction, bundle, "editUndoManagerAction");

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
    public void unregisterPlugin(String pluginId) {
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
    public void setUndoHandler(XBUndoHandler undoHandler) {
        this.undoHandler = undoHandler;
        undoModel.setUndoHandler(undoHandler);
        undoHandler.addUndoUpdateListener(new UndoUpdateListener() {
            @Override
            public void undoChanged() {
                updateUndoStatus();
            }
        });
    }

    @Override
    public void updateUndoStatus() {
        boolean canUndo = undoHandler != null && undoHandler.canUndo();
        boolean canRedo = undoHandler != null && undoHandler.canRedo();
        undoAction.setEnabled(canUndo);
        redoAction.setEnabled(canRedo);
    }

    @Override
    public void registerMainMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuGroup(GuiFrameModuleApi.EDIT_MENU_ID, new MenuGroup(UNDO_MENU_GROUP_ID, new MenuPosition(PositionMode.TOP), SeparationMode.BELOW));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, undoAction, new MenuPosition(UNDO_MENU_GROUP_ID));
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, redoAction, new MenuPosition(UNDO_MENU_GROUP_ID));
    }

    @Override
    public void registerUndoManagerInMainMenu() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, undoManagerAction, new MenuPosition(UNDO_MENU_GROUP_ID));
    }

    @Override
    public void registerMainToolBar() {
        GuiMenuModuleApi menuModule = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        menuModule.registerToolBarGroup(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, new ToolBarGroup(UNDO_TOOL_BAR_GROUP_ID, new ToolBarPosition(PositionMode.TOP), SeparationMode.AROUND));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, undoAction, new ToolBarPosition(UNDO_TOOL_BAR_GROUP_ID));
        menuModule.registerToolBarItem(GuiFrameModuleApi.MAIN_TOOL_BAR_ID, MODULE_ID, redoAction, new ToolBarPosition(UNDO_TOOL_BAR_GROUP_ID));
    }

    @Override
    public XBUndoHandler getUndoHandler() {
        return undoHandler;
    }
}
