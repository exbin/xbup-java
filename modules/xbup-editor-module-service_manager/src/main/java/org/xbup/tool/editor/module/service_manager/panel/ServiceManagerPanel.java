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
package org.xbup.tool.editor.module.service_manager.panel;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.client.catalog.XBARCatalog;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.service.XBCXHDocService;
import org.xbup.lib.core.catalog.base.service.XBCXIconService;
import org.xbup.lib.core.catalog.base.service.XBCXLangService;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.service.XBCXPaneService;
import org.xbup.lib.core.catalog.base.service.XBCXPlugService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.client.XBCatalogNetServiceClient;
import org.xbup.lib.client.catalog.remote.service.XBRXDescService;
import org.xbup.lib.client.catalog.remote.service.XBRXFileService;
import org.xbup.lib.client.catalog.remote.service.XBRXHDocService;
import org.xbup.lib.client.catalog.remote.service.XBRXIconService;
import org.xbup.lib.client.catalog.remote.service.XBRXLangService;
import org.xbup.lib.client.catalog.remote.service.XBRXLineService;
import org.xbup.lib.client.catalog.remote.service.XBRXNameService;
import org.xbup.lib.client.catalog.remote.service.XBRXPaneService;
import org.xbup.lib.client.catalog.remote.service.XBRXPlugService;
import org.xbup.lib.client.catalog.remote.service.XBRXStriService;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.service.XBEXDescService;
import org.xbup.lib.catalog.entity.service.XBEXFileService;
import org.xbup.lib.catalog.entity.service.XBEXHDocService;
import org.xbup.lib.catalog.entity.service.XBEXIconService;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.catalog.entity.service.XBEXLineService;
import org.xbup.lib.catalog.entity.service.XBEXNameService;
import org.xbup.lib.catalog.entity.service.XBEXPaneService;
import org.xbup.lib.catalog.entity.service.XBEXPlugService;
import org.xbup.lib.catalog.entity.service.XBEXStriService;
import org.xbup.lib.catalog.remote.XBDbServiceClient;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.ApplicationPanel;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.MenuManagement;

/**
 * XBManager Service Management Panel.
 *
 * @version 0.1.24 2014/10/03
 * @author XBUP Project (http://xbup.org)
 */
public class ServiceManagerPanel extends javax.swing.JPanel implements ApplicationPanel, ActivePanelActionHandling {

    private XBServiceClient service;
    private final CatalogBrowserPanel catalogPanel;
    private MenuManagement menuManagement;

    public ServiceManagerPanel() {
        initComponents();

        catalogPanel = new CatalogBrowserPanel();
        cardPanel.add(catalogPanel, "catalog");
        cardPanel.add(new CatalogUpdatePanel(), "update");
        cardPanel.add(new TransformationPluginsPanel(), "transplug");
        managerTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        MutableTreeNode top = new MutableTreeNode("Root", "");
        createNodes(top);
        managerTree.setModel(new DefaultTreeModel(top, true));
        managerTree.getSelectionModel().addTreeSelectionListener(new MySelectionListener());
        managerTree.setSelectionRow(0);

    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode item = new MutableTreeNode("Server Information", "info");
//        item.add(new MutableTreeNode("Connection","empty"));
        top.add(item);
        item = new MutableTreeNode("Service Control", "control");
        top.add(item);
        item = new MutableTreeNode("Service Startup", "startup");
        top.add(item);
        item = new MutableTreeNode("Catalog", "catalog");
        item.add(new MutableTreeNode("Update Service", "update"));
        top.add(item);
        item = new MutableTreeNode("Plugin", "empty");
        item.add(new MutableTreeNode("Transformations", "transplug"));
        top.add(item);
    }

    public XBServiceClient getService() {
        return service;
    }

    public void setService(XBServiceClient service) {
        this.service = service;
        if (service == null) {
            String unknown = "unknown";
            serviceVersionTextField.setText(unknown);
            connectionHostTextField.setText(unknown);
            connectionPortTextField.setText(unknown);
            connectionProtocolTextField.setText(unknown);
            serviceNameTextField.setText(unknown);
            serviceNetworkIPTextField.setText(unknown);
            managerNameTextField.setText(unknown);
            managerNetworkIPTextField.setText(unknown);
            managerSystemTextField.setText(unknown);
            managerHardwareTextField.setText(unknown);

            getCatalogPanel().setCatalog(null);

            return;
        }

        XBACatalog catalog = null;
        if (service instanceof XBDbServiceClient) {
            serviceVersionTextField.setText(service.getVersion());
            connectionHostTextField.setText(service.getHost());
            connectionPortTextField.setText(Integer.toString(service.getPort()));
            connectionProtocolTextField.setText("Dummy (DB)");
            serviceNameTextField.setText("XBService");
            serviceNetworkIPTextField.setText(service.getHostAddress());
            managerNameTextField.setText("XBManager");
            managerNetworkIPTextField.setText(service.getLocalAddress());
            managerSystemTextField.setText(System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")");
            managerHardwareTextField.setText(System.getProperty("os.arch"));
            EntityManagerFactory emf = ((XBDbServiceClient) service).getEntityManagerFactory();
            try {
                catalog = new XBAECatalog(emf.createEntityManager(), System.getProperty("user.home") + "/.XBUP/repository"); // TODO: Kill on failure
                if (((XBAECatalog) catalog).isShallInit()) {
                    ((XBAECatalog) catalog).initCatalog();
                }
                ((XBAECatalog) catalog).addCatalogService(XBCXLangService.class, new XBEXLangService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXStriService.class, new XBEXStriService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXNameService.class, new XBEXNameService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXDescService.class, new XBEXDescService((XBAECatalog) catalog));

                ((XBAECatalog) catalog).addCatalogService(XBCXFileService.class, new XBEXFileService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXIconService.class, new XBEXIconService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXPlugService.class, new XBEXPlugService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXLineService.class, new XBEXLineService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXPaneService.class, new XBEXPaneService((XBAECatalog) catalog));
                ((XBAECatalog) catalog).addCatalogService(XBCXHDocService.class, new XBEXHDocService((XBAECatalog) catalog));
            } catch (Exception ex) {
                Logger.getLogger(ServiceManagerPanel.class.getName()).log(Level.SEVERE, null, ex);
                catalog = null;
            }
        } else {
            if (service.validate()) {
                serviceVersionTextField.setText(service.getVersion());
                connectionHostTextField.setText(service.getHost());
                connectionPortTextField.setText(Integer.toString(service.getPort()));
                connectionProtocolTextField.setText("UDP (IP)");
                serviceNameTextField.setText("XBService");
                serviceNetworkIPTextField.setText(service.getHostAddress());
                managerNameTextField.setText("XBManager");
                managerNetworkIPTextField.setText(service.getLocalAddress());
                managerSystemTextField.setText(System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")");
                managerHardwareTextField.setText(System.getProperty("os.arch"));
                jButton2.setEnabled(true);
                XBCatalogNetServiceClient serviceClient = new XBCatalogNetServiceClient(service.getHost(), service.getPort());
                if (serviceClient.validate()) {
                    catalog = new XBARCatalog(serviceClient); // 22594 is 0x5842 (XB)
                    ((XBARCatalog) catalog).addCatalogService(XBCXLangService.class, new XBRXLangService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXStriService.class, new XBRXStriService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXNameService.class, new XBRXNameService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXDescService.class, new XBRXDescService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXFileService.class, new XBRXFileService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXIconService.class, new XBRXIconService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXPlugService.class, new XBRXPlugService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXLineService.class, new XBRXLineService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXPaneService.class, new XBRXPaneService((XBARCatalog) catalog));
                    ((XBARCatalog) catalog).addCatalogService(XBCXHDocService.class, new XBRXHDocService((XBARCatalog) catalog));
                } else {
                    catalog = null;
                }
            }
        }

        getCatalogPanel().setCatalog(catalog);
    }

    /**
     * @return the catalogPanel
     */
    public CatalogBrowserPanel getCatalogPanel() {
        return catalogPanel;
    }

    @Override
    public boolean updateActionStatus(Component component) {
        // String test = ((CardLayout) cardPanel.getLayout()).toString();
        // if ("catalog".equals(test)) {
        catalogPanel.updateActionStatus(component);
        // }

        return false;
    }

    @Override
    public void releaseActionStatus() {
        // if ("catalog".equals(((CardLayout) cardPanel.getLayout()).toString())) {
        catalogPanel.releaseActionStatus();
        // }
    }

    @Override
    public boolean performAction(String eventName, ActionEvent event) {
        // if ("catalog".equals(((CardLayout) cardPanel.getLayout()).toString())) {
        return catalogPanel.performAction(eventName, event);
        // }

        // return false;
    }

    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;
        catalogPanel.setMenuManagement(menuManagement);
    }

    public void setMainFrameManagement(MainFrameManagement mainFramenManagement) {
        catalogPanel.setMainFrameManagement(mainFramenManagement);
    }

    private class MutableTreeNode extends DefaultMutableTreeNode {

        private final String caption;

        public MutableTreeNode(Object userObject, String caption) {
            super(userObject);
            this.caption = caption;
        }

        public String getCaption() {
            return caption;
        }
    }

    private class MySelectionListener implements TreeSelectionListener {

        private TreePath lastPath;

        public MySelectionListener() {
            super();
            lastPath = null;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath itemPath = e.getPath();
            if (lastPath != null) { // Colapse last relevant node
                if (!lastPath.equals(itemPath)) {
                    if (itemPath.getPathCount() <= lastPath.getPathCount()) {
                        ArrayList<Object> collapsePath = new ArrayList<>();
                        int i;
                        for (i = 0; i < itemPath.getPathCount(); i++) {
                            if (!itemPath.getPathComponent(i).equals(lastPath.getPathComponent(i))) {
                                break;
                            }
                            collapsePath.add(itemPath.getPathComponent(i));
                        }
                        if (i <= itemPath.getPathCount()) {
                            collapsePath.add(lastPath.getPathComponent(i));
                            managerTree.collapsePath(new TreePath(collapsePath.toArray()));
                        }
                    }
                }
            }
            if (itemPath != null) {
                lastPath = itemPath;
                managerTree.expandPath(itemPath);
                String caption = ((MutableTreeNode) managerTree.getLastSelectedPathComponent()).getCaption();
                if (caption != null) {
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, caption);
                } else {
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, "empty");
                }
            }
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        managerTree = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        cardPanel = new javax.swing.JPanel();
        serviceInfoPanel = new javax.swing.JPanel();
        serviceInfoPanelLabel = new javax.swing.JLabel();
        connectionInfoBorderPanel = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        connectionHostLabel = new javax.swing.JLabel();
        connectionHostTextField = new javax.swing.JTextField();
        connectionPortLabel = new javax.swing.JLabel();
        connectionPortTextField = new javax.swing.JTextField();
        connectionProtocolLabel = new javax.swing.JLabel();
        connectionProtocolTextField = new javax.swing.JTextField();
        serviceInfoBorderPanel = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        serviceNameLabel = new javax.swing.JLabel();
        serviceNameTextField = new javax.swing.JTextField();
        serviceVersionLabel = new javax.swing.JLabel();
        serviceVersionTextField = new javax.swing.JTextField();
        serviceNetworkNameLabel = new javax.swing.JLabel();
        serviceNetworkNameTextField = new javax.swing.JTextField();
        serviceNetworkIPLabel = new javax.swing.JLabel();
        serviceNetworkIPTextField = new javax.swing.JTextField();
        managerInfoBorderPanel = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        managerNameLabel = new javax.swing.JLabel();
        managerNameTextField = new javax.swing.JTextField();
        managerVersionLabel = new javax.swing.JLabel();
        managerVersionTextField = new javax.swing.JTextField();
        managerNetworkNameLabel = new javax.swing.JLabel();
        managerNetworkNameTextField = new javax.swing.JTextField();
        managerNetworkIPLabel = new javax.swing.JLabel();
        managerNetworkIPTextField = new javax.swing.JTextField();
        managerSystemLabel = new javax.swing.JLabel();
        managerSystemTextField = new javax.swing.JTextField();
        managerHardwareLabel = new javax.swing.JLabel();
        managerHardwareTextField = new javax.swing.JTextField();
        serviceStartupPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        emptyPanel = new javax.swing.JPanel();
        serviceControlPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        managerTree.setName("managerTree"); // NOI18N
        managerTree.setRootVisible(false);
        jScrollPane1.setViewportView(managerTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        cardPanel.setName("cardPanel"); // NOI18N
        cardPanel.setLayout(new java.awt.CardLayout());

        serviceInfoPanel.setName("serviceInfoPanel"); // NOI18N

        serviceInfoPanelLabel.setName("serviceInfoPanelLabel"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/service_manager/panel/resources/ServiceManagerPanel"); // NOI18N
        connectionInfoBorderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("connectionInfoBorderPanel.border.title"))); // NOI18N
        connectionInfoBorderPanel.setName("connectionInfoBorderPanel"); // NOI18N

        jPanel13.setName("jPanel13"); // NOI18N
        jPanel13.setLayout(new java.awt.GridLayout(3, 2, 5, 5));

        connectionHostLabel.setText(bundle.getString("hostNameLabel.text")); // NOI18N
        connectionHostLabel.setName("connectionHostLabel"); // NOI18N
        jPanel13.add(connectionHostLabel);

        connectionHostTextField.setEditable(false);
        connectionHostTextField.setText("unknown");
        connectionHostTextField.setBorder(null);
        connectionHostTextField.setName("connectionHostTextField"); // NOI18N
        jPanel13.add(connectionHostTextField);

        connectionPortLabel.setText(bundle.getString("connectionPortLabel.text")); // NOI18N
        connectionPortLabel.setName("connectionPortLabel"); // NOI18N
        jPanel13.add(connectionPortLabel);

        connectionPortTextField.setEditable(false);
        connectionPortTextField.setText("unknown");
        connectionPortTextField.setBorder(null);
        connectionPortTextField.setName("connectionPortTextField"); // NOI18N
        jPanel13.add(connectionPortTextField);

        connectionProtocolLabel.setText(bundle.getString("connectionProtocolLabel.text")); // NOI18N
        connectionProtocolLabel.setName("connectionProtocolLabel"); // NOI18N
        jPanel13.add(connectionProtocolLabel);

        connectionProtocolTextField.setEditable(false);
        connectionProtocolTextField.setText("unknown");
        connectionProtocolTextField.setBorder(null);
        connectionProtocolTextField.setName("connectionProtocolTextField"); // NOI18N
        jPanel13.add(connectionProtocolTextField);

        javax.swing.GroupLayout connectionInfoBorderPanelLayout = new javax.swing.GroupLayout(connectionInfoBorderPanel);
        connectionInfoBorderPanel.setLayout(connectionInfoBorderPanelLayout);
        connectionInfoBorderPanelLayout.setHorizontalGroup(
            connectionInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionInfoBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        connectionInfoBorderPanelLayout.setVerticalGroup(
            connectionInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionInfoBorderPanelLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        serviceInfoBorderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("serviceInfoBorderPanel.border.title"))); // NOI18N
        serviceInfoBorderPanel.setName("serviceInfoBorderPanel"); // NOI18N

        jPanel14.setName("jPanel14"); // NOI18N
        jPanel14.setLayout(new java.awt.GridLayout(4, 2, 5, 5));

        serviceNameLabel.setText(bundle.getString("serviceNameLabel.text")); // NOI18N
        serviceNameLabel.setName("serviceNameLabel"); // NOI18N
        jPanel14.add(serviceNameLabel);

        serviceNameTextField.setEditable(false);
        serviceNameTextField.setText("unknown");
        serviceNameTextField.setBorder(null);
        serviceNameTextField.setName("serviceNameTextField"); // NOI18N
        jPanel14.add(serviceNameTextField);

        serviceVersionLabel.setText(bundle.getString("serviceVersionLabel.text")); // NOI18N
        serviceVersionLabel.setName("serviceVersionLabel"); // NOI18N
        jPanel14.add(serviceVersionLabel);

        serviceVersionTextField.setEditable(false);
        serviceVersionTextField.setText("unknown");
        serviceVersionTextField.setBorder(null);
        serviceVersionTextField.setName("serviceVersionTextField"); // NOI18N
        jPanel14.add(serviceVersionTextField);

        serviceNetworkNameLabel.setText("Network Name:");
        serviceNetworkNameLabel.setName("serviceNetworkNameLabel"); // NOI18N
        jPanel14.add(serviceNetworkNameLabel);

        serviceNetworkNameTextField.setEditable(false);
        serviceNetworkNameTextField.setText("unknown");
        serviceNetworkNameTextField.setBorder(null);
        serviceNetworkNameTextField.setName("serviceNetworkNameTextField"); // NOI18N
        jPanel14.add(serviceNetworkNameTextField);

        serviceNetworkIPLabel.setText("IP Address:");
        serviceNetworkIPLabel.setName("serviceNetworkIPLabel"); // NOI18N
        jPanel14.add(serviceNetworkIPLabel);

        serviceNetworkIPTextField.setEditable(false);
        serviceNetworkIPTextField.setText("unknown");
        serviceNetworkIPTextField.setBorder(null);
        serviceNetworkIPTextField.setName("serviceNetworkIPTextField"); // NOI18N
        jPanel14.add(serviceNetworkIPTextField);

        javax.swing.GroupLayout serviceInfoBorderPanelLayout = new javax.swing.GroupLayout(serviceInfoBorderPanel);
        serviceInfoBorderPanel.setLayout(serviceInfoBorderPanelLayout);
        serviceInfoBorderPanelLayout.setHorizontalGroup(
            serviceInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInfoBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        serviceInfoBorderPanelLayout.setVerticalGroup(
            serviceInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInfoBorderPanelLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        managerInfoBorderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("managerInfoBorderPanel.border.title"))); // NOI18N
        managerInfoBorderPanel.setName("managerInfoBorderPanel"); // NOI18N

        jPanel15.setName("jPanel15"); // NOI18N
        jPanel15.setLayout(new java.awt.GridLayout(6, 2, 5, 5));

        managerNameLabel.setText(bundle.getString("managerNameLabel.text")); // NOI18N
        managerNameLabel.setName("managerNameLabel"); // NOI18N
        jPanel15.add(managerNameLabel);

        managerNameTextField.setEditable(false);
        managerNameTextField.setText("unknown");
        managerNameTextField.setBorder(null);
        managerNameTextField.setName("managerNameTextField"); // NOI18N
        jPanel15.add(managerNameTextField);

        managerVersionLabel.setText(bundle.getString("managerVersionLabel.text")); // NOI18N
        managerVersionLabel.setName("managerVersionLabel"); // NOI18N
        jPanel15.add(managerVersionLabel);

        managerVersionTextField.setEditable(false);
        managerVersionTextField.setText("unknown");
        managerVersionTextField.setBorder(null);
        managerVersionTextField.setName("managerVersionTextField"); // NOI18N
        jPanel15.add(managerVersionTextField);

        managerNetworkNameLabel.setText(bundle.getString("managerNetworkNameLabel.text")); // NOI18N
        managerNetworkNameLabel.setName("managerNetworkNameLabel"); // NOI18N
        jPanel15.add(managerNetworkNameLabel);

        managerNetworkNameTextField.setEditable(false);
        managerNetworkNameTextField.setText("unknown");
        managerNetworkNameTextField.setBorder(null);
        managerNetworkNameTextField.setName("managerNetworkNameTextField"); // NOI18N
        jPanel15.add(managerNetworkNameTextField);

        managerNetworkIPLabel.setText(bundle.getString("managerNetworkIPLabel.text")); // NOI18N
        managerNetworkIPLabel.setName("managerNetworkIPLabel"); // NOI18N
        jPanel15.add(managerNetworkIPLabel);

        managerNetworkIPTextField.setEditable(false);
        managerNetworkIPTextField.setText("unknown");
        managerNetworkIPTextField.setBorder(null);
        managerNetworkIPTextField.setName("managerNetworkIPTextField"); // NOI18N
        jPanel15.add(managerNetworkIPTextField);

        managerSystemLabel.setText(bundle.getString("managerSystemLabel.text")); // NOI18N
        managerSystemLabel.setName("managerSystemLabel"); // NOI18N
        jPanel15.add(managerSystemLabel);

        managerSystemTextField.setEditable(false);
        managerSystemTextField.setText("unknown");
        managerSystemTextField.setBorder(null);
        managerSystemTextField.setName("managerSystemTextField"); // NOI18N
        jPanel15.add(managerSystemTextField);

        managerHardwareLabel.setText(bundle.getString("managerHardwareLabel.text")); // NOI18N
        managerHardwareLabel.setName("managerHardwareLabel"); // NOI18N
        jPanel15.add(managerHardwareLabel);

        managerHardwareTextField.setEditable(false);
        managerHardwareTextField.setText("unknown");
        managerHardwareTextField.setBorder(null);
        managerHardwareTextField.setName("managerHardwareTextField"); // NOI18N
        jPanel15.add(managerHardwareTextField);

        javax.swing.GroupLayout managerInfoBorderPanelLayout = new javax.swing.GroupLayout(managerInfoBorderPanel);
        managerInfoBorderPanel.setLayout(managerInfoBorderPanelLayout);
        managerInfoBorderPanelLayout.setHorizontalGroup(
            managerInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(managerInfoBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        managerInfoBorderPanelLayout.setVerticalGroup(
            managerInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout serviceInfoPanelLayout = new javax.swing.GroupLayout(serviceInfoPanel);
        serviceInfoPanel.setLayout(serviceInfoPanelLayout);
        serviceInfoPanelLayout.setHorizontalGroup(
            serviceInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serviceInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(managerInfoBorderPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(connectionInfoBorderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(serviceInfoPanelLabel)
                    .addComponent(serviceInfoBorderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        serviceInfoPanelLayout.setVerticalGroup(
            serviceInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceInfoPanelLabel)
                .addGap(5, 5, 5)
                .addComponent(connectionInfoBorderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceInfoBorderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(managerInfoBorderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardPanel.add(serviceInfoPanel, "info");

        serviceStartupPanel.setEnabled(false);
        serviceStartupPanel.setName("serviceStartupPanel"); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Startup"));
        jPanel11.setName("jPanel11"); // NOI18N

        jCheckBox1.setText("Run on system startup");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setEnabled(false);
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.setName("jCheckBox1"); // NOI18N

        jCheckBox2.setText("Run on system login");
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox2.setEnabled(false);
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox2.setName("jCheckBox2"); // NOI18N

        jLabel15.setText("Run as");
        jLabel15.setName("jLabel15"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setEnabled(false);
        jComboBox1.setName("jComboBox1"); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel15))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jComboBox1, 0, 187, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Updating"));
        jPanel12.setName("jPanel12"); // NOI18N

        jCheckBox3.setText("Check for newer version on startup");
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setEnabled(false);
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox3.setName("jCheckBox3"); // NOI18N

        jLabel16.setText("Updating source");
        jLabel16.setName("jLabel16"); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setEnabled(false);
        jComboBox2.setName("jComboBox2"); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout serviceStartupPanelLayout = new javax.swing.GroupLayout(serviceStartupPanel);
        serviceStartupPanel.setLayout(serviceStartupPanelLayout);
        serviceStartupPanelLayout.setHorizontalGroup(
            serviceStartupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceStartupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serviceStartupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        serviceStartupPanelLayout.setVerticalGroup(
            serviceStartupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceStartupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardPanel.add(serviceStartupPanel, "startup");

        emptyPanel.setEnabled(false);
        emptyPanel.setName("emptyPanel"); // NOI18N
        emptyPanel.setLayout(new java.awt.BorderLayout());
        cardPanel.add(emptyPanel, "empty");

        serviceControlPanel.setEnabled(false);
        serviceControlPanel.setName("serviceControlPanel"); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Service Status"));
        jPanel8.setName("jPanel8"); // NOI18N

        jButton1.setText("Restart");
        jButton1.setEnabled(false);
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setText("Stop");
        jButton2.setEnabled(false);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Start");
        jButton3.setEnabled(false);
        jButton3.setName("jButton3"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Log Messages"));
        jPanel9.setName("jPanel9"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout serviceControlPanelLayout = new javax.swing.GroupLayout(serviceControlPanel);
        serviceControlPanel.setLayout(serviceControlPanelLayout);
        serviceControlPanelLayout.setHorizontalGroup(
            serviceControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serviceControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        serviceControlPanelLayout.setVerticalGroup(
            serviceControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardPanel.add(serviceControlPanel, "control");

        jScrollPane3.setViewportView(cardPanel);

        jSplitPane1.setRightComponent(jScrollPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 711, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //getService().stop();
}//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardPanel;
    private javax.swing.JLabel connectionHostLabel;
    private javax.swing.JTextField connectionHostTextField;
    private javax.swing.JPanel connectionInfoBorderPanel;
    private javax.swing.JLabel connectionPortLabel;
    private javax.swing.JTextField connectionPortTextField;
    private javax.swing.JLabel connectionProtocolLabel;
    private javax.swing.JTextField connectionProtocolTextField;
    private javax.swing.JPanel emptyPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel managerHardwareLabel;
    private javax.swing.JTextField managerHardwareTextField;
    private javax.swing.JPanel managerInfoBorderPanel;
    private javax.swing.JLabel managerNameLabel;
    private javax.swing.JTextField managerNameTextField;
    private javax.swing.JLabel managerNetworkIPLabel;
    private javax.swing.JTextField managerNetworkIPTextField;
    private javax.swing.JLabel managerNetworkNameLabel;
    private javax.swing.JTextField managerNetworkNameTextField;
    private javax.swing.JLabel managerSystemLabel;
    private javax.swing.JTextField managerSystemTextField;
    private javax.swing.JTree managerTree;
    private javax.swing.JLabel managerVersionLabel;
    private javax.swing.JTextField managerVersionTextField;
    private javax.swing.JPanel serviceControlPanel;
    private javax.swing.JPanel serviceInfoBorderPanel;
    private javax.swing.JPanel serviceInfoPanel;
    private javax.swing.JLabel serviceInfoPanelLabel;
    private javax.swing.JLabel serviceNameLabel;
    private javax.swing.JTextField serviceNameTextField;
    private javax.swing.JLabel serviceNetworkIPLabel;
    private javax.swing.JTextField serviceNetworkIPTextField;
    private javax.swing.JLabel serviceNetworkNameLabel;
    private javax.swing.JTextField serviceNetworkNameTextField;
    private javax.swing.JPanel serviceStartupPanel;
    private javax.swing.JLabel serviceVersionLabel;
    private javax.swing.JTextField serviceVersionTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getPanelName() {
        return "Service Manager";
    }
}
