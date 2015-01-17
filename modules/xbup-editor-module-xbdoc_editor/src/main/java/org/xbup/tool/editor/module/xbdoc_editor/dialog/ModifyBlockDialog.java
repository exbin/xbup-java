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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import org.xbup.lib.core.block.XBBlockDataMode;
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
import org.xbup.lib.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.xbup.lib.core.serial.XBASerialReader;
import org.xbup.lib.core.serial.XBASerialWriter;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.parser_tree.XBATreeParamExtractor;
import org.xbup.lib.parser_tree.XBTTreeDocument;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.parser_tree.XBTTreeWriter;
import org.xbup.lib.plugin.XBLineEditor;
import org.xbup.lib.plugin.XBPanelEditor;
import org.xbup.lib.plugin.XBPlugin;
import org.xbup.lib.plugin.XBPluginRepository;
import org.xbup.tool.editor.base.api.utils.WindowUtils;

/**
 * Dialog for modifying item attributes or data.
 *
 * @version 0.1.24 2015/01/16
 * @author XBUP Project (http://xbup.org)
 */
public class ModifyBlockDialog extends javax.swing.JDialog {

    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;

    private final AttributesTableModel attributesTableModel = new AttributesTableModel();
    private final ParametersTableModel parametersTableModel = new ParametersTableModel();
    private XBTTreeDocument doc;
    private XBTTreeNode srcNode;
    private XBTTreeNode newNode = null;

    private final HexEditPanel hexPanel;
    private XBPanelEditor customPanel;
    private XBBlockDataMode dataMode = XBBlockDataMode.NODE_BLOCK;
    private List<UBNatural> attributes = null;
    private HexEditPanel extAreaHexPanel = null;

    private final String attributesEditorPanelTitle;
    private final String dataEditorPanelTitle;
    private final String paramEditorPanelTitle;
    private final String extAreaEditorPanelTitle;
    private final String customEditorPanelTitle = "Custom";
    private int dialogOption = JOptionPane.CLOSED_OPTION;
    private boolean dataChanged = false;

    public ModifyBlockDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        hexPanel = new HexEditPanel((JFrame) parent);
        customPanel = null;
        hexEditPanel.add(hexPanel);

        attributesEditorPanelTitle = mainTabbedPane.getTitleAt(mainTabbedPane.indexOfComponent(attributesEditorPanel));
        dataEditorPanelTitle = mainTabbedPane.getTitleAt(mainTabbedPane.indexOfComponent(dataEditorPanel));
        paramEditorPanelTitle = mainTabbedPane.getTitleAt(mainTabbedPane.indexOfComponent(paramEditorPanel));
        extAreaEditorPanelTitle = mainTabbedPane.getTitleAt(mainTabbedPane.indexOfComponent(extendedAreaPanel));

        attributesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateAttributesButtons();
            }
        });

        attributesTableModel.attachChangeListener(new AttributesTableModel.ChangeListener() {

            @Override
            public void valueChanged() {
                dataChanged = true;
            }
        });

        // DefaultCellEditor attributesTableCellEditor = new DefaultCellEditor(new JTextField());
        // attributesTableCellEditor.setClickCountToStart(0);
        // attributesTable.getColumnModel().getColumn(1).setCellEditor(attributesTableCellEditor);
        int parametersTableWidth = parametersTable.getWidth();
        parametersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        parametersTable.getColumnModel().getColumn(0).setPreferredWidth(parametersTableWidth / 6);
        parametersTable.getColumnModel().getColumn(1).setPreferredWidth(parametersTableWidth / 6);
        parametersTable.getColumnModel().getColumn(2).setPreferredWidth(parametersTableWidth / 6);
        parametersTable.getColumnModel().getColumn(3).setPreferredWidth(parametersTableWidth / 2);

        mainTabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane pane = (JTabbedPane) e.getSource();
                if (pane.getSelectedIndex() < 0) {
                    return;
                }

                String currentTitle = pane.getTitleAt(pane.getSelectedIndex());
                if (attributesEditorPanelTitle.equals(currentTitle)) {
                    if (dataChanged || attributes == null) {
                        reloadAttributes();
                    }
                    dataChanged = false;
                } else if (paramEditorPanelTitle.equals(currentTitle)) {
                    if (dataChanged || parametersTableModel.isEmpty()) {
                        reloadParameters();
                    }
                    dataChanged = false;
                } else if (customEditorPanelTitle.equals(currentTitle)) {
                    if (dataChanged) {
                        reloadCustomEditor();
                    }
                    dataChanged = false;
                } else if (extAreaEditorPanelTitle.equals(currentTitle)) {
                    if (extAreaHexPanel == null) {
                        reloadExtendedArea();
                    }
                }
            }
        });

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
        attributesTable = new JTable(attributesTableModel) {
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
        extendedAreaPanel = new javax.swing.JPanel();
        hexEditScrollPane = new javax.swing.JScrollPane();
        extLoadFromButton = new javax.swing.JButton();
        extSaveFromButto = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/xbdoc_editor/dialog/resources/ModifyBlockDialog"); // NOI18N
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

        attributesTable.setModel(attributesTableModel);
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

        extLoadFromButton.setText(bundle.getString("loadFromButton.text")); // NOI18N
        extLoadFromButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extLoadFromButtonActionPerformed(evt);
            }
        });

        extSaveFromButto.setText(bundle.getString("saveAsButton.text")); // NOI18N
        extSaveFromButto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extSaveFromButtoActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout extendedAreaPanelLayout = new org.jdesktop.layout.GroupLayout(extendedAreaPanel);
        extendedAreaPanel.setLayout(extendedAreaPanelLayout);
        extendedAreaPanelLayout.setHorizontalGroup(
            extendedAreaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(extendedAreaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(extendedAreaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, hexEditScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .add(extendedAreaPanelLayout.createSequentialGroup()
                        .add(extLoadFromButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(extSaveFromButto)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        extendedAreaPanelLayout.setVerticalGroup(
            extendedAreaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, extendedAreaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(hexEditScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(extendedAreaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(extSaveFromButto)
                    .add(extLoadFromButton))
                .addContainerGap())
        );

        mainTabbedPane.addTab(bundle.getString("extendedAreaPanel.tabTitle"), extendedAreaPanel); // NOI18N

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
        attributesTableModel.fireTableDataChanged();
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

            attributesTableModel.fireTableDataChanged();
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
                hexPanel.saveToStream(stream);
            } catch (IOException ex) {
                Logger.getLogger(ModifyBlockDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

            newNode = srcNode.cloneNode();
            newNode.setData(new ByteArrayInputStream(stream.toByteArray()));
        } else {
            // TODO: Store active tab
            String currentTitle = mainTabbedPane.getTitleAt(mainTabbedPane.getSelectedIndex());
            if (attributesEditorPanelTitle.equals(currentTitle)) {
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
            } else if (paramEditorPanelTitle.equals(currentTitle)) {
                // Values stored automatically
            } else if (customEditorPanelTitle.equals(currentTitle)) {
                // Values stored automatically
            } else if (extAreaEditorPanelTitle.equals(currentTitle)) {
                // No need to store
            }
        }

        if (srcNode.getParent() == null && extAreaHexPanel != null) {
            // TODO: Horrible extraction of data from HexEditPanel
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                extAreaHexPanel.saveToStream(buffer);
            } catch (IOException ex) {
                Logger.getLogger(ModifyBlockDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

            doc.setExtendedArea(new ByteArrayInputStream(buffer.toByteArray()));
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
        hexPanel.openFile(null);
        hexPanel.repaint();
    }//GEN-LAST:event_loadFromButtonActionPerformed

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
        hexPanel.saveFile();
    }//GEN-LAST:event_saveAsButtonActionPerformed

    private void extLoadFromButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extLoadFromButtonActionPerformed
        extAreaHexPanel.openFile(null);
        extendedAreaPanel.repaint();
    }//GEN-LAST:event_extLoadFromButtonActionPerformed

    private void extSaveFromButtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extSaveFromButtoActionPerformed
        extAreaHexPanel.saveFile();
    }//GEN-LAST:event_extSaveFromButtoActionPerformed

    public XBTTreeNode runDialog(XBTTreeNode srcNode, XBTTreeDocument doc) {
        this.srcNode = srcNode;
        this.doc = doc;
        newNode = srcNode.cloneNode(true);

        mainTabbedPane.removeAll();
        customPanel = null;

        dataMode = srcNode.getDataMode();
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
            mainTabbedPane.addTab(dataEditorPanelTitle, dataEditorPanel);

            hexPanel.loadFromStream(srcNode.getData(), srcNode.getDataSize());
        } else {
            reloadParameters();
            TableColumnModel columnModel = parametersTable.getColumnModel();
            TableColumn column = columnModel.getColumn(3);
            column.setCellEditor(new ParametersTableCellEditor(catalog, pluginRepository, newNode, doc));
            column.setCellRenderer(new ParametersTableCellRenderer(catalog, pluginRepository, newNode, doc));

            customPanel = getCustomPanel(srcNode);
            if (customPanel != null) {
                ((XBPanelEditor) customPanel).attachChangeListener(new XBPanelEditor.ChangeListener() {

                    @Override
                    public void valueChanged() {
                        dataChanged = true;
                    }
                });

                reloadCustomEditor();
                mainTabbedPane.addTab(customEditorPanelTitle, customPanel.getPanel());
            }

            mainTabbedPane.addTab(paramEditorPanelTitle, paramEditorPanel);
            mainTabbedPane.addTab(attributesEditorPanelTitle, attributesEditorPanel);
        }

        if (srcNode.getParent() == null) {
            mainTabbedPane.addTab(extAreaEditorPanelTitle, extendedAreaPanel);
            extAreaHexPanel = null;
        }

        setVisible(true);
        return newNode;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeWindow(new BlockPropertiesDialog(new javax.swing.JFrame(), true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel attributesEditorPanel;
    private javax.swing.JScrollPane attributesScrollPane;
    private javax.swing.JTable attributesTable;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel dataEditorPanel;
    private javax.swing.JButton extLoadFromButton;
    private javax.swing.JButton extSaveFromButto;
    private javax.swing.JPanel extendedAreaPanel;
    private javax.swing.JPanel hexEditPanel;
    private javax.swing.JScrollPane hexEditScrollPane;
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

    private XBPanelEditor getCustomPanel(XBTTreeNode srcNode) {
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

        return pluginHandler.getPanelEditor(plugPane.getPaneIndex());
    }

    private void reloadAttributes() {
        attributes = new ArrayList<>();
        XBFixedBlockType fixedBlockType = srcNode.getFixedBlockType();
        attributes.add(fixedBlockType.getGroupID());
        if (!srcNode.getSingleAttributeType()) {
            attributes.add(fixedBlockType.getBlockID());
            attributes.addAll(srcNode.getAttributes());
        }
        attributesTableModel.setAttribs(attributes);
        updateAttributesButtons();
    }

    private void reloadParameters() {
        parametersTableModel.clear();

        if (srcNode == null) {
            return;
        }

        XBBlockDecl decl = srcNode.getBlockDecl();
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        if (decl instanceof XBCBlockDecl) {
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
            XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpec().getParent();
            if (spec != null) {
                long bindCount = specService.getSpecDefsCount(spec);
                XBATreeParamExtractor paramExtractor = new XBATreeParamExtractor(srcNode, catalog);
                // TODO: if (desc != null) descTextField.setText(desc.getText());
                for (int paramIndex = 0; paramIndex < bindCount; paramIndex++) {
                    // TODO: Exclusive lock
                    XBCSpecDef specDef = specService.getSpecDefByOrder(spec, paramIndex);
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
                            if (lineEditor != null) {
                                paramExtractor.setParameterIndex(paramIndex);
                                XBASerialReader serialReader = new XBASerialReader(paramExtractor);
                                serialReader.read(lineEditor);

                                lineEditor.attachChangeListener(new LineEditorChangeListener(lineEditor, paramExtractor, paramIndex));
                            }

                            XBCXName typeName = nameService.getDefaultItemName(rowSpec);
                            specType = typeName.getText();
                        }
                    }

                    ParametersTableItem itemRecord = new ParametersTableItem(specDef, specName, specType, lineEditor);
                    itemRecord.setTypeName(itemRecord.getDefTypeName());
                    parametersTableModel.addRow(itemRecord);
                }
            }
        }
    }

    private void reloadExtendedArea() {
        extAreaHexPanel = new HexEditPanel((JFrame) WindowUtils.getFrame(this));
        hexEditScrollPane.setViewportView(extAreaHexPanel);

        if (doc.getExtendedAreaSize() > 0) {
            extAreaHexPanel.loadFromStream(doc.getExtendedArea(), doc.getExtendedAreaSize());
        }
    }

    private void reloadCustomEditor() {
        XBASerialReader serialReader = new XBASerialReader(new XBTProviderToPullProvider(new XBTTreeWriter(srcNode)));
        serialReader.read((XBSerializable) customPanel);
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
            XBASerialWriter serialWriter = new XBASerialWriter(paramExtractor);
            serialWriter.write(lineEditor);
            dataChanged = true;
        }
    }
}
