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
package org.xbup.tool.editor.module.service_manager.catalog.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCSpecDefType;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogRevsComboBoxModel;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogSpecItemType;
import org.xbup.tool.editor.base.api.XBEditorFrame;

/**
 * XBManager Catalog Specification Selection Dialog.
 *
 * @version 0.1.23 2013/09/22
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogSpecDefEditorDialog extends javax.swing.JDialog {

    private int dialogOption = JOptionPane.CLOSED_OPTION;
    private final XBACatalog catalog;
    private XBCSpec spec;
    private XBCSpec targetSpec;
    private CatalogRevsComboBoxModel revsModel;

    private XBCItemService itemService;
    private XBCRevService revService;
    private CatalogSpecItemType targetType;

    /**
     * Creates new form CatalogSelectSpecDialog
     */
    public CatalogSpecDefEditorDialog(java.awt.Frame parent, boolean modal, XBACatalog catalog) {
        super(parent, modal);
        revsModel = new CatalogRevsComboBoxModel();
        targetType = CatalogSpecItemType.BLOCK;
        initComponents();
        if (parent instanceof XBEditorFrame) {
            setIconImage(((XBEditorFrame) parent).getMainFrameManagement().getFrameIcon());
        }

        targetRevisionComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                DefaultListCellRenderer retValue = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof XBCRev) {
                    retValue.setText("Revision " + ((XBCRev) value).getXBIndex());
                }

                return retValue;
            }
        });

        // TODO change listener for definitionTypeComboBox.

        assignGlobalKeyListener();
        this.catalog = catalog;
        if (catalog!=null) {
            itemService = (XBCItemService) catalog.getCatalogService(XBCItemService.class);
            revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        }
        targetSpec = null;
    }

    private void assignGlobalKeyListener() {
        assignGlobalKeyListener(this);
    }

    /** Assign ESCAPE/ENTER key for all focusable components recursively */
    private void assignGlobalKeyListener(Container comp) {
        Component[] comps = comp.getComponents();
        for (int i = 0; i < comps.length; i++) {
            Component item = comps[i];
            if (item.isFocusable()) {
                item.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent evt) {
                        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            cancelButton.doClick();
                        }
                        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (evt.getSource() instanceof JButton) {
                                ((JButton) evt.getSource()).doClick();
                            } else {
                                selectButton.doClick();
                            }
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });
            }
            if (item instanceof Container) {
                assignGlobalKeyListener((Container) item);
            }
        }
    }

    public int getOption() {
        return dialogOption;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        selectButton = new javax.swing.JButton();
        definitionTypePanel = new javax.swing.JPanel();
        definitionTypeLabel = new javax.swing.JLabel();
        definitionTypeComboBox = new javax.swing.JComboBox();
        definitionTargetLabel = new javax.swing.JLabel();
        definitionTargetTextField = new javax.swing.JTextField();
        selectTargetButton = new javax.swing.JButton();
        targetRevisionLabel = new javax.swing.JLabel();
        targetRevisionComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Definition Editor");

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap(184, Short.MAX_VALUE)
                .addComponent(selectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(selectButton))
                .addContainerGap())
        );

        getContentPane().add(controlPanel, java.awt.BorderLayout.PAGE_END);

        definitionTypeLabel.setText("Definition Type");

        definitionTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Consist", "Join" }));
        definitionTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                definitionTypeComboBoxItemStateChanged(evt);
            }
        });

        definitionTargetLabel.setText("Definition Target");

        definitionTargetTextField.setEditable(false);

        selectTargetButton.setText("Select...");
        selectTargetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectTargetButtonActionPerformed(evt);
            }
        });

        targetRevisionLabel.setText("Revision");

        targetRevisionComboBox.setModel(revsModel);

        javax.swing.GroupLayout definitionTypePanelLayout = new javax.swing.GroupLayout(definitionTypePanel);
        definitionTypePanel.setLayout(definitionTypePanelLayout);
        definitionTypePanelLayout.setHorizontalGroup(
            definitionTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(definitionTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(definitionTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(targetRevisionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(definitionTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(definitionTypePanelLayout.createSequentialGroup()
                        .addComponent(definitionTargetTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectTargetButton))
                    .addGroup(definitionTypePanelLayout.createSequentialGroup()
                        .addGroup(definitionTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(definitionTypeLabel)
                            .addComponent(definitionTargetLabel)
                            .addComponent(targetRevisionLabel))
                        .addGap(0, 223, Short.MAX_VALUE)))
                .addContainerGap())
        );
        definitionTypePanelLayout.setVerticalGroup(
            definitionTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(definitionTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(definitionTypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(definitionTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(definitionTargetLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(definitionTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(definitionTargetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectTargetButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(targetRevisionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(targetRevisionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        getContentPane().add(definitionTypePanel, java.awt.BorderLayout.NORTH);

        setSize(new java.awt.Dimension(377, 295));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dialogOption = JOptionPane.CANCEL_OPTION;
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        dialogOption = JOptionPane.OK_OPTION;
        dispose();
    }//GEN-LAST:event_selectButtonActionPerformed

    private void selectTargetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectTargetButtonActionPerformed
        CatalogSelectSpecDialog specSelectDialog = new CatalogSelectSpecDialog((Frame) SwingUtilities.getWindowAncestor(this), true, catalog, targetType);
        specSelectDialog.setVisible(true);
        if (specSelectDialog.getOption() == JOptionPane.OK_OPTION) {
            setRevSpec((XBCSpec) specSelectDialog.getSpec());
        }
        targetRevisionComboBox.repaint();
    }//GEN-LAST:event_selectTargetButtonActionPerformed

    private void definitionTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_definitionTypeComboBoxItemStateChanged
        updateSpecDefType();
    }//GEN-LAST:event_definitionTypeComboBoxItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CatalogSpecDefEditorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CatalogSpecDefEditorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CatalogSpecDefEditorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CatalogSpecDefEditorDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CatalogSpecDefEditorDialog dialog = new CatalogSpecDefEditorDialog(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel definitionTargetLabel;
    private javax.swing.JTextField definitionTargetTextField;
    private javax.swing.JComboBox definitionTypeComboBox;
    private javax.swing.JLabel definitionTypeLabel;
    private javax.swing.JPanel definitionTypePanel;
    private javax.swing.JButton selectButton;
    private javax.swing.JButton selectTargetButton;
    private javax.swing.JComboBox targetRevisionComboBox;
    private javax.swing.JLabel targetRevisionLabel;
    // End of variables declaration//GEN-END:variables

    public XBCItem getTargetSpec() {
        return targetSpec;
    }

    public void setRevSpec(XBCSpec targetSpec) {
        if (targetSpec != null) {
            definitionTargetTextField.setText(Long.toString(targetSpec.getId()));
            if (targetSpec instanceof XBCSpec) {
                revsModel.setRevs(revService.getRevs((XBCSpec) targetSpec));
                targetRevisionComboBox.setSelectedIndex(revsModel.getSize()-1);

            } else {
                revsModel.getRevs().clear();
            }
        } else {
            definitionTargetTextField.setText("");
            targetRevisionComboBox.setSelectedIndex(-1);
            revsModel.getRevs().clear();
        }

    }

    public void setTargetSpec(XBCSpec targetSpec) {
        this.targetSpec = targetSpec;

        definitionTargetTextField.setText("");
        targetRevisionComboBox.setSelectedIndex(-1);
        revsModel.getRevs().clear();
    }

    public XBCRev getTarget() {
        return (XBCRev) targetRevisionComboBox.getSelectedItem();
    }

    public void setTarget(XBCRev rev) {
        if (rev != null) {
            setTargetSpec(rev.getParent());
            definitionTargetTextField.setText(Long.toString(rev.getParent().getId()));
            revsModel.setRevs(revService.getRevs((XBCSpec) targetSpec));
            targetRevisionComboBox.setSelectedIndex(revsModel.getSize()-1);
            targetRevisionComboBox.setSelectedIndex(rev.getXBIndex().intValue());
        } else {
            setTargetSpec(null);
        }
    }

    public XBCSpecDefType getSpecDefType() {
        if (spec instanceof XBCBlockSpec) {
            switch (definitionTypeComboBox.getSelectedIndex()) {
                case 0: return XBCSpecDefType.CONS;
                case 1: return XBCSpecDefType.JOIN;
                case 2: return XBCSpecDefType.JOIN; // Attribute
                case 3: return XBCSpecDefType.CONS; // Blob
                case 4: return XBCSpecDefType.LIST_CONS;
                case 5: return XBCSpecDefType.LIST_JOIN;
                default: return XBCSpecDefType.CONS;
            }
        } else {
            if (definitionTypeComboBox.getSelectedIndex() == 0) {
                return XBCSpecDefType.CONS;
            } else {
                return XBCSpecDefType.JOIN;
            }
        }
    }

    private void switchSpecDefType(CatalogSpecItemType newType) {
        if (!targetType.equals(newType)) {
            definitionTargetTextField.setText("");
            targetRevisionComboBox.setSelectedIndex(-1);
            revsModel.getRevs().clear();
            targetType = newType;
        }
    }

    public void setSpec(XBCSpec spec) {
        this.spec = spec;
        if (spec instanceof XBCBlockSpec) {
            definitionTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Consist", "Join", "Attribute", "Blob", "List Consist", "List Join" }));
            switchSpecDefType(CatalogSpecItemType.BLOCK);
        } else {
            definitionTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Consist", "Join" }));
            if (spec instanceof XBCGroupSpec) {
                switchSpecDefType(CatalogSpecItemType.BLOCK);
            } else {
                switchSpecDefType(CatalogSpecItemType.GROUP);
            }
        }
    }

    public void setSpecDef(XBCSpecDef specDef) {
        setSpec(specDef.getSpec());

        repaint();

        setTarget(specDef.getTarget());

        switch (specDef.getType()) {
            case CONS: {
                definitionTypeComboBox.setSelectedIndex((specDef.getTarget() == null) && (definitionTypeComboBox.getItemCount() > 3) ? 3 : 0);
                break;
            }
            case JOIN: {
                definitionTypeComboBox.setSelectedIndex((specDef.getTarget() == null) && (definitionTypeComboBox.getItemCount() > 2) ? 2 : 1);
                break;
            }
            case LIST_CONS: {
                definitionTypeComboBox.setSelectedIndex(4);
                break;
            }
            case LIST_JOIN: {
                definitionTypeComboBox.setSelectedIndex(5);
                break;
            }
        }
        updateSpecDefType();
    }

    private void updateSpecDefType() {
        if (spec instanceof XBCBlockSpec) {
            switchSpecDefType(CatalogSpecItemType.BLOCK);
        } else if (spec instanceof XBCGroupSpec) {
            if (definitionTypeComboBox.getSelectedIndex() == 0) {
                switchSpecDefType(CatalogSpecItemType.BLOCK);
            } else {
                switchSpecDefType(CatalogSpecItemType.GROUP);
            }
        } else if (spec instanceof XBCFormatSpec) {
            if (definitionTypeComboBox.getSelectedIndex() == 0) {
                switchSpecDefType(CatalogSpecItemType.GROUP);
            } else {
                switchSpecDefType(CatalogSpecItemType.FORMAT);
            }
        }

        if (spec instanceof XBCBlockSpec) {
            boolean enabled = ! ((definitionTypeComboBox.getSelectedIndex() == 2)||(definitionTypeComboBox.getSelectedIndex() == 3));
            targetRevisionComboBox.setEnabled(enabled);
            selectTargetButton.setEnabled(enabled);
        } else {
            targetRevisionComboBox.setEnabled(true);
            selectTargetButton.setEnabled(true);
        }
    }
}
