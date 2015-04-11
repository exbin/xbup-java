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

import org.xbup.tool.editor.module.service_manager.YamlFileType;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
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
import org.xbup.lib.catalog.entity.service.XBEXNameService;
import org.xbup.lib.catalog.yaml.XBCatalogYaml;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCGroupDecl;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.parser.basic.convert.XBTTypeUndeclaringFilter;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.tool.editor.module.service_manager.catalog.dialog.CatalogAddItemDialog;
import org.xbup.tool.editor.module.service_manager.catalog.dialog.CatalogEditItemDialog;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogNodesTreeModel.CatalogNodesTreeItem;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.MenuManagement;
import org.xbup.tool.editor.utils.WindowUtils;
import org.xbup.tool.editor.module.service_manager.XBFileType;
import org.xbup.tool.editor.module.service_manager.panel.CatalogManagerPanelable;

/**
 * Catalog editor panel.
 *
 * @version 0.1.25 2015/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogEditorPanel extends javax.swing.JPanel implements ActivePanelActionHandling, CatalogManagerPanelable {

    private XBCItem currentItem;

    private XBACatalog catalog;
    private MainFrameManagement mainFrameManagement;
    private CatalogNodesTreeModel nodesModel;
    private CatalogSpecsTableModel specsModel;
    private final CatalogItemPanel itemPanel;
    private XBCatalogYaml catalogYaml;

    // Cached values
    private XBCNodeService nodeService;
    private XBCSpecService specService;
    private XBCRevService revService;
    private XBCXNameService nameService;
    private XBCXDescService descService;
    private XBCXStriService striService;

    private Map<String, ActionListener> actionListenerMap = new HashMap<>();
    private MenuManagement menuManagement;

    public CatalogEditorPanel() {
        nodesModel = new CatalogNodesTreeModel();
        specsModel = new CatalogSpecsTableModel();
        catalogYaml = new XBCatalogYaml();

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

        itemPanel = new CatalogItemPanel();
        catalogItemSplitPane.setRightComponent(itemPanel);
        itemPanel.setJumpActionListener(new CatalogItemPanel.JumpActionListener() {
            @Override
            public void jumpToRev(XBCRev rev) {
                XBCSpec spec = rev.getParent();
                TreePath nodePath = nodesModel.findPathForSpec(spec);
                if (nodePath != null) {
                    catalogTree.scrollPathToVisible(nodePath);
                    catalogTree.setSelectionPath(nodePath);
                    // TODO doesn't properly select tree node

                    selectSpecTableRow(spec);
                }
            }
        });

        updateItem();

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
        popupAddMenuItem = new javax.swing.JMenuItem();
        popupEditMenuItem = new javax.swing.JMenuItem();
        popupRefreshMenuItem = new javax.swing.JMenuItem();
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
        popupAddMenuItem.setText(bundle.getString("addMenuItem.text")); // NOI18N
        popupAddMenuItem.setToolTipText(bundle.getString("addMenuItem.toolTipText")); // NOI18N
        popupAddMenuItem.setName("popupAddMenuItem"); // NOI18N
        popupAddMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupAddMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupAddMenuItem);

        popupEditMenuItem.setText(bundle.getString("editMenuItem.text")); // NOI18N
        popupEditMenuItem.setToolTipText(bundle.getString("editMenuItem,toolTipText")); // NOI18N
        popupEditMenuItem.setName("popupEditMenuItem"); // NOI18N
        popupEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupEditMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupEditMenuItem);

        popupRefreshMenuItem.setText(bundle.getString("refreshMenuItem,text")); // NOI18N
        popupRefreshMenuItem.setToolTipText(bundle.getString("refreshMenuItem,toolTipText")); // NOI18N
        popupRefreshMenuItem.setName("popupRefreshMenuItem"); // NOI18N
        popupRefreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupRefreshMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupRefreshMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        catalogTreePopupMenu.add(jSeparator1);

        jSeparator2.setName("jSeparator2"); // NOI18N
        catalogTreePopupMenu.add(jSeparator2);

        popupImportItemMenuItem.setText(bundle.getString("importItemMenuItem,text")); // NOI18N
        popupImportItemMenuItem.setToolTipText(bundle.getString("importItemMenuItem,toolTipText")); // NOI18N
        popupImportItemMenuItem.setName("popupImportItemMenuItem"); // NOI18N
        popupImportItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupImportItemMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupImportItemMenuItem);

        popupExportItemMenuItem.setText(bundle.getString("exportItemMenuItem,text")); // NOI18N
        popupExportItemMenuItem.setToolTipText(bundle.getString("exportItemMenuItem,toolTipText")); // NOI18N
        popupExportItemMenuItem.setName("popupExportItemMenuItem"); // NOI18N
        popupExportItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupExportItemMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupExportItemMenuItem);

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

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

        add(panelSplitPane, java.awt.BorderLayout.CENTER);
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

    private void popupEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupEditMenuItemActionPerformed
        if (currentItem != null) {
            CatalogEditItemDialog editDialog = new CatalogEditItemDialog(WindowUtils.getFrame(this), true);
            editDialog.setMenuManagement(menuManagement);
            editDialog.setCatalog(catalog);
            editDialog.setCatalogItem(currentItem);
            editDialog.setVisible(true);

            if (editDialog.getDialogOption() == JOptionPane.OK_OPTION) {
                EntityManager em = ((XBECatalog) catalog).getEntityManager();
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                editDialog.persist();
                setItem(currentItem);
                em.flush();
                transaction.commit();
                specsModel.setNode(specsModel.getNode());
                selectSpecTableRow(currentItem);
            }
        }
    }//GEN-LAST:event_popupEditMenuItemActionPerformed

    private void popupExportItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupExportItemMenuItemActionPerformed
        if (currentItem != null) {
            JFileChooser exportFileChooser = new JFileChooser();
            YamlFileType yamlFileType = new YamlFileType();
            XBFileType xbFileType = new XBFileType();
            exportFileChooser.addChoosableFileFilter(yamlFileType);
            exportFileChooser.addChoosableFileFilter(xbFileType);
            exportFileChooser.setAcceptAllFileFilterUsed(true);
            if (exportFileChooser.showSaveDialog(WindowUtils.getFrame(this)) == JFileChooser.APPROVE_OPTION) {
                if (exportFileChooser.getFileFilter() == yamlFileType) {
                    FileWriter fileWriter;
                    try {
                        fileWriter = new FileWriter(exportFileChooser.getSelectedFile().getAbsolutePath());
                        try {
                            catalogYaml.exportCatalogItem(currentItem, fileWriter);
                        } finally {
                            fileWriter.close();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (exportFileChooser.getFileFilter() == xbFileType) {
                    if (currentItem instanceof XBCSpec) {
                        FileOutputStream output = null;
                        try {
                            output = new FileOutputStream(exportFileChooser.getSelectedFile().getAbsolutePath());
                            long revision = revService.findMaxRevXB((XBCSpec) currentItem);
                            XBCRev specRev = revService.findRevByXB((XBCSpec) currentItem, revision);
                            XBSerializable decl
                                    = currentItem instanceof XBCFormatSpec ? specService.getFormatDeclAsLocal(new XBCFormatDecl((XBCFormatRev) specRev, catalog))
                                            : currentItem instanceof XBCGroupSpec ? specService.getGroupDeclAsLocal(new XBCGroupDecl((XBCGroupRev) specRev, catalog))
                                                    : currentItem instanceof XBCBlockSpec ? specService.getBlockDeclAsLocal(new XBCBlockDecl((XBCBlockRev) specRev, catalog)) : null;
                            XBTTypeUndeclaringFilter typeProcessing = new XBTTypeUndeclaringFilter(catalog);
                            typeProcessing.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(new XBEventWriter(output))));
                            XBPSerialWriter writer = new XBPSerialWriter(new XBTListenerToEventListener(typeProcessing));
                            writer.write(decl);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            if (output != null) {
                                try {
                                    output.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    } else {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                } else {
                    throw new IllegalStateException("Unknown file type");
                }
            }
        }
    }//GEN-LAST:event_popupExportItemMenuItemActionPerformed

    private void popupImportItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupImportItemMenuItemActionPerformed
        if ((currentItem != null) && (currentItem instanceof XBCNode)) {
            JFileChooser importFileChooser = new JFileChooser();
            importFileChooser.addChoosableFileFilter(new YamlFileType());
            importFileChooser.setAcceptAllFileFilterUsed(true);
            if (importFileChooser.showOpenDialog(WindowUtils.getFrame(this)) == JFileChooser.APPROVE_OPTION) {
                FileInputStream fileStream;
                try {
                    fileStream = new FileInputStream(importFileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        catalogYaml.importCatalogItem(fileStream, (XBENode) currentItem);
                    } finally {
                        fileStream.close();
                    }
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                    Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_popupImportItemMenuItemActionPerformed

    private void popupAddMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupAddMenuItemActionPerformed
        CatalogAddItemDialog addDialog = new CatalogAddItemDialog(WindowUtils.getFrame(this), true);
        addDialog.setVisible(true);
        if (addDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            // TODO: Use different transaction management later
            EntityManager em = ((XBECatalog) catalog).getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            XBENode node = (XBENode) (currentItem instanceof XBCNode ? currentItem : currentItem.getParent());
            XBEItem item = null;
            switch (addDialog.getItemType()) {
                case NODE: {
                    item = (XBENode) nodeService.createItem();
                    Long newXbIndex = nodeService.findMaxSubNodeXB(node);
                    item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                    break;
                }
                case BLOCK: {
                    item = (XBESpec) specService.createBlockSpec();
                    Long newXbIndex = specService.findMaxBlockSpecXB(node);
                    item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                    break;
                }
                case GROUP: {
                    item = (XBESpec) specService.createGroupSpec();
                    Long newXbIndex = specService.findMaxBlockSpecXB(node);
                    item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                    break;
                }
                case FORMAT: {
                    item = (XBESpec) specService.createFormatSpec();
                    Long newXbIndex = specService.findMaxBlockSpecXB(node);
                    item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                    break;
                }
                default: {
                    throw new IllegalStateException();
                }
            }

            if (item == null) {
                throw new IllegalStateException();
            }
            item.setParent(node);
            if (item instanceof XBCNode) {
                nodeService.persistItem(item);
            } else {
                specService.persistItem(item);
            }
            ((XBEXNameService) nameService).setDefaultText(item, addDialog.getItemName());
            em.flush();
            transaction.commit();

            setNode(specsModel.getNode());
            selectSpecTableRow(item);
        }
    }//GEN-LAST:event_popupAddMenuItemActionPerformed

    private void popupRefreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupRefreshMenuItemActionPerformed
        Component invoker = catalogTreePopupMenu.getInvoker();
        if (invoker == catalogTree) {
            reloadNodesTree();
        } else {
            setNode((XBCNode) (currentItem == null || currentItem instanceof XBCNode ? currentItem : currentItem.getParent()));
        }
    }//GEN-LAST:event_popupRefreshMenuItemActionPerformed

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

        updateItem();
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

    @Override
    public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
        this.mainFrameManagement = mainFrameManagement;
        itemPanel.setMainFrameManagement(mainFrameManagement);
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
    private javax.swing.JMenuItem popupAddMenuItem;
    private javax.swing.JMenuItem popupEditMenuItem;
    private javax.swing.JMenuItem popupExportItemMenuItem;
    private javax.swing.JMenuItem popupImportItemMenuItem;
    private javax.swing.JMenuItem popupRefreshMenuItem;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        nodeService = catalog == null ? null : (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        specService = catalog == null ? null : (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        revService = catalog == null ? null : (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        nameService = catalog == null ? null : (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
        descService = catalog == null ? null : (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
        striService = catalog == null ? null : (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);

        reloadNodesTree();
        specsModel.setCatalog(catalog);
        catalogYaml.setCatalog(catalog);
        itemPanel.setCatalog(catalog);
    }

    private void selectSpecTableRow(XBCItem item) {
        int specRow = specsModel.getIndexOfItem(specsModel.new CatalogSpecTableItem(item));
        if (specRow >= 0) {
            catalogSpecListTable.setRowSelectionInterval(specRow, specRow);
            catalogSpecListTable.scrollRectToVisible(new Rectangle(catalogSpecListTable.getCellRect(specRow, 0, true)));
        }
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

        String itemIdentification = nameService.getDefaultText(currentItem);
        if (currentItem instanceof XBCNode) {
            itemIdentification = "node " + itemIdentification;
        } else {
            itemIdentification = "item " + itemIdentification;
        }
        int result = JOptionPane.showOptionDialog(this,
                "Are you sure you want to delete " + itemIdentification + "?",
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

    @Override
    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;
        menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 4);
    }

    private void reloadNodesTree() {
        nodesModel = new CatalogNodesTreeModel(catalog == null ? null : nodeService.getRootNode());
        nodesModel.setCatalog(catalog);
        catalogTree.setModel(nodesModel);
    }

    private void updateItem() {
        popupEditMenuItem.setEnabled(currentItem != null);
        popupExportItemMenuItem.setEnabled(currentItem != null);
        popupAddMenuItem.setEnabled(currentItem instanceof XBCNode);
        popupImportItemMenuItem.setEnabled(currentItem instanceof XBCNode);
    }
}
