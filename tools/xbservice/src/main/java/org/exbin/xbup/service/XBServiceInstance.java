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
package org.exbin.xbup.service;

import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.entity.XBERoot;
import org.exbin.xbup.catalog.entity.service.XBEXDescService;
import org.exbin.xbup.catalog.entity.service.XBEXFileService;
import org.exbin.xbup.catalog.entity.service.XBEXHDocService;
import org.exbin.xbup.catalog.entity.service.XBEXIconService;
import org.exbin.xbup.catalog.entity.service.XBEXLangService;
import org.exbin.xbup.catalog.entity.service.XBEXLineService;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.catalog.entity.service.XBEXPaneService;
import org.exbin.xbup.catalog.entity.service.XBEXPlugService;
import org.exbin.xbup.catalog.entity.service.XBEXStriService;
import org.exbin.xbup.catalog.update.XBCUpdatePHPHandler;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBDeclaration;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCFormatDecl;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXLangService;
import org.exbin.xbup.core.catalog.base.service.XBCXLineService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXPaneService;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.exbin.xbup.core.parser.XBProcessingException;

/**
 * Instance class for XBUP framework service.
 *
 * @version 0.2.0 2016/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBServiceInstance {

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("org/exbin/xbup/service/XBService");
    private boolean shallUpdate;
    private boolean verboseMode;
    private boolean rootCatalogMode;
    private boolean devMode;
    private boolean forceUpdate;
    private boolean derbyMode = false;
    private boolean debugMode = false;

    private String tcpipPort;
    private String tcpipInterface;
    private String derbyPath;
    private XBCatalogNetServiceServer serviceServer = null;
    private EntityManager entityManager = null;
    private XBAECatalog catalog = null;

    public XBServiceInstance() {
    }

    public void run() {
        Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("service_head"));
        Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");

        initCatalog();

        int tcpipPortInt;
        try {
            tcpipPortInt = Integer.parseInt(tcpipPort);
        } catch (NumberFormatException e) {
            tcpipPortInt = 22594; // Fallback to default port
            Logger.getLogger(XBServiceInstance.class.getName()).log(Level.SEVERE, "{0}{1}: {2}", new Object[]{resourceBundle.getString("error_warning"), resourceBundle.getString("error_tcpip_port"), tcpipPort});
        }

        Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, (resourceBundle.getString("init_service") + " " + tcpipInterface + ":" + Integer.toString(tcpipPortInt) + "..."));

        serviceServer = new XBCatalogNetServiceServer(entityManager, catalog);

        serviceServer.setDebugMode(debugMode);
        try {
            serviceServer.open(tcpipPortInt);
            performUpdate();

            // TODO Separate method?
            Long[] serviceFormatPath = new Long[3];
            serviceFormatPath[0] = 0l;
            serviceFormatPath[1] = 2l;
            serviceFormatPath[2] = 0l;
            XBCFormatDecl serviceFormatDecl = (XBCFormatDecl) catalog.findFormatTypeByPath(serviceFormatPath, 0);
            XBContext serviceContext = new XBContext();
            for (XBGroupDecl groupDeclservice : serviceFormatDecl.getGroupDecls()) {
                serviceContext.getGroups().add(XBDeclaration.convertCatalogGroup(groupDeclservice, catalog));
            }
            catalog.setRootContext(serviceContext);

            serviceServer.run();
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, (resourceBundle.getString("stop_service_success") + "."));
        } catch (IOException e) {
            Logger.getLogger(XBServiceInstance.class.getName()).log(Level.WARNING, (resourceBundle.getString("init_service_failed") + ": " + e));
        }
    }

    private void initCatalog() {
        Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, (resourceBundle.getString("init_catalog") + "..."));
        try {
            derbyMode = false;
            // Database Initialization
            String derbyHome = System.getProperty("user.home") + "/.java/.userPrefs/" + derbyPath;
            if (devMode) {
                derbyHome += "-dev";
            }
            System.setProperty("derby.system.home", derbyHome);
            EntityManagerFactory emf;
            String persistenceUnitName;
            try {
                if (rootCatalogMode) {
                    if (devMode) {
                        persistenceUnitName = "XBServiceMySQLDevPU";
                    } else {
                        persistenceUnitName = "XBServiceMySQLPU";
                    }
                } else {
                    persistenceUnitName = "XBServicePU";
                }

                emf = Persistence.createEntityManagerFactory(persistenceUnitName);
                entityManager = emf.createEntityManager();
                catalog = createCatalog(entityManager);
            } catch (DatabaseException | javax.persistence.PersistenceException e) {
                persistenceUnitName = "XBServiceDerbyPU";

                emf = Persistence.createEntityManagerFactory(persistenceUnitName);
                entityManager = emf.createEntityManager();
                catalog = createCatalog(entityManager);
                derbyMode = true;
            }
            

            if (catalog.isShallInit()) {
                catalog.initCatalog();
            }
        } catch (DatabaseException e) {
            System.err.println(resourceBundle.getString("init_catalog_failed") + ": " + e);
        }
    }

    private void performUpdate() {
        // TODO: Only single connection for testing purposes (no connection pooling yet)
        shallUpdate = (serviceServer.shallUpdate() || forceUpdate) && (!rootCatalogMode);
        try {
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("init_service_success"));
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
            if (shallUpdate) {
                // TODO: Should be threaded
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("init_update"));
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");

                if (catalog.isShallInit()) {
                    catalog.initCatalog();
                }

                XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
                Date lastUpdate = serviceServer.getWsHandler().getPort().getRootLastUpdate();
                Date localLastUpdate = nodeService.getRoot().getLastUpdate();
                if (localLastUpdate == null || localLastUpdate.before(lastUpdate)) {
                    // TODO: As there is currently no diff update available - wipe out entire database instead
                    EntityManagerFactory emfDrop = Persistence.createEntityManagerFactory(derbyMode ? "XBServiceDerbyPU-drop" : "XBServicePU-drop");
                    EntityManager emDrop = emfDrop.createEntityManager();
                    emDrop.setFlushMode(FlushModeType.AUTO);
                    catalog = (XBAECatalog) createCatalog(emDrop);
                    ((XBAECatalog) catalog).initCatalog();
                    nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
                    performUpdate((XBERoot) nodeService.getRoot(), lastUpdate);
                }

                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("done_update"));
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
            }
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBServiceInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void performUpdate(XBERoot catalogRoot, Date lastUpdate) {
        XBCUpdatePHPHandler wsHandler = new XBCUpdatePHPHandler((XBAECatalog) catalog);
        wsHandler.init();
        wsHandler.getPort().getLanguageId("en");

        wsHandler.fireUsageEvent(false);
        wsHandler.updateCatalog(catalogRoot, lastUpdate);
    }

    private XBAECatalog createCatalog(EntityManager em) {
        XBAECatalog createdCatalog = new XBAECatalog(em);

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

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public boolean isShallUpdate() {
        return shallUpdate;
    }

    public void setShallUpdate(boolean shallUpdate) {
        this.shallUpdate = shallUpdate;
    }

    public boolean isVerboseMode() {
        return verboseMode;
    }

    public void setVerboseMode(boolean verboseMode) {
        this.verboseMode = verboseMode;
    }

    public boolean isRootCatalogMode() {
        return rootCatalogMode;
    }

    public void setRootCatalogMode(boolean rootCatalogMode) {
        this.rootCatalogMode = rootCatalogMode;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isDerbyMode() {
        return derbyMode;
    }

    public void setDerbyMode(boolean derbyMode) {
        this.derbyMode = derbyMode;
    }

    public String getTcpipPort() {
        return tcpipPort;
    }

    public void setTcpipPort(String tcpipPort) {
        this.tcpipPort = tcpipPort;
    }

    public String getTcpipInterface() {
        return tcpipInterface;
    }

    public void setTcpipInterface(String tcpipInterface) {
        this.tcpipInterface = tcpipInterface;
    }

    public String getDerbyPath() {
        return derbyPath;
    }

    public void setDerbyPath(String derbyPath) {
        this.derbyPath = derbyPath;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
