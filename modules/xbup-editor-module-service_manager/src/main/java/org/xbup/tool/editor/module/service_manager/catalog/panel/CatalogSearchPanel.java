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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.DefaultCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultEditorKit;
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
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.yaml.XBCatalogYaml;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.FileType;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.MenuManagement;
import org.xbup.tool.editor.base.api.utils.WindowUtils;

/**
 * Catalog Specification Panel.
 *
 * @version 0.1.24 2014/12/11
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogSearchPanel extends javax.swing.JPanel implements ActivePanelActionHandling {

    private XBCItem currentItem;

    private XBACatalog catalog;
    private MainFrameManagement mainFrameManagement;
    private CatalogItemsTableModel itemsModel;
    private final CatalogSearchTableModel searchModel;
    private XBCatalogYaml catalogYaml;

    // Cached values
    private XBCNodeService nodeService;
    private XBCSpecService specService;
    private XBCXNameService nameService;
    private XBCXDescService descService;
    private XBCXStriService striService;

    private Map<String, ActionListener> actionListenerMap = new HashMap<>();
    public static final String YAML_FILE_TYPE = "CatalogItemsTreePanel.YamlFileType";
    private MenuManagement menuManagement;
    private CatalogSearchTableModel.CatalogSearchTableItem searchConditions = null;

    public CatalogSearchPanel() {
        itemsModel = new CatalogItemsTableModel();
        searchModel = new CatalogSearchTableModel();
        catalogYaml = new XBCatalogYaml();

        initComponents();

        DefaultCellEditor defaultCellEditor = new DefaultCellEditor(new JTextField());
        defaultCellEditor.setClickCountToStart(0);
        int columnCount = catalogSearchTable.getColumnModel().getColumnCount();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            TableColumn column = catalogSearchTable.getColumnModel().getColumn(columnIndex);
            column.setCellEditor(defaultCellEditor);
        }

        catalogItemsListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (catalogItemsListTable.getSelectedRow() >= 0) {
                        setItem(itemsModel.getItem(catalogItemsListTable.getSelectedRow()));
                    } else {
                        setItem(null);
                    }
                }
            }
        });

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
        popupRefreshMenuItem = new javax.swing.JMenuItem();
        popupEditMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popupExportItemMenuItem = new javax.swing.JMenuItem();
        searchPanel = new javax.swing.JPanel();
        catalogSearchScrollPane = new javax.swing.JScrollPane();
        catalogSearchTable = new javax.swing.JTable();
        searchButton = new javax.swing.JButton();
        catalogItemListScrollPane = new javax.swing.JScrollPane();
        catalogItemsListTable = new javax.swing.JTable();

        catalogTreePopupMenu.setName("catalogTreePopupMenu"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/service_manager/catalog/panel/resources/CatalogItemsTreePanel"); // NOI18N
        popupRefreshMenuItem.setText(bundle.getString("refreshMenuItem,text")); // NOI18N
        popupRefreshMenuItem.setToolTipText(bundle.getString("refreshMenuItem,toolTipText")); // NOI18N
        popupRefreshMenuItem.setName("popupRefreshMenuItem"); // NOI18N
        popupRefreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupRefreshMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupRefreshMenuItem);

        popupEditMenuItem.setText(bundle.getString("editMenuItem.text")); // NOI18N
        popupEditMenuItem.setToolTipText(bundle.getString("editMenuItem,toolTipText")); // NOI18N
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

        searchPanel.setName("searchPanel"); // NOI18N

        catalogSearchScrollPane.setComponentPopupMenu(catalogTreePopupMenu);
        catalogSearchScrollPane.setName("catalogSearchScrollPane"); // NOI18N

        catalogSearchTable.setModel(searchModel);
        catalogSearchTable.setComponentPopupMenu(catalogTreePopupMenu);
        catalogSearchTable.setName("catalogSearchTable"); // NOI18N
        catalogSearchScrollPane.setViewportView(catalogSearchTable);

        searchButton.setText("Search");
        searchButton.setName("searchButton"); // NOI18N
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addGap(0, 409, Short.MAX_VALUE)
                .addComponent(searchButton)
                .addContainerGap())
            .addComponent(catalogSearchScrollPane)
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addComponent(catalogSearchScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchButton)
                .addContainerGap())
        );

        add(searchPanel, java.awt.BorderLayout.NORTH);

        catalogItemListScrollPane.setComponentPopupMenu(catalogTreePopupMenu);
        catalogItemListScrollPane.setName("catalogItemListScrollPane"); // NOI18N

        catalogItemsListTable.setModel(itemsModel);
        catalogItemsListTable.setComponentPopupMenu(catalogTreePopupMenu);
        catalogItemsListTable.setName("catalogItemsListTable"); // NOI18N
        catalogItemListScrollPane.setViewportView(catalogItemsListTable);

        add(catalogItemListScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void popupExportItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupExportItemMenuItemActionPerformed
        if (currentItem != null) {
            JFileChooser exportFileChooser = new JFileChooser();
            exportFileChooser.addChoosableFileFilter(new YamlFileType());
            exportFileChooser.setAcceptAllFileFilterUsed(true);
            if (exportFileChooser.showSaveDialog(WindowUtils.getFrame(this)) == JFileChooser.APPROVE_OPTION) {
                FileWriter fileWriter;
                try {
                    fileWriter = new FileWriter(exportFileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        catalogYaml.exportCatalogItem(currentItem, fileWriter);
                    } finally {
                        fileWriter.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CatalogSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CatalogSearchPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_popupExportItemMenuItemActionPerformed

    private void popupRefreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupRefreshMenuItemActionPerformed
        Component invoker = catalogTreePopupMenu.getInvoker();
        reload();
    }//GEN-LAST:event_popupRefreshMenuItemActionPerformed

    private void popupEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupEditMenuItemActionPerformed
        throw new UnsupportedOperationException("Not supported yet.");
//        if (currentItem != null) {
//            CatalogEditItemDialog editDialog = new CatalogEditItemDialog(WindowUtils.getFrame(this), true);
//            editDialog.setMenuManagement(menuManagement);
//            editDialog.setCatalog(catalog);
//            editDialog.setCatalogItem(currentItem);
//            editDialog.setVisible(true);
//
//            if (editDialog.getDialogOption() == JOptionPane.OK_OPTION) {
//                EntityManager em = ((XBECatalog) catalog).getEntityManager();
//                EntityTransaction transaction = em.getTransaction();
//                transaction.begin();
//                editDialog.persist();
//                setItem(currentItem);
//                em.flush();
//                transaction.commit();
//                specsModel.setNode(specsModel.getNode());
//                selectSpecTableRow(currentItem);
//            }
//        }
    }//GEN-LAST:event_popupEditMenuItemActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        searchConditions = searchModel.getSearchConditions();
        reload();
    }//GEN-LAST:event_searchButtonActionPerformed

    public void setItem(XBCItem item) {
        currentItem = item;

        if (mainFrameManagement != null) {
            updateActionStatus(mainFrameManagement.getLastFocusOwner());
        }
    }

    @Override
    public boolean updateActionStatus(Component component) {
//        if (component == catalogTree) {
//            // clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
//            if (mainFrameManagement != null) {
//                mainFrameManagement.getCutAction().setEnabled(currentItem != null);
//                mainFrameManagement.getCopyAction().setEnabled(currentItem != null);
//                mainFrameManagement.getPasteAction().setEnabled(false); // TODO clipboard.isDataFlavorAvailable(xbFlavor));
//                mainFrameManagement.getDeleteAction().setEnabled(currentItem != null);
//                mainFrameManagement.getSelectAllAction().setEnabled(false);
//            }
//
//            // frameManagement.getUndoAction().setEnabled(treeUndo.canUndo());
//            // frameManagement.getRedoAction().setEnabled(treeUndo.canRedo());
//            return true;
//        }

        return false;
    }

    public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
        this.mainFrameManagement = mainFrameManagement;
    }

    private void reload() {
        itemsModel.performSearch(searchConditions);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane catalogItemListScrollPane;
    private javax.swing.JTable catalogItemsListTable;
    private javax.swing.JScrollPane catalogSearchScrollPane;
    private javax.swing.JTable catalogSearchTable;
    private javax.swing.JPopupMenu catalogTreePopupMenu;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem popupEditMenuItem;
    private javax.swing.JMenuItem popupExportItemMenuItem;
    private javax.swing.JMenuItem popupRefreshMenuItem;
    private javax.swing.JButton searchButton;
    private javax.swing.JPanel searchPanel;
    // End of variables declaration//GEN-END:variables

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        nodeService = catalog == null ? null : (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        specService = catalog == null ? null : (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        nameService = catalog == null ? null : (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
        descService = catalog == null ? null : (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
        striService = catalog == null ? null : (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);

        itemsModel.setCatalog(catalog);
        catalogYaml.setCatalog(catalog);
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

            reload();
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

    /**
     * Gets the extension part of file name.
     *
     * @param file Source file
     * @return extension part of file name
     */
    public static String getExtension(File file) {
        String ext = null;
        String str = file.getName();
        int i = str.lastIndexOf('.');

        if (i > 0 && i < str.length() - 1) {
            ext = str.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;
        menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 3);
    }

    public class YamlFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.length() < 3) {
                    return false;
                }

                return extension.length() >= 4 && "yaml".contains(extension.substring(0, 4));
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "YAML File (*.yaml)";
        }

        @Override
        public String getFileTypeId() {
            return YAML_FILE_TYPE;
        }
    }
}
