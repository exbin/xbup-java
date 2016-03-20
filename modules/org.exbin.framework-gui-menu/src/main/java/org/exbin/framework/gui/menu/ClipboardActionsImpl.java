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

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import org.exbin.framework.gui.menu.api.ClipboardActionsApi;
import org.exbin.framework.gui.menu.api.ClipboardActionsUpdateListener;
import org.exbin.framework.gui.menu.api.ComponentClipboardHandler;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Clipboard operations.
 *
 * @version 0.2.0 2016/03/16
 * @author ExBin Project (http://exbin.org)
 */
public class ClipboardActionsImpl implements ClipboardActionsApi {

    private ResourceBundle resourceBundle;
    private int metaMask;
    private ActionMap actionMap;
    private Component actionFocusOwner = null;

    private JComponent lastFocusOwner = null;
    private Clipboard clipboard = null;
    private boolean isValidClipboardFlavor = false;
    private CaretListener textComponentCaretListener;
    private PropertyChangeListener textComponentPCL;

    private ComponentClipboardHandler clipboardHandler;
    private BasicClipboardActions clipboardActionsSet;

    private Action cutTextAction;
    private Action copyTextAction;
    private Action pasteTextAction;
    private Action deleteTextAction;
    private Action selectAllTextAction;

    private ActionMap defaultTextActionMap;
    private JPopupMenu defaultPopupMenu;
    private DefaultPopupClipboardAction defaultCutAction;
    private DefaultPopupClipboardAction defaultCopyAction;
    private DefaultPopupClipboardAction defaultPasteAction;
    private DefaultPopupClipboardAction defaultDeleteAction;
    private DefaultPopupClipboardAction defaultSelectAllAction;
    private DefaultPopupClipboardAction[] defaultTextActions;

    public ClipboardActionsImpl() {
    }

    public void init() {
        resourceBundle = ActionUtils.getResourceBundleByClass(GuiMenuModule.class);
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        initializeClipboardActions();
        initializeTextActions();
        initDefaultPopupMenu();
    }

    private void initializeClipboardActions() {
        clipboardActionsSet = new BasicClipboardActions();
    }

    private void initializeTextActions() {
        actionMap = new ActionMap();
        cutTextAction = new PassingTextAction(new DefaultEditorKit.CutAction());
        ActionUtils.setupAction(cutTextAction, resourceBundle, "editCutAction");
        cutTextAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, metaMask));
        cutTextAction.setEnabled(false);
        actionMap.put(TransferHandler.getCutAction().getValue(Action.NAME), cutTextAction);

        copyTextAction = new PassingTextAction(new DefaultEditorKit.CopyAction());
        ActionUtils.setupAction(copyTextAction, resourceBundle, "editCopyAction");
        copyTextAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, metaMask));
        copyTextAction.setEnabled(false);
        actionMap.put(TransferHandler.getCopyAction().getValue(Action.NAME), copyTextAction);

        pasteTextAction = new PassingTextAction(new DefaultEditorKit.PasteAction());
        ActionUtils.setupAction(pasteTextAction, resourceBundle, "editPasteAction");
        pasteTextAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, metaMask));
        pasteTextAction.setEnabled(false);
        actionMap.put(TransferHandler.getPasteAction().getValue(Action.NAME), pasteTextAction);

        deleteTextAction = new PassingTextAction(new TextAction("delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = actionFocusOwner;

                if (src instanceof JTextComponent) {
                    invokeTextAction((JTextComponent) src, DefaultEditorKit.deleteNextCharAction);
                }
            }
        });
        ActionUtils.setupAction(deleteTextAction, resourceBundle, "editDeleteAction");
        deleteTextAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteTextAction.setEnabled(false);
        actionMap.put("delete", deleteTextAction);

        selectAllTextAction = new PassingTextAction(new TextAction("selectAll") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object src = actionFocusOwner;

                if (src instanceof JTextComponent) {
                    invokeTextAction((JTextComponent) src, DefaultEditorKit.selectAllAction);
                }
            }
        });
        ActionUtils.setupAction(selectAllTextAction, resourceBundle, "editSelectAllAction");
        selectAllTextAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, metaMask));
    }

    public JPopupMenu getDefaultPopupMenu() {
        if (defaultPopupMenu == null) {
            defaultPopupMenu = new JPopupMenu();

            defaultPopupMenu.setName("defaultPopupMenu"); // NOI18N
            fillPopupMenu(defaultPopupMenu, -1);
        }

        return defaultPopupMenu;
    }
    
    public void fillPopupMenu(JPopupMenu popupMenu, int position) {
        
        JMenuItem basicPopupCutMenuItem = new javax.swing.JMenuItem();
        JMenuItem basicPopupCopyMenuItem = new javax.swing.JMenuItem();
        JMenuItem basicPopupPasteMenuItem = new javax.swing.JMenuItem();
        JMenuItem basicPopupDeleteMenuItem = new javax.swing.JMenuItem();
        JMenuItem basicPopupSelectAllMenuItem = new javax.swing.JMenuItem();

        basicPopupCutMenuItem.setAction(defaultCutAction);
        basicPopupCutMenuItem.setName("basicPopupCutMenuItem"); // NOI18N
        basicPopupCopyMenuItem.setAction(defaultCopyAction);
        basicPopupCopyMenuItem.setName("basicPopupCopyMenuItem"); // NOI18N
        basicPopupPasteMenuItem.setAction(defaultPasteAction);
        basicPopupPasteMenuItem.setName("basicPopupPasteMenuItem"); // NOI18N
        basicPopupDeleteMenuItem.setAction(defaultDeleteAction);
        basicPopupDeleteMenuItem.setName("basicPopupDeleteMenuItem"); // NOI18N
        basicPopupSelectAllMenuItem.setAction(defaultSelectAllAction);
        basicPopupSelectAllMenuItem.setName("basicPopupSelectAllMenuItem"); // NOI18N

        if (position >= 0) {
            popupMenu.insert(basicPopupCutMenuItem, position);
            popupMenu.insert(basicPopupCopyMenuItem, position + 1);
            popupMenu.insert(basicPopupPasteMenuItem, position + 2);
            popupMenu.insert(basicPopupDeleteMenuItem, position + 3);
            popupMenu.insert(new JPopupMenu.Separator(), position + 4);
            popupMenu.insert(basicPopupSelectAllMenuItem, position + 5);
        } else {
            popupMenu.add(basicPopupCutMenuItem);
            popupMenu.add(basicPopupCopyMenuItem);
            popupMenu.add(basicPopupPasteMenuItem);
            popupMenu.add(basicPopupDeleteMenuItem);
            popupMenu.addSeparator();
            popupMenu.add(basicPopupSelectAllMenuItem);
        }
    }

    private void initDefaultPopupMenu() {
        defaultTextActionMap = new ActionMap();
        defaultCutAction = new DefaultPopupClipboardAction(DefaultEditorKit.cutAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.performCut();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isEditable() && clipboardHandler.isSelection());
            }
        };
        ActionUtils.setupAction(defaultCutAction, resourceBundle, "editCutAction");
        defaultCutAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, metaMask));
        defaultCutAction.setEnabled(false);
        defaultTextActionMap.put(TransferHandler.getCutAction().getValue(Action.NAME), defaultCutAction);

        defaultCopyAction = new DefaultPopupClipboardAction(DefaultEditorKit.copyAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.performCopy();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isSelection());
            }
        };
        ActionUtils.setupAction(defaultCopyAction, resourceBundle, "editCopyAction");
        defaultCopyAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, metaMask));
        defaultCopyAction.setEnabled(false);
        defaultTextActionMap.put(TransferHandler.getCopyAction().getValue(Action.NAME), defaultCopyAction);

        defaultPasteAction = new DefaultPopupClipboardAction(DefaultEditorKit.pasteAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.performPaste();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isEditable());
            }
        };
        ActionUtils.setupAction(defaultPasteAction, resourceBundle, "editPasteAction");
        defaultPasteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, metaMask));
        defaultPasteAction.setEnabled(false);
        defaultTextActionMap.put(TransferHandler.getPasteAction().getValue(Action.NAME), defaultPasteAction);

        defaultDeleteAction = new DefaultPopupClipboardAction(DefaultEditorKit.deleteNextCharAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.performDelete();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.isEditable() && clipboardHandler.isSelection());
            }
        };
        ActionUtils.setupAction(defaultDeleteAction, resourceBundle, "editDeleteAction");
        defaultDeleteAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        defaultDeleteAction.setEnabled(false);
        defaultTextActionMap.put("delete", defaultDeleteAction);

        defaultSelectAllAction = new DefaultPopupClipboardAction("Select all") {
            @Override
            public void actionPerformed(ActionEvent e) {
                clipboardHandler.performSelectAll();
            }

            @Override
            protected void postTextComponentInitialize() {
                setEnabled(clipboardHandler.canSelectAll());
            }
        };
        ActionUtils.setupAction(defaultSelectAllAction, resourceBundle, "editSelectAllAction");
        defaultSelectAllAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, metaMask));
        defaultTextActionMap.put("selectAll", defaultSelectAllAction);

        DefaultPopupClipboardAction[] actions = {defaultCutAction, defaultCopyAction, defaultPasteAction, defaultDeleteAction, defaultSelectAllAction};
        defaultTextActions = actions;

        getDefaultPopupMenu();
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new PopupEventQueue());
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
        return clipboardActionsSet.getCutAction();
    }

    @Override
    public Action getCopyAction() {
        return clipboardActionsSet.getCopyAction();
    }

    @Override
    public Action getPasteAction() {
        return clipboardActionsSet.getPasteAction();
    }

    @Override
    public Action getDeleteAction() {
        return clipboardActionsSet.getDeleteAction();
    }

    @Override
    public Action getSelectAllAction() {
        return clipboardActionsSet.getSelectAllAction();
    }

    public void setClipboardHandler(ComponentClipboardHandler clipboardHandler) {
        this.clipboardHandler = clipboardHandler;
        clipboardActionsSet.setClipboardHandler(clipboardHandler);
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
            super((String) parentAction.getValue(Action.NAME));
            this.parentAction = parentAction;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
//            if (activePanel instanceof ActivePanelActionHandling) {
//                ActivePanelActionHandling childHandling = (ActivePanelActionHandling) activePanel;
//                if (childHandling.performAction((String) parentAction.getValue(Action.NAME), actionEvent)) {
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
            copyTextAction.setEnabled(false);
            cutTextAction.setEnabled(false);
            pasteTextAction.setEnabled(false);
            deleteTextAction.setEnabled(false);
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

    private void updateTextActions(JTextComponent text) {
        Caret caret = text.getCaret();
        boolean selection = (caret.getDot() != caret.getMark());
        // text.getSelectionEnd() > text.getSelectionStart();
        boolean editable = text.isEditable();
        boolean data = isValidClipboardFlavor;
        copyTextAction.setEnabled(selection);
        cutTextAction.setEnabled(editable && selection);
        deleteTextAction.setEnabled(editable && selection);
        pasteTextAction.setEnabled(editable && data);
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

    public class PopupEventQueue extends EventQueue {

        @Override
        protected void dispatchEvent(AWTEvent event) {
            if (event.getID() == MouseEvent.MOUSE_RELEASED) {
                MouseEvent e = (MouseEvent) event;
                Component c = getSource(e);

                if (c instanceof JViewport) {
                    c = ((JViewport) c).getView();
                }

                if (c instanceof JTextComponent) {
                    if ((SwingUtilities.isRightMouseButton(e)) && (((JTextComponent) c).getComponentPopupMenu() == null)) {
                        TextComponentClipboardHandler clipboardHandler = new TextComponentClipboardHandler((JTextComponent) c);
                        for (Object action : defaultTextActions) {
                            ((DefaultPopupClipboardAction) action).setClipboardHandler(clipboardHandler);
                        }

                        defaultPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                } else if (c instanceof JList) {
                    if ((SwingUtilities.isRightMouseButton(e)) && (((JList) c).getComponentPopupMenu() == null)) {
                        ListClipboardHandler clipboardHandler = new ListClipboardHandler((JList) c);
                        for (Object action : defaultTextActions) {
                            ((DefaultPopupClipboardAction) action).setClipboardHandler(clipboardHandler);
                        }

                        defaultPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                } else if (c instanceof JTable) {
                    if ((SwingUtilities.isRightMouseButton(e)) && (((JTable) c).getComponentPopupMenu() == null)) {
                        TableClipboardHandler clipboardHandler = new TableClipboardHandler((JTable) c);
                        for (Object action : defaultTextActions) {
                            ((DefaultPopupClipboardAction) action).setClipboardHandler(clipboardHandler);
                        }

                        defaultPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

            super.dispatchEvent(event);
        }

        private Component getSource(MouseEvent e) {
            return SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
        }

        private class TextComponentClipboardHandler implements ComponentClipboardHandler {

            private final JTextComponent txtComp;

            public TextComponentClipboardHandler(JTextComponent txtComp) {
                this.txtComp = txtComp;
            }

            @Override
            public void performCut() {
                txtComp.cut();
            }

            @Override
            public void performCopy() {
                txtComp.copy();
            }

            @Override
            public void performPaste() {
                txtComp.paste();
            }

            @Override
            public void performDelete() {
                invokeTextAction(txtComp, DefaultEditorKit.deleteNextCharAction);
            }

            @Override
            public void performSelectAll() {
                txtComp.requestFocus();
                txtComp.selectAll();
            }

            @Override
            public boolean isSelection() {
                return txtComp.isEnabled() && txtComp.getSelectionStart() != txtComp.getSelectionEnd();
            }

            @Override
            public boolean isEditable() {
                return txtComp.isEnabled() && txtComp.isEditable();
            }

            @Override
            public boolean canSelectAll() {
                return txtComp.isEnabled() && !txtComp.getText().isEmpty();
            }

            @Override
            public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
                // Ignore
            }

            @Override
            public boolean canPaste() {
                return true;
            }
        }

        private class ListClipboardHandler implements ComponentClipboardHandler {

            private final JList listComp;

            public ListClipboardHandler(JList listComp) {
                this.listComp = listComp;
            }

            @Override
            public void performCut() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performCopy() {
                StringBuilder builder = new StringBuilder();
                List rows = listComp.getSelectedValuesList();
                boolean empty = true;
                for (Object row : rows) {
                    builder.append(empty ? row.toString() : System.getProperty("line.separator") + row);

                    if (empty) {
                        empty = false;
                    }
                }

                getClipboard().setContents(new StringSelection(builder.toString()), null);
            }

            @Override
            public void performPaste() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performDelete() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performSelectAll() {
                if (listComp.getModel().getSize() > 0) {
                    listComp.setSelectionInterval(0, listComp.getModel().getSize() - 1);
                }
            }

            @Override
            public boolean isSelection() {
                return listComp.isEnabled() && !listComp.isSelectionEmpty();
            }

            @Override
            public boolean isEditable() {
                return false;
            }

            @Override
            public boolean canSelectAll() {
                return listComp.isEnabled() && listComp.getSelectionMode() != DefaultListSelectionModel.SINGLE_SELECTION;
            }

            @Override
            public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
                // Ignore
            }

            @Override
            public boolean canPaste() {
                return true;
            }
        }

        private class TableClipboardHandler implements ComponentClipboardHandler {

            private final JTable tableComp;

            public TableClipboardHandler(JTable tableComp) {
                this.tableComp = tableComp;
            }

            @Override
            public void performCut() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performCopy() {
                StringBuilder builder = new StringBuilder();
                int[] rows = tableComp.getSelectedRows();
                int[] columns;
                if (tableComp.getSelectionModel().getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
                    columns = new int[tableComp.getColumnCount()];
                    for (int i = 0; i < tableComp.getColumnCount(); i++) {
                        columns[i] = i;
                    }
                } else {
                    columns = tableComp.getSelectedColumns();
                }

                boolean empty = true;
                for (int rowIndex : rows) {
                    if (!empty) {
                        builder.append(System.getProperty("line.separator"));
                    } else {
                        empty = false;
                    }

                    boolean columnEmpty = true;
                    for (int columnIndex : columns) {
                        if (!columnEmpty) {
                            builder.append("\t");
                        } else {
                            columnEmpty = false;
                        }

                        Object value = tableComp.getModel().getValueAt(rowIndex, columnIndex);
                        if (value != null) {
                            builder.append(value.toString());
                        }
                    }
                }

                getClipboard().setContents(new StringSelection(builder.toString()), null);
            }

            @Override
            public void performPaste() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performDelete() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performSelectAll() {
                tableComp.selectAll();
            }

            @Override
            public boolean isSelection() {
                return tableComp.isEnabled() && (tableComp.getSelectedColumn() >= 0 || tableComp.getSelectedRow() >= 0);
            }

            @Override
            public boolean isEditable() {
                return false;
            }

            @Override
            public boolean canSelectAll() {
                return tableComp.isEnabled() && tableComp.getSelectionModel().getSelectionMode() != ListSelectionModel.SINGLE_SELECTION;
            }

            @Override
            public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
                // Ignore
            }

            @Override
            public boolean canPaste() {
                return true;
            }
        }
    }

    /**
     * Clipboard action for default popup menu.
     */
    private static abstract class DefaultPopupClipboardAction extends AbstractAction {

        protected ComponentClipboardHandler clipboardHandler;

        public DefaultPopupClipboardAction(String name) {
            super(name);
        }

        public void setClipboardHandler(ComponentClipboardHandler clipboardHandler) {
            this.clipboardHandler = clipboardHandler;
            postTextComponentInitialize();
        }

        protected abstract void postTextComponentInitialize();
    }
}
