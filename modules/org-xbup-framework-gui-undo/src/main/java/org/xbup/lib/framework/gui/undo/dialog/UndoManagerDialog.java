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
package org.xbup.lib.framework.gui.undo.dialog;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.operation.XBTDocOperation;
import org.xbup.lib.operation.XBTOpDocCommand;
import org.xbup.lib.framework.gui.utils.WindowUtils;

/**
 * Dialog for undo management.
 *
 * @version 0.2.0 2015/11/10
 * @author XBUP Project (http://xbup.org)
 */
public class UndoManagerDialog extends javax.swing.JDialog {

    private int dialogOption = JOptionPane.CLOSED_OPTION;

    private final UndoManagerModel undoModel;
    private final java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/undo/dialog/resources/UndoManagerDialog");

    public UndoManagerDialog(java.awt.Frame parent, boolean modal, UndoManagerModel undoModel) {
        super(parent, modal);
        this.undoModel = undoModel;
        initComponents();
        init();
    }

    private void init() {
        WindowUtils.initWindow(this);
        WindowUtils.addHeaderPanel(this, bundle.getString("header.title"), bundle.getString("header.description"), bundle.getString("header.icon"));
        WindowUtils.assignGlobalKeyListener(this, closeButton);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        undoListScrollPane = new javax.swing.JScrollPane();
        undoList = new javax.swing.JList();
        undoDetailPanel = new javax.swing.JPanel();
        undoDetailInfoPanel = new javax.swing.JPanel();
        commandCaptionLabel = new javax.swing.JLabel();
        commandCaptionTextField = new javax.swing.JTextField();
        commandTypeLabel = new javax.swing.JLabel();
        commandTypeTextField = new javax.swing.JTextField();
        executionTimeLabel = new javax.swing.JLabel();
        executionTimeTextField = new javax.swing.JTextField();
        operationCaptionLabel = new javax.swing.JLabel();
        operationCaptionTextField = new javax.swing.JTextField();
        operationTypeLabel = new javax.swing.JLabel();
        operationTypeTextField = new javax.swing.JTextField();
        dataSizeLabel = new javax.swing.JLabel();
        dataSizeTextField = new javax.swing.JTextField();
        exportButton = new javax.swing.JButton();
        controlPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        revertButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/undo/dialog/resources/UndoManagerDialog"); // NOI18N
        setTitle(bundle.getString("title")); // NOI18N
        setModal(true);

        splitPane.setBorder(null);
        splitPane.setDividerLocation(200);

        undoList.setModel(undoModel);
        undoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        undoList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                undoListValueChanged(evt);
            }
        });
        undoListScrollPane.setViewportView(undoList);

        splitPane.setLeftComponent(undoListScrollPane);

        undoDetailInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("undoDetailInfoPanel.border.title"))); // NOI18N

        commandCaptionLabel.setText(bundle.getString("commandCaptionLabel.text")); // NOI18N

        commandCaptionTextField.setEditable(false);

        commandTypeLabel.setText(bundle.getString("commandTypeLabel.text")); // NOI18N

        commandTypeTextField.setEditable(false);

        executionTimeLabel.setText(bundle.getString("executionTimeLabel.text")); // NOI18N

        executionTimeTextField.setEditable(false);

        operationCaptionLabel.setText(bundle.getString("operationCaptionLabel.text")); // NOI18N

        operationCaptionTextField.setEditable(false);

        operationTypeLabel.setText(bundle.getString("operationTypeLabel.text")); // NOI18N

        operationTypeTextField.setEditable(false);

        dataSizeLabel.setText(bundle.getString("dataSizeLabel.text")); // NOI18N

        dataSizeTextField.setEditable(false);

        org.jdesktop.layout.GroupLayout undoDetailInfoPanelLayout = new org.jdesktop.layout.GroupLayout(undoDetailInfoPanel);
        undoDetailInfoPanel.setLayout(undoDetailInfoPanelLayout);
        undoDetailInfoPanelLayout.setHorizontalGroup(
            undoDetailInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(undoDetailInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(undoDetailInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(commandCaptionTextField)
                    .add(commandTypeTextField)
                    .add(operationCaptionTextField)
                    .add(operationTypeTextField)
                    .add(undoDetailInfoPanelLayout.createSequentialGroup()
                        .add(undoDetailInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(commandCaptionLabel)
                            .add(commandTypeLabel)
                            .add(operationCaptionLabel)
                            .add(operationTypeLabel))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(undoDetailInfoPanelLayout.createSequentialGroup()
                        .add(undoDetailInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(undoDetailInfoPanelLayout.createSequentialGroup()
                                .add(executionTimeLabel)
                                .add(0, 0, Short.MAX_VALUE))
                            .add(executionTimeTextField))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(undoDetailInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(dataSizeLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dataSizeTextField))))
                .addContainerGap())
        );
        undoDetailInfoPanelLayout.setVerticalGroup(
            undoDetailInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(undoDetailInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(commandCaptionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandCaptionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandTypeLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commandTypeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(operationCaptionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(operationCaptionTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(undoDetailInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(undoDetailInfoPanelLayout.createSequentialGroup()
                        .add(operationTypeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(operationTypeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(executionTimeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(executionTimeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(undoDetailInfoPanelLayout.createSequentialGroup()
                        .add(dataSizeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dataSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        exportButton.setText(bundle.getString("exportButton.text")); // NOI18N
        exportButton.setEnabled(false);
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout undoDetailPanelLayout = new org.jdesktop.layout.GroupLayout(undoDetailPanel);
        undoDetailPanel.setLayout(undoDetailPanelLayout);
        undoDetailPanelLayout.setHorizontalGroup(
            undoDetailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, undoDetailPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(exportButton)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, undoDetailPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(undoDetailInfoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        undoDetailPanelLayout.setVerticalGroup(
            undoDetailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(undoDetailPanelLayout.createSequentialGroup()
                .add(undoDetailInfoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(exportButton)
                .addContainerGap())
        );

        splitPane.setRightComponent(undoDetailPanel);

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(splitPane))
        );

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        closeButton.setText(bundle.getString("closeButton.text")); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        revertButton.setText(bundle.getString("revertButton.text")); // NOI18N
        revertButton.setEnabled(false);
        revertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revertButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout controlPanelLayout = new org.jdesktop.layout.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap(481, Short.MAX_VALUE)
                .add(revertButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(closeButton)
                .addContainerGap())
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeButton)
                    .add(revertButton))
                .addContainerGap())
        );

        getContentPane().add(controlPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dialogOption = JOptionPane.CANCEL_OPTION;
        WindowUtils.closeWindow(this);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void undoListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_undoListValueChanged
        if (!evt.getValueIsAdjusting()) {
            updateDetail(undoList.getSelectedIndex());
        }
    }//GEN-LAST:event_undoListValueChanged

    private void revertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revertButtonActionPerformed
        dialogOption = JOptionPane.OK_OPTION;
        WindowUtils.closeWindow(this);
    }//GEN-LAST:event_revertButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        int selectedIndex = undoList.getSelectedIndex();
        XBTDocCommand command = null;
        if (selectedIndex >= 0) {
            command = undoModel.getItem(selectedIndex);
        }

        if (command instanceof XBTOpDocCommand) {
            JFileChooser exportFileChooser = new JFileChooser();
            exportFileChooser.setAcceptAllFileFilterUsed(true);
            if (exportFileChooser.showSaveDialog(WindowUtils.getFrame(this)) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fileStream;
                try {
                    fileStream = new FileOutputStream(exportFileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        ((XBTOpDocCommand) command).getOperation().getData().saveToStream(fileStream);
                    } finally {
                        fileStream.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(UndoManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UndoManagerDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    public long getCommandPosition() {
        return undoList.getSelectedIndex();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        UndoManagerDialog dialog = new UndoManagerDialog(new javax.swing.JFrame(), true, null);
        // TODO propertiesDialog.setDevMode(true);
        WindowUtils.invokeWindow(dialog);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel commandCaptionLabel;
    private javax.swing.JTextField commandCaptionTextField;
    private javax.swing.JLabel commandTypeLabel;
    private javax.swing.JTextField commandTypeTextField;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel dataSizeLabel;
    private javax.swing.JTextField dataSizeTextField;
    private javax.swing.JLabel executionTimeLabel;
    private javax.swing.JTextField executionTimeTextField;
    private javax.swing.JButton exportButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel operationCaptionLabel;
    private javax.swing.JTextField operationCaptionTextField;
    private javax.swing.JLabel operationTypeLabel;
    private javax.swing.JTextField operationTypeTextField;
    private javax.swing.JButton revertButton;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JPanel undoDetailInfoPanel;
    private javax.swing.JPanel undoDetailPanel;
    private javax.swing.JList undoList;
    private javax.swing.JScrollPane undoListScrollPane;
    // End of variables declaration//GEN-END:variables

    public int getDialogOption() {
        return dialogOption;
    }

    private void updateDetail(int selectedIndex) {
        XBTDocCommand command = null;
        if (selectedIndex >= 0) {
            command = undoModel.getItem(selectedIndex);
        }

        revertButton.setEnabled(selectedIndex >= 0 && selectedIndex != undoModel.getCurrentPosition());
        exportButton.setEnabled(command != null);

        commandCaptionTextField.setText(command != null ? command.getCaption() : "");
        commandTypeTextField.setText(command != null ? command.getBasicType().name() : "");
        executionTimeTextField.setText(command != null && command.getExecutionTime() != null ? command.getExecutionTime().toString() : "");

        XBTDocOperation operation = null;
        if (command instanceof XBTOpDocCommand) {
            operation = ((XBTOpDocCommand) command).getCurrentOperation();
        }
        operationCaptionTextField.setText(operation != null ? operation.getCaption() : "");
        operationTypeTextField.setText(operation != null ? operation.getBasicType().name() : "");
        dataSizeTextField.setText(operation != null ? String.valueOf(operation.getData().getDataSize()) : "");
    }
}