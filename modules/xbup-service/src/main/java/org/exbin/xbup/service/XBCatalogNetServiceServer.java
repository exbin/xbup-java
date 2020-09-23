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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityManager;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.client.update.XBCUpdateHandler;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.service.XBCRootService;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.service.skeleton.XBPCatalogSkeleton;
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
 * @version 0.2.1 2020/09/23
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBCatalogNetServiceServer extends XBTCPServiceServer {

    private static final String LOG_BUNDLE = "sun.util.logging.resources.logging";
    public static final Level XB_SERVICE_STATUS = new XBServiceStatus("XB_SERVICE_STATUS", 758, LOG_BUNDLE);

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("org/exbin/xbup/service/messages");
    private XBCUpdateHandler updateHandler = null;
    private final List<XBPCatalogSkeleton> skeletons = new ArrayList<>();

    public XBCatalogNetServiceServer() {
        super();

//        updateHandler = new XBCatalogServiceUpdateHandler(catalog, /* MainServiceClient */);
//        updateHandler.init();
//        ((XBAECatalog) catalog).setUpdateHandler(updateHandler);
        // Register procedures
        new XBPServiceSkeleton(this).registerProcedures(this);
    }

    @Override
    public void replaceCatalog() {
        super.replaceCatalog();
        XBAECatalog aeCatalog = (XBAECatalog) this.catalog;

        if (skeletons.isEmpty()) {
            XBServiceServer server = this;
            skeletons.add(new XBPItemSkeleton(aeCatalog));
            skeletons.add(new XBPRootSkeleton(aeCatalog));
            skeletons.add(new XBPNodeSkeleton(aeCatalog));
            skeletons.add(new XBPSpecSkeleton(aeCatalog));
            skeletons.add(new XBPRevSkeleton(aeCatalog));

            skeletons.add(new XBPXLangSkeleton(aeCatalog));
            skeletons.add(new XBPXNameSkeleton(aeCatalog));
            skeletons.add(new XBPXDescSkeleton(aeCatalog));
            skeletons.add(new XBPInfoSkeleton(aeCatalog));
            skeletons.add(new XBPXFileSkeleton(aeCatalog));
            skeletons.add(new XBPXIconSkeleton(aeCatalog));
            skeletons.add(new XBPXPlugSkeleton(aeCatalog));
            skeletons.add(new XBPXStriSkeleton(aeCatalog));
            skeletons.add(new XBPXUiSkeleton(aeCatalog));
            skeletons.add(new XBPXHDocSkeleton(aeCatalog));

            for (XBPCatalogSkeleton skeleton : skeletons) {
                skeleton.registerProcedures(server);
            }
        } else {
            for (XBPCatalogSkeleton skeleton : skeletons) {
                skeleton.setCatalog(aeCatalog);
            }
        }
    }

    public void performStop() {
        Logger.getLogger(org.exbin.xbup.service.XBCatalogNetServiceServer.class.getName()).log(org.exbin.xbup.service.XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("stop_service"));
        setStop(true);
    }

    @Nonnull
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

    @ParametersAreNonnullByDefault
    private static class XBServiceStatus extends Level {

        public XBServiceStatus(String string, int value, String defaultBundle) {
            super(string, value, defaultBundle);
        }
    }

    @ParametersAreNonnullByDefault
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
    public boolean shallUpdate(XBACatalog checkedCatalog) {
        XBCRootService rootService = checkedCatalog.getCatalogService(XBCRootService.class);

        if (!rootService.isMainPresent()) {
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
        ((XBAECatalog) catalog).getUpdateHandler().performUpdate();
        // TODO: Support for sending full image latter...
    }

    @Nullable
    public XBCUpdateHandler getUpdateHandler() {
        return updateHandler;
    }
}
