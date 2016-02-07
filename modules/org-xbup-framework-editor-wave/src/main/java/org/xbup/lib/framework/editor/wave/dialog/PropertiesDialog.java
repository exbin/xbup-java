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
package org.xbup.lib.framework.editor.wave.dialog;

import javax.sound.sampled.AudioFormat;
import org.xbup.lib.framework.gui.utils.WindowUtils;
import org.xbup.lib.framework.editor.wave.panel.AudioPanel;

/**
 * File Properties Dialog.
 *
 * @version 0.2.0 2016/01/29
 * @author XBUP Project (http://xbup.org)
 */
public class PropertiesDialog extends javax.swing.JDialog {

    public PropertiesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    private void init() {
        WindowUtils.initWindow(this);
        WindowUtils.assignGlobalKeyListener(this, closeButton);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileNameLabel = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();
        documentPanel = new javax.swing.JPanel();
        waveLengthLabel = new javax.swing.JLabel();
        waveLengthTextField = new javax.swing.JTextField();
        sampleRateLabel = new javax.swing.JLabel();
        sampleRateTextField = new javax.swing.JTextField();
        channelsLabel = new javax.swing.JLabel();
        channelsTextField = new javax.swing.JTextField();
        encodingLabel = new javax.swing.JLabel();
        encodingTextField = new javax.swing.JTextField();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/editor/wave/dialog/resources/PropertiesDialog"); // NOI18N
        setTitle(bundle.getString("Form.title")); // NOI18N
        setLocationByPlatform(true);
        setModal(true);
        setName("Form"); // NOI18N

        fileNameLabel.setText(bundle.getString("fileNameLabel.text")); // NOI18N
        fileNameLabel.setName("fileNameLabel"); // NOI18N

        fileNameTextField.setEditable(false);
        fileNameTextField.setName("fileNameTextField"); // NOI18N

        documentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("documentPanel.border.title"))); // NOI18N
        documentPanel.setName("documentPanel"); // NOI18N

        waveLengthLabel.setText(bundle.getString("waveLengthLabel.text")); // NOI18N
        waveLengthLabel.setName("waveLengthLabel"); // NOI18N

        waveLengthTextField.setEditable(false);
        waveLengthTextField.setName("waveLengthTextField"); // NOI18N

        sampleRateLabel.setText(bundle.getString("sampleRateLabel.text")); // NOI18N
        sampleRateLabel.setName("sampleRateLabel"); // NOI18N

        sampleRateTextField.setEditable(false);
        sampleRateTextField.setName("sampleRateTextField"); // NOI18N

        channelsLabel.setText(bundle.getString("channelsLabel.text")); // NOI18N
        channelsLabel.setName("channelsLabel"); // NOI18N

        channelsTextField.setEditable(false);
        channelsTextField.setName("channelsTextField"); // NOI18N

        encodingLabel.setText(bundle.getString("encodingLabel.text")); // NOI18N
        encodingLabel.setName("encodingLabel"); // NOI18N

        encodingTextField.setEditable(false);
        encodingTextField.setName("encodingTextField"); // NOI18N

        javax.swing.GroupLayout documentPanelLayout = new javax.swing.GroupLayout(documentPanel);
        documentPanel.setLayout(documentPanelLayout);
        documentPanelLayout.setHorizontalGroup(
            documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(waveLengthLabel)
                    .addComponent(sampleRateLabel)
                    .addComponent(channelsLabel)
                    .addComponent(encodingLabel)
                    .addComponent(sampleRateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(channelsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(encodingTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(waveLengthTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addContainerGap())
        );
        documentPanelLayout.setVerticalGroup(
            documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentPanelLayout.createSequentialGroup()
                .addComponent(waveLengthLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waveLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sampleRateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sampleRateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(encodingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(encodingTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        closeButton.setText(bundle.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(documentPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeButton)
                    .addComponent(fileNameLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(documentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        WindowUtils.closeWindow(this);
    }//GEN-LAST:event_closeButtonActionPerformed

    public static void main(String args[]) {
        WindowUtils.invokeWindow(new PropertiesDialog(new javax.swing.JFrame(), true));
    }

    public void setDocument(AudioPanel panel) {
        if (!panel.isEmpty()) {
            fileNameTextField.setText(panel.getFileName());
            waveLengthTextField.setText(panel.getWaveLength());
            AudioFormat waveFormat = panel.getWaveFormat();
            sampleRateTextField.setText(String.valueOf((long) waveFormat.getSampleRate()));
            channelsTextField.setText(String.valueOf(waveFormat.getChannels()));
            encodingTextField.setText(String.valueOf(waveFormat.getEncoding().toString()));
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel channelsLabel;
    private javax.swing.JTextField channelsTextField;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel documentPanel;
    private javax.swing.JLabel encodingLabel;
    private javax.swing.JTextField encodingTextField;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JLabel sampleRateLabel;
    private javax.swing.JTextField sampleRateTextField;
    private javax.swing.JLabel waveLengthLabel;
    private javax.swing.JTextField waveLengthTextField;
    // End of variables declaration//GEN-END:variables

}