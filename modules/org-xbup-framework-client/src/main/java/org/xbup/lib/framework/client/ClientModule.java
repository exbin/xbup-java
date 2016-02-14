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
package org.xbup.lib.framework.client;

import java.awt.CardLayout;
import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.xbup.lib.core.block.XBTBlock;
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
import org.xbup.lib.framework.client.api.ClientModuleApi;
import org.xbup.lib.framework.gui.api.XBApplication;

/**
 * Implementation of XBUP framework client module.
 *
 * @version 0.2.0 2016/02/14
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class ClientModule implements ClientModuleApi {

    private XBApplication application;

    private boolean devMode = false;
    private XBCUpdatePHPHandler wsHandler;
    private XBACatalog catalog;
    private Thread catInitThread = null;

    public ClientModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
    }

    private void initService() {
        // Catalog initialization Thread
        catInitThread = new Thread(new Runnable() {
            @Override
            public void run() {
////                boolean panelStatus = jPanel1.isVisible();
//                boolean panelStatus = false;
////                getStatusBar().setVisible(true);
//
//                // is 0x5842 (XB)
//                int defaultPort = ("DEV".equals(editorApp.getAppBundle().getString("Application.mode"))) ? 22595 : 22594;
////                ((CardLayout) statusPanel.getLayout()).show(statusPanel, "initCat");
////                operationProgressBar.setString(resourceBundle.getString("main_initlocal") + "...");
//
//                Boolean serviceConnectionAllowed = Boolean.valueOf(preferences.get("serviceConnectionAllowed", Boolean.toString(true)));
//                if (serviceConnectionAllowed) {
////                    catalogConnection = preferences.get("serviceConnectionURL", null);
////                    if (catalogConnection == null || "".equals(catalogConnection)) {
////                        catalogConnection = "localhost:" + defaultPort;
////                    }
//                    String catalogHost;
//                    int catalogPort = defaultPort;
//                    int pos = catalogConnection.indexOf(":");
//                    if (pos >= 0) {
//                        catalogHost = catalogConnection.substring(0, pos);
//                        catalogPort = Integer.valueOf(catalogConnection.substring(pos + 1));
//                    } else {
//                        catalogHost = catalogConnection;
//                    }
//
//                    setCatalog(null);
//
//                    // Attempt to connect to catalog service
//                    if ((catalogConnection != null) && (!"".equals(catalogConnection))) {
//                        XBCatalogNetServiceClient serviceClient = new XBCatalogNetServiceClient(catalogHost, catalogPort);
//                        setConnectionStatus(ConnectionStatus.CONNECTING);
//                        if (serviceClient.validate()) {
//                            setCatalog(new XBARCatalog(serviceClient));
//
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXLangService.class, new XBRXLangService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXStriService.class, new XBRXStriService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXNameService.class, new XBRXNameService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXDescService.class, new XBRXDescService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXFileService.class, new XBRXFileService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXIconService.class, new XBRXIconService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXPlugService.class, new XBRXPlugService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXLineService.class, new XBRXLineService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXPaneService.class, new XBRXPaneService((XBARCatalog) getCatalog()));
//                            ((XBARCatalog) getCatalog()).addCatalogService(XBCXHDocService.class, new XBRXHDocService((XBARCatalog) getCatalog()));
//
//                            if ("localhost".equals(serviceClient.getHost()) || "127.0.0.1".equals(serviceClient.getHost())) {
//                                setConnectionStatus(ConnectionStatus.LOCAL);
//                            } else {
//                                setConnectionStatus(ConnectionStatus.NETWORK);
//                            }
//                            setCatalog(getCatalog());
//                        }
//                    }
//                }
//
//                // Create internal catalog instead
//                if (getCatalog() == null) {
//                    setConnectionStatus(ConnectionStatus.DISCONNECTED);
//                    operationProgressBar.setString(resourceBundle.getString("main_initcat") + "...");
//                    try {
//                        // Database Initialization
//                        String derbyHome = System.getProperty("user.home") + "/.java/.userPrefs/" + preferences.absolutePath();
//                        if (devMode) {
//                            derbyHome += "-dev";
//                        }
//                        System.setProperty("derby.system.home", derbyHome);
//                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("XBEditorPU");
//                        EntityManager em = emf.createEntityManager();
//                        em.setFlushMode(FlushModeType.AUTO);
//
//                        setCatalog(createCatalog(em));
//
//                        if (((XBAECatalog) getCatalog()).isShallInit()) {
//                            operationProgressBar.setString(resourceBundle.getString("main_defaultcat") + "...");
//                            ((XBAECatalog) getCatalog()).initCatalog();
//                        }
//
//                        Boolean catalogUpdateAllowed = Boolean.valueOf(preferences.get("catalogUpdateAllowed", Boolean.toString(true)));
//                        // Update internal catalog
//                        if (catalogUpdateAllowed && getCatalog() instanceof XBAECatalog) {
//                            // String catalogUpdate = preferences.get("catalogUpdateURL", null); // TODO
//
//                            try {
//                                operationProgressBar.setString(resourceBundle.getString("main_initws") + "...");
//                                wsHandler = new XBCUpdatePHPHandler((XBAECatalog) getCatalog());
//                                wsHandler.init();
//                                wsHandler.getPort().getLanguageId("en");
//                                ((XBAECatalog) getCatalog()).setUpdateHandler(wsHandler);
//                                XBCNodeService nodeService = (XBCNodeService) getCatalog().getCatalogService(XBCNodeService.class);
//                                XBCRoot catalogRoot = nodeService.getRoot();
//                                if (catalogRoot != null) {
//                                    Date localLastUpdate = catalogRoot.getLastUpdate();
//                                    Date lastUpdate = wsHandler.getPort().getRootLastUpdate();
//                                    if (localLastUpdate == null || localLastUpdate.before(lastUpdate)) {
//                                        // TODO: As there is currently no diff update available - wipe out entire database instead
//                                        em.close();
//                                        EntityManagerFactory emfDrop = Persistence.createEntityManagerFactory("XBEditorPU-drop");
//                                        EntityManager emDrop = emfDrop.createEntityManager();
//                                        emDrop.setFlushMode(FlushModeType.AUTO);
//                                        setCatalog(createCatalog(emDrop));
//                                        ((XBAECatalog) getCatalog()).initCatalog();
//                                        nodeService = (XBCNodeService) getCatalog().getCatalogService(XBCNodeService.class);
//                                        performUpdate((XBERoot) nodeService.getRoot(), lastUpdate);
//
//                                    }
//                                    setConnectionStatus(ConnectionStatus.INTERNET);
//                                }
//                            } catch (Exception ex) {
//                                Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
//                                wsHandler = null;
//                            }
//                        }
//
//                        setCatalog(getCatalog());
//                    } catch (Exception ex) {
//                        Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//
//                // Initialize plugins
//                if (getCatalog() != null) {
//                    pluginRepository = new XBPluginRepository();
//                    pluginRepository.addPluginsFrom(new File("plugins/").toURI());
//                    pluginRepository.processPlugins();
//                    pluginRepository.setCatalog(getCatalog());
//                    activePanel.setPluginRepository(pluginRepository);
//                }
//
//                ((CardLayout) statusPanel.getLayout()).first(statusPanel);
            }

            private XBACatalog createCatalog(EntityManager em) {
                XBACatalog createdCatalog = new XBAECatalog(em);

                ((XBAECatalog) createdCatalog).addCatalogService(XBCXLangService.class, new XBEXLangService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXStriService.class, new XBEXStriService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXNameService.class, new XBEXNameService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXDescService.class, new XBEXDescService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXFileService.class, new XBEXFileService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXIconService.class, new XBEXIconService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXPlugService.class, new XBEXPlugService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXLineService.class, new XBEXLineService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXPaneService.class, new XBEXPaneService((XBAECatalog) createdCatalog));
                ((XBAECatalog) createdCatalog).addCatalogService(XBCXHDocService.class, new XBEXHDocService((XBAECatalog) createdCatalog));
                return createdCatalog;
            }

            private void performUpdate(XBERoot catalogRoot, Date lastUpdate) {
                XBCUpdatePHPHandler wsHandler = new XBCUpdatePHPHandler((XBAECatalog) getCatalog());
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
        });
        catInitThread.start();
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

    public XBACatalog getCatalog() {
        return catalog;
    }
}
