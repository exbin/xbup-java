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
package org.xbup.lib.framework.gui.about;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
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
import org.xbup.lib.framework.api.XBApplicationModule;
import org.xbup.lib.framework.api.XBApplication;
import org.xbup.lib.framework.gui.utils.BareBonesBrowserLaunch;
import org.xbup.lib.framework.gui.utils.WindowUtils;

/**
 * Basic about dialog.
 *
 * @version 0.2.0 2015/12/03
 * @author ExBin Project (http://exbin.org)
 */
public class AboutDialog extends javax.swing.JDialog implements HyperlinkListener {

    private static final String BUNDLE_PACKAGE = "org/xbup/lib/framework/gui/about/resources/";
    private final XBApplication appEditor;
    private ResourceBundle appBundle;
    private final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PACKAGE + "AboutDialog");

    public AboutDialog(java.awt.Frame parent, boolean modal, XBApplication appEditor) {
        super(parent, modal);

        this.appEditor = appEditor;
        if (appEditor != null) {
            appBundle = appEditor.getAppBundle();
        } else {
            appBundle = bundle;
        }

        init();
    }

    private void init() {
        initComponents();
        getRootPane().setDefaultButton(closeButton);
        HashMap<TextAttribute, Object> attribs = new HashMap<>();
        attribs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);

        // Fill system properties tab
        environmentTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    bundle.getString("environmentTable.propertyColumn"), bundle.getString("environmentTable.valueColumn")
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        Properties systemProperties = System.getProperties();
        DefaultTableModel tableModel = (DefaultTableModel) environmentTable.getModel();
        Set<java.util.Map.Entry<Object, Object>> items = systemProperties.entrySet();
        for (java.util.Map.Entry<Object, Object> entry : items) {
            Object[] line = new Object[2];
            line[0] = entry.getKey();
            line[1] = entry.getValue();
            tableModel.addRow(line);
        }

        // Fill list of modules
        modulesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    bundle.getString("modulesTable.moduleNameColumn"), bundle.getString("modulesTable.moduleDescriptionColumn")
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        if (appEditor.getModuleRepository() != null) {
            DefaultTableModel modulesTableModel = (DefaultTableModel) modulesTable.getModel();
            List<XBApplicationModule> modulesList = appEditor.getModuleRepository().getModulesList();
            for (XBApplicationModule moduleInfo : modulesList) {
                String[] newRow = {moduleInfo.getName(), moduleInfo.getDescription()};
                modulesTableModel.addRow(newRow);
            }
        }

        // Load license
        try {
            String licenseFilePath = appBundle.getString("Application.licenseFile");
            if (licenseFilePath != null && !licenseFilePath.isEmpty()) {
                licenseEditorPane.setPage(getClass().getResource(licenseFilePath));
            }
            licenseEditorPane.addHyperlinkListener(this);
        } catch (IOException ex) {
            Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        WindowUtils.initWindow(this);
        WindowUtils.assignGlobalKeyListener(this, closeButton);
    }

    /**
     * Opens hyperlink in external browser.
     *
     * @param event hyperlink event
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            BareBonesBrowserLaunch.openURL(event.getURL().toExternalForm());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        linkPopupMenu = new javax.swing.JPopupMenu();
        copyLinkMenuItem = new javax.swing.JMenuItem();
        closeButton = new javax.swing.JButton();
        productTabbedPane = new javax.swing.JTabbedPane();
        applicationPanel = new javax.swing.JPanel();
        javax.swing.JLabel nameLabel = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel homepageLabel = new javax.swing.JLabel();
        appHomepageLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
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
        jSeparator1 = new javax.swing.JSeparator();

        linkPopupMenu.setName("linkPopupMenu"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/about/resources/AboutDialog"); // NOI18N
        copyLinkMenuItem.setText(bundle.getString("copyLinkMenuItem.text")); // NOI18N
        copyLinkMenuItem.setName("copyLinkMenuItem"); // NOI18N
        copyLinkMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyLinkMenuItemActionPerformed(evt);
            }
        });
        linkPopupMenu.add(copyLinkMenuItem);

        setTitle(bundle.getString("aboutBox.title")); // NOI18N
        setLocationByPlatform(true);

        closeButton.setText(bundle.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        productTabbedPane.setMinimumSize(new java.awt.Dimension(38, 15));
        productTabbedPane.setName("productTabbedPane"); // NOI18N

        applicationPanel.setAutoscrolls(true);
        applicationPanel.setName("applicationPanel"); // NOI18N

        nameLabel.setFont(nameLabel.getFont().deriveFont(nameLabel.getFont().getStyle() | java.awt.Font.BOLD));
        nameLabel.setText(bundle.getString("nameLabel.text")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText(bundle.getString("vendorLabel.text")); // NOI18N
        vendorLabel.setName("vendorLabel"); // NOI18N

        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel.setText(bundle.getString("homepageLabel.text")); // NOI18N
        homepageLabel.setName("homepageLabel"); // NOI18N

        appHomepageLabel.setForeground(java.awt.Color.blue);
        appHomepageLabel.setText(appBundle.getString("Application.homepage"));
        appHomepageLabel.setComponentPopupMenu(linkPopupMenu);
        appHomepageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        appHomepageLabel.setName("appHomepageLabel"); // NOI18N
        HashMap<TextAttribute, Object> attribs = new HashMap<TextAttribute, Object>();
        attribs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
        appHomepageLabel.setFont(appHomepageLabel.getFont().deriveFont(attribs));
        appHomepageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appHomepageLabelMouseClicked(evt);
            }
        });

        nameTextField.setEditable(false);
        nameTextField.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        nameTextField.setText(appBundle.getString("Application.product"));
        nameTextField.setBorder(null);
        nameTextField.setName("nameTextField"); // NOI18N

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

        javax.swing.GroupLayout applicationPanelLayout = new javax.swing.GroupLayout(applicationPanel);
        applicationPanel.setLayout(applicationPanelLayout);
        applicationPanelLayout.setHorizontalGroup(
            applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(applicationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(applicationPanelLayout.createSequentialGroup()
                        .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(vendorLabel)
                            .addComponent(homepageLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                            .addComponent(vendorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                            .addComponent(licenseTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                            .addComponent(appHomepageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)))
                    .addGroup(applicationPanelLayout.createSequentialGroup()
                        .addComponent(appLicenseLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        applicationPanelLayout.setVerticalGroup(
            applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(applicationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendorLabel)
                    .addComponent(vendorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(appLicenseLabel)
                    .addComponent(licenseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(homepageLabel)
                    .addComponent(appHomepageLabel))
                .addContainerGap(235, Short.MAX_VALUE))
        );

        productTabbedPane.addTab(bundle.getString("applicationPanel.TabConstraints.tabTitle"), applicationPanel); // NOI18N

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
            .addComponent(authorsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
        );

        productTabbedPane.addTab(bundle.getString("authorsPanel.TabConstraints.tabTitle"), authorsPanel); // NOI18N

        licensePanel.setName("licensePanel"); // NOI18N

        licenseScrollPane.setName("licenseScrollPane"); // NOI18N

        licenseEditorPane.setEditable(false);
        licenseEditorPane.setContentType("text/html"); // NOI18N
        licenseEditorPane.setText("<html>   <head>    </head>   <body>     <p style=\"margin-top: 0\"></p>   </body> </html> ");
        licenseEditorPane.setName("licenseEditorPane"); // NOI18N
        licenseScrollPane.setViewportView(licenseEditorPane);

        javax.swing.GroupLayout licensePanelLayout = new javax.swing.GroupLayout(licensePanel);
        licensePanel.setLayout(licensePanelLayout);
        licensePanelLayout.setHorizontalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(licenseScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        licensePanelLayout.setVerticalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(licenseScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
        );

        productTabbedPane.addTab(bundle.getString("licensePanel.TabConstraints.tabTitle"), licensePanel); // NOI18N

        modulesPanel.setEnabled(false);
        modulesPanel.setName("modulesPanel"); // NOI18N

        modulesScrollPane.setName("modulesScrollPane"); // NOI18N
        modulesScrollPane.setViewportView(modulesTable);

        javax.swing.GroupLayout modulesPanelLayout = new javax.swing.GroupLayout(modulesPanel);
        modulesPanel.setLayout(modulesPanelLayout);
        modulesPanelLayout.setHorizontalGroup(
            modulesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        modulesPanelLayout.setVerticalGroup(
            modulesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
        );

        productTabbedPane.addTab(bundle.getString("modulesPanel.TabConstraints.tabTitle"), modulesPanel); // NOI18N

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
        environmentScrollPane.setViewportView(environmentTable);

        javax.swing.GroupLayout environmentPanelLayout = new javax.swing.GroupLayout(environmentPanel);
        environmentPanel.setLayout(environmentPanelLayout);
        environmentPanelLayout.setHorizontalGroup(
            environmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(environmentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        environmentPanelLayout.setVerticalGroup(
            environmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(environmentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
        );

        productTabbedPane.addTab(bundle.getString("environmentPanel.TabConstraints.tabTitle"), environmentPanel); // NOI18N

        aboutHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        aboutHeaderPanel.setName("aboutHeaderPanel"); // NOI18N

        imageLabel.setBackground(new java.awt.Color(255, 255, 255));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(appBundle.getString("Application.aboutImage"))));
        imageLabel.setName("imageLabel"); // NOI18N
        imageLabel.setOpaque(true);

        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize()+4));
        appTitleLabel.setText(appBundle.getString("Application.name"));
        appTitleLabel.setName("appTitleLabel"); // NOI18N

        appDescLabel.setText(appBundle.getString("Application.description"));
        appDescLabel.setName("appDescLabel"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

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
            .addComponent(jSeparator1)
        );
        aboutHeaderPanelLayout.setVerticalGroup(
            aboutHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutHeaderPanelLayout.createSequentialGroup()
                .addGroup(aboutHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(aboutHeaderPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(appTitleLabel)
                        .addGap(7, 7, 7)
                        .addComponent(appDescLabel))
                    .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aboutHeaderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(productTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(aboutHeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void appHomepageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_appHomepageLabelMouseClicked
        if (!evt.isPopupTrigger()) {
            String targetURL = ((JLabel) evt.getSource()).getText();
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    java.net.URI uri = new java.net.URI(targetURL);
                    desktop.browse(uri);
                } catch (IOException | URISyntaxException ex) {
                    Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                BareBonesBrowserLaunch.openURL(targetURL);
            }
        }
    }//GEN-LAST:event_appHomepageLabelMouseClicked

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        productTabbedPane.setSelectedIndex(0);
        WindowUtils.closeWindow(this);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void copyLinkMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyLinkMenuItemActionPerformed
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(appHomepageLabel.getText()), null);
    }//GEN-LAST:event_copyLinkMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeWindow(new AboutDialog(new javax.swing.JFrame(), true, WindowUtils.getDefaultAppEditor()));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aboutHeaderPanel;
    private javax.swing.JLabel appHomepageLabel;
    private javax.swing.JPanel applicationPanel;
    private javax.swing.JPanel authorsPanel;
    private javax.swing.JScrollPane authorsScrollPane;
    private javax.swing.JTextArea authorsTextArea;
    private javax.swing.JButton closeButton;
    private javax.swing.JMenuItem copyLinkMenuItem;
    private javax.swing.JPanel environmentPanel;
    private javax.swing.JScrollPane environmentScrollPane;
    private javax.swing.JTable environmentTable;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JEditorPane licenseEditorPane;
    private javax.swing.JPanel licensePanel;
    private javax.swing.JScrollPane licenseScrollPane;
    private javax.swing.JTextField licenseTextField;
    private javax.swing.JPopupMenu linkPopupMenu;
    private javax.swing.JPanel modulesPanel;
    private javax.swing.JScrollPane modulesScrollPane;
    private javax.swing.JTable modulesTable;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTabbedPane productTabbedPane;
    private javax.swing.JTextField vendorTextField;
    // End of variables declaration//GEN-END:variables

    public ResourceBundle getProjectResourceBundle() {
        return appBundle;
    }

    public void setProjectResourceBundle(ResourceBundle projectResourceBundle) {
        this.appBundle = projectResourceBundle;
    }
}
