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
package org.xbup.tool.editor.module.frame.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;
import org.xbup.tool.editor.module.frame.util.BareBonesBrowserLaunch;
import org.xbup.tool.editor.base.api.ApplicationModuleInfo;
import org.xbup.tool.editor.base.api.XBEditorApp;

/**
 * MainFrame About Dialog.
 *
 * @version 0.1 wr22.0 2013/04/14
 * @author XBUP Project (http://xbup.org)
 */
public class AboutDialog extends javax.swing.JDialog implements HyperlinkListener {

    private XBEditorApp appEditor;
    private ResourceBundle appBundle;

    /** Creates new form AboutDialog */
    public AboutDialog(java.awt.Frame parent, boolean modal, XBEditorApp appEditor) {
        super(parent, modal);

        this.appEditor = appEditor;
        if (appEditor != null) {
            setIconImage(appEditor.getApplicationIcon());
            appBundle = appEditor.getAppBundle();
        } else {
            appBundle = java.util.ResourceBundle.getBundle("org/xbup/tool/xbeditor/module/mainframe/dialog/resources/AboutDialog");
        }

        init();
    }

    private void init() {
        initComponents();
        getRootPane().setDefaultButton(closeButton);
        HashMap<TextAttribute, Object> attribs = new HashMap<TextAttribute, Object>();
        attribs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
        assignGlobalKeyListener(this);

        // Fill system properties tab
        Properties systemProperties = System.getProperties();
        DefaultTableModel tableModel = (DefaultTableModel) environmentTable.getModel();
        Set<java.util.Map.Entry<Object,Object>> items = systemProperties.entrySet();
        for (Iterator<java.util.Map.Entry<Object, Object>> it = items.iterator(); it.hasNext();) {
            java.util.Map.Entry<Object, Object> entry = it.next();
            Object[] line = new Object[2];
            line[0] = entry.getKey();
            line[1] = entry.getValue();
            tableModel.addRow(line);
        }

        // Fill list of modules
        if (appEditor.getModuleRepository() != null) {
            DefaultTableModel modulesTableModel = (DefaultTableModel) modulesTable.getModel();
            List<ApplicationModuleInfo> modulesList = appEditor.getModuleRepository().getModulesList();
            for (int moduleIndex = 0; moduleIndex < modulesList.size(); moduleIndex++) {
                ApplicationModuleInfo moduleInfo = modulesList.get(moduleIndex);
                String[] newRow = {moduleInfo.getPluginName(), moduleInfo.getPluginDescription()};
                modulesTableModel.addRow(newRow);
            }
        }

        // Load license
        try {
            String licenseFilePath = appBundle.getString("Application.licenseFile");
            licenseEditorPane.setPage(getClass().getResource(licenseFilePath));
            licenseEditorPane.addHyperlinkListener(this);
        } catch (IOException ex) {
            Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                        if ((evt.getKeyCode() == KeyEvent.VK_ENTER)||(evt.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                            closeButtonActionPerformed(null);
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

    /** Open hyperlinks in external browser */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            BareBonesBrowserLaunch.openURL(event.getURL().toExternalForm());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        aboutTabbedPane = new javax.swing.JTabbedPane();
        aboutPanel = new javax.swing.JPanel();
        javax.swing.JLabel productLabel = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel homepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appHomepageLabel = new javax.swing.JLabel();
        appHomepageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        productTextField = new javax.swing.JTextField();
        vendorTextField = new javax.swing.JTextField();
        javax.swing.JLabel appLicenseLabel = new javax.swing.JLabel();
        licenseTextField = new javax.swing.JTextField();
        authorsPanel = new javax.swing.JPanel();
        authorsScrollPane = new javax.swing.JScrollPane();
        authorsTextArea = new javax.swing.JTextArea();
        licensePanel = new javax.swing.JPanel();
        licenseScrollPane = new javax.swing.JScrollPane();
        licenseEditorPane = new javax.swing.JEditorPane();
        modulesPanel = new javax.swing.JPanel();
        modulesScrollPane = new javax.swing.JScrollPane();
        modulesTable = new javax.swing.JTable();
        environmentPanel = new javax.swing.JPanel();
        environmentScrollPane = new javax.swing.JScrollPane();
        environmentTable = new javax.swing.JTable();
        aboutHeaderPanel = new javax.swing.JPanel();
        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appTitleLabel = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/xbeditor/module/mainframe/dialog/resources/AboutDialog"); // NOI18N
        setTitle(bundle.getString("aboutBox.title")); // NOI18N

        closeButton.setText(bundle.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        aboutTabbedPane.setMinimumSize(new java.awt.Dimension(38, 15));
        aboutTabbedPane.setName("aboutTabbedPane"); // NOI18N

        aboutPanel.setAutoscrolls(true);
        aboutPanel.setName("aboutPanel"); // NOI18N

        productLabel.setFont(productLabel.getFont().deriveFont(productLabel.getFont().getStyle() | java.awt.Font.BOLD));
        productLabel.setText(bundle.getString("productLabel.text")); // NOI18N
        productLabel.setName("productLabel"); // NOI18N

        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText(bundle.getString("vendorLabel.text")); // NOI18N
        vendorLabel.setName("vendorLabel"); // NOI18N

        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel.setText(bundle.getString("homepageLabel.text")); // NOI18N
        homepageLabel.setName("homepageLabel"); // NOI18N

        appHomepageLabel.setForeground(java.awt.Color.blue);
        appHomepageLabel.setText(appBundle.getString("Application.homepage"));
        appHomepageLabel.setName("appHomepageLabel"); // NOI18N
        HashMap<TextAttribute, Object> attribs = new HashMap<TextAttribute, Object>();
        attribs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
        appHomepageLabel.setFont(appHomepageLabel.getFont().deriveFont(attribs));
        appHomepageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appHomepageLabelMouseClicked(evt);
            }
        });

        productTextField.setEditable(false);
        productTextField.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        productTextField.setText(appBundle.getString("Application.product"));
        productTextField.setBorder(null);
        productTextField.setName("productTextField"); // NOI18N

        vendorTextField.setEditable(false);
        vendorTextField.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        vendorTextField.setText(appBundle.getString("Application.vendor"));
        vendorTextField.setBorder(null);
        vendorTextField.setName("vendorTextField"); // NOI18N

        appLicenseLabel.setFont(appLicenseLabel.getFont().deriveFont(appLicenseLabel.getFont().getStyle() | java.awt.Font.BOLD));
        appLicenseLabel.setText(bundle.getString("appLicenseLabel.text")); // NOI18N
        appLicenseLabel.setName("appLicenseLabel"); // NOI18N

        licenseTextField.setEditable(false);
        licenseTextField.setFont(new java.awt.Font("Dialog 12", 1, 12)); // NOI18N
        licenseTextField.setText(appBundle.getString("Application.license"));
        licenseTextField.setBorder(null);
        licenseTextField.setName("licenseTextField"); // NOI18N

        javax.swing.GroupLayout aboutPanelLayout = new javax.swing.GroupLayout(aboutPanel);
        aboutPanel.setLayout(aboutPanelLayout);
        aboutPanelLayout.setHorizontalGroup(
            aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(aboutPanelLayout.createSequentialGroup()
                        .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productLabel)
                            .addComponent(vendorLabel)
                            .addComponent(homepageLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                            .addComponent(vendorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                            .addComponent(licenseTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                            .addComponent(appHomepageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)))
                    .addGroup(aboutPanelLayout.createSequentialGroup()
                        .addComponent(appLicenseLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        aboutPanelLayout.setVerticalGroup(
            aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productLabel)
                    .addComponent(productTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendorLabel)
                    .addComponent(vendorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(appLicenseLabel)
                    .addComponent(licenseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(homepageLabel)
                    .addComponent(appHomepageLabel))
                .addContainerGap(163, Short.MAX_VALUE))
        );

        aboutTabbedPane.addTab(bundle.getString("aboutPanel.TabConstraints.tabTitle"), aboutPanel); // NOI18N

        authorsPanel.setName("authorsPanel"); // NOI18N

        authorsScrollPane.setName("authorsScrollPane"); // NOI18N

        authorsTextArea.setEditable(false);
        authorsTextArea.setText(appBundle.getString("Application.authors"));
        authorsTextArea.setName("authorsTextArea"); // NOI18N
        authorsScrollPane.setViewportView(authorsTextArea);

        javax.swing.GroupLayout authorsPanelLayout = new javax.swing.GroupLayout(authorsPanel);
        authorsPanel.setLayout(authorsPanelLayout);
        authorsPanelLayout.setHorizontalGroup(
            authorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(authorsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        authorsPanelLayout.setVerticalGroup(
            authorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(authorsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );

        aboutTabbedPane.addTab(bundle.getString("authorsPanel.TabConstraints.tabTitle"), authorsPanel); // NOI18N

        licensePanel.setName("licensePanel"); // NOI18N

        licenseScrollPane.setName("licenseScrollPane"); // NOI18N

        licenseEditorPane.setContentType("text/html"); // NOI18N
        licenseEditorPane.setEditable(false);
        licenseEditorPane.setText("<html>   <head>    </head>   <body>     <p style=\"margin-top: 0\"></p>   </body> </html> ");
        licenseEditorPane.setToolTipText("");
        licenseEditorPane.setName("licenseEditorPane"); // NOI18N
        licenseScrollPane.setViewportView(licenseEditorPane);

        javax.swing.GroupLayout licensePanelLayout = new javax.swing.GroupLayout(licensePanel);
        licensePanel.setLayout(licensePanelLayout);
        licensePanelLayout.setHorizontalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(licenseScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        licensePanelLayout.setVerticalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(licenseScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );

        aboutTabbedPane.addTab(bundle.getString("licensePanel.TabConstraints.tabTitle"), licensePanel); // NOI18N

        modulesPanel.setEnabled(false);
        modulesPanel.setName("modulesPanel"); // NOI18N

        modulesScrollPane.setName("modulesScrollPane"); // NOI18N

        modulesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Module Name", "Provider"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        modulesTable.setName("modulesTable"); // NOI18N
        modulesScrollPane.setViewportView(modulesTable);

        javax.swing.GroupLayout modulesPanelLayout = new javax.swing.GroupLayout(modulesPanel);
        modulesPanel.setLayout(modulesPanelLayout);
        modulesPanelLayout.setHorizontalGroup(
            modulesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        modulesPanelLayout.setVerticalGroup(
            modulesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );

        aboutTabbedPane.addTab(bundle.getString("modulesPanel.TabConstraints.tabTitle"), modulesPanel); // NOI18N

        environmentPanel.setName("environmentPanel"); // NOI18N

        environmentScrollPane.setName("environmentScrollPane"); // NOI18N

        environmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Property", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        environmentTable.setName("environmentTable"); // NOI18N
        environmentScrollPane.setViewportView(environmentTable);

        javax.swing.GroupLayout environmentPanelLayout = new javax.swing.GroupLayout(environmentPanel);
        environmentPanel.setLayout(environmentPanelLayout);
        environmentPanelLayout.setHorizontalGroup(
            environmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(environmentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        environmentPanelLayout.setVerticalGroup(
            environmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(environmentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );

        aboutTabbedPane.addTab(bundle.getString("environmentPanel.TabConstraints.tabTitle"), environmentPanel); // NOI18N

        aboutHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        aboutHeaderPanel.setName("aboutHeaderPanel"); // NOI18N

        imageLabel.setBackground(new java.awt.Color(255, 255, 255));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(appBundle.getString("Application.aboutIcon"))));
        imageLabel.setName("imageLabel"); // NOI18N
        imageLabel.setOpaque(true);

        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize()+4));
        appTitleLabel.setText(appBundle.getString("Application.name"));
        appTitleLabel.setName("appTitleLabel"); // NOI18N

        appDescLabel.setText(appBundle.getString("Application.description"));
        appDescLabel.setName("appDescLabel"); // NOI18N

        javax.swing.GroupLayout aboutHeaderPanelLayout = new javax.swing.GroupLayout(aboutHeaderPanel);
        aboutHeaderPanel.setLayout(aboutHeaderPanelLayout);
        aboutHeaderPanelLayout.setHorizontalGroup(
            aboutHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutHeaderPanelLayout.createSequentialGroup()
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(aboutHeaderPanelLayout.createSequentialGroup()
                        .addComponent(appTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(235, 235, 235))
                    .addGroup(aboutHeaderPanelLayout.createSequentialGroup()
                        .addComponent(appDescLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        aboutHeaderPanelLayout.setVerticalGroup(
            aboutHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutHeaderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(appDescLabel)
                .addContainerGap())
            .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aboutHeaderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(aboutTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(aboutHeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aboutTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void appHomepageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_appHomepageLabelMouseClicked
        String targetURL = ((JLabel) evt.getSource()).getText();
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            try {
                java.net.URI uri = new java.net.URI(targetURL);
                desktop.browse(uri);
            } catch (IOException ex) {
                Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            BareBonesBrowserLaunch.openURL(targetURL);
        }
    }//GEN-LAST:event_appHomepageLabelMouseClicked

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        aboutTabbedPane.setSelectedIndex(0);
        setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

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
            java.util.logging.Logger.getLogger(AboutDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AboutDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AboutDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AboutDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                AboutDialog dialog = new AboutDialog(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JPanel aboutHeaderPanel;
    private javax.swing.JPanel aboutPanel;
    private javax.swing.JTabbedPane aboutTabbedPane;
    private javax.swing.JPanel authorsPanel;
    private javax.swing.JScrollPane authorsScrollPane;
    private javax.swing.JTextArea authorsTextArea;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel environmentPanel;
    private javax.swing.JScrollPane environmentScrollPane;
    private javax.swing.JTable environmentTable;
    private javax.swing.JEditorPane licenseEditorPane;
    private javax.swing.JPanel licensePanel;
    private javax.swing.JScrollPane licenseScrollPane;
    private javax.swing.JTextField licenseTextField;
    private javax.swing.JPanel modulesPanel;
    private javax.swing.JScrollPane modulesScrollPane;
    private javax.swing.JTable modulesTable;
    private javax.swing.JTextField productTextField;
    private javax.swing.JTextField vendorTextField;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the appBundle
     */
    public ResourceBundle getProjectResourceBundle() {
        return appBundle;
    }

    /**
     * @param appBundle the appBundle to set
     */
    public void setProjectResourceBundle(ResourceBundle projectResourceBundle) {
        this.appBundle = projectResourceBundle;
    }
}
