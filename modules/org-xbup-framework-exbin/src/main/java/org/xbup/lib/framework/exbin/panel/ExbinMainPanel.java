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
package org.xbup.lib.framework.exbin.panel;

import java.awt.Component;
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
import org.xbup.lib.framework.exbin.ServiceClient;
import org.xbup.lib.framework.gui.menu.api.MenuManagement;

/**
 * ExBin IS main panel.
 *
 * @version 0.2.0 2016/02/17
 * @author XBUP Project (http://xbup.org)
 */
public class ExbinMainPanel extends javax.swing.JPanel {

    private XBCatalogServiceClient service;
    private MenuManagement menuManagement;
    private String currentPanelCode;

    private final Map<String, Component> panelMap = new HashMap<>();
    private XBACatalog catalog = null;

    public ExbinMainPanel() {
        initComponents();

        activePageScrollPane.setViewportView(emptyPanel);
        panelMap.put("empty", emptyPanel);
//        panelMap.put("info", serviceInfoPanel);
//        panelMap.put("startup", serviceStartupPanel);
//        panelMap.put("control", serviceControlPanel);

//        catalogStatusPanel = new CatalogStatusPanel();
//        catalogAvailabilityPanel = new CatalogAvailabilityPanel();
//        catalogBrowserPanel = new CatalogBrowserPanel();
//        catalogEditorPanel = new CatalogEditorPanel();
//        catalogSearchPanel = new CatalogSearchPanel();
//
//        panelMap.put("catalog", catalogStatusPanel);
//        panelMap.put("catalog_availability", catalogAvailabilityPanel);
//        panelMap.put("catalog_browse", catalogBrowserPanel);
//        panelMap.put("catalog_editor", catalogEditorPanel);
//        panelMap.put("catalog_search", catalogSearchPanel);
//        panelMap.put("update", new CatalogUpdateManagerPanel());
//        panelMap.put("transplug", new TransformationPluginsManagerPanel());
        activityTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        MutableTreeNode top = new MutableTreeNode("Root", "");
        createNodes(top);
        activityTree.setModel(new DefaultTreeModel(top, true));
        activityTree.getSelectionModel().addTreeSelectionListener(new MySelectionListener());
        activityTree.setSelectionRow(0);
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode item = new MutableTreeNode("Server Information", "info");
//        item.add(new MutableTreeNode("Connection","empty"));
        top.add(item);
//        item = new MutableTreeNode("Service Control", "control");
//        top.add(item);
//        item = new MutableTreeNode("Service Startup", "startup");
//        top.add(item);
//        item = new MutableTreeNode("Catalog", "catalog");
//        item.add(new MutableTreeNode("Browse", "catalog_browse"));
//        item.add(new MutableTreeNode("Editor", "catalog_editor"));
//        item.add(new MutableTreeNode("Search", "catalog_search"));
//        item.add(new MutableTreeNode("Update Service", "update"));
//        top.add(item);
//        item = new MutableTreeNode("Plugin", "empty");
//        item.add(new MutableTreeNode("Transformations", "transplug"));
//        top.add(item);
    }

    public XBCatalogServiceClient getService() {
        return service;
    }

    public void setService(XBCatalogServiceClient service) {
        this.service = service;
    }

//    public boolean updateActionStatus(Component component) {
//        // String test = ((CardLayout) cardPanel.getLayout()).toString();
//        // if ("catalog".equals(test)) {
//        catalogEditorPanel.updateActionStatus(component);
//        // }
//
//        return false;
//    }
//    public void releaseActionStatus() {
//        // if ("catalog".equals(((CardLayout) cardPanel.getLayout()).toString())) {
//        catalogEditorPanel.releaseActionStatus();
//        // }
//    }
//    public boolean performAction(String eventName, ActionEvent event) {
//        // if ("catalog".equals(((CardLayout) cardPanel.getLayout()).toString())) {
//        return catalogEditorPanel.performAction(eventName, event);
//        // }
//
//        // return false;
//    }
    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;
//        catalogBrowserPanel.setMenuManagement(menuManagement);
//        catalogEditorPanel.setMenuManagement(menuManagement);
//        catalogSearchPanel.setMenuManagement(menuManagement);
    }

//    public void setMainFrameManagement(MainFrameManagement mainFramenManagement) {
//        catalogBrowserPanel.setMainFrameManagement(mainFramenManagement);
//        catalogEditorPanel.setMainFrameManagement(mainFramenManagement);
//        catalogSearchPanel.setMainFrameManagement(mainFramenManagement);
//    }
    public void setCatalog(XBACatalog catalog) {
//        catalogAvailabilityPanel.setCatalog(catalog);
//        catalogStatusPanel.setCatalog(catalog);
//        catalogBrowserPanel.setCatalog(catalog);
//        catalogEditorPanel.setCatalog(catalog);
//        catalogSearchPanel.setCatalog(catalog);

        if (this.catalog == null && catalog != null) {
            Component panel = panelMap.get(currentPanelCode);
            activePageScrollPane.setViewportView(panel);
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
                            activityTree.collapsePath(new TreePath(collapsePath.toArray()));
                        }
                    }
                }
            }
            if (itemPath != null) {
                lastPath = itemPath;
                activityTree.expandPath(itemPath);
                String panelStringCode = ((MutableTreeNode) activityTree.getLastSelectedPathComponent()).getCaption();
                if (panelStringCode != null) {
                    currentPanelCode = panelStringCode;
//                    Component panel = panelMap.get(panelStringCode);
//                    if (panel instanceof CatalogManagerPanelable && catalog == null) {
//                        panel = panelMap.get("catalog_availability");
//
//                    }
//
//                    activePageScrollPane.setViewportView(panel);
//                    panel.revalidate();
                } else {
                    activePageScrollPane.setViewportView(panelMap.get("empty"));
                }
                activePageScrollPane.revalidate();
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

        emptyPanel = new javax.swing.JPanel();
        mainSplitPane = new javax.swing.JSplitPane();
        activityTreeScrollPane = new javax.swing.JScrollPane();
        activityTree = new javax.swing.JTree();
        activePageScrollPane = new javax.swing.JScrollPane();

        emptyPanel.setEnabled(false);
        emptyPanel.setName("emptyPanel"); // NOI18N
        emptyPanel.setLayout(new java.awt.BorderLayout());

        mainSplitPane.setDividerLocation(150);
        mainSplitPane.setName("mainSplitPane"); // NOI18N

        activityTreeScrollPane.setName("activityTreeScrollPane"); // NOI18N

        activityTree.setName("activityTree"); // NOI18N
        activityTree.setRootVisible(false);
        activityTreeScrollPane.setViewportView(activityTree);

        mainSplitPane.setLeftComponent(activityTreeScrollPane);

        activePageScrollPane.setName("activePageScrollPane"); // NOI18N
        mainSplitPane.setRightComponent(activePageScrollPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane activePageScrollPane;
    private javax.swing.JTree activityTree;
    private javax.swing.JScrollPane activityTreeScrollPane;
    private javax.swing.JPanel emptyPanel;
    private javax.swing.JSplitPane mainSplitPane;
    // End of variables declaration//GEN-END:variables
}
