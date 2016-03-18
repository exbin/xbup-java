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
package org.exbin.framework.gui.menu;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.gui.menu.api.ClipboardActionSet;
import org.exbin.framework.gui.menu.api.ClipboardActionsUpdateListener;
import org.exbin.framework.gui.menu.api.ComponentClipboardHandler;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Basic clipboard action set.
 *
 * @version 0.2.0 2016/03/16
 * @author ExBin Project (http://exbin.org)
 */
public class BasicClipboardActionsSet implements ClipboardActionSet {

    private final ResourceBundle resourceBundle = ActionUtils.getResourceBundleByClass(GuiMenuModule.class);
    private final int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private ComponentClipboardHandler clipboardHandler = null;

    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action deleteAction;
    private Action selectAllAction;

    public BasicClipboardActionsSet() {
        this(null);
    }

    public BasicClipboardActionsSet(ComponentClipboardHandler handler) {
        this.clipboardHandler = handler;

        cutAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clipboardHandler != null) {
                    clipboardHandler.performCut();
                }
            }
        };
        ActionUtils.setupAction(cutAction, resourceBundle, "editCutAction");
        cutAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, metaMask));
        cutAction.setEnabled(false);

        copyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clipboardHandler != null) {
                    clipboardHandler.performCopy();
                }
            }
        };
        ActionUtils.setupAction(copyAction, resourceBundle, "editCopyAction");
        copyAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, metaMask));
        copyAction.setEnabled(false);

        pasteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clipboardHandler != null) {
                    clipboardHandler.performPaste();
                }
            }
        };
        ActionUtils.setupAction(pasteAction, resourceBundle, "editPasteAction");
        pasteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, metaMask));
        pasteAction.setEnabled(false);

        deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clipboardHandler != null) {
                    clipboardHandler.performDelete();
                }
            }
        };
        ActionUtils.setupAction(deleteAction, resourceBundle, "editDeleteAction");
        deleteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteAction.setEnabled(false);

        selectAllAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clipboardHandler != null) {
                    clipboardHandler.performSelectAll();
                }
            }
        };
        ActionUtils.setupAction(selectAllAction, resourceBundle, "editSelectAllAction");
        selectAllAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, metaMask));

        changeClipboardHandler();

        Clipboard clipboard;
        try {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        } catch (SecurityException e) {
            clipboard = new Clipboard("sandbox");
        }
        clipboard.addFlavorListener(new FlavorListener() {
            @Override
            public void flavorsChanged(FlavorEvent e) {
                updateClipboardActions();
            }
        });
    }

    @Override
    public void updateClipboardActions() {
        cutAction.setEnabled(clipboardHandler != null && clipboardHandler.isEditable() && clipboardHandler.isSelection());
        copyAction.setEnabled(clipboardHandler != null && clipboardHandler.isSelection());
        pasteAction.setEnabled(clipboardHandler != null && clipboardHandler.isEditable() && clipboardHandler.canPaste());
        deleteAction.setEnabled(clipboardHandler != null && clipboardHandler.isEditable() && clipboardHandler.isSelection());
        selectAllAction.setEnabled(clipboardHandler != null && clipboardHandler.canSelectAll());
    }

    @Override
    public void setClipboardHandler(ComponentClipboardHandler clipboardHandler) {
        this.clipboardHandler = clipboardHandler;
        changeClipboardHandler();
    }

    private void changeClipboardHandler() {
        if (clipboardHandler != null) {
            clipboardHandler.setUpdateListener(new ClipboardActionsUpdateListener() {
                @Override
                public void stateChanged() {
                    updateClipboardActions();
                }
            });
        }
    }

    @Override
    public Action getCutAction() {
        return cutAction;
    }

    @Override
    public Action getCopyAction() {
        return copyAction;
    }

    @Override
    public Action getPasteAction() {
        return pasteAction;
    }

    @Override
    public Action getDeleteAction() {
        return deleteAction;
    }

    @Override
    public Action getSelectAllAction() {
        return selectAllAction;
    }
}
