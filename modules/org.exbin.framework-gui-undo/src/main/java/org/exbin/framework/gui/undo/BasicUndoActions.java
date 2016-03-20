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

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.gui.utils.ActionUtils;
import org.exbin.framework.gui.undo.api.UndoActions;
import org.exbin.framework.gui.undo.api.UndoHandler;

/**
 * Basic clipboard action set.
 *
 * @version 0.2.0 2016/03/20
 * @author ExBin Project (http://exbin.org)
 */
public class BasicUndoActions implements UndoActions {

    private final ResourceBundle resourceBundle = ActionUtils.getResourceBundleByClass(GuiUndoModule.class);
    private final int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private UndoHandler undoHandler = null;
    private Action undoAction = null;
    private Action redoAction = null;
    private Action undoManagerAction = null;

    public BasicUndoActions() {
    }

    @Override
    public void updateUndoActions() {
        boolean canUndo = undoHandler != null && undoHandler.canUndo();
        boolean canRedo = undoHandler != null && undoHandler.canRedo();
        if (undoAction != null) {
            undoAction.setEnabled(canUndo);
        }
        if (redoAction != null) {
            redoAction.setEnabled(canRedo);
        }
    }

    @Override
    public void setUndoHandler(UndoHandler undoHandler) {
        this.undoHandler = undoHandler;
    }

    @Override
    public Action getUndoAction() {
        if (undoAction == null) {
            undoAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    undoHandler.performUndo();
                }
            };
            ActionUtils.setupAction(undoAction, resourceBundle, "editUndoAction");
            undoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, metaMask));
            undoAction.setEnabled(false);
        }
        return undoAction;
    }

    @Override
    public Action getRedoAction() {
        if (redoAction == null) {
            redoAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    undoHandler.performRedo();
                }
            };
            ActionUtils.setupAction(redoAction, resourceBundle, "editRedoAction");
            redoAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | metaMask));
            redoAction.setEnabled(false);
        }
        return redoAction;
    }

    @Override
    public Action getUndoManagerAction() {
        if (undoManagerAction == null) {
            undoManagerAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    undoHandler.performUndoManager();
                }
            };
            undoManagerAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
            ActionUtils.setupAction(undoManagerAction, resourceBundle, "editUndoManagerAction");
        }
        return undoManagerAction;
    }
}
