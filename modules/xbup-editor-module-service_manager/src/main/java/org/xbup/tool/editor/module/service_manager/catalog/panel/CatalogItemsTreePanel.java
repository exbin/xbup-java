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
package org.xbup.tool.editor.module.service_manager.catalog.panel;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.yaml.XBCatalogYaml;
import org.xbup.tool.editor.module.service_manager.catalog.dialog.CatalogAddItemDialog;
import org.xbup.tool.editor.module.service_manager.catalog.dialog.CatalogItemEditDialog;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogNodesTreeModel.CatalogNodesTreeItem;
import static org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogSpecItemType.FORMAT;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.XBEditorFrame;

/**
 * Catalog Specification Panel.
 *
 * @version 0.1.24 2014/11/12
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogItemsTreePanel extends javax.swing.JPanel implements ActivePanelActionHandling {

    private XBACatalog catalog;
    private XBCNodeService nodeService;
    private XBCSpecService specService;
    private XBCXNameService nameService;
    private XBCXDescService descService;
    private XBCXStriService striService;
    private CatalogNodesTreeModel nodesModel;
    private CatalogSpecsTableModel specsModel;
    private final CatalogItemPanel itemPanel;
    private XBCItem currentItem;
    private XBCatalogYaml catalogYaml;
    private MainFrameManagement mainFrameManagement;

    private Map<String, ActionListener> actionListenerMap = new HashMap<>();

    public CatalogItemsTreePanel(XBACatalog catalog) {
        this.catalog = catalog;

        Frame frame = getFrame();
        if (frame instanceof XBEditorFrame) {
            mainFrameManagement = ((XBEditorFrame) frame).getMainFrameManagement();
        }

        nameService = null;
        descService = null;
        if (catalog != null) {
            nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
            specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
            nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            descService = (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
            striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);
            // TODO: OnAddExtension
        }
        nodesModel = new CatalogNodesTreeModel(catalog);
        specsModel = new CatalogSpecsTableModel(catalog);
        catalogYaml = new XBCatalogYaml(catalog);

        initComponents();
        catalogTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                DefaultTreeCellRenderer retValue = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof CatalogNodesTreeItem) {
                    XBCNode node = ((CatalogNodesTreeItem) value).getNode();
                    String nodeName = ((CatalogNodesTreeItem) value).getName();
                    if (nodeName == null) {
                        retValue.setText("node [" + node.getId().toString() + "]");
                    } else {
                        retValue.setText(nodeName);
                    }
                }

                return retValue;
            }
        });
        catalogSpecListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (catalogSpecListTable.getSelectedRow() >= 0) {
                        setItem(specsModel.getItem(catalogSpecListTable.getSelectedRow()));
                    } else {
                        setItem(null);
                    }
                }
            }
        });
        /*        jTable2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) {
         jButton1.setEnabled(jTable2.getSelectedRow()>=0);
         }
         }); */

        itemPanel = new CatalogItemPanel(catalog);
        catalogItemSplitPane.setRightComponent(itemPanel);

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

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        catalogTreePopupMenu = new javax.swing.JPopupMenu();
        popupAddItemMenu = new javax.swing.JMenu();
        popupAddCustomMenuItem = new javax.swing.JMenuItem();
        popupAddSpecMenuItem = new javax.swing.JMenuItem();
        popupAddNodeMenuItem = new javax.swing.JMenuItem();
        popupEditMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popupImportItemMenuItem = new javax.swing.JMenuItem();
        popupExportItemMenuItem = new javax.swing.JMenuItem();
        panelSplitPane = new javax.swing.JSplitPane();
        catalogTreeScrollPane = new javax.swing.JScrollPane();
        catalogTree = new javax.swing.JTree();
        catalogItemSplitPane = new javax.swing.JSplitPane();
        catalogItemListScrollPane = new javax.swing.JScrollPane();
        catalogSpecListTable = new javax.swing.JTable();

        catalogTreePopupMenu.setName("catalogTreePopupMenu"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/service_manager/catalog/panel/resources/CatalogItemsTreePanel"); // NOI18N
        popupAddItemMenu.setText(bundle.getString("addItemMenu.text")); // NOI18N
        popupAddItemMenu.setName("popupAddItemMenu"); // NOI18N

        popupAddCustomMenuItem.setText(bundle.getString("addCustomMenuItem.text")); // NOI18N
        popupAddCustomMenuItem.setEnabled(false);
        popupAddCustomMenuItem.setName("popupAddCustomMenuItem"); // NOI18N
        popupAddCustomMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupAddCustomMenuItemActionPerformed(evt);
            }
        });
        popupAddItemMenu.add(popupAddCustomMenuItem);

        popupAddSpecMenuItem.setText(bundle.getString("addBlockMenuItem.text")); // NOI18N
        popupAddSpecMenuItem.setName("popupAddSpecMenuItem"); // NOI18N
        popupAddSpecMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupAddSpecMenuItemActionPerformed(evt);
            }
        });
        popupAddItemMenu.add(popupAddSpecMenuItem);

        popupAddNodeMenuItem.setText(bundle.getString("addNodeMenuItem.text")); // NOI18N
        popupAddNodeMenuItem.setName("popupAddNodeMenuItem"); // NOI18N
        popupAddNodeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupAddNodeMenuItemActionPerformed(evt);
            }
        });
        popupAddItemMenu.add(popupAddNodeMenuItem);

        catalogTreePopupMenu.add(popupAddItemMenu);

        popupEditMenuItem.setText(bundle.getString("editMenuItem.text")); // NOI18N
        popupEditMenuItem.setName("popupEditMenuItem"); // NOI18N
        popupEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupEditMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupEditMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        catalogTreePopupMenu.add(jSeparator1);

        jSeparator2.setName("jSeparator2"); // NOI18N
        catalogTreePopupMenu.add(jSeparator2);

        popupImportItemMenuItem.setText("Import...");
        popupImportItemMenuItem.setName("popupImportItemMenuItem"); // NOI18N
        popupImportItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupImportItemMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupImportItemMenuItem);

        popupExportItemMenuItem.setText("Export...");
        popupExportItemMenuItem.setName("popupExportItemMenuItem"); // NOI18N
        popupExportItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupExportItemMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupExportItemMenuItem);

        setName("Form"); // NOI18N

        panelSplitPane.setDividerLocation(100);
        panelSplitPane.setName("panelSplitPane"); // NOI18N

        catalogTreeScrollPane.setComponentPopupMenu(catalogTreePopupMenu);
        catalogTreeScrollPane.setName("catalogTreeScrollPane"); // NOI18N

        catalogTree.setModel(nodesModel);
        catalogTree.setComponentPopupMenu(catalogTreePopupMenu);
        catalogTree.setName("catalogTree"); // NOI18N
        catalogTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                catalogTreeValueChanged(evt);
            }
        });
        catalogTreeScrollPane.setViewportView(catalogTree);

        panelSplitPane.setLeftComponent(catalogTreeScrollPane);

        catalogItemSplitPane.setDividerLocation(180);
        catalogItemSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        catalogItemSplitPane.setName("catalogItemSplitPane"); // NOI18N

        catalogItemListScrollPane.setComponentPopupMenu(catalogTreePopupMenu);
        catalogItemListScrollPane.setName("catalogItemListScrollPane"); // NOI18N

        catalogSpecListTable.setModel(specsModel);
        catalogSpecListTable.setComponentPopupMenu(catalogTreePopupMenu);
        catalogSpecListTable.setName("catalogSpecListTable"); // NOI18N
        catalogItemListScrollPane.setViewportView(catalogSpecListTable);

        catalogItemSplitPane.setLeftComponent(catalogItemListScrollPane);

        panelSplitPane.setRightComponent(catalogItemSplitPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void catalogTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_catalogTreeValueChanged
        if (catalogTree.getLastSelectedPathComponent() != null) {
            if (nodesModel.isLeaf(catalogTree.getLastSelectedPathComponent())) {
                setNode(((CatalogNodesTreeItem) catalogTree.getLastSelectedPathComponent()).getNode());
            } else {
                setNode(((CatalogNodesTreeItem) catalogTree.getLastSelectedPathComponent()).getNode());
            }
        } else {
            setNode(null);
        }
    }//GEN-LAST:event_catalogTreeValueChanged

    private void popupAddNodeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupAddNodeMenuItemActionPerformed
        if (currentItem != null) {
            // TODO: Use different transaction management later
            EntityManager em = ((XBECatalog) catalog).getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            XBCNode newNode = (XBCNode) nodeService.createItem();
            if (newNode instanceof XBENode) {
                ((XBENode) newNode).setParent((XBEItem) currentItem);

                nodeService.persistItem((XBENode) newNode);
                nodesModel.valueForPathChanged(null, newNode);
            }
            em.flush();
            transaction.commit();
        }
    }//GEN-LAST:event_popupAddNodeMenuItemActionPerformed

    private void popupEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupEditMenuItemActionPerformed
        if (currentItem != null) {
            CatalogItemEditDialog propertiesDialog = new CatalogItemEditDialog(getFrame(), true);
            propertiesDialog.setCatalog(catalog);
            propertiesDialog.setCatalogItem(currentItem);
            propertiesDialog.setVisible(true);
        }
    }//GEN-LAST:event_popupEditMenuItemActionPerformed

    private void popupExportItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupExportItemMenuItemActionPerformed
        if (currentItem != null) {
            JFileChooser exportFileChooser = new JFileChooser();
            // exportFileChooser.setAcceptAllFileFilterUsed(true);
            if (exportFileChooser.showSaveDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
                FileWriter fileWriter;
                try {
                    fileWriter = new FileWriter(exportFileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        catalogYaml.exportCatalogItem(currentItem, fileWriter);
                    } finally {
                        fileWriter.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CatalogItemsTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CatalogItemsTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_popupExportItemMenuItemActionPerformed

    private void popupImportItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupImportItemMenuItemActionPerformed
        if ((currentItem != null) && (currentItem instanceof XBCNode)) {
            JFileChooser importFileChooser = new JFileChooser();
            // exportFileChooser.setAcceptAllFileFilterUsed(true);
            if (importFileChooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
                FileInputStream fileStream;
                try {
                    fileStream = new FileInputStream(importFileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        catalogYaml.importCatalogItem(fileStream, (XBENode) currentItem);
                    } finally {
                        fileStream.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CatalogItemsTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CatalogItemsTreePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_popupImportItemMenuItemActionPerformed

    private void popupAddCustomMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupAddCustomMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_popupAddCustomMenuItemActionPerformed

    private void popupAddSpecMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupAddSpecMenuItemActionPerformed
        CatalogAddItemDialog addDialog = new CatalogAddItemDialog(getFrame(), true);
        addDialog.setVisible(true);
        if (addDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            // TODO: Use different transaction management later
            EntityManager em = ((XBECatalog) catalog).getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            XBENode node = (XBENode) (currentItem instanceof XBCNode ? currentItem : currentItem.getParent());
            XBESpec spec = null;
            switch (addDialog.getItemType()) {
                case BLOCK: {
                    spec = (XBESpec) specService.createBlockSpec();
                    Long newXbIndex = specService.findMaxBlockSpecXB(node);
                    spec.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                    break;
                }
                case GROUP: {
                    spec = (XBESpec) specService.createGroupSpec();
                    Long newXbIndex = specService.findMaxBlockSpecXB(node);
                    spec.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                    break;
                }
                case FORMAT: {
                    spec = (XBESpec) specService.createFormatSpec();
                    Long newXbIndex = specService.findMaxBlockSpecXB(node);
                    spec.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                    break;
                }
            }

            spec.setParent(node);
            specService.persistItem(spec);
            nameService.setItemNameText(spec, addDialog.getItemName());
            em.flush();
            transaction.commit();

            setNode(specsModel.getNode());
            repaint();
        }
    }//GEN-LAST:event_popupAddSpecMenuItemActionPerformed

    public void setNode(XBCNode node) {
        setItem(node);
        specsModel.setNode(node);
        if (node != null) {
            catalogSpecListTable.setRowSelectionInterval(0, 0);
        }
        catalogSpecListTable.revalidate();
    }

    public void setItem(XBCItem item) {
        currentItem = item;
        itemPanel.setItem(item);

        if (mainFrameManagement != null) {
            updateActionStatus(mainFrameManagement.getLastFocusOwner());
        }
    }

    @Override
    public boolean updateActionStatus(Component component) {
        if (component == catalogTree) {
            // clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            if (mainFrameManagement != null) {
                mainFrameManagement.getCutAction().setEnabled(currentItem != null);
                mainFrameManagement.getCopyAction().setEnabled(currentItem != null);
                mainFrameManagement.getPasteAction().setEnabled(false); // TODO clipboard.isDataFlavorAvailable(xbFlavor));
                mainFrameManagement.getDeleteAction().setEnabled(currentItem != null);
                mainFrameManagement.getSelectAllAction().setEnabled(false);
            }

            // frameManagement.getUndoAction().setEnabled(treeUndo.canUndo());
            // frameManagement.getRedoAction().setEnabled(treeUndo.canRedo());
            return true;
        }

        return false;
    }

    public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
        this.mainFrameManagement = mainFrameManagement;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane catalogItemListScrollPane;
    private javax.swing.JSplitPane catalogItemSplitPane;
    private javax.swing.JTable catalogSpecListTable;
    private javax.swing.JTree catalogTree;
    private javax.swing.JPopupMenu catalogTreePopupMenu;
    private javax.swing.JScrollPane catalogTreeScrollPane;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane panelSplitPane;
    private javax.swing.JMenuItem popupAddCustomMenuItem;
    private javax.swing.JMenu popupAddItemMenu;
    private javax.swing.JMenuItem popupAddNodeMenuItem;
    private javax.swing.JMenuItem popupAddSpecMenuItem;
    private javax.swing.JMenuItem popupEditMenuItem;
    private javax.swing.JMenuItem popupExportItemMenuItem;
    private javax.swing.JMenuItem popupImportItemMenuItem;
    // End of variables declaration//GEN-END:variables

    public JPopupMenu getPopupMenu() {
        return catalogTreePopupMenu;
    }

    public void performCut() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void performCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void performPaste() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void performDelete() {
        Object[] options = {
            "Delete",
            "Cancel"
        };

        int result = JOptionPane.showOptionDialog(this,
                "Are you sure you want to delete this item?",
                "Delete Item",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (result == JOptionPane.OK_OPTION) {
            // TODO: Use different transaction management later
            EntityManager em = ((XBECatalog) catalog).getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<XBEXName> names = nameService.getItemNames(currentItem);
            for (XBEXName name : names) {
                nameService.removeItem(name);
            }

            List<XBEXDesc> descs = descService.getItemDescs(currentItem);
            for (XBEXDesc desc : descs) {
                descService.removeItem(desc);
            }

            XBCXStri stri = striService.getItemStringId(currentItem);
            if (stri != null) {
                striService.removeItem(stri);
            }

            if (currentItem instanceof XBCNode) {
                nodeService.removeItem(currentItem);
            } else {
                specService.removeItem(currentItem);
            }
            em.flush();
            transaction.commit();

            setNode(specsModel.getNode());
            repaint();
        }
    }

    @Override
    public void releaseActionStatus() {
    }

    @Override
    public boolean performAction(String eventName, ActionEvent event) {
        if (mainFrameManagement != null && mainFrameManagement.getLastFocusOwner() != null) {
            ActionListener actionListener = actionListenerMap.get(eventName);
            if (actionListener != null) {
                actionListener.actionPerformed(event);
                return true;
            }
        }

        return false;
    }

    private Frame getFrame() {
        Component component = SwingUtilities.getWindowAncestor(this);
        while (!(component == null || component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }
}
