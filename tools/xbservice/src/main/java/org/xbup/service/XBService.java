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
package org.xbup.service;

import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.persistence.exceptions.DatabaseException;
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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
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
import org.xbup.lib.service.XBServiceServer;

/**
 * Console class for XBUP Service.
 *
 * @version 0.1.22 2013/08/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBService {

    private static ResourceBundle myBundle = ResourceBundle.getBundle("org/xbup/service/messages");
    private static Preferences preferences = Preferences.userNodeForPackage(XBService.class);
    private static boolean shallUpdate;
    private static boolean verboseMode;
    private static boolean rootCatalogMode;
    private static boolean devMode;
    private static boolean forceUpdate;
    private static boolean derbyMode = false;
    private static String tcpipPort;
    private static String tcpipInterface;
    private static XBAECatalog catalog = null;

    /** Creates a new instance of XBService */
    public XBService() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Parameters processing
            Options opt = new Options();
            Logger logger = Logger.getLogger("");
            logger.setLevel(Level.ALL);

            opt.addOption("h", "help", false, myBundle.getString("cl_option_help"));
            opt.addOption("port", true, myBundle.getString("cl_option_port"));
            opt.addOption("ip", true, myBundle.getString("cl_option_ip"));
            opt.addOption("v", false, myBundle.getString("cl_option_verbose"));
            opt.addOption("nopref", false, myBundle.getString("cl_option_nopref"));
            opt.addOption("stop", false, myBundle.getString("cl_option_stop"));
            opt.addOption("log", true, myBundle.getString("cl_option_log"));
            opt.addOption("db", true, myBundle.getString("cl_option_db"));
            opt.addOption("rootcat", false, myBundle.getString("cl_option_rootcat"));
            opt.addOption("dev", false, myBundle.getString("cl_option_dev"));
            opt.addOption("update", false, myBundle.getString("cl_option_update"));
            //opt.addOption("derby", false, myBundle.getString("cl_option_derby"));
            //opt.addOption("noderby", false, myBundle.getString("cl_option_noderby"));

            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);

            if ( cl.hasOption('h') ) {
                logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                logger.addHandler(new XBServiceServer.XBServiceLogHandler(true));
                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, myBundle.getString("service_head"));
                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "");
                HelpFormatter f = new HelpFormatter();
                f.printHelp(myBundle.getString("cl_syntax"), opt);
            } else {
                // Preferences processing
                verboseMode = cl.hasOption("v") || Boolean.getBoolean(preferences.get("verbose", Boolean.toString(false)));
                devMode = cl.hasOption("dev");
                forceUpdate = cl.hasOption("update");
                if (devMode) {
                    Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "Set development mode.");
                }
                rootCatalogMode = cl.hasOption("rootcat");
                if (rootCatalogMode) {
                    Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "Set root catalog mode.");
                }
//                if (cl.hasOption("nopref"))
                // 22594 is 0x5842 (XB)
                tcpipPort = cl.getOptionValue("port", preferences.get("tcpip_port", ((devMode) ? "22595" : "22594")));
                tcpipInterface = cl.getOptionValue("ip", preferences.get("tcpip_interface", "localhost"));
                logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                logger.addHandler(new XBServiceServer.XBServiceLogHandler(true));

                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, myBundle.getString("service_head"));
                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "");

                initCatalog();
            }
        }
        catch (ParseException ex) {
            Logger.getLogger(XBService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initCatalog() {
        Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, (myBundle.getString("init_catalog")+"..."));
        try {
            derbyMode = false;
            EntityManagerFactory emf;
            try {
                if (rootCatalogMode) {
                    if (devMode) {
                        emf = Persistence.createEntityManagerFactory("XBServiceMySQLDevPU");
                        catalog = new XBAECatalog(emf.createEntityManager(), "/var/www/html/catalog-dev/root");
                    } else {
                        emf = Persistence.createEntityManagerFactory("XBServiceMySQLPU");
                        catalog = new XBAECatalog(emf.createEntityManager(), "/var/www/html/catalog/root");
                    }
                } else {
                    emf = Persistence.createEntityManagerFactory("XBServicePU");
                    catalog = new XBAECatalog(emf.createEntityManager(), preferences.get("catalog_repository_path", System.getProperty("user.home") + "/.XBUP/repository"));
                }
            } catch (DatabaseException e) {
                System.setProperty("derby.system.home", System.getProperty("user.home") + "/.java/.userPrefs/" + preferences.absolutePath());
                emf = Persistence.createEntityManagerFactory("XBServiceDerbyPU");
                catalog = new XBAECatalog(emf.createEntityManager(), preferences.get("catalog_repository_path", System.getProperty("user.home") + "/.XBUP/repository"));
                derbyMode = true;
            }

            catalog.addCatalogService(XBCXLangService.class, new XBEXLangService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXStriService.class, new XBEXStriService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXNameService.class, new XBEXNameService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXDescService.class, new XBEXDescService((XBAECatalog) catalog));
            preferences.put("catalog_mode", "1");
            try {
                preferences.flush();
            } catch (BackingStoreException ex) {
                Logger.getLogger(XBService.class.getName()).log(Level.SEVERE, null, ex);
            }
            catalog.addCatalogService(XBCXFileService.class, new XBEXFileService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXIconService.class, new XBEXIconService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXPlugService.class, new XBEXPlugService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXLineService.class, new XBEXLineService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXPaneService.class, new XBEXPaneService((XBAECatalog) catalog));
            catalog.addCatalogService(XBCXHDocService.class, new XBEXHDocService((XBAECatalog) catalog));

            performUpdate();

            if (catalog.isShallInit()) {
                catalog.initCatalog();
            }
        } catch (DatabaseException e) {
            System.err.println(myBundle.getString("init_catalog_failed")+": "+e);
        }
    }

    private static void performUpdate() {
        int tcpipPortInt;
        try {
            tcpipPortInt = Integer.valueOf(tcpipPort);
        } catch (NumberFormatException e) {
            tcpipPortInt = 22594; // Fallback to default port
            Logger.getLogger(XBService.class.getName()).log(Level.SEVERE, "{0}{1}: {2}", new Object[]{myBundle.getString("error_warning"), myBundle.getString("error_tcpip_port"), tcpipPort});
        }

        Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, (myBundle.getString("init_service")+" "+tcpipInterface+":"+Integer.toString(tcpipPortInt)+"..."));
        // TODO: Only single connection for testing purposes (no connection pooling yet)
        XBServiceServer serviceHandler = new XBServiceServer(catalog);
        shallUpdate = (serviceHandler.shallUpdate() || forceUpdate) && (!rootCatalogMode);
        try {
            serviceHandler.open(tcpipPortInt);
            Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "");
            Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, myBundle.getString("init_service_success"));
            Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "");
            if (shallUpdate) {
                // TODO: Should be threaded
                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, myBundle.getString("init_update"));
                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "");

                if (catalog.isShallInit()) {
                    catalog.initCatalog();
                }

                XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
                Date lastUpdate = serviceHandler.getWsHandler().getPort().getRootLastUpdate();
                Date localLastUpdate = nodeService.getRoot().getLastUpdate();
                if (localLastUpdate != null) {
                    // TODO: As there is currently no diff update available - wipe out entire database instead
                    EntityManagerFactory emfDrop = Persistence.createEntityManagerFactory(derbyMode ? "XBServiceDerbyPU-drop" : "XBServicePU-drop");
                    EntityManager emDrop = emfDrop.createEntityManager();
                    emDrop.setFlushMode(FlushModeType.AUTO);
                    ((XBAECatalog) catalog).initCatalog();
                    serviceHandler.getWsHandler().reinit();
                }

                if (catalog.isShallInit()) {
                    catalog.initCatalog();
                }

                serviceHandler.update();

                XBERoot root = (XBERoot) nodeService.getRoot();
                root.setLastUpdate(lastUpdate);
                nodeService.persistItem(root);

                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "");
                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, myBundle.getString("done_update"));
                Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, "");
            }
            serviceHandler.run();
            Logger.getLogger(XBService.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, (myBundle.getString("stop_service_success")+"."));
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(XBService.class.getName()).log(Level.WARNING, (myBundle.getString("init_service_failed")+": "+e));
        }
    }
}
