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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.tool.editor.module.service_manager.XBDbServiceClient;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.ApplicationPanel;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.MenuManagement;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogEditorPanel;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogBrowserPanel;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogSearchPanel;
import org.xbup.tool.editor.module.service_manager.catalog.panel.CatalogStatusPanel;

/**
 * XBManager Service Management Panel.
 *
 * @version 0.1.24 2015/02/22
 * @author XBUP Project (http://xbup.org)
 */
public class ServiceManagerPanel extends javax.swing.JPanel implements ApplicationPanel, ActivePanelActionHandling {

    private XBCatalogServiceClient service;
    private final CatalogStatusPanel catalogStatusPanel;
    private final CatalogAvailabilityPanel catalogAvailabilityPanel;
    private final CatalogBrowserPanel catalogBrowserPanel;
    private final CatalogEditorPanel catalogEditorPanel;
    private final CatalogSearchPanel catalogSearchPanel;
    private MenuManagement menuManagement;
    private String currentPanelCode;

    private final Map<String, Component> panelMap = new HashMap<>();
    private XBACatalog catalog = null;

    public ServiceManagerPanel() {
        initComponents();

        serviceInfoScrollPane.setViewportView(serviceInfoPanel);
        panelMap.put("info", serviceInfoPanel);
        panelMap.put("startup", serviceStartupPanel);
        panelMap.put("empty", emptyPanel);
        panelMap.put("control", serviceControlPanel);

        catalogStatusPanel = new CatalogStatusPanel();
        catalogAvailabilityPanel = new CatalogAvailabilityPanel();
        catalogBrowserPanel = new CatalogBrowserPanel();
        catalogEditorPanel = new CatalogEditorPanel();
        catalogSearchPanel = new CatalogSearchPanel();

        panelMap.put("catalog", catalogStatusPanel);
        panelMap.put("catalog_availability", catalogAvailabilityPanel);
        panelMap.put("catalog_browse", catalogBrowserPanel);
        panelMap.put("catalog_editor", catalogEditorPanel);
        panelMap.put("catalog_search", catalogSearchPanel);
        panelMap.put("update", new CatalogUpdateManagerPanel());
        panelMap.put("transplug", new TransformationPluginsManagerPanel());
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
        item.add(new MutableTreeNode("Browse", "catalog_browse"));
        item.add(new MutableTreeNode("Editor", "catalog_editor"));
        item.add(new MutableTreeNode("Search", "catalog_search"));
        item.add(new MutableTreeNode("Update Service", "update"));
        top.add(item);
        item = new MutableTreeNode("Plugin", "empty");
        item.add(new MutableTreeNode("Transformations", "transplug"));
        top.add(item);
    }

    public XBCatalogServiceClient getService() {
        return service;
    }

    public void setService(XBCatalogServiceClient service) {
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

            return;
        }

        XBACatalog connectedCatalog = null;
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
                connectedCatalog = new XBAECatalog(emf.createEntityManager()); // TODO: Kill on failure
                if (((XBAECatalog) connectedCatalog).isShallInit()) {
                    ((XBAECatalog) connectedCatalog).initCatalog();
                }
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXLangService.class, new XBEXLangService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXStriService.class, new XBEXStriService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXNameService.class, new XBEXNameService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXDescService.class, new XBEXDescService((XBAECatalog) connectedCatalog));

                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXFileService.class, new XBEXFileService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXIconService.class, new XBEXIconService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXPlugService.class, new XBEXPlugService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXLineService.class, new XBEXLineService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXPaneService.class, new XBEXPaneService((XBAECatalog) connectedCatalog));
                ((XBAECatalog) connectedCatalog).addCatalogService(XBCXHDocService.class, new XBEXHDocService((XBAECatalog) connectedCatalog));
            } catch (Exception ex) {
                Logger.getLogger(ServiceManagerPanel.class.getName()).log(Level.SEVERE, null, ex);
                connectedCatalog = null;
            }
        } else {
            if (service.validate()) {
                serviceVersionTextField.setText(service.getVersion());
                connectionHostTextField.setText(service.getHost());
                connectionPortTextField.setText(Integer.toString(service.getPort())); // 22594 is 0x5842 (XB)
                connectionProtocolTextField.setText("UDP (IP)");
                serviceNameTextField.setText("XBService");
                serviceNetworkIPTextField.setText(service.getHostAddress());
                managerNameTextField.setText("XBManager");
                managerNetworkIPTextField.setText(service.getLocalAddress());
                managerSystemTextField.setText(System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")");
                managerHardwareTextField.setText(System.getProperty("os.arch"));
                stopServiceButton.setEnabled(true);

                connectedCatalog = new XBARCatalog(service);
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXLangService.class, new XBRXLangService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXStriService.class, new XBRXStriService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXNameService.class, new XBRXNameService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXDescService.class, new XBRXDescService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXFileService.class, new XBRXFileService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXIconService.class, new XBRXIconService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXPlugService.class, new XBRXPlugService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXLineService.class, new XBRXLineService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXPaneService.class, new XBRXPaneService((XBARCatalog) connectedCatalog));
                ((XBARCatalog) connectedCatalog).addCatalogService(XBCXHDocService.class, new XBRXHDocService((XBARCatalog) connectedCatalog));
            }
        }

        setCatalog(connectedCatalog);
    }

    @Override
    public boolean updateActionStatus(Component component) {
        // String test = ((CardLayout) cardPanel.getLayout()).toString();
        // if ("catalog".equals(test)) {
        catalogEditorPanel.updateActionStatus(component);
        // }

        return false;
    }

    @Override
    public void releaseActionStatus() {
        // if ("catalog".equals(((CardLayout) cardPanel.getLayout()).toString())) {
        catalogEditorPanel.releaseActionStatus();
        // }
    }

    @Override
    public boolean performAction(String eventName, ActionEvent event) {
        // if ("catalog".equals(((CardLayout) cardPanel.getLayout()).toString())) {
        return catalogEditorPanel.performAction(eventName, event);
        // }

        // return false;
    }

    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;
        catalogBrowserPanel.setMenuManagement(menuManagement);
        catalogEditorPanel.setMenuManagement(menuManagement);
        catalogSearchPanel.setMenuManagement(menuManagement);
    }

    public void setMainFrameManagement(MainFrameManagement mainFramenManagement) {
        catalogBrowserPanel.setMainFrameManagement(mainFramenManagement);
        catalogEditorPanel.setMainFrameManagement(mainFramenManagement);
        catalogSearchPanel.setMainFrameManagement(mainFramenManagement);
    }

    public void setCatalog(XBACatalog catalog) {
        catalogAvailabilityPanel.setCatalog(catalog);
        catalogStatusPanel.setCatalog(catalog);
        catalogBrowserPanel.setCatalog(catalog);
        catalogEditorPanel.setCatalog(catalog);
        catalogSearchPanel.setCatalog(catalog);

        if (this.catalog == null && catalog != null) {
            Component panel = panelMap.get(currentPanelCode);
            serviceInfoScrollPane.setViewportView(panel);
            panel.revalidate();
        }

        this.catalog = catalog;
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
                String panelStringCode = ((MutableTreeNode) managerTree.getLastSelectedPathComponent()).getCaption();
                if (panelStringCode != null) {
                    currentPanelCode = panelStringCode;
                    Component panel = panelMap.get(panelStringCode);
                    if (panel instanceof CatalogManagerPanelable && catalog == null) {
                        panel = panelMap.get("catalog_availability");

                    }

                    serviceInfoScrollPane.setViewportView(panel);
                    panel.revalidate();
                } else {
                    serviceInfoScrollPane.setViewportView(panelMap.get("empty"));
                }
                serviceInfoScrollPane.revalidate();
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

        serviceInfoPanel = new javax.swing.JPanel();
        serviceInfoPanelLabel = new javax.swing.JLabel();
        connectionInfoBorderPanel = new javax.swing.JPanel();
        connectionInfoPanel = new javax.swing.JPanel();
        connectionHostLabel = new javax.swing.JLabel();
        connectionHostTextField = new javax.swing.JTextField();
        connectionPortLabel = new javax.swing.JLabel();
        connectionPortTextField = new javax.swing.JTextField();
        connectionProtocolLabel = new javax.swing.JLabel();
        connectionProtocolTextField = new javax.swing.JTextField();
        serviceInformationBorderPanel = new javax.swing.JPanel();
        serviceInformationPanel = new javax.swing.JPanel();
        serviceNameLabel = new javax.swing.JLabel();
        serviceNameTextField = new javax.swing.JTextField();
        serviceVersionLabel = new javax.swing.JLabel();
        serviceVersionTextField = new javax.swing.JTextField();
        serviceNetworkNameLabel = new javax.swing.JLabel();
        serviceNetworkNameTextField = new javax.swing.JTextField();
        serviceNetworkIPLabel = new javax.swing.JLabel();
        serviceNetworkIPTextField = new javax.swing.JTextField();
        managerInfoBorderPanel = new javax.swing.JPanel();
        managerInfoPanel = new javax.swing.JPanel();
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
        startupControlPanel = new javax.swing.JPanel();
        runOnSystemStartCheckBox = new javax.swing.JCheckBox();
        runOnSystemLoginCheckBox2 = new javax.swing.JCheckBox();
        runAsLabel = new javax.swing.JLabel();
        runAsComboBox = new javax.swing.JComboBox();
        updatingModePanel = new javax.swing.JPanel();
        checkForNewVersionheckBox = new javax.swing.JCheckBox();
        updatingSourceLabel = new javax.swing.JLabel();
        updatingSourceComboBox = new javax.swing.JComboBox();
        emptyPanel = new javax.swing.JPanel();
        serviceControlPanel = new javax.swing.JPanel();
        serviceStatusPanel = new javax.swing.JPanel();
        restartServiceButton = new javax.swing.JButton();
        stopServiceButton = new javax.swing.JButton();
        startServiceButton = new javax.swing.JButton();
        logMessagesPanel = new javax.swing.JPanel();
        logMessagesScrollPane = new javax.swing.JScrollPane();
        logMessagesTextArea = new javax.swing.JTextArea();
        mainSplitPane = new javax.swing.JSplitPane();
        managerTreeScrollPane = new javax.swing.JScrollPane();
        managerTree = new javax.swing.JTree();
        serviceInfoScrollPane = new javax.swing.JScrollPane();

        serviceInfoPanel.setName("serviceInfoPanel"); // NOI18N

        serviceInfoPanelLabel.setName("serviceInfoPanelLabel"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/service_manager/panel/resources/ServiceManagerPanel"); // NOI18N
        connectionInfoBorderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("connectionInfoBorderPanel.border.title"))); // NOI18N
        connectionInfoBorderPanel.setName("connectionInfoBorderPanel"); // NOI18N

        connectionInfoPanel.setName("connectionInfoPanel"); // NOI18N
        connectionInfoPanel.setLayout(new java.awt.GridLayout(3, 2, 5, 5));

        connectionHostLabel.setText(bundle.getString("hostNameLabel.text")); // NOI18N
        connectionHostLabel.setName("connectionHostLabel"); // NOI18N
        connectionInfoPanel.add(connectionHostLabel);

        connectionHostTextField.setEditable(false);
        connectionHostTextField.setText("unknown");
        connectionHostTextField.setBorder(null);
        connectionHostTextField.setName("connectionHostTextField"); // NOI18N
        connectionInfoPanel.add(connectionHostTextField);

        connectionPortLabel.setText(bundle.getString("connectionPortLabel.text")); // NOI18N
        connectionPortLabel.setName("connectionPortLabel"); // NOI18N
        connectionInfoPanel.add(connectionPortLabel);

        connectionPortTextField.setEditable(false);
        connectionPortTextField.setText("unknown");
        connectionPortTextField.setBorder(null);
        connectionPortTextField.setName("connectionPortTextField"); // NOI18N
        connectionInfoPanel.add(connectionPortTextField);

        connectionProtocolLabel.setText(bundle.getString("connectionProtocolLabel.text")); // NOI18N
        connectionProtocolLabel.setName("connectionProtocolLabel"); // NOI18N
        connectionInfoPanel.add(connectionProtocolLabel);

        connectionProtocolTextField.setEditable(false);
        connectionProtocolTextField.setText("unknown");
        connectionProtocolTextField.setBorder(null);
        connectionProtocolTextField.setName("connectionProtocolTextField"); // NOI18N
        connectionInfoPanel.add(connectionProtocolTextField);

        javax.swing.GroupLayout connectionInfoBorderPanelLayout = new javax.swing.GroupLayout(connectionInfoBorderPanel);
        connectionInfoBorderPanel.setLayout(connectionInfoBorderPanelLayout);
        connectionInfoBorderPanelLayout.setHorizontalGroup(
            connectionInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionInfoBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        connectionInfoBorderPanelLayout.setVerticalGroup(
            connectionInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionInfoBorderPanelLayout.createSequentialGroup()
                .addComponent(connectionInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        serviceInformationBorderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("serviceInfoBorderPanel.border.title"))); // NOI18N
        serviceInformationBorderPanel.setName("serviceInformationBorderPanel"); // NOI18N

        serviceInformationPanel.setName("serviceInformationPanel"); // NOI18N
        serviceInformationPanel.setLayout(new java.awt.GridLayout(4, 2, 5, 5));

        serviceNameLabel.setText(bundle.getString("serviceNameLabel.text")); // NOI18N
        serviceNameLabel.setName("serviceNameLabel"); // NOI18N
        serviceInformationPanel.add(serviceNameLabel);

        serviceNameTextField.setEditable(false);
        serviceNameTextField.setText("unknown");
        serviceNameTextField.setBorder(null);
        serviceNameTextField.setName("serviceNameTextField"); // NOI18N
        serviceInformationPanel.add(serviceNameTextField);

        serviceVersionLabel.setText(bundle.getString("serviceVersionLabel.text")); // NOI18N
        serviceVersionLabel.setName("serviceVersionLabel"); // NOI18N
        serviceInformationPanel.add(serviceVersionLabel);

        serviceVersionTextField.setEditable(false);
        serviceVersionTextField.setText("unknown");
        serviceVersionTextField.setBorder(null);
        serviceVersionTextField.setName("serviceVersionTextField"); // NOI18N
        serviceInformationPanel.add(serviceVersionTextField);

        serviceNetworkNameLabel.setText("Network Name:");
        serviceNetworkNameLabel.setName("serviceNetworkNameLabel"); // NOI18N
        serviceInformationPanel.add(serviceNetworkNameLabel);

        serviceNetworkNameTextField.setEditable(false);
        serviceNetworkNameTextField.setText("unknown");
        serviceNetworkNameTextField.setBorder(null);
        serviceNetworkNameTextField.setName("serviceNetworkNameTextField"); // NOI18N
        serviceInformationPanel.add(serviceNetworkNameTextField);

        serviceNetworkIPLabel.setText("IP Address:");
        serviceNetworkIPLabel.setName("serviceNetworkIPLabel"); // NOI18N
        serviceInformationPanel.add(serviceNetworkIPLabel);

        serviceNetworkIPTextField.setEditable(false);
        serviceNetworkIPTextField.setText("unknown");
        serviceNetworkIPTextField.setBorder(null);
        serviceNetworkIPTextField.setName("serviceNetworkIPTextField"); // NOI18N
        serviceInformationPanel.add(serviceNetworkIPTextField);

        javax.swing.GroupLayout serviceInformationBorderPanelLayout = new javax.swing.GroupLayout(serviceInformationBorderPanel);
        serviceInformationBorderPanel.setLayout(serviceInformationBorderPanelLayout);
        serviceInformationBorderPanelLayout.setHorizontalGroup(
            serviceInformationBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInformationBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceInformationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addContainerGap())
        );
        serviceInformationBorderPanelLayout.setVerticalGroup(
            serviceInformationBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInformationBorderPanelLayout.createSequentialGroup()
                .addComponent(serviceInformationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        managerInfoBorderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("managerInfoBorderPanel.border.title"))); // NOI18N
        managerInfoBorderPanel.setName("managerInfoBorderPanel"); // NOI18N

        managerInfoPanel.setName("managerInfoPanel"); // NOI18N
        managerInfoPanel.setLayout(new java.awt.GridLayout(6, 2, 5, 5));

        managerNameLabel.setText(bundle.getString("managerNameLabel.text")); // NOI18N
        managerNameLabel.setName("managerNameLabel"); // NOI18N
        managerInfoPanel.add(managerNameLabel);

        managerNameTextField.setEditable(false);
        managerNameTextField.setText("unknown");
        managerNameTextField.setBorder(null);
        managerNameTextField.setName("managerNameTextField"); // NOI18N
        managerInfoPanel.add(managerNameTextField);

        managerVersionLabel.setText(bundle.getString("managerVersionLabel.text")); // NOI18N
        managerVersionLabel.setName("managerVersionLabel"); // NOI18N
        managerInfoPanel.add(managerVersionLabel);

        managerVersionTextField.setEditable(false);
        managerVersionTextField.setText("unknown");
        managerVersionTextField.setBorder(null);
        managerVersionTextField.setName("managerVersionTextField"); // NOI18N
        managerInfoPanel.add(managerVersionTextField);

        managerNetworkNameLabel.setText(bundle.getString("managerNetworkNameLabel.text")); // NOI18N
        managerNetworkNameLabel.setName("managerNetworkNameLabel"); // NOI18N
        managerInfoPanel.add(managerNetworkNameLabel);

        managerNetworkNameTextField.setEditable(false);
        managerNetworkNameTextField.setText("unknown");
        managerNetworkNameTextField.setBorder(null);
        managerNetworkNameTextField.setName("managerNetworkNameTextField"); // NOI18N
        managerInfoPanel.add(managerNetworkNameTextField);

        managerNetworkIPLabel.setText(bundle.getString("managerNetworkIPLabel.text")); // NOI18N
        managerNetworkIPLabel.setName("managerNetworkIPLabel"); // NOI18N
        managerInfoPanel.add(managerNetworkIPLabel);

        managerNetworkIPTextField.setEditable(false);
        managerNetworkIPTextField.setText("unknown");
        managerNetworkIPTextField.setBorder(null);
        managerNetworkIPTextField.setName("managerNetworkIPTextField"); // NOI18N
        managerInfoPanel.add(managerNetworkIPTextField);

        managerSystemLabel.setText(bundle.getString("managerSystemLabel.text")); // NOI18N
        managerSystemLabel.setName("managerSystemLabel"); // NOI18N
        managerInfoPanel.add(managerSystemLabel);

        managerSystemTextField.setEditable(false);
        managerSystemTextField.setText("unknown");
        managerSystemTextField.setBorder(null);
        managerSystemTextField.setName("managerSystemTextField"); // NOI18N
        managerInfoPanel.add(managerSystemTextField);

        managerHardwareLabel.setText(bundle.getString("managerHardwareLabel.text")); // NOI18N
        managerHardwareLabel.setName("managerHardwareLabel"); // NOI18N
        managerInfoPanel.add(managerHardwareLabel);

        managerHardwareTextField.setEditable(false);
        managerHardwareTextField.setText("unknown");
        managerHardwareTextField.setBorder(null);
        managerHardwareTextField.setName("managerHardwareTextField"); // NOI18N
        managerInfoPanel.add(managerHardwareTextField);

        javax.swing.GroupLayout managerInfoBorderPanelLayout = new javax.swing.GroupLayout(managerInfoBorderPanel);
        managerInfoBorderPanel.setLayout(managerInfoBorderPanelLayout);
        managerInfoBorderPanelLayout.setHorizontalGroup(
            managerInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(managerInfoBorderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(managerInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        managerInfoBorderPanelLayout.setVerticalGroup(
            managerInfoBorderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(managerInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout serviceInfoPanelLayout = new javax.swing.GroupLayout(serviceInfoPanel);
        serviceInfoPanel.setLayout(serviceInfoPanelLayout);
        serviceInfoPanelLayout.setHorizontalGroup(
            serviceInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serviceInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(serviceInfoPanelLayout.createSequentialGroup()
                        .addComponent(serviceInfoPanelLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceInfoPanelLayout.createSequentialGroup()
                        .addGroup(serviceInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(serviceInformationBorderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(connectionInfoBorderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(managerInfoBorderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        serviceInfoPanelLayout.setVerticalGroup(
            serviceInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceInfoPanelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectionInfoBorderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceInformationBorderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(managerInfoBorderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        serviceStartupPanel.setEnabled(false);
        serviceStartupPanel.setName("serviceStartupPanel"); // NOI18N

        startupControlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Startup"));
        startupControlPanel.setName("startupControlPanel"); // NOI18N

        runOnSystemStartCheckBox.setText("Run on system startup");
        runOnSystemStartCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        runOnSystemStartCheckBox.setEnabled(false);
        runOnSystemStartCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        runOnSystemStartCheckBox.setName("runOnSystemStartCheckBox"); // NOI18N

        runOnSystemLoginCheckBox2.setText("Run on system login");
        runOnSystemLoginCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        runOnSystemLoginCheckBox2.setEnabled(false);
        runOnSystemLoginCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        runOnSystemLoginCheckBox2.setName("runOnSystemLoginCheckBox2"); // NOI18N

        runAsLabel.setText("Run as");
        runAsLabel.setName("runAsLabel"); // NOI18N

        runAsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        runAsComboBox.setEnabled(false);
        runAsComboBox.setName("runAsComboBox"); // NOI18N

        javax.swing.GroupLayout startupControlPanelLayout = new javax.swing.GroupLayout(startupControlPanel);
        startupControlPanel.setLayout(startupControlPanelLayout);
        startupControlPanelLayout.setHorizontalGroup(
            startupControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startupControlPanelLayout.createSequentialGroup()
                .addGroup(startupControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(runOnSystemStartCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(runOnSystemLoginCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(runAsLabel))
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(runAsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        startupControlPanelLayout.setVerticalGroup(
            startupControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startupControlPanelLayout.createSequentialGroup()
                .addComponent(runOnSystemStartCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runOnSystemLoginCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runAsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runAsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        updatingModePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Updating"));
        updatingModePanel.setName("updatingModePanel"); // NOI18N

        checkForNewVersionheckBox.setText("Check for newer version on startup");
        checkForNewVersionheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkForNewVersionheckBox.setEnabled(false);
        checkForNewVersionheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkForNewVersionheckBox.setName("checkForNewVersionheckBox"); // NOI18N

        updatingSourceLabel.setText("Updating source");
        updatingSourceLabel.setName("updatingSourceLabel"); // NOI18N

        updatingSourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        updatingSourceComboBox.setEnabled(false);
        updatingSourceComboBox.setName("updatingSourceComboBox"); // NOI18N

        javax.swing.GroupLayout updatingModePanelLayout = new javax.swing.GroupLayout(updatingModePanel);
        updatingModePanel.setLayout(updatingModePanelLayout);
        updatingModePanelLayout.setHorizontalGroup(
            updatingModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatingModePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updatingModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkForNewVersionheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatingSourceLabel)
                    .addComponent(updatingSourceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        updatingModePanelLayout.setVerticalGroup(
            updatingModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatingModePanelLayout.createSequentialGroup()
                .addComponent(checkForNewVersionheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatingSourceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatingSourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout serviceStartupPanelLayout = new javax.swing.GroupLayout(serviceStartupPanel);
        serviceStartupPanel.setLayout(serviceStartupPanelLayout);
        serviceStartupPanelLayout.setHorizontalGroup(
            serviceStartupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceStartupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serviceStartupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updatingModePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startupControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        serviceStartupPanelLayout.setVerticalGroup(
            serviceStartupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceStartupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startupControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatingModePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        emptyPanel.setEnabled(false);
        emptyPanel.setName("emptyPanel"); // NOI18N
        emptyPanel.setLayout(new java.awt.BorderLayout());

        serviceControlPanel.setEnabled(false);
        serviceControlPanel.setName("serviceControlPanel"); // NOI18N

        serviceStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Service Status"));
        serviceStatusPanel.setName("serviceStatusPanel"); // NOI18N

        restartServiceButton.setText("Restart");
        restartServiceButton.setEnabled(false);
        restartServiceButton.setName("restartServiceButton"); // NOI18N

        stopServiceButton.setText("Stop");
        stopServiceButton.setEnabled(false);
        stopServiceButton.setName("stopServiceButton"); // NOI18N
        stopServiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopServiceButtonActionPerformed(evt);
            }
        });

        startServiceButton.setText("Start");
        startServiceButton.setEnabled(false);
        startServiceButton.setName("startServiceButton"); // NOI18N

        javax.swing.GroupLayout serviceStatusPanelLayout = new javax.swing.GroupLayout(serviceStatusPanel);
        serviceStatusPanel.setLayout(serviceStatusPanelLayout);
        serviceStatusPanelLayout.setHorizontalGroup(
            serviceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceStatusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startServiceButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopServiceButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(restartServiceButton)
                .addContainerGap())
        );
        serviceStatusPanelLayout.setVerticalGroup(
            serviceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceStatusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(serviceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(restartServiceButton)
                    .addComponent(stopServiceButton)
                    .addComponent(startServiceButton))
                .addContainerGap())
        );

        logMessagesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Log Messages"));
        logMessagesPanel.setName("logMessagesPanel"); // NOI18N

        logMessagesScrollPane.setName("logMessagesScrollPane"); // NOI18N

        logMessagesTextArea.setEditable(false);
        logMessagesTextArea.setColumns(20);
        logMessagesTextArea.setRows(5);
        logMessagesTextArea.setName("logMessagesTextArea"); // NOI18N
        logMessagesScrollPane.setViewportView(logMessagesTextArea);

        javax.swing.GroupLayout logMessagesPanelLayout = new javax.swing.GroupLayout(logMessagesPanel);
        logMessagesPanel.setLayout(logMessagesPanelLayout);
        logMessagesPanelLayout.setHorizontalGroup(
            logMessagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logMessagesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logMessagesScrollPane)
                .addContainerGap())
        );
        logMessagesPanelLayout.setVerticalGroup(
            logMessagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logMessagesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logMessagesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout serviceControlPanelLayout = new javax.swing.GroupLayout(serviceControlPanel);
        serviceControlPanel.setLayout(serviceControlPanelLayout);
        serviceControlPanelLayout.setHorizontalGroup(
            serviceControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serviceControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logMessagesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(serviceStatusPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        serviceControlPanelLayout.setVerticalGroup(
            serviceControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logMessagesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainSplitPane.setDividerLocation(150);
        mainSplitPane.setName("mainSplitPane"); // NOI18N

        managerTreeScrollPane.setName("managerTreeScrollPane"); // NOI18N

        managerTree.setName("managerTree"); // NOI18N
        managerTree.setRootVisible(false);
        managerTreeScrollPane.setViewportView(managerTree);

        mainSplitPane.setLeftComponent(managerTreeScrollPane);

        serviceInfoScrollPane.setName("serviceInfoScrollPane"); // NOI18N
        mainSplitPane.setRightComponent(serviceInfoScrollPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void stopServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopServiceButtonActionPerformed
        //getService().stop();
}//GEN-LAST:event_stopServiceButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkForNewVersionheckBox;
    private javax.swing.JLabel connectionHostLabel;
    private javax.swing.JTextField connectionHostTextField;
    private javax.swing.JPanel connectionInfoBorderPanel;
    private javax.swing.JPanel connectionInfoPanel;
    private javax.swing.JLabel connectionPortLabel;
    private javax.swing.JTextField connectionPortTextField;
    private javax.swing.JLabel connectionProtocolLabel;
    private javax.swing.JTextField connectionProtocolTextField;
    private javax.swing.JPanel emptyPanel;
    private javax.swing.JPanel logMessagesPanel;
    private javax.swing.JScrollPane logMessagesScrollPane;
    private javax.swing.JTextArea logMessagesTextArea;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JLabel managerHardwareLabel;
    private javax.swing.JTextField managerHardwareTextField;
    private javax.swing.JPanel managerInfoBorderPanel;
    private javax.swing.JPanel managerInfoPanel;
    private javax.swing.JLabel managerNameLabel;
    private javax.swing.JTextField managerNameTextField;
    private javax.swing.JLabel managerNetworkIPLabel;
    private javax.swing.JTextField managerNetworkIPTextField;
    private javax.swing.JLabel managerNetworkNameLabel;
    private javax.swing.JTextField managerNetworkNameTextField;
    private javax.swing.JLabel managerSystemLabel;
    private javax.swing.JTextField managerSystemTextField;
    private javax.swing.JTree managerTree;
    private javax.swing.JScrollPane managerTreeScrollPane;
    private javax.swing.JLabel managerVersionLabel;
    private javax.swing.JTextField managerVersionTextField;
    private javax.swing.JButton restartServiceButton;
    private javax.swing.JComboBox runAsComboBox;
    private javax.swing.JLabel runAsLabel;
    private javax.swing.JCheckBox runOnSystemLoginCheckBox2;
    private javax.swing.JCheckBox runOnSystemStartCheckBox;
    private javax.swing.JPanel serviceControlPanel;
    private javax.swing.JPanel serviceInfoPanel;
    private javax.swing.JLabel serviceInfoPanelLabel;
    private javax.swing.JScrollPane serviceInfoScrollPane;
    private javax.swing.JPanel serviceInformationBorderPanel;
    private javax.swing.JPanel serviceInformationPanel;
    private javax.swing.JLabel serviceNameLabel;
    private javax.swing.JTextField serviceNameTextField;
    private javax.swing.JLabel serviceNetworkIPLabel;
    private javax.swing.JTextField serviceNetworkIPTextField;
    private javax.swing.JLabel serviceNetworkNameLabel;
    private javax.swing.JTextField serviceNetworkNameTextField;
    private javax.swing.JPanel serviceStartupPanel;
    private javax.swing.JPanel serviceStatusPanel;
    private javax.swing.JLabel serviceVersionLabel;
    private javax.swing.JTextField serviceVersionTextField;
    private javax.swing.JButton startServiceButton;
    private javax.swing.JPanel startupControlPanel;
    private javax.swing.JButton stopServiceButton;
    private javax.swing.JPanel updatingModePanel;
    private javax.swing.JComboBox updatingSourceComboBox;
    private javax.swing.JLabel updatingSourceLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getPanelName() {
        return "Service Manager";
    }
}
