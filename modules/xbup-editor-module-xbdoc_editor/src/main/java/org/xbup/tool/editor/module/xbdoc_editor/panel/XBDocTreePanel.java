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
package org.xbup.tool.editor.module.xbdoc_editor.panel;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.TreePath;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.parser_tree.XBTTreeDocument;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.operation.XBTCommand;
import org.xbup.lib.operation.basic.XBTAddBlockCommand;
import org.xbup.lib.operation.basic.XBTDeleteBlockCommand;
import org.xbup.lib.operation.undo.XBTLinearUndo;
import org.xbup.tool.editor.module.xbdoc_editor.XBDocEditorFrame;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.ItemAddDialog;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.MainFrameManagement;

/**
 * Panel with document tree visualization.
 *
 * @version 0.1.23 2013/09/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBDocTreePanel extends javax.swing.JPanel implements ActivePanelActionHandling {

    private final XBTTreeDocument mainDoc;
    private final XBDocTreeModel mainDocModel;
    private XBDocTreeCellRenderer cellRenderer;

    private XBACatalog catalog;
    private final XBTLinearUndo treeUndo;
    private final List<ActionListener> updateEventList;
    private boolean editEnabled;
    private boolean addEnabled;
    private Clipboard clipboard;
    private static final DataFlavor xbFlavor = new DataFlavor(XBHead.MIME_XBUP, "XBUP Document");

    private Component lastFocusedComponent = null;
    private final Map<String, ActionListener> actionListenerMap = new HashMap<>();
    private final XBDocEditorFrame mainFrame;

    public XBDocTreePanel(XBDocEditorFrame mainFrame, XBTTreeDocument mainDoc, XBACatalog catalog, JPopupMenu popupMenu) {
        this.mainFrame = mainFrame;
        this.mainDoc = mainDoc;
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.catalog = catalog;
        mainDocModel = new XBDocTreeModel(mainDoc);
        updateEventList = new ArrayList<>();
        treeUndo = new XBTLinearUndo(mainDoc);

        initComponents();

        mainTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                updateItemStatus();
            }
        });
        /*clipboard.addFlavorListener(new FlavorListener() {
         @Override
         public void flavorsChanged(FlavorEvent e) {
         pasteAction.setEnabled(pasteAction.isEnabled());
         }
         });*/

        actionListenerMap.put(DefaultEditorKit.cutAction, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performCut();
            }
        });
        actionListenerMap.put(DefaultEditorKit.copyAction, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performCopy();
            }
        });
        actionListenerMap.put(DefaultEditorKit.pasteAction, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performPaste();
            }
        });
        actionListenerMap.put(DefaultEditorKit.deleteNextCharAction, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDelete();
            }
        });
        actionListenerMap.put("delete", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDelete();
            }
        });

        mainTree.setDragEnabled(true);
        mainTree.setDropMode(DropMode.USE_SELECTION);

        mainTree.setTransferHandler(new XBDocTreeTransferHandler(this));
//        mainTree.setDropTarget(new );
    }

    /**
     * Updating selected item available operations status, like add, edit,
     * delete
     */
    public void updateItemStatus() {
        setEditEnabled(!mainTree.isSelectionEmpty());
        updateUndoAvailable();
        if (!editEnabled) {
            setAddEnabled(mainDoc.getRootBlock() == null);
        } else {
            setAddEnabled(((XBTTreeNode) mainTree.getLastSelectedPathComponent()).getDataMode() == XBBlockDataMode.NODE_BLOCK);
        }
        for (Iterator it = updateEventList.iterator(); it.hasNext();) {
            ((ActionListener) it.next()).actionPerformed(null);
        }

        updateActionStatus(lastFocusedComponent);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        mainTree = new javax.swing.JTree();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane2.setBorder(null);

        mainTree.setModel(mainDocModel);
        mainTree.setAutoscrolls(true);
        mainTree.setShowsRootHandles(true);
        mainTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mainTreeMouseReleased(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mainTreeMouseReleased1(evt);
            }
        });
        jScrollPane2.setViewportView(mainTree);

        add(jScrollPane2);
    }// </editor-fold>//GEN-END:initComponents

    private void mainTreeMouseReleased1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainTreeMouseReleased1
        if (evt.isPopupTrigger()) {
            mainTree.setSelectionPath(mainTree.getPathForLocation(evt.getX(), evt.getY()));
            boolean availableItem = (mainTree.getLastSelectedPathComponent() != null);
            setEditEnabled(availableItem);
            boolean addPossible;
            if (!availableItem) {
                addPossible = mainDoc.getRootBlock() == null;
            } else {
                addPossible = ((XBTTreeNode) mainTree.getLastSelectedPathComponent()).getDataMode() == XBBlockDataMode.NODE_BLOCK;
            }
            setAddEnabled(addPossible);
        }
    }//GEN-LAST:event_mainTreeMouseReleased1

    private void mainTreeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainTreeMouseReleased
        if (evt.isPopupTrigger()) {
            mainTree.setSelectionPath(mainTree.getPathForLocation(evt.getX(), evt.getY()));
            boolean availableItem = (mainTree.getLastSelectedPathComponent() != null);
            setEditEnabled(availableItem);
            boolean addPossible;
            if (!availableItem) {
                addPossible = mainDoc.getRootBlock() == null;
            } else {
                addPossible = ((XBTTreeNode) mainTree.getLastSelectedPathComponent()).getDataMode() == XBBlockDataMode.NODE_BLOCK;
            }
            setAddEnabled(addPossible);
        }
    }//GEN-LAST:event_mainTreeMouseReleased

    public XBTTreeNode getSelectedItem() {
        return (XBTTreeNode) mainTree.getLastSelectedPathComponent();
    }

    public void reportStructureChange(XBTTreeNode node) {
        if ((cellRenderer == null) && (this.isShowing())) {
            cellRenderer = new XBDocTreeCellRenderer(catalog);
            mainTree.setCellRenderer(cellRenderer);
        }
        if (node == null) {
            mainDocModel.fireTreeChanged();
        } else {
            mainDocModel.fireTreeStructureChanged(node);
        }
    }

    public boolean isEditEnabled() {
        return editEnabled;
    }

    public boolean isAddEnabled() {
        return addEnabled;
    }

    public boolean isPasteEnabled() {
        return addEnabled && clipboard.isDataFlavorAvailable(xbFlavor);
    }

    public void addUpdateListener(ActionListener tml) {
        updateEventList.add(tml);
    }

    public void removeUpdateListener(ActionListener tml) {
        updateEventList.remove(tml);
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        if (cellRenderer != null) {
            cellRenderer.setCatalog(catalog);
        }
        this.catalog = catalog;
        if (mainDoc != null) {
            mainDoc.setCatalog(catalog);
            mainDoc.processSpec();
        }
    }

    public void performCut() {
        performCopy();
        performDelete();
    }

    public void performCopy() {
        XBTSelection selection = new XBTSelection(getSelectedItem());
        clipboard.setContents(selection, selection);
    }

    public void performPaste() {
        if (clipboard.isDataFlavorAvailable(xbFlavor)) {
            try {
                ByteArrayOutputStream stream = (ByteArrayOutputStream) clipboard.getData(xbFlavor);
                XBTTreeNode node = getSelectedItem();
                XBTTreeNode newNode = new XBTTreeNode(node);
                try {
                    newNode.fromStreamUB(new ByteArrayInputStream(stream.toByteArray()));
                    try {
                        XBTCommand step = new XBTAddBlockCommand(node, newNode);
                        getTreeUndo().performStep(step);
                        reportStructureChange(node);
                        updateItemStatus();
                    } catch (Exception ex) {
                        Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException | XBProcessingException ex) {
                    Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void performSelectAll() {
        mainTree.setSelectionRow(0);
    }

    public void performUndo() {
        try {
            getTreeUndo().performUndo();
            reportStructureChange(null);
            updateItemStatus();
        } catch (Exception ex) {
            Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void performRedo() {
        try {
            getTreeUndo().performRedo();
            reportStructureChange(null);
            updateItemStatus();
        } catch (Exception ex) {
            Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void performAdd() {
        XBTTreeNode node = getSelectedItem();
        ItemAddDialog itemAddDialog = new ItemAddDialog(getFrame(), true, catalog);
        itemAddDialog.setLocationRelativeTo(itemAddDialog.getParent());
        itemAddDialog.setParentNode(node);
        XBTTreeNode newNode = itemAddDialog.showDialog();
        if (newNode != null) {
            try {
                XBTCommand step = new XBTAddBlockCommand(node, newNode);
                getTreeUndo().performStep(step);
            } catch (Exception ex) {
                Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            reportStructureChange(newNode);
            mainDoc.setModified(true);
            updateItemStatus();
        }
    }

    public void performDelete() {
        deleteNode(getSelectedItem());
    }

    public void deleteNode(XBTTreeNode node) {
        XBTTreeNode parent = (XBTTreeNode) node.getParent();
        try {
            XBTCommand step = new XBTDeleteBlockCommand(node);
            getTreeUndo().performStep(step);
        } catch (Exception ex) {
            Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (parent == null) {
            mainDocModel.fireTreeChanged();
        } else {
            mainDocModel.fireTreeStructureChanged(parent);
        }
        mainDoc.setModified(true);
    }

    private StringBuffer nodeAsText(XBTTreeNode node, String prefix) {
        StringBuffer result = new StringBuffer();
        result.append(prefix);
        result.append("<").append(getCaption(node));
        if (node.getAttributesCount() > 2) {
            Iterator attributesIterator = node.getAttributes().iterator();
            int i = 1;
            for (; attributesIterator.hasNext();) {
                UBNat32 attr = (UBNat32) attributesIterator.next();
                result.append(" ").append(i).append("=\"").append(attr.getLong()).append("\"");
                i++;
            }
        }

        if (node.getChildren() != null) {
            result.append(">\n");
            for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
                XBTTreeNode elem = (XBTTreeNode) it.next();
                result.append(nodeAsText(elem, prefix + "  "));
            }
            result.append(prefix);
            result.append("</").append(getCaption(node)).append(">\n");
        } else {
            result.append("/>\n");
        }
        return result;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree mainTree;
    // End of variables declaration//GEN-END:variables

    public void setEditEnabled(boolean editEnabled) {
        if (editEnabled != this.editEnabled) {
            this.editEnabled = editEnabled;
            firePropertyChange("editEnabled", !editEnabled, editEnabled);
        }
    }

    public void setAddEnabled(boolean addEnabled) {
        if (addEnabled != this.addEnabled) {
            this.addEnabled = addEnabled;
            firePropertyChange("addEnabled", !addEnabled, addEnabled);
            firePropertyChange("pasteEnabled", !editEnabled, editEnabled);
        }
    }

    public void updateUndoAvailable() {
        firePropertyChange("undoAvailable", false, true);
        firePropertyChange("redoAvailable", false, true);
    }

    public XBTLinearUndo getTreeUndo() {
        return treeUndo;
    }

    /**
     * @param popupMenu the popupMenu to set
     */
    public void setPopupMenu(JPopupMenu popupMenu) {
        mainTree.setComponentPopupMenu(popupMenu);
    }

    @Override
    public boolean updateActionStatus(Component component) {
        if (component == mainTree) {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            MainFrameManagement frameManagement = mainFrame.getMainFrameManagement();
            lastFocusedComponent = component;
            frameManagement.getCutAction().setEnabled(editEnabled);
            frameManagement.getCopyAction().setEnabled(editEnabled);
            frameManagement.getPasteAction().setEnabled(addEnabled && clipboard.isDataFlavorAvailable(xbFlavor));
            frameManagement.getDeleteAction().setEnabled(editEnabled);
            frameManagement.getSelectAllAction().setEnabled(false);

            frameManagement.getUndoAction().setEnabled(treeUndo.canUndo());
            frameManagement.getRedoAction().setEnabled(treeUndo.canRedo());

            mainFrame.getItemAddAction().setEnabled(addEnabled);
            mainFrame.getItemModifyAction().setEnabled(editEnabled);
            mainFrame.getItemPropertiesAction().setEnabled(editEnabled);
            mainFrame.getEditFindAction().setEnabled(false);
            mainFrame.getEditFindAgainAction().setEnabled(false);
            mainFrame.getEditReplaceAction().setEnabled(false);
            mainFrame.getEditGotoAction().setEnabled(false);

            return true;
        }

        lastFocusedComponent = null;
        return false;
    }

    @Override
    public void releaseActionStatus() {
        mainFrame.getItemAddAction().setEnabled(false);
        mainFrame.getItemModifyAction().setEnabled(false);
        mainFrame.getItemPropertiesAction().setEnabled(false);
        mainFrame.getEditFindAction().setEnabled(false);
        mainFrame.getEditFindAgainAction().setEnabled(false);
        mainFrame.getEditReplaceAction().setEnabled(false);
        mainFrame.getEditGotoAction().setEnabled(false);
    }

    @Override
    public boolean performAction(String eventName, ActionEvent event) {
        if (lastFocusedComponent != null) {
            ActionListener actionListener = actionListenerMap.get(eventName);
            if (actionListener != null) {
                actionListener.actionPerformed(event);
                return true;
            }
        }

        return false;
    }

    public static class XBTSelection implements Transferable, ClipboardOwner {

        private ByteArrayOutputStream data;

        public XBTSelection(XBTTreeNode node) {
            if (node != null) {
                data = new ByteArrayOutputStream();
                try {
                    node.toStreamUB(data);
                } catch (IOException ex) {
                    Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            // TODO: Later also as text
            DataFlavor[] result = new DataFlavor[1];
            result[0] = xbFlavor;
            return result;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(xbFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(xbFlavor)) {
                return data;
            }
            return null;
        }

        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
            // do nothing
        }
    }

    private static class XBDocTreeTransferHandler extends TransferHandler {

        private final XBDocTreePanel docTreePanel;
        private XBTTreeNode sourceNode;

        public XBDocTreeTransferHandler(XBDocTreePanel docTreePanel) {
            super();
            this.docTreePanel = docTreePanel;
            sourceNode = null;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        @Override
        public Transferable createTransferable(JComponent c) {
            if (c instanceof JTree) {
                sourceNode = docTreePanel.getSelectedItem();
                return new XBTSelection(sourceNode);
                //java.awt.datatransfer.StringSelection("Test");
            } else {
                return null;
            }
        }

        @Override
        public void exportDone(JComponent c, Transferable t, int action) {
            if (action == MOVE) {
                docTreePanel.deleteNode(sourceNode);
            } else if (action == COPY) {

            }
            sourceNode = null;
        }

        @Override
        public boolean canImport(TransferSupport supp) {
            // Check for String flavor
            if (!supp.isDataFlavorSupported(xbFlavor)) {
                return false;
            }

            // Fetch the drop location
            DropLocation loc = supp.getDropLocation();

            // Return whether we accept the location
            TreePath treePath = docTreePanel.mainTree.getPathForLocation(loc.getDropPoint().x, loc.getDropPoint().y);
            if (treePath == null) {
                return false;
            }
            Object nodeObject = treePath.getLastPathComponent();
            if (!(nodeObject instanceof XBTTreeNode)) {
                return false;
            }
            XBTTreeNode node = (XBTTreeNode) nodeObject;
            if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                return false;
            }
            if ((sourceNode != null) && (supp.getDropAction() == MOVE)) {
                if (node == sourceNode) {
                    return false;
                }
                if (node == sourceNode.getParent()) {
                    return false;
                }
                XBTTreeNode parent = node.getParent();
                while (parent != null) {
                    if (parent == sourceNode) {
                        return false;
                    }
                    parent = parent.getParent();
                }
            }
            return true;
        }

        @Override
        public boolean importData(TransferSupport supp) {
            if (!canImport(supp)) {
                return false;
            }

            // Fetch the Transferable and its data
            Transferable t = supp.getTransferable();
//            String data = t.getTransferData(stringFlavor);

            // Fetch the drop location
            DropLocation loc = supp.getDropLocation();

            // Return whether we accept the location
            TreePath treePath = docTreePanel.mainTree.getPathForLocation(loc.getDropPoint().x, loc.getDropPoint().y);
            if (treePath == null) {
                return false;
            }
            Object nodeObject = treePath.getLastPathComponent();
            if (!(nodeObject instanceof XBTTreeNode)) {
                return false;
            }
            XBTTreeNode node = (XBTTreeNode) nodeObject;

            // Insert the data at this location
            try {
                ByteArrayOutputStream stream = (ByteArrayOutputStream) t.getTransferData(xbFlavor);
                XBTTreeNode newNode = new XBTTreeNode(node);
                try {
                    newNode.fromStreamUB(new ByteArrayInputStream(stream.toByteArray()));
                    try {
                        XBTCommand step = new XBTAddBlockCommand(node, newNode);
                        docTreePanel.getTreeUndo().performStep(step);
                        docTreePanel.reportStructureChange(node);
                        docTreePanel.mainDoc.processSpec();
                        docTreePanel.updateItemStatus();
                    } catch (Exception ex) {
                        Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException | XBProcessingException ex) {
                    Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(XBDocTreePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
    }

    private String getCaption(XBTTreeNode node) {
        if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            return "Data Block";
        }
        XBBlockType blockType = node.getBlockType();
        if (catalog != null) {
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            if (blockType instanceof XBDeclBlockType) {
                XBBlockDecl blockDecl = ((XBDeclBlockType) blockType).getBlockDecl();
                if (blockDecl instanceof XBCBlockDecl) {
                    return nameService.getDefaultCaption(((XBCBlockDecl) blockDecl).getBlockSpec().getParent());
                } else {
                    // TODO locally defined blocks later
                }
            }
        }

        return "Unknown" + " (" + Integer.toString(((XBFBlockType) blockType).getGroupID().getInt()) + ", " + Integer.toString(((XBFBlockType) blockType).getBlockID().getInt()) + ")";
    }

    private Frame getFrame() {
        Component component = SwingUtilities.getWindowAncestor(this);
        while (!(component == null || component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }
}
