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
import java.awt.HeadlessException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCXBlockLine;
import org.xbup.lib.core.catalog.base.XBCXPlugLine;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.parser_tree.XBATreeParamExtractor;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.plugin.XBLineEditor;
import org.xbup.lib.plugin.XBPlugin;
import org.xbup.lib.plugin.XBPluginRepository;
import org.xbup.tool.editor.base.api.utils.WindowUtils;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.BlockPropertiesDialog;

/**
 * Panel for properties of the actual panel.
 *
 * @version 0.1.24 2015/01/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBPropertyTablePanel extends javax.swing.JPanel {

    private XBACatalog catalog;
    private XBDocumentPanel activePanel;
    private final XBPropertyTableModel tableModel;
    private final XBPropertyTableCellRenderer valueCellRenderer;
    private final TableCellRenderer nameCellRenderer;
    private final XBPropertyTableCellEditor valueCellEditor;
    private XBCXLineService lineService = null;
    private XBPluginRepository pluginRepository = null;

    private Thread propertyThread;
    private final Semaphore valueFillingSemaphore;
    private XBTTreeNode node;

    public XBPropertyTablePanel(XBACatalog catalog) {
        this.catalog = catalog;
        tableModel = new XBPropertyTableModel();

        initComponents();

        TableColumnModel columns = propertiesTable.getColumnModel();
        columns.getColumn(0).setPreferredWidth(190);
        columns.getColumn(1).setPreferredWidth(190);
        columns.getColumn(0).setWidth(190);
        columns.getColumn(1).setWidth(190);
        nameCellRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JComponent component = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                XBPropertyTableItem tableItem = ((XBPropertyTableModel) table.getModel()).getRow(row);
                component.setToolTipText("(" + tableItem.getDefTypeName() + ") " + tableItem.getValueName());
                return component;
            }

        };
        columns.getColumn(0).setCellRenderer(nameCellRenderer);
        valueCellRenderer = new XBPropertyTableCellRenderer(catalog, pluginRepository, null, null);
        columns.getColumn(1).setCellRenderer(valueCellRenderer);
        valueCellEditor = new XBPropertyTableCellEditor(catalog, pluginRepository, null, null);
        columns.getColumn(1).setCellEditor(valueCellEditor);

        propertiesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (component instanceof JComponent) {
                    ((JComponent) component).setBorder(noFocusBorder);
                }

                return component;
            }
        });

        propertyThread = null;
        valueFillingSemaphore = new Semaphore(1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        propertyPopupMenu = new javax.swing.JPopupMenu();
        popupItemOpenMenuItem = new javax.swing.JMenuItem();
        popupItemAddMenuItem = new javax.swing.JMenuItem();
        popupItemModifyMenuItem = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        popupUndoMenuItem = new javax.swing.JMenuItem();
        popupRedoMenuItem = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        popupCutMenuItem = new javax.swing.JMenuItem();
        popupCopyMenuItem = new javax.swing.JMenuItem();
        popupPasteMenuItem = new javax.swing.JMenuItem();
        popupDeleteMenuItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        popupSelectAllMenuItem = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        popupItemPropertiesMenuItem = new javax.swing.JMenuItem();
        mainScrollPane = new javax.swing.JScrollPane();
        propertiesTable = new javax.swing.JTable();

        propertyPopupMenu.setName("propertyPopupMenu"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/xbdoc_editor/panel/resources/XBPropertyTablePanel"); // NOI18N
        popupItemOpenMenuItem.setText(bundle.getString("popupItemOpenMenuItem.text")); // NOI18N
        popupItemOpenMenuItem.setToolTipText(bundle.getString("popupItemOpenMenuItem.toolTipText")); // NOI18N
        popupItemOpenMenuItem.setEnabled(false);
        popupItemOpenMenuItem.setName("popupItemOpenMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemOpenMenuItem);

        popupItemAddMenuItem.setText(bundle.getString("popupItemAddMenuItem.text")); // NOI18N
        popupItemAddMenuItem.setToolTipText(bundle.getString("popupItemAddMenuItem.toolTipText")); // NOI18N
        popupItemAddMenuItem.setEnabled(false);
        popupItemAddMenuItem.setName("popupItemAddMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemAddMenuItem);

        popupItemModifyMenuItem.setText(bundle.getString("popupItemModifyMenuItem.text")); // NOI18N
        popupItemModifyMenuItem.setToolTipText(bundle.getString("popupItemModifyMenuItem.toolTipText")); // NOI18N
        popupItemModifyMenuItem.setEnabled(false);
        popupItemModifyMenuItem.setName("popupItemModifyMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemModifyMenuItem);

        jSeparator14.setName("jSeparator14"); // NOI18N
        propertyPopupMenu.add(jSeparator14);

        popupUndoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        popupUndoMenuItem.setText(bundle.getString("popupUndoMenuItem.text")); // NOI18N
        popupUndoMenuItem.setToolTipText(bundle.getString("popupUndoMenuItem.toolTipText")); // NOI18N
        popupUndoMenuItem.setEnabled(false);
        popupUndoMenuItem.setName("popupUndoMenuItem"); // NOI18N
        propertyPopupMenu.add(popupUndoMenuItem);

        popupRedoMenuItem.setText(bundle.getString("popupRedoMenuItem.text")); // NOI18N
        popupRedoMenuItem.setToolTipText(bundle.getString("popupRedoMenuItem.toolTipText")); // NOI18N
        popupRedoMenuItem.setEnabled(false);
        popupRedoMenuItem.setName("popupRedoMenuItem"); // NOI18N
        propertyPopupMenu.add(popupRedoMenuItem);

        jSeparator10.setName("jSeparator10"); // NOI18N
        propertyPopupMenu.add(jSeparator10);

        popupCutMenuItem.setText("Cut"); // NOI18N
        popupCutMenuItem.setEnabled(false);
        popupCutMenuItem.setName("popupCutMenuItem"); // NOI18N
        propertyPopupMenu.add(popupCutMenuItem);

        popupCopyMenuItem.setText("Copy"); // NOI18N
        popupCopyMenuItem.setEnabled(false);
        popupCopyMenuItem.setName("popupCopyMenuItem"); // NOI18N
        propertyPopupMenu.add(popupCopyMenuItem);

        popupPasteMenuItem.setText("Paste"); // NOI18N
        popupPasteMenuItem.setEnabled(false);
        popupPasteMenuItem.setName("popupPasteMenuItem"); // NOI18N
        propertyPopupMenu.add(popupPasteMenuItem);

        popupDeleteMenuItem.setText("Delete"); // NOI18N
        popupDeleteMenuItem.setEnabled(false);
        popupDeleteMenuItem.setName("popupDeleteMenuItem"); // NOI18N
        propertyPopupMenu.add(popupDeleteMenuItem);

        jSeparator7.setName("jSeparator7"); // NOI18N
        propertyPopupMenu.add(jSeparator7);

        popupSelectAllMenuItem.setText(bundle.getString("popupSelectAllMenuItem.text")); // NOI18N
        popupSelectAllMenuItem.setToolTipText(bundle.getString("popupSelectAllMenuItem.toolTipText")); // NOI18N
        popupSelectAllMenuItem.setEnabled(false);
        popupSelectAllMenuItem.setName("popupSelectAllMenuItem"); // NOI18N
        propertyPopupMenu.add(popupSelectAllMenuItem);

        jSeparator16.setName("jSeparator16"); // NOI18N
        propertyPopupMenu.add(jSeparator16);

        popupItemPropertiesMenuItem.setText(bundle.getString("popupItemPropertiesMenuItem.text")); // NOI18N
        popupItemPropertiesMenuItem.setToolTipText(bundle.getString("popupItemPropertiesMenuItem.toolTipText")); // NOI18N
        popupItemPropertiesMenuItem.setEnabled(false);
        popupItemPropertiesMenuItem.setName("popupItemPropertiesMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemPropertiesMenuItem);

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        mainScrollPane.setName("mainScrollPane"); // NOI18N

        propertiesTable.setModel(tableModel);
        propertiesTable.setComponentPopupMenu(propertyPopupMenu);
        propertiesTable.setName("propertiesTable"); // NOI18N
        propertiesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        mainScrollPane.setViewportView(propertiesTable);

        add(mainScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JMenuItem popupCopyMenuItem;
    private javax.swing.JMenuItem popupCutMenuItem;
    private javax.swing.JMenuItem popupDeleteMenuItem;
    private javax.swing.JMenuItem popupItemAddMenuItem;
    private javax.swing.JMenuItem popupItemModifyMenuItem;
    private javax.swing.JMenuItem popupItemOpenMenuItem;
    private javax.swing.JMenuItem popupItemPropertiesMenuItem;
    private javax.swing.JMenuItem popupPasteMenuItem;
    private javax.swing.JMenuItem popupRedoMenuItem;
    private javax.swing.JMenuItem popupSelectAllMenuItem;
    private javax.swing.JMenuItem popupUndoMenuItem;
    private javax.swing.JTable propertiesTable;
    private javax.swing.JPopupMenu propertyPopupMenu;
    // End of variables declaration//GEN-END:variables

    public void setActiveNode(XBTTreeNode node) {
        this.node = node;
        valueCellRenderer.setNode(node);
        valueCellEditor.setNode(node);
        new PropertyThread(this, node).start();
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        lineService = catalog == null ? null : (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);

        valueCellRenderer.setCatalog(catalog);
        valueCellEditor.setCatalog(catalog);
    }

    public XBDocumentPanel getActivePanel() {
        return activePanel;
    }

    public void setActivePanel(XBDocumentPanel activePanel) {
        this.activePanel = activePanel;
    }

    private Thread getPropertyThread() {
        return propertyThread;
    }

    private void setPropertyThread(Thread propertyThread) {
        this.propertyThread = propertyThread;
    }

    private Semaphore getValueFillingSemaphore() {
        return valueFillingSemaphore;
    }

    // TODO: Prepare values and then fill it to property panel
    private class PropertyThread extends Thread {

        private final XBPropertyTablePanel propertyPanel;
        private final XBTTreeNode node;

        public PropertyThread(XBPropertyTablePanel propertyPanel, XBTTreeNode node) {
            super();
            this.propertyPanel = propertyPanel;
            this.node = node;
        }

        @Override
        public void start() {
            super.start();
            try {
                getValueFillingSemaphore().acquire();
                for (int rowIndex = tableModel.getRowCount() - 1; rowIndex >= 0; rowIndex--) {
                    tableModel.removeRow(rowIndex);
                }
                propertyPanel.setPropertyThread(this);
                getValueFillingSemaphore().release();
                fillPanel();
            } catch (InterruptedException ex) {
                Logger.getLogger(XBPropertyTablePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void fillPanel() {
            if (node == null) {
                return;
            }
            XBBlockType type = node.getBlockType();
            XBBlockDecl decl = node.getBlockDecl();
            if (propertyThread != this) {
                return;
            }
            XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
            if (decl instanceof XBCBlockDecl) {
                XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
                XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpec().getParent();
                if (propertyThread != this) {
                    return;
                }
                if (spec != null) {
                    if (propertyThread != this) {
                        return;
                    }
                    long bindCount = specService.getSpecDefsCount(spec);
                    try {
                        getValueFillingSemaphore().acquire();
                        XBATreeParamExtractor paramExtractor = new XBATreeParamExtractor(node, catalog);

                        if (propertyPanel.getPropertyThread() == this) {
                            for (int parameterIndex = 0; parameterIndex < bindCount; parameterIndex++) {
                                String rowNameText = "";
                                String typeNameText = "";
                                XBPropertyTableItem row;
                                XBCSpecDef def = specService.getSpecDefByOrder(spec, parameterIndex);
                                XBCBlockSpec rowSpec;
                                XBLineEditor lineEditor = null;
                                if (def != null) {
                                    try {
                                        rowNameText = nameService.getDefaultText(def);
                                        XBCBlockRev rowRev = (XBCBlockRev) def.getTarget();
                                        if (rowRev != null) {
                                            rowSpec = rowRev.getParent();
                                            typeNameText = nameService.getDefaultText(rowSpec);
                                            if (rowNameText.isEmpty()) {
                                                rowNameText = typeNameText;
                                            }

                                            lineEditor = getCustomEditor(rowRev);
                                            if (lineEditor != null) {
                                                paramExtractor.setParameterIndex(parameterIndex);
                                                XBPSerialReader serialReader = new XBPSerialReader(paramExtractor);
                                                serialReader.read(lineEditor);

                                                lineEditor.attachChangeListener(new LineEditorChangeListener(lineEditor, paramExtractor, parameterIndex));
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Logger.getLogger(XBPropertyTablePanel.class.getName()).log(Level.SEVERE, null, ex);
                                        JOptionPane.showMessageDialog(propertyPanel, ex.getMessage(), "Exception in property panel", JOptionPane.ERROR_MESSAGE);
                                    }
                                }

                                row = new XBPropertyTableItem(def, rowNameText, typeNameText, lineEditor);
                                tableModel.addRow(row);
                            }
                        }
                        getValueFillingSemaphore().release();
                    } catch (InterruptedException | HeadlessException ex) {
                        Logger.getLogger(XBPropertyTablePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private XBLineEditor getCustomEditor(XBCBlockRev rev) {
        if (rev == null || catalog == null) {
            return null;
        }

        XBCXBlockLine blockLine = lineService.findLineByPR(rev, 0);
        if (blockLine == null) {
            return null;
        }
        XBCXPlugLine plugLine = blockLine.getLine();
        if (plugLine == null) {
            return null;
        }
        XBCXPlugin plugin = plugLine.getPlugin();
        XBPlugin pluginHandler;

        pluginHandler = pluginRepository.getPluginHandler(plugin);
        if (pluginHandler == null) {
            return null;
        }

        return pluginHandler.getLineEditor(plugLine.getLineIndex());
    }

    public void actionEditUndo() {
        activePanel.performUndo();
        activePanel.getDoc().processSpec();
        firePropertyChange("undoAvailable", false, true);
        firePropertyChange("redoAvailable", false, true);
    }

    public void actionEditRedo() {
        activePanel.performRedo();
        activePanel.getDoc().processSpec();
        firePropertyChange("undoAvailable", false, true);
        firePropertyChange("redoAvailable", false, true);
    }

    public void actionEditSelectAll() {
        activePanel.performSelectAll();
    }

    public void actionItemAdd() {
        activePanel.performAdd();
    }

    public void actionItemModify() {
        activePanel.performModify();
    }

    public void actionItemProperties() {
        BlockPropertiesDialog dialog = new BlockPropertiesDialog(WindowUtils.getFrame(this), true);
        dialog.setCatalog(catalog);
        dialog.runDialog(activePanel.getSelectedItem());
    }

    public void actionItemOpen() {
    }

    public boolean isStub1Enabled() {
        return activePanel.getMode() == XBDocumentPanel.PanelMode.TEXT;
    }

    private boolean addEnabled = false;

    public boolean isAddEnabled() {
        return addEnabled;
    }

    public void setAddEnabled(boolean b) {
        boolean old = isAddEnabled();
        this.addEnabled = b;
        firePropertyChange("addEnabled", old, isAddEnabled());
    }

    private boolean undoAvailable = false;

    public boolean isUndoAvailable() {
        return undoAvailable;
    }

    public void setUndoAvailable(boolean b) {
        boolean old = isUndoAvailable();
        this.undoAvailable = b;
        firePropertyChange("undoAvailable", old, isUndoAvailable());
    }

    public boolean isRedoAvailable() {
        return false; //activePanel.canRedo();
    }

    public boolean isEditEnabled() {
        if (activePanel == null) {
            return false;
        }
        return activePanel.isEditEnabled() && (activePanel.getMode() == XBDocumentPanel.PanelMode.TREE);
    }

    public boolean isPasteEnabled() {
        if (activePanel == null) {
            return false;
        }
        return activePanel.isPasteEnabled();
    }

    public boolean isFalse() {
        return false;
    }

    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    private class LineEditorChangeListener implements XBLineEditor.ChangeListener {

        private final XBATreeParamExtractor paramExtractor;
        private final int parameterIndex;
        private final XBLineEditor lineEditor;

        private LineEditorChangeListener(XBLineEditor lineEditor, XBATreeParamExtractor paramExtractor, int parameterIndex) {
            this.lineEditor = lineEditor;
            this.paramExtractor = paramExtractor;
            this.parameterIndex = parameterIndex;
        }

        @Override
        public void valueChanged() {
            paramExtractor.setParameterIndex(parameterIndex);
            XBPSerialWriter serialWriter = new XBPSerialWriter(paramExtractor);
            serialWriter.write(lineEditor);
        }
    }
}
