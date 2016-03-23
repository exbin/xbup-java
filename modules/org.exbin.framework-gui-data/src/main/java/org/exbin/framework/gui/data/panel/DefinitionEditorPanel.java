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
package org.exbin.framework.gui.data.panel;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.gui.component.api.EditItemActionsHandler;
import org.exbin.framework.gui.component.api.EditItemActionsUpdateListener;
import org.exbin.framework.gui.component.api.MoveItemActionsHandler;
import org.exbin.framework.gui.component.api.MoveItemActionsUpdateListener;
import org.exbin.framework.gui.component.panel.ToolBarEditorPanel;
import org.exbin.framework.gui.component.panel.ToolBarSidePanel;
import org.exbin.framework.gui.menu.api.ClipboardActionsHandler;
import org.exbin.framework.gui.menu.api.ClipboardActionsUpdateListener;
import org.exbin.framework.gui.undo.api.UndoActionsHandler;
import org.exbin.framework.gui.undo.api.UndoUpdateListener;
import org.exbin.xbup.catalog.entity.XBERev;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.catalog.entity.XBESpecDef;
import org.exbin.xbup.catalog.entity.service.XBEXDescService;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.catalog.entity.service.XBEXStriService;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;

/**
 * Data type definition editor panel.
 *
 * @version 0.2.0 2016/03/23
 * @author ExBin Project (http://exbin.org)
 */
public class DefinitionEditorPanel extends javax.swing.JPanel {

    private XBACatalog catalog;
    private XBCItem catalogItem;
    private XBCSpecService specService;
    private final CatalogDefsTableModel defsModel = new CatalogDefsTableModel();
    private final CatalogDefsDetailTableModel detailModel = new CatalogDefsDetailTableModel();
    private List<CatalogDefsTableItem> removeList;
    private List<CatalogDefsTableItem> updateList;

    private ToolBarEditorPanel toolBarEditorPanel;
    private ToolBarSidePanel definitionSidePanel;
    private final XBApplication application;

    public DefinitionEditorPanel(XBApplication application) {
        super();
        this.application = application;
        initComponents();
        init();
    }

    private void init() {
        toolBarEditorPanel = new ToolBarEditorPanel(application);
        add(toolBarEditorPanel, BorderLayout.CENTER);
        toolBarEditorPanel.add(definitionControlSplitPane, BorderLayout.CENTER);
        toolBarEditorPanel.setUndoHandler(new UndoActionsHandler() {
            @Override
            public Boolean canUndo() {
                return true;
            }

            @Override
            public Boolean canRedo() {
                return true;
            }

            @Override
            public void performUndo() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performRedo() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performUndoManager() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setUndoUpdateListener(UndoUpdateListener undoUpdateListener) {
            }
        });
        toolBarEditorPanel.setClipboardHandler(new ClipboardActionsHandler() {
            @Override
            public void performCut() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performCopy() {
                throw new UnsupportedOperationException("Not supported yet.");
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
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSelection() {
                return true;
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public boolean canSelectAll() {
                return true;
            }

            @Override
            public boolean canPaste() {
                return true;
            }

            @Override
            public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
            }
        });

        definitionSidePanel = new ToolBarSidePanel(application);
        definitionSidePanel.setEditItemsHandler(new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performEditItem() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performDeleteItem() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSelection() {
                return true;
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public void setUpdateListener(EditItemActionsUpdateListener updateListener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        definitionSidePanel.setMoveItemsHandler(new MoveItemActionsHandler() {
            @Override
            public void performMoveUp() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performMoveDown() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performMoveTop() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performMoveBottom() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSelection() {
                return true;
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public void setUpdateListener(MoveItemActionsUpdateListener updateListener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        definitionSidePanel.add(definitionScrollPane, BorderLayout.CENTER);
        definitionControlSplitPane.setLeftComponent(definitionSidePanel);
        definitionControlSplitPane.setRightComponent(propertiesScrollPanel);

        definitionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = definitionsTable.getSelectedRow();
                    detailModel.setItem(selectedRow >= 0 ? defsModel.getRowItem(selectedRow) : null);
                    propertiesTable.repaint();
                    updateItemStatus();
                }
            }
        });

        updateItemStatus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        definitionScrollPane = new javax.swing.JScrollPane();
        definitionsTable = new javax.swing.JTable();
        propertiesScrollPanel = new javax.swing.JScrollPane();
        propertiesTable = new javax.swing.JTable();
        definitionControlSplitPane = new javax.swing.JSplitPane();

        definitionsTable.setModel(defsModel);
        definitionsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        definitionScrollPane.setViewportView(definitionsTable);

        propertiesTable.setModel(detailModel);
        propertiesScrollPanel.setViewportView(propertiesTable);

        definitionControlSplitPane.setDividerLocation(400);

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane definitionControlSplitPane;
    private javax.swing.JScrollPane definitionScrollPane;
    private javax.swing.JTable definitionsTable;
    private javax.swing.JScrollPane propertiesScrollPanel;
    private javax.swing.JTable propertiesTable;
    // End of variables declaration//GEN-END:variables

    public void persist() {
        for (CatalogDefsTableItem defItem : updateList) {
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            XBCXDescService descService = (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
            XBCXStriService striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);

            XBESpecDef specDef = (XBESpecDef) defItem.getSpecDef();
            if (specDef != null && specDef.getType() != defItem.getDefType()) {
                specService.removeItem(specDef);
                specDef = null;
            }

            if (specDef == null) {
                specDef = (XBESpecDef) specService.createSpecDef((XBCSpec) catalogItem, defItem.getDefType());
                specDef.setCatalogItem((XBESpec) catalogItem);
            }

            specDef.setXBIndex(defItem.getXbIndex());
            specDef.setTarget((XBERev) defItem.getTarget());

            specService.persistItem(specDef);

            ((XBEXNameService) nameService).setDefaultText(specDef, defItem.getName());
            ((XBEXDescService) descService).setDefaultText(specDef, defItem.getDescription());
            ((XBEXStriService) striService).setItemStringIdText(specDef, defItem.getStringId());
        }

        for (CatalogDefsTableItem defItem : removeList) {
            if (defItem.getSpecDef() != null) {
                specService.removeItemDepth(defItem.getSpecDef());
            }
        }
    }

    private void updateItemStatus() {
//        int selectedRow = itemDefinitionsTable.getSelectedRow();
//        int rowsCount = defsModel.getRowCount();
//        if ((selectedRow >= 0) && (selectedRow < rowsCount)) {
//            moveUpDefButton.setEnabled(selectedRow > 0);
//            moveDownDefButton.setEnabled(selectedRow < rowsCount - 1);
//            modifyButton.setEnabled(true);
//            removeDefButton.setEnabled(true);
//        } else {
//            moveUpDefButton.setEnabled(false);
//            moveDownDefButton.setEnabled(false);
//            modifyButton.setEnabled(false);
//            removeDefButton.setEnabled(false);
//        }
        definitionsTable.repaint();
    }

    public void setCatalogItem(XBCItem catalogItem) {
        this.catalogItem = catalogItem;
//        addButton.setEnabled(!(catalogItem instanceof XBCNode));
        defsModel.setCatalogItem(catalogItem);
        updateList = new ArrayList<>();
        removeList = new ArrayList<>();
        updateItemStatus();
    }

    public XBCItem getCatalogItem() {
        return catalogItem;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        defsModel.setCatalog(catalog);
    }

    public CatalogDefsTableModel getDefsModel() {
        return defsModel;
    }
}
