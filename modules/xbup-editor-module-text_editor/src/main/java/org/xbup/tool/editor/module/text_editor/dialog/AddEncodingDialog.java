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
package org.xbup.tool.editor.module.text_editor.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

/**
 * Encoding Selection Panel.
 *
 * @version 0.1.22 2013/03/09
 * @author XBUP Project (http://xbup.org)
 */
public class AddEncodingDialog extends javax.swing.JDialog {

    protected int Closed_Option = JOptionPane.CLOSED_OPTION;

    /** Creates new form AddEncodingDialog */
    public AddEncodingDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        assignGlobalKeyListener();
    }

    /** Assign ESCAPE/ENTER key for all focusable components recursively */
    private void assignGlobalKeyListener() {
        assignGlobalKeyListener(this);
    }

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
                            okButton.doClick();
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
        return Closed_Option;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        supportedEncodingsLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Supported Encoding"); // NOI18N
        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/xbeditor/module/xbtexteditor/dialog/resources/AddEncodingDialog"); // NOI18N
        supportedEncodingsLabel.setText(bundle.getString("supportedEncodingsLabel.text")); // NOI18N
        supportedEncodingsLabel.setName("supportedEncodingsLabel"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jList1.setModel(new AvailableEncodingsListModel());
        jList1.setName("jList1"); // NOI18N
        jScrollPane2.setViewportView(jList1);

        cancelButton.setText(bundle.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText(bundle.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(supportedEncodingsLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(supportedEncodingsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        Closed_Option = JOptionPane.CANCEL_OPTION;
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        Closed_Option = JOptionPane.OK_OPTION;
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AddEncodingDialog dialog = new AddEncodingDialog(new javax.swing.JFrame(), true);
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

    public void setEncodings(List<String> encodings) {
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        for (Iterator<Entry<String, Charset>> it = Charset.availableCharsets().entrySet().iterator(); it.hasNext();) {
            Entry<String, Charset> entry = (Entry<String, Charset>) it.next();
            if (!"UTF-8".equals(entry.getKey())) {
                set.add(entry.getValue().name()); //displayName(Locale.getDefault()));
            }
        }
        set.remove("UTF-8");
        for (Iterator<String> it = encodings.iterator(); it.hasNext();) {
            set.remove(it.next());
        }
        ArrayList<String> list = new ArrayList<String>(set);
        ((AvailableEncodingsListModel) jList1.getModel()).setCharsets(list);
    }

    public List<String> getEncodings() {
        ArrayList<String> result = new ArrayList<String>();
        List selectedValues = jList1.getSelectedValuesList();
        for (int i = 0; i < selectedValues.size(); i++) {
            Object value = selectedValues.get(i);
            if (value instanceof String) {
                result.add((String) value);
            }
        }
        return result;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel supportedEncodingsLabel;
    // End of variables declaration//GEN-END:variables

    private class AvailableEncodingsListModel extends AbstractListModel {

        private List<String> charsets = null;

        @Override
        public int getSize() {
            if (charsets == null) {
                return 0;
            }
            return charsets.size();
        }

        @Override
        public Object getElementAt(int index) {
            return charsets.get(index);
        }

        /**
         * @return the charsets
         */
        public List<String> getCharsets() {
            return charsets;
        }

        /**
         * @param charsets the charsets to set
         */
        public void setCharsets(List<String> charsets) {
            this.charsets = charsets;
            fireContentsChanged(this, 0, charsets.size());
        }
    }
}