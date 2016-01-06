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
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.MenuPositionMode;
import org.xbup.lib.framework.gui.undo.api.GuiUndoModuleApi;
import org.xbup.lib.framework.gui.undo.dialog.UndoManagerModel;
import org.xbup.lib.operation.undo.XBTLinearUndo;
import org.xbup.lib.operation.undo.XBUndoHandler;

/**
 * Implementation of XBUP framework undo/redo module.
 *
 * @version 0.2.0 2015/01/06
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiUndoModule implements GuiUndoModuleApi {

    private XBApplication application;
    private final UndoManagerModel undoModel = new UndoManagerModel();
    private XBTLinearUndo undoHandler;

    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/undo/resources/GuiUndoModule");
    private int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private Action undoAction;
    private Action redoAction;

    public GuiUndoModule() {
        undoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditUndo();
            }
        };
        undoAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource(bundle.getString("actionEditUndo.Action.icon"))));
        // TODO keystroke from string with meta mask translation
        undoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, metaMask));
        undoAction.putValue(Action.NAME, bundle.getString("actionEditUndo.Action.text"));
        undoAction.putValue(Action.SHORT_DESCRIPTION, bundle.getString("actionEditUndo.Action.shortDescription"));
        undoAction.setEnabled(false);

        redoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionEditRedo();
            }
        };
        redoAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource(bundle.getString("actionEditRedo.Action.icon"))));
        redoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | metaMask));
        redoAction.putValue(Action.NAME, bundle.getString("actionEditRedo.Action.text"));
        redoAction.putValue(Action.SHORT_DESCRIPTION, bundle.getString("actionEditRedo.Action.shortDescription"));
        redoAction.setEnabled(false);
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public void setUndoHandle(XBTLinearUndo undoHandler) {
        this.undoHandler = undoHandler;
        undoModel.setUndoHandler(undoHandler);
        // TODO undoHandler.register undo change listener
    }

    /**
     * Registers undo/redo operations to main frame menu.
     */
    @Override
    public void registerMainMenu() {
        GuiMenuModuleApi module = application.getModuleRepository().getModuleByInterface(GuiMenuModuleApi.class);
        module.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, undoAction, new MenuPosition(MenuPositionMode.TOP));
        module.registerMenuItem(GuiFrameModuleApi.EDIT_MENU_ID, GuiUndoModuleApi.MODULE_ID, redoAction, new MenuPosition(MenuPositionMode.TOP));
    }

    @Override
    public XBUndoHandler getUndoHandler() {
        return undoHandler;
    }
}
