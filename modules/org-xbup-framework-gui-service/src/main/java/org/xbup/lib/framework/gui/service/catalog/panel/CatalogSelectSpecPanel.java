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
package org.xbup.lib.framework.gui.service.catalog.panel;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;

/**
 * XBManager Catalog Specification Selection Panel.
 *
 * @version 0.1.24 2014/12/12
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogSelectSpecPanel extends javax.swing.JPanel {

    private XBCXNameService nameService = null;
    private CatalogSelectSpecTreeModel treeModel;
    private SelectionListener selectionListener;
    private XBCItem selectedItem;
    private final CatalogSpecItemType specType;

    public CatalogSelectSpecPanel(final CatalogSpecItemType specType) {
        this.specType = specType;
        treeModel = new CatalogSelectSpecTreeModel(null, specType);
        selectedItem = null;

        initComponents();

        specSelectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        specSelectTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                DefaultTreeCellRenderer retValue = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof XBCNode) {
                    XBCNode node = (XBCNode) value;
                    if (nameService != null) {
                        XBCXName name = nameService.getDefaultItemName(node);
                        if (name == null) {
                            retValue.setText("unknown name");
                        } else {
                            retValue.setText(name.getText());
                        }
                    } else {
                        retValue.setText("node");
                    }
                } else if (value instanceof XBCSpec) {
                    XBCSpec spec = (XBCSpec) value;
                    if (nameService != null) {
                        XBCXName name = nameService.getDefaultItemName(spec);
                        if (name == null) {
                            retValue.setText("unknown name");
                        } else {
                            retValue.setText(name.getText());
                        }
                    } else {
                        retValue.setText("node");
                    }
                }

                return retValue;
            }
        });

        specSelectTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                XBCItem item = (XBCItem) specSelectTree.getLastSelectedPathComponent();
                if ((item instanceof XBCNode) && (specType != CatalogSpecItemType.NODE)) {
                    item = null;
                }
                if (item == null) {
                    if (selectedItem != null) {
                        selectedItem = null;
                        selectionListener.selectedItem(selectedItem);
                    }
                } else {
                    if (selectedItem != item) {
                        selectedItem = item;
                        selectionListener.selectedItem(selectedItem);
                    }
                }
            }
        });
    }

    public void setCatalog(XBACatalog catalog) {
        treeModel = new CatalogSelectSpecTreeModel(catalog, specType);
        specSelectTree.setModel(treeModel);
        nameService = catalog == null ? null : (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        specSelectTree = new javax.swing.JTree();

        specSelectTree.setModel(treeModel);
        jScrollPane1.setViewportView(specSelectTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree specSelectTree;
    // End of variables declaration//GEN-END:variables

    public XBCItem getSpec() {
        return (XBCItem) specSelectTree.getLastSelectedPathComponent();
    }

    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public interface SelectionListener {

        void selectedItem(XBCItem item);
    }
}
