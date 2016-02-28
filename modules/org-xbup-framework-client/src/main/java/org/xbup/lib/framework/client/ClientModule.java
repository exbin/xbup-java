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
package org.xbup.lib.framework.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBERoot;
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
import org.xbup.lib.catalog.update.XBCUpdateListener;
import org.xbup.lib.catalog.update.XBCUpdatePHPHandler;
import org.xbup.lib.client.XBCatalogNetServiceClient;
import org.xbup.lib.client.catalog.XBARCatalog;
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
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCRoot;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
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
import org.xbup.lib.framework.client.api.ClientConnectionEvent;
import org.xbup.lib.framework.client.api.ClientConnectionListener;
import org.xbup.lib.framework.client.api.ClientModuleApi;
import org.xbup.lib.framework.client.api.ConnectionStatus;
import org.xbup.lib.framework.api.XBApplication;
import org.xbup.lib.plugin.XBPluginRepository;

/**
 * Implementation of XBUP framework client module.
 *
 * @version 0.2.0 2016/02/15
 * @author ExBin Project (http://exbin.org)
 */
@PluginImplementation
public class ClientModule implements ClientModuleApi {

    private XBApplication application;

    private boolean devMode = false;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;
    private final List<ClientConnectionListener> connectionListeners = new ArrayList<>();

    public ClientModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    @Override
    public boolean connectToService() {
        connectionStatusChanged(ConnectionStatus.DISCONNECTED);
        setCatalog(null);

        // is 0x5842 (XB)
        int defaultPort = devMode ? 22595 : 22594;

        String catalogConnection = "localhost:" + defaultPort;
        String catalogHost;
        int catalogPort = defaultPort;
        int pos = catalogConnection.indexOf(":");
        if (pos >= 0) {
            catalogHost = catalogConnection.substring(0, pos);
            catalogPort = Integer.valueOf(catalogConnection.substring(pos + 1));
        } else {
            catalogHost = catalogConnection;
        }

        XBCatalogNetServiceClient serviceClient = new XBCatalogNetServiceClient(catalogHost, catalogPort);
        connectionStatusChanged(ConnectionStatus.CONNECTING);
        if (serviceClient.validate()) {
            XBARCatalog catalogHandler = new XBARCatalog(serviceClient);
            catalog = catalogHandler;

            catalogHandler.addCatalogService(XBCXLangService.class, new XBRXLangService(catalogHandler));
            catalogHandler.addCatalogService(XBCXStriService.class, new XBRXStriService(catalogHandler));
            catalogHandler.addCatalogService(XBCXNameService.class, new XBRXNameService(catalogHandler));
            catalogHandler.addCatalogService(XBCXDescService.class, new XBRXDescService(catalogHandler));
            catalogHandler.addCatalogService(XBCXFileService.class, new XBRXFileService(catalogHandler));
            catalogHandler.addCatalogService(XBCXIconService.class, new XBRXIconService(catalogHandler));
            catalogHandler.addCatalogService(XBCXPlugService.class, new XBRXPlugService(catalogHandler));
            catalogHandler.addCatalogService(XBCXLineService.class, new XBRXLineService(catalogHandler));
            catalogHandler.addCatalogService(XBCXPaneService.class, new XBRXPaneService(catalogHandler));
            catalogHandler.addCatalogService(XBCXHDocService.class, new XBRXHDocService(catalogHandler));

            if ("localhost".equals(serviceClient.getHost()) || "127.0.0.1".equals(serviceClient.getHost())) {
                connectionStatusChanged(ConnectionStatus.LOCAL);
            } else {
                connectionStatusChanged(ConnectionStatus.NETWORK);
            }

            return true;
        } else {
            connectionStatusChanged(ConnectionStatus.FAILED);
            return false;
        }
    }

    private void connectionStatusChanged(ConnectionStatus connectionStatus) {
        ClientConnectionEvent connectionEvent = new ClientConnectionEvent(connectionStatus);
        for (int i = 0; i < connectionListeners.size(); i++) {
            ClientConnectionListener listener = connectionListeners.get(i);
            listener.connectionChanged(connectionEvent);
        }
    }

    @Override
    public boolean connectToFallbackService() {
        connectionStatusChanged(ConnectionStatus.CONNECTING);
        Preferences preferences = application.getAppPreferences();
        try {
            // Database Initialization
            String derbyHome = System.getProperty("user.home") + "/.java/.userPrefs/" + preferences.absolutePath();
            if (devMode) {
                derbyHome += "-dev";
            }
            System.setProperty("derby.system.home", derbyHome);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("XBEditorPU");
            EntityManager em = emf.createEntityManager();
            em.setFlushMode(FlushModeType.AUTO);

            XBAECatalog catalogHandler = createInternalCatalog(em);

            if (catalogHandler.isShallInit()) {
                connectionStatusChanged(ConnectionStatus.INITIALIZING);
                catalogHandler.initCatalog();
            }

            try {
                XBCUpdatePHPHandler wsHandler = new XBCUpdatePHPHandler(catalogHandler);
                wsHandler.init();
                wsHandler.getPort().getLanguageId("en");
                catalogHandler.setUpdateHandler(wsHandler);
                XBCNodeService nodeService = (XBCNodeService) catalogHandler.getCatalogService(XBCNodeService.class);
                XBCRoot catalogRoot = nodeService.getRoot();
                if (catalogRoot != null) {
                    Date localLastUpdate = catalogRoot.getLastUpdate();
                    Date lastUpdate = wsHandler.getPort().getRootLastUpdate();
                    if (localLastUpdate == null || localLastUpdate.before(lastUpdate)) {
                        connectionStatusChanged(ConnectionStatus.UPDATING);
                        // TODO: As there is currently no diff update available - wipe out entire database instead
                        em.close();
                        EntityManagerFactory emfDrop = Persistence.createEntityManagerFactory("XBEditorPU-drop");
                        EntityManager emDrop = emfDrop.createEntityManager();
                        emDrop.setFlushMode(FlushModeType.AUTO);
                        catalogHandler = createInternalCatalog(emDrop);
                        catalogHandler.initCatalog();
                        nodeService = (XBCNodeService) catalogHandler.getCatalogService(XBCNodeService.class);
                        performUpdate(catalogHandler, (XBERoot) nodeService.getRoot(), lastUpdate);
                    }
                    initializePlugins(catalogHandler);
                    connectionStatusChanged(ConnectionStatus.INTERNET);
                }
            } catch (Exception ex) {
                Logger.getLogger(ClientModule.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            catalog = catalogHandler;
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ClientModule.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public void useBuildInService() {
        Preferences preferences = application.getAppPreferences();
        // Database Initialization
        String derbyHome = System.getProperty("user.home") + "/.java/.userPrefs/" + preferences.absolutePath();
        if (devMode) {
            derbyHome += "-dev";
        }
        System.setProperty("derby.system.home", derbyHome);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("XBEditorPU");
        EntityManager em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.AUTO);

        XBAECatalog catalogHandler = createInternalCatalog(em);

        if (catalogHandler.isShallInit()) {
//            operationProgressBar.setString(resourceBundle.getString("main_defaultcat") + "...");
            catalogHandler.initCatalog();
        }

        this.catalog = catalogHandler;
    }

    private void initializePlugins(XBACatalog catalogHandler) {
        if (catalogHandler != null) {
            pluginRepository = new XBPluginRepository();
            pluginRepository.addPluginsFrom(new File("plugins/").toURI());
            pluginRepository.processPlugins();
            pluginRepository.setCatalog(catalogHandler);
//            activePanel.setPluginRepository(pluginRepository);
        }
    }

    private XBAECatalog createInternalCatalog(EntityManager em) {
        XBAECatalog createdCatalog = new XBAECatalog(em);

        createdCatalog.addCatalogService(XBCXLangService.class, new XBEXLangService(createdCatalog));
        createdCatalog.addCatalogService(XBCXStriService.class, new XBEXStriService(createdCatalog));
        createdCatalog.addCatalogService(XBCXNameService.class, new XBEXNameService(createdCatalog));
        createdCatalog.addCatalogService(XBCXDescService.class, new XBEXDescService(createdCatalog));
        createdCatalog.addCatalogService(XBCXFileService.class, new XBEXFileService(createdCatalog));
        createdCatalog.addCatalogService(XBCXIconService.class, new XBEXIconService(createdCatalog));
        createdCatalog.addCatalogService(XBCXPlugService.class, new XBEXPlugService(createdCatalog));
        createdCatalog.addCatalogService(XBCXLineService.class, new XBEXLineService(createdCatalog));
        createdCatalog.addCatalogService(XBCXPaneService.class, new XBEXPaneService(createdCatalog));
        createdCatalog.addCatalogService(XBCXHDocService.class, new XBEXHDocService(createdCatalog));
        return createdCatalog;
    }

    private void performUpdate(XBAECatalog catalogHandler, XBERoot catalogRoot, Date lastUpdate) {
        XBCUpdatePHPHandler wsHandler = new XBCUpdatePHPHandler(catalogHandler);
        wsHandler.init();
        wsHandler.getPort().getLanguageId("en");

        wsHandler.fireUsageEvent(false);
        wsHandler.addWSListener(new XBCUpdateListener() {
            private boolean toolBarVisibleTemp;

            @Override
            public void webServiceUsage(boolean status) {
//                        if (status == true) {
//                            toolBarVisibleTemp = getStatusBar().isVisible();
//                            ((CardLayout) statusPanel.getLayout()).show(statusPanel, "updateCat");
//                            activityProgressBar.setString(resourceBundle.getString("main_updatecat") + "...");
//                            getStatusBar().setVisible(true);
//                        } else {
//                            ((CardLayout) statusPanel.getLayout()).first(statusPanel);
//                            //                                statusBar.setVisible(toolBarVisibleTemp);
//                        }
            }
        });
        wsHandler.updateCatalog(catalogRoot, lastUpdate);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

//        activePanel.setCatalog(catalog);
//        activePanel.reportStructureChange((XBTBlock) activePanel.getDoc().getRootBlock());
//
//        if (propertiesDialog != null) {
//            propertiesDialog.setCatalog(catalog);
//        }
//
//        if (catalogEditorDialog != null) {
//            catalogEditorDialog.setCatalog(catalog);
//        }
    }

    @Override
    public XBACatalog getCatalog() {
        return catalog;
    }

    @Override
    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    @Override
    public boolean isDevMode() {
        return devMode;
    }

    @Override
    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    @Override
    public void addClientConnectionListener(ClientConnectionListener listener) {
        connectionListeners.add(listener);
    }

    @Override
    public void removeClientConnectionListener(ClientConnectionListener listener) {
        connectionListeners.remove(listener);
    }
}
