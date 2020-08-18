/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.service;

import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.update.XBCUpdateHandler;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.service.skeleton.XBPInfoSkeleton;
import org.exbin.xbup.service.skeleton.XBPItemSkeleton;
import org.exbin.xbup.service.skeleton.XBPNodeSkeleton;
import org.exbin.xbup.service.skeleton.XBPRevSkeleton;
import org.exbin.xbup.service.skeleton.XBPRootSkeleton;
import org.exbin.xbup.service.skeleton.XBPServiceSkeleton;
import org.exbin.xbup.service.skeleton.XBPSpecSkeleton;
import org.exbin.xbup.service.skeleton.XBPXDescSkeleton;
import org.exbin.xbup.service.skeleton.XBPXFileSkeleton;
import org.exbin.xbup.service.skeleton.XBPXHDocSkeleton;
import org.exbin.xbup.service.skeleton.XBPXIconSkeleton;
import org.exbin.xbup.service.skeleton.XBPXLangSkeleton;
import org.exbin.xbup.service.skeleton.XBPXNameSkeleton;
import org.exbin.xbup.service.skeleton.XBPXPlugSkeleton;
import org.exbin.xbup.service.skeleton.XBPXStriSkeleton;
import org.exbin.xbup.service.skeleton.XBPXUiSkeleton;

/**
 * XBUP catalog service server.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 */
public class XBCatalogNetServiceServer extends XBTCPServiceServer {

    private static final String LOG_BUNDLE = "sun.util.logging.resources.logging";
    public static final Level XB_SERVICE_STATUS = new XBServiceStatus("XB_SERVICE_STATUS", 758, LOG_BUNDLE);

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("org/exbin/xbup/service/messages");
    private XBCUpdateHandler wsHandler = null;

    public XBCatalogNetServiceServer(EntityManager entityManager, final XBAECatalog catalog) {
        super(entityManager, catalog);

//        wsHandler = XBCUpdatePHPHandler((XBAECatalog) catalog);
//        wsHandler.init();
//        wsHandler.getPort().getLanguageId("en");
//        ((XBAECatalog) catalog).setUpdateHandler(wsHandler);
//        wsHandler.fireUsageEvent(false);
        XBServiceServer server = this;
        // Register procedures
        new XBPServiceSkeleton(this).registerProcedures(server);

        new XBPItemSkeleton(catalog).registerProcedures(server);
        new XBPRootSkeleton(catalog).registerProcedures(server);
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
        new XBPXUiSkeleton(catalog).registerProcedures(server);
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
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

        if (nodeService.getMainRootNode() == null) {
            return true;
        }

        return false;
//        Date localLastUpdate = nodeService.getRoot().getLastUpdate().orElse(null);
//        Date lastUpdate = getWsHandler().getPort().getRootLastUpdate();
//        return (localLastUpdate == null || (lastUpdate != null && localLastUpdate.before(lastUpdate)));
    }

    public void update() {
        // TODO: Update only new items, currently removes all
        ((XBAECatalog) catalog).clear();
        ((XBAECatalog) catalog).getUpdateHandler().processAllData(new Long[0]);
        // TODO: Support for sending full image latter...
    }

    @Nullable
    public XBCUpdateHandler getWsHandler() {
        return wsHandler;
    }
}
