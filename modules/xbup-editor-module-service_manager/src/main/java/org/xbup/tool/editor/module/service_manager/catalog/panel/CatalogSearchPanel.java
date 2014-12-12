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
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
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
 * @version 0.1.24 2014/12/12
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogSearchPanel extends javax.swing.JPanel implements ActivePanelActionHandling {

    private XBCItem currentItem;

    private XBACatalog catalog;
    private MainFrameManagement mainFrameManagement;
    private final CatalogItemsSearchPanel searchPanel;
    private final CatalogSpecsTableModel specsModel;
    private final CatalogItemPanel itemPanel;
    private final XBCatalogYaml catalogYaml;

    // Cached values
    private XBCNodeService nodeService;
    private XBCSpecService specService;
    private XBCXNameService nameService;
    private XBCXDescService descService;
    private XBCXStriService striService;

    private final Map<String, ActionListener> actionListenerMap = new HashMap<>();
    public static final String YAML_FILE_TYPE = "CatalogItemsTreePanel.YamlFileType";
    private MenuManagement menuManagement;

    public CatalogSearchPanel() {
        searchPanel = new CatalogItemsSearchPanel();
        specsModel = new CatalogSpecsTableModel();
        catalogYaml = new XBCatalogYaml();
        itemPanel = new CatalogItemPanel();

        initComponents();

        catalogItemSplitPane.setLeftComponent(searchPanel);
        catalogItemSplitPane.setRightComponent(itemPanel);

        searchPanel.setSelectionListener(new CatalogItemsSearchPanel.SelectionListener() {

            @Override
            public void selectedItem(XBCItem item) {
                itemPanel.setItem(item);
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
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popupExportItemMenuItem = new javax.swing.JMenuItem();
        catalogItemSplitPane = new javax.swing.JSplitPane();

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

        catalogItemSplitPane.setDividerLocation(180);
        catalogItemSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        catalogItemSplitPane.setName("catalogItemSplitPane"); // NOI18N
        add(catalogItemSplitPane, java.awt.BorderLayout.CENTER);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }//GEN-LAST:event_popupRefreshMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        JDialog dialog = new JDialog(new javax.swing.JFrame(), true);
        dialog.setSize(600, 400);
        dialog.getContentPane().add(new CatalogSearchPanel());
        WindowUtils.invokeWindow(dialog);
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
        itemPanel.setMainFrameManagement(mainFrameManagement);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane catalogItemSplitPane;
    private javax.swing.JPopupMenu catalogTreePopupMenu;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem popupExportItemMenuItem;
    private javax.swing.JMenuItem popupRefreshMenuItem;
    // End of variables declaration//GEN-END:variables

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        nodeService = catalog == null ? null : (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        specService = catalog == null ? null : (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        nameService = catalog == null ? null : (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
        descService = catalog == null ? null : (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
        striService = catalog == null ? null : (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);

        specsModel.setCatalog(catalog);
        catalogYaml.setCatalog(catalog);
        itemPanel.setCatalog(catalog);
        searchPanel.setCatalog(catalog);
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
        menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 4);
        searchPanel.setMenuManagement(menuManagement);
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