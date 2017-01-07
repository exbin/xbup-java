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

import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.update.XBCUpdatePHPHandler;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.service.skeleton.XBPInfoSkeleton;
import org.exbin.xbup.service.skeleton.XBPItemSkeleton;
import org.exbin.xbup.service.skeleton.XBPNodeSkeleton;
import org.exbin.xbup.service.skeleton.XBPRevSkeleton;
import org.exbin.xbup.service.skeleton.XBPServiceSkeleton;
import org.exbin.xbup.service.skeleton.XBPSpecSkeleton;
import org.exbin.xbup.service.skeleton.XBPXDescSkeleton;
import org.exbin.xbup.service.skeleton.XBPXFileSkeleton;
import org.exbin.xbup.service.skeleton.XBPXHDocSkeleton;
import org.exbin.xbup.service.skeleton.XBPXIconSkeleton;
import org.exbin.xbup.service.skeleton.XBPXLangSkeleton;
import org.exbin.xbup.service.skeleton.XBPXLineSkeleton;
import org.exbin.xbup.service.skeleton.XBPXNameSkeleton;
import org.exbin.xbup.service.skeleton.XBPXPaneSkeleton;
import org.exbin.xbup.service.skeleton.XBPXPlugSkeleton;
import org.exbin.xbup.service.skeleton.XBPXStriSkeleton;

/**
 * XBUP catalog service server.
 *
 * @version 0.2.0 2016/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBCatalogNetServiceServer extends XBTCPServiceServer {

    private static final String LOG_BUNDLE = "sun.util.logging.resources.logging";
    public static final Level XB_SERVICE_STATUS = new XBServiceStatus("XB_SERVICE_STATUS", 758, LOG_BUNDLE);

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("org/exbin/xbup/service/messages");
    private final XBCUpdatePHPHandler wsHandler;

    public XBCatalogNetServiceServer(EntityManager entityManager, final XBAECatalog catalog) {
        super(entityManager, catalog);

        wsHandler = new XBCUpdatePHPHandler((XBAECatalog) catalog);
        wsHandler.init();
        wsHandler.getPort().getLanguageId("en");
        ((XBAECatalog) catalog).setUpdateHandler(wsHandler);
        wsHandler.fireUsageEvent(false);

        XBServiceServer server = this;
        // Register procedures
        new XBPServiceSkeleton(this).registerProcedures(server);

        new XBPItemSkeleton(catalog).registerProcedures(server);
        new XBPNodeSkeleton(catalog).registerProcedures(server);
        new XBPSpecSkeleton(catalog).registerProcedures(server);
        new XBPRevSkeleton(catalog).registerProcedures(server);

        new XBPXLangSkeleton(catalog).registerProcedures(server);
        new XBPXNameSkeleton(catalog).registerProcedures(server);
        new XBPXDescSkeleton(catalog).registerProcedures(server);
        new XBPInfoSkeleton(catalog).registerProcedures(server);
        new XBPXFileSkeleton(catalog).registerProcedures(server);
        new XBPXIconSkeleton(catalog).registerProcedures(server);
        new XBPXPlugSkeleton(catalog).registerProcedures(server);
        new XBPXStriSkeleton(catalog).registerProcedures(server);
        new XBPXLineSkeleton(catalog).registerProcedures(server);
        new XBPXPaneSkeleton(catalog).registerProcedures(server);
        new XBPXHDocSkeleton(catalog).registerProcedures(server);
    }

    public void performStop() {
        Logger.getLogger(org.exbin.xbup.service.XBCatalogNetServiceServer.class.getName()).log(org.exbin.xbup.service.XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("stop_service"));
        setStop(true);
    }

    public String getVersion() {
        return resourceBundle.getString("Application.version");
    }

    /**
     * Performs login to the server
     *
     * @param user user
     * @param password password
     * @return 0 for OK
     */
    public int login(String user, char[] password) {
        return 0;
    }

    private static class XBServiceStatus extends Level {

        public XBServiceStatus(String string, int i, String defaultBundle) {
            super(string, i, defaultBundle);
        }
    }

    public static class XBServiceLogHandler extends Handler {

        boolean verboseMode;

        public XBServiceLogHandler(boolean verboseMode) {
            this.verboseMode = verboseMode;
        }

        @Override
        public void publish(LogRecord record) {
            if (record.getLevel() == XB_SERVICE_STATUS) {
                if (verboseMode) {
                    if ("".equals(record.getMessage())) {
                        System.out.println();
                    } else {
                        System.out.println("STATUS: " + record.getMessage());
                    }
                }
            }
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    /**
     * Performs update from update source.
     *
     * @return true if update required
     */
    public boolean shallUpdate() {
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);

        if (nodeService.getRootNode() == null) {
            return true;
        }

        Date localLastUpdate = nodeService.getRoot().getLastUpdate();
        Date lastUpdate = getWsHandler().getPort().getRootLastUpdate();
        return (localLastUpdate == null || (lastUpdate != null && localLastUpdate.before(lastUpdate)));
    }

    public void update() {
        // TODO: Update only new items, currently removes all
        ((XBAECatalog) catalog).clear();
        ((XBAECatalog) catalog).getUpdateHandler().processAllData(new Long[0]);
        // TODO: Support for sending full image latter...
    }

    public XBCUpdatePHPHandler getWsHandler() {
        return wsHandler;
    }
}
