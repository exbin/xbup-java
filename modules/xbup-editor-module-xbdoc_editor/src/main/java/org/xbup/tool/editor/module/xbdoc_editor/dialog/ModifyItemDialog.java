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
package org.xbup.tool.editor.module.xbdoc_editor.dialog;

import hexedit.HexEditPanel;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCXBlockLine;
import org.xbup.lib.core.catalog.base.XBCXBlockPane;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.XBCXPlugLine;
import org.xbup.lib.core.catalog.base.XBCXPlugPane;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.service.XBCXPaneService;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.parser_tree.XBTTreeDocument;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.plugin.XBLineEditor;
import org.xbup.lib.plugin.XBPanelEditor;
import org.xbup.lib.plugin.XBPlugin;
import org.xbup.lib.plugin.XBPluginRepository;
import org.xbup.tool.editor.base.api.utils.WindowUtils;

/**
 * Dialog for modifying item attributes or data.
 *
 * @version 0.1.24 2015/01/09
 * @author XBUP Project (http://xbup.org)
 */
public class ModifyItemDialog extends javax.swing.JDialog {

    private final AttributesTableModel tableModel = new AttributesTableModel();
    private final ParametersTableModel parametersTableModel = new ParametersTableModel();
    private XBTTreeNode srcNode;
    private XBTTreeNode newNode = null;
    private final HexEditPanel hexPanel;
    private JPanel customPanel;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;

    private XBBlockDataMode dataMode = XBBlockDataMode.NODE_BLOCK;
    private List<UBNatural> attributes;

    private final String attributesEditorPanelTitle;
    private final String dataEditorPanelTitle;
    private final String paramEditorPanelTitle;
    private int dialogOption = JOptionPane.CLOSED_OPTION;

    public ModifyItemDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        hexPanel = new HexEditPanel((JFrame) parent);
        customPanel = null;
        hexEditPanel.add(hexPanel);

        attributesEditorPanelTitle = mainTabbedPane.getTitleAt(mainTabbedPane.indexOfComponent(attributesEditorPanel));
        dataEditorPanelTitle = mainTabbedPane.getTitleAt(mainTabbedPane.indexOfComponent(dataEditorPanel));
        paramEditorPanelTitle = mainTabbedPane.getTitleAt(mainTabbedPane.indexOfComponent(paramEditorPanel));

        attributesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateAttributesButtons();
            }
        });
        /*attributesTable.getCellEditor().addCellEditorListener(new CellEditorListener() {

         @Override
         public void editingStopped(ChangeEvent e) {
         }

         @Override
         public void editingCanceled(ChangeEvent e) {
         attributesTable.setValueAt(((DefaultCellEditor) e.getSource()).getCellEditorValue(), attributesTable.getSelectedRow(), attributesTable.getSelectedColumn());
         }
         }); */

        int parametersTableWidth = parametersTable.getWidth();
        parametersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        parametersTable.getColumnModel().getColumn(0).setPreferredWidth(parametersTableWidth / 6);
        parametersTable.getColumnModel().getColumn(1).setPreferredWidth(parametersTableWidth / 6);
        parametersTable.getColumnModel().getColumn(2).setPreferredWidth(parametersTableWidth / 6);
        parametersTable.getColumnModel().getColumn(3).setPreferredWidth(parametersTableWidth / 2);

        init();
    }

    private void init() {
        WindowUtils.initWindow(this);
        WindowUtils.assignGlobalKeyListener(this, okButton, cancelButton);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();
        paramEditorPanel = new javax.swing.JPanel();
        parametersScrollPane = new javax.swing.JScrollPane();
        parametersTable = new javax.swing.JTable();
        attributesEditorPanel = new javax.swing.JPanel();
        attributesScrollPane = new javax.swing.JScrollPane();
        attributesTable = new JTable(tableModel) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                boolean result = super.editCellAt(row, column, e);
                final Component editor = getEditorComponent();
                if (editor == null || !(editor instanceof JTextComponent)) {
                    return result;
                }
                if (e instanceof MouseEvent) {
                    EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            ((JTextComponent) editor).selectAll();
                        }

                    });
                } else {
                    ((JTextComponent) editor).selectAll();
                }
                return result;
            }
        };
        removeButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        dataEditorPanel = new javax.swing.JPanel();
        saveAsButton = new javax.swing.JButton();
        loadFromButton = new javax.swing.JButton();
        hexEditPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/xbdoc_editor/dialog/resources/ItemModifyDialog"); // NOI18N
        setTitle(bundle.getString("title")); // NOI18N
        setLocationByPlatform(true);
        setModal(true);

        parametersTable.setModel(parametersTableModel);
        parametersScrollPane.setViewportView(parametersTable);

        org.jdesktop.layout.GroupLayout paramEditorPanelLayout = new org.jdesktop.layout.GroupLayout(paramEditorPanel);
        paramEditorPanel.setLayout(paramEditorPanelLayout);
        paramEditorPanelLayout.setHorizontalGroup(
            paramEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(paramEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(parametersScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                .addContainerGap())
        );
        paramEditorPanelLayout.setVerticalGroup(
            paramEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(paramEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(parametersScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTabbedPane.addTab(bundle.getString("paramEditorPanel.title"), null, paramEditorPanel, "List of parameters on level 1"); // NOI18N

        attributesTable.setModel(tableModel);
        attributesTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                attributesTablePropertyChange(evt);
            }
        });
        attributesScrollPane.setViewportView(attributesTable);
        attributesTable.getAccessibleContext().setAccessibleName("");

        removeButton.setText(bundle.getString("removeButton.text")); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        addButton.setText(bundle.getString("addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout attributesEditorPanelLayout = new org.jdesktop.layout.GroupLayout(attributesEditorPanel);
        attributesEditorPanel.setLayout(attributesEditorPanelLayout);
        attributesEditorPanelLayout.setHorizontalGroup(
            attributesEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(attributesEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(attributesEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(attributesScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                    .add(attributesEditorPanelLayout.createSequentialGroup()
                        .add(addButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeButton)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        attributesEditorPanelLayout.setVerticalGroup(
            attributesEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(attributesEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(attributesScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(attributesEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addButton)
                    .add(removeButton))
                .addContainerGap())
        );

        mainTabbedPane.addTab(bundle.getString("attributesEditorPanel.title"), null, attributesEditorPanel, "List of attributes on level 0"); // NOI18N

        saveAsButton.setText(bundle.getString("saveAsButton.text")); // NOI18N
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });

        loadFromButton.setText(bundle.getString("loadFromButton.text")); // NOI18N
        loadFromButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFromButtonActionPerformed(evt);
            }
        });

        hexEditPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        hexEditPanel.setLayout(new java.awt.BorderLayout());

        org.jdesktop.layout.GroupLayout dataEditorPanelLayout = new org.jdesktop.layout.GroupLayout(dataEditorPanel);
        dataEditorPanel.setLayout(dataEditorPanelLayout);
        dataEditorPanelLayout.setHorizontalGroup(
            dataEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dataEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(dataEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(hexEditPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .add(dataEditorPanelLayout.createSequentialGroup()
                        .add(loadFromButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(saveAsButton)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dataEditorPanelLayout.setVerticalGroup(
            dataEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dataEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(hexEditPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dataEditorPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(saveAsButton)
                    .add(loadFromButton))
                .addContainerGap())
        );

        mainTabbedPane.addTab("Data (Level 0)", dataEditorPanel);

        cancelButton.setText(bundle.getString("cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText(bundle.getString("okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(okButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(cancelButton)
                .addContainerGap())
            .add(mainTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(mainTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        attributes.add(new UBNat32());
        tableModel.fireTableDataChanged();
//        tableModel.fireTableRowsInserted(tableModel.getParameters().size()-1,tableModel.getParameters().size());
        attributesTable.revalidate();
        updateAttributesButtons();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int[] selectedRows = attributesTable.getSelectedRows();
        if (selectedRows.length > 0) {
            Arrays.sort(selectedRows);
            for (int index = selectedRows.length - 1; index >= 0; index--) {
                attributes.remove(selectedRows[index]);
            }

            tableModel.fireTableDataChanged();
            attributesTable.clearSelection();
            attributesTable.revalidate();
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (attributesTable.getCellEditor() != null) {
            attributesTable.getCellEditor().stopCellEditing();
        }

        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                HexEditPanel.saveToStream(stream);
            } catch (IOException ex) {
                Logger.getLogger(ModifyItemDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

            newNode = srcNode.cloneNode();
            newNode.setData(new ByteArrayInputStream(stream.toByteArray()));
        } else {
            if (customPanel != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                XBTTreeDocument doc = new XBTTreeDocument(catalog);
                try {
                    // TODO: Do proper loading later
                    XBTChildOutputSerialHandler handler = new XBTChildListenerSerialHandler();
                    handler.attachXBTEventListener(new XBTToXBEventConvertor(new XBEventWriter(stream)));
                    ((XBTChildSerializable) customPanel).serializeToXB(handler);
                    doc.fromStreamUB(new ByteArrayInputStream(stream.toByteArray()));

                    throw new UnsupportedOperationException("Not supported yet.");
                    // node = (XBTTreeNode) doc.getRootBlock();
                    // TODO: Patching node type, should be handled by context later
                    // node.setAttribute(srcNode.getParameter(0), 0);
                    // node.setAttribute(srcNode.getParameter(1), 1);
                } catch (IOException | XBProcessingException ex) {
                    Logger.getLogger(ModifyItemDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (attributes.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "There must be at least one attribute", "Attribute Needed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                newNode = srcNode.cloneNode();
                newNode.setAttributesCount(0);

                UBNatural groupId = attributes.get(0);
                UBNatural blockId = attributes.size() > 1 ? attributes.get(1) : new UBNat32();
                newNode.setFixedBlockType(new XBFixedBlockType(groupId, blockId));
                newNode.setSingleAttributeType(attributes.size() == 1);

                for (int attributeIndex = 2; attributeIndex < attributes.size(); attributeIndex++) {
                    newNode.addAttribute(attributes.get(attributeIndex));
                }
            }
        }

        dialogOption = JOptionPane.OK_OPTION;
        WindowUtils.closeWindow(this);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dialogOption = JOptionPane.CANCEL_OPTION;
        WindowUtils.closeWindow(this);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void attributesTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_attributesTablePropertyChange
        attributesTable.repaint();
    }//GEN-LAST:event_attributesTablePropertyChange

    private void loadFromButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFromButtonActionPerformed
        HexEditPanel.openFile(null);
        hexPanel.repaint();
    }//GEN-LAST:event_loadFromButtonActionPerformed

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
        HexEditPanel.saveFile();
    }//GEN-LAST:event_saveAsButtonActionPerformed

    public XBTTreeNode runDialog(XBTTreeNode srcNode) {
        this.srcNode = srcNode;
        newNode = srcNode.cloneNode(true);

        mainTabbedPane.removeAll();
        customPanel = null;

        dataMode = srcNode.getDataMode();
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
            mainTabbedPane.addTab(dataEditorPanelTitle, dataEditorPanel);

            HexEditPanel.loadFromStream(srcNode.getData(), srcNode.getDataSize());
        } else {
            customPanel = getCustomPanel(srcNode);
            if (customPanel instanceof XBSerializable) {
                try {
                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                    srcNode.toStreamUB(ostream);
                    ByteArrayInputStream stream = new ByteArrayInputStream(ostream.toByteArray());
                    XBTChildInputSerialHandler handler = new XBTChildProviderSerialHandler();
                    handler.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(stream, XBParserMode.SINGLE_BLOCK)));
                    ((XBTChildSerializable) customPanel).serializeFromXB(handler);
                    mainTabbedPane.addTab("Default", customPanel);
                    // defaultEditorPanel.add(customPanel, "custom");
                    //((CardLayout) defaultEditorPanel.getLayout()).show(defaultEditorPanel, "custom");
                } catch (XBProcessingException | IOException ex) {
                    Logger.getLogger(ModifyItemDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                attributes = new ArrayList<>();
                XBFixedBlockType fixedBlockType = srcNode.getFixedBlockType();
                attributes.add(fixedBlockType.getGroupID());
                if (!srcNode.getSingleAttributeType()) {
                    attributes.add(fixedBlockType.getBlockID());
                    attributes.addAll(srcNode.getAttributes());
                }
                tableModel.setAttribs(attributes);
                updateAttributesButtons();

                loadParameters(srcNode);
                TableColumnModel columnModel = parametersTable.getColumnModel();
                TableColumn column = columnModel.getColumn(3);
                column.setCellEditor(new ParametersTableCellEditor(catalog, newNode));
                column.setCellRenderer(new ParametersTableCellRenderer(catalog, newNode));
            }

            mainTabbedPane.addTab(paramEditorPanelTitle, paramEditorPanel);
            mainTabbedPane.addTab(attributesEditorPanelTitle, attributesEditorPanel);
        }

        setVisible(true);
        return newNode;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeWindow(new ItemPropertiesDialog(new javax.swing.JFrame(), true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel attributesEditorPanel;
    private javax.swing.JScrollPane attributesScrollPane;
    private javax.swing.JTable attributesTable;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel dataEditorPanel;
    private javax.swing.JPanel hexEditPanel;
    private javax.swing.JButton loadFromButton;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel paramEditorPanel;
    private javax.swing.JScrollPane parametersScrollPane;
    private javax.swing.JTable parametersTable;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton saveAsButton;
    // End of variables declaration//GEN-END:variables

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    private void updateAttributesButtons() {
        removeButton.setEnabled(attributesTable.getSelectedRowCount() > 0);
    }

    public int getDialogOption() {
        return dialogOption;
    }

    private JPanel getCustomPanel(XBTTreeNode srcNode) {
        if (catalog == null) {
            return null;
        }
        if (srcNode.getBlockType() == null) {
            return null;
        }
        if (srcNode.getBlockDecl() == null) {
            return null;
        }
        XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
        XBCBlockDecl decl = (XBCBlockDecl) srcNode.getBlockDecl();
        if (decl == null) {
            return null;
        }
        XBCBlockRev rev = decl.getBlockSpec();
        if (rev == null) {
            return null;
        }
        XBCXBlockPane pane = paneService.findPaneByPR(rev, 0);
        if (pane == null) {
            return null;
        }
        XBCXPlugPane plugPane = pane.getPane();
        if (plugPane == null) {
            return null;
        }
        XBCXPlugin plugin = plugPane.getPlugin();
        XBPlugin pluginHandler;

        // This part is stub for Java Webstart, uncomment it if needed
        /*if ("XBPicturePlugin.jar".equals(plugin.getPluginFile().getFilename())) {
         pluginHandler = new XBPicturePlugin();
         } else */
        pluginHandler = pluginRepository.getPluginHandler(plugin);
        if (pluginHandler == null) {
            return null;
        }

        XBPanelEditor panelEditor = pluginHandler.getPanelEditor(plugPane.getPaneIndex());
        if (panelEditor == null) {
            return null;
        }

        return panelEditor.getPanel();
    }

    private void loadParameters(XBTTreeNode node) {
        List<ParametersTableItem> parameters = parametersTableModel.getParameters();

        if (node == null) {
            return;
        }
        XBBlockType type = node.getBlockType();
        XBBlockDecl decl = node.getBlockDecl();

        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        if (decl instanceof XBCBlockDecl) {
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
            XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpec().getParent();
            if (spec != null) {
                long bindCount = specService.getSpecDefsCount(spec);
                // TODO: if (desc != null) descTextField.setText(desc.getText());
                for (int i = 0; i < bindCount; i++) {
                    // TODO: Exclusive lock
                    XBCSpecDef specDef = specService.getSpecDefByOrder(spec, i);
                    String specName = "";
                    String specType = "";
                    XBLineEditor lineEditor = null;

                    if (specDef != null) {
                        XBCXName specDefName = nameService.getDefaultItemName(specDef);
                        if (specDefName != null) {
                            specName = specDefName.getText();
                        }

                        XBCRev rowRev = specDef.getTarget();
                        if (rowRev != null) {
                            XBCSpec rowSpec = rowRev.getParent();
                            lineEditor = getCustomEditor((XBCBlockRev) rowRev, lineService);

                            XBCXName typeName = nameService.getDefaultItemName(rowSpec);
                            specType = typeName.getText();
                        }
                    }

                    ParametersTableItem itemRecord = new ParametersTableItem(spec, specName, specType, lineEditor);
                    parameters.add(itemRecord);
                }
            }
        }
    }

    private XBLineEditor getCustomEditor(XBCBlockRev rev, XBCXLineService lineService) {
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
}
