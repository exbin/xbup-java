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
package org.xbup.lib.framework.gui.menu;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import org.xbup.lib.framework.gui.menu.api.ClipboardActionsApi;

/**
 * Clipboard operations.
 *
 * @version 0.2.0 2016/01/09
 * @author XBUP Project (http://xbup.org)
 */
public class ClipboardActions implements ClipboardActionsApi {

    private ResourceBundle resourceBundle;
    private int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private ActionMap actionMap;
    private Component actionFocusOwner = null;

    private JComponent lastFocusOwner = null;
    private Clipboard clipboard = null;
    private boolean isValidClipboardFlavor = false;
    private CaretListener textComponentCaretListener;
    private PropertyChangeListener textComponentPCL;

    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action deleteAction;
    private Action selectAllAction;

    public ClipboardActions() {
        resourceBundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/menu/resources/GuiMenuModule");
    }

    public void init() {
        // Note: There is probably better way for clipboard action's handling
        actionMap = new ActionMap();
        cutAction = new PassingTextAction(new DefaultEditorKit.CutAction());
        cutAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/menu/resources/icons/tango-icon-theme/16x16/actions/edit-cut.png")));
        cutAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, metaMask));
        cutAction.putValue(Action.NAME, resourceBundle.getString("actionEditCut.Action.text"));
        cutAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditCut.Action.shortDescription"));
        cutAction.setEnabled(false);
        actionMap.put(TransferHandler.getCutAction().getValue(Action.NAME), cutAction);

        copyAction = new PassingTextAction(new DefaultEditorKit.CopyAction());
        copyAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/menu/resources/icons/tango-icon-theme/16x16/actions/edit-copy.png")));
        copyAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, metaMask));
        copyAction.putValue(Action.NAME, resourceBundle.getString("actionEditCopy.Action.text"));
        copyAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditCopy.Action.shortDescription"));
        copyAction.setEnabled(false);
        actionMap.put(TransferHandler.getCopyAction().getValue(Action.NAME), copyAction);

        pasteAction = new PassingTextAction(new DefaultEditorKit.PasteAction());
        pasteAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/menu/resources/icons/tango-icon-theme/16x16/actions/edit-paste.png")));
        pasteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, metaMask));
        pasteAction.putValue(Action.NAME, resourceBundle.getString("actionEditPaste.Action.text"));
        pasteAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditPaste.Action.shortDescription"));
        pasteAction.setEnabled(false);
        actionMap.put(TransferHandler.getPasteAction().getValue(Action.NAME), pasteAction);

        deleteAction = new PassingTextAction(new TextAction("delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = actionFocusOwner;

                if (src instanceof JTextComponent) {
                    invokeTextAction((JTextComponent) src, DefaultEditorKit.deleteNextCharAction);
                }
            }
        });
        deleteAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/menu/resources/icons/tango-icon-theme/16x16/actions/edit-delete.png")));
        deleteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteAction.putValue(Action.NAME, resourceBundle.getString("actionEditDelete.Action.text"));
        deleteAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditDelete.Action.shortDescription"));
        deleteAction.setEnabled(false);
        actionMap.put("delete", deleteAction);

        selectAllAction = new PassingTextAction(new TextAction("selectAll") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = actionFocusOwner;

                if (src instanceof JTextComponent) {
                    invokeTextAction((JTextComponent) src, DefaultEditorKit.selectAllAction);
                }
            }
        });
        selectAllAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/menu/resources/icons/tango-icon-theme/16x16/actions/edit-select-all.png")));
        selectAllAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, metaMask));
        selectAllAction.putValue(Action.NAME, resourceBundle.getString("actionEditSelectAll.Action.text"));
        selectAllAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionEditSelectAll.Action.shortDescription"));
    }

    public void performCut(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JTextComponent) {
            invokeTextAction((JTextComponent) src, DefaultEditorKit.cutAction);
        }
    }

    public void performCopy(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JTextComponent) {
            invokeTextAction((JTextComponent) src, DefaultEditorKit.copyAction);
        }
    }

    public void performPaste(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JTextComponent) {
            invokeTextAction((JTextComponent) src, DefaultEditorKit.pasteAction);
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

    private void invokeTextAction(JTextComponent text, String actionName) {
        ActionMap textActionMap = text.getActionMap().getParent();
        long eventTime = EventQueue.getMostRecentEventTime();
        int eventMods = getCurrentEventModifiers();
        ActionEvent actionEvent = new ActionEvent(text, ActionEvent.ACTION_PERFORMED, actionName, eventTime, eventMods);
        textActionMap.get(actionName).actionPerformed(actionEvent);
    }

    /**
     * This method was lifted from JTextComponent.java.
     */
    private int getCurrentEventModifiers() {
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        return modifiers;
    }

    public class PassingTextAction extends TextAction {

        private final TextAction parentAction;

        public PassingTextAction(TextAction parentAction) {
            super((String) parentAction.getValue(NAME));
            this.parentAction = parentAction;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
//            if (activePanel instanceof ActivePanelActionHandling) {
//                ActivePanelActionHandling childHandling = (ActivePanelActionHandling) activePanel;
//                if (childHandling.performAction((String) parentAction.getValue(NAME), actionEvent)) {
//                    return;
//                }
//            }

            parentAction.actionPerformed(actionEvent);
        }
    }

    /**
     * Called by the KeyboardFocus PropertyChangeListener, before any other
     * focus-change related work is done.
     */
    private void updateFocusOwner(JComponent oldOwner, JComponent newOwner) {
        if (oldOwner instanceof JTextComponent) {
            JTextComponent text = (JTextComponent) oldOwner;
            text.removeCaretListener(textComponentCaretListener);
            text.removePropertyChangeListener(textComponentPCL);
        }
        if (newOwner instanceof JTextComponent) {
            JTextComponent text = (JTextComponent) newOwner;
            // maybeInstallTextActions(text);

            text.addCaretListener(textComponentCaretListener);
            text.addPropertyChangeListener(textComponentPCL);
        } else if (newOwner == null) {
            copyAction.setEnabled(false);
            cutAction.setEnabled(false);
            pasteAction.setEnabled(false);
            deleteAction.setEnabled(false);
        }

        lastFocusOwner = newOwner;

//        if (activePanel instanceof ActivePanelActionHandling) {
//            if (((ActivePanelActionHandling) activePanel).updateActionStatus(newOwner)) {
//                return;
//            }
//        }
//        if (newOwner instanceof JTextComponent) {
//            isValidClipboardFlavor = getClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor);
//            updateTextActions((JTextComponent) newOwner);
//        }
    }

    private final class KeyboardFocusPCL implements PropertyChangeListener {

        KeyboardFocusPCL() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            Component oldOwner = getFocusOwner();
            Object newValue = e.getNewValue();
            JComponent newOwner = (newValue instanceof JComponent) ? (JComponent) newValue : null;
            if (oldOwner instanceof JComponent) {
                updateFocusOwner((JComponent) oldOwner, newOwner);
            }

            if (newOwner != null) {
                actionFocusOwner = newOwner;
                /* ActionMap textActionMap = newOwner.getActionMap();
                 if (textActionMap != null) {
                 if (actionMap.get(markerActionKey) == null) {
                 actionFocusOwner = newOwner;
                 }
                 } */

 /*if (newOwner instanceof JTextComponent) {
                 if (((JTextComponent) newOwner).getComponentPopupMenu() == null) {
                 ((JTextComponent) newOwner).setComponentPopupMenu(defaultPopupMenu);
                 }
                 } */
            }
        }

    }

    private Component getFocusOwner() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    }

    /**
     * A shared {@code Clipboard}.
     *
     * @return clipboard
     */
    public Clipboard getClipboard() {
        if (clipboard == null) {
            try {
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            } catch (SecurityException e) {
                clipboard = new Clipboard("sandbox");
            }
        }

        return clipboard;
    }

    private final class ClipboardListener implements FlavorListener {

        @Override
        public void flavorsChanged(FlavorEvent e) {
            JComponent c = (JComponent) getFocusOwner();
            if (c instanceof JTextComponent) {
                isValidClipboardFlavor = getClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor);
                updateTextActions((JTextComponent) c);
            }
        }
    }

    private final class TextComponentCaretListener implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e) {
            updateTextActions((JTextComponent) (e.getSource()));
        }
    }

    private final class TextComponentPCL implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if ((propertyName == null) || "editable".equals(propertyName)) {
                updateTextActions((JTextComponent) (e.getSource()));
            }
        }
    }

    private void updateTextActions(JTextComponent text) {
        Caret caret = text.getCaret();
        boolean selection = (caret.getDot() != caret.getMark());
        // text.getSelectionEnd() > text.getSelectionStart();
        boolean editable = text.isEditable();
        boolean data = isValidClipboardFlavor;
        copyAction.setEnabled(selection);
        cutAction.setEnabled(editable && selection);
        deleteAction.setEnabled(editable && selection);
        pasteAction.setEnabled(editable && data);
    }
}
