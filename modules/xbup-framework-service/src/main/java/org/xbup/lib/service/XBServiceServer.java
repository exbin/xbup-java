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
package org.xbup.lib.service;

import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.remote.XBTCPRemoteServer;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.update.XBCUpdatePHPHandler;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.service.skeleton.XBPInfoSkeleton;
import org.xbup.lib.service.skeleton.XBPItemSkeleton;
import org.xbup.lib.service.skeleton.XBPNodeSkeleton;
import org.xbup.lib.service.skeleton.XBPRevSkeleton;
import org.xbup.lib.service.skeleton.XBPSpecSkeleton;
import org.xbup.lib.service.skeleton.XBPXDescSkeleton;
import org.xbup.lib.service.skeleton.XBPXFileSkeleton;
import org.xbup.lib.service.skeleton.XBPXIconSkeleton;
import org.xbup.lib.service.skeleton.XBPXLangSkeleton;
import org.xbup.lib.service.skeleton.XBPXLineSkeleton;
import org.xbup.lib.service.skeleton.XBPXNameSkeleton;
import org.xbup.lib.service.skeleton.XBPXPaneSkeleton;
import org.xbup.lib.service.skeleton.XBPXPlugSkeleton;
import org.xbup.lib.service.skeleton.XBPXStriSkeleton;

/**
 * XBUP service server.
 *
 * @version 0.1.25 2015/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBServiceServer extends XBTCPRemoteServer {

    private static final ResourceBundle myBundle = ResourceBundle.getBundle("org/xbup/lib/xbservice/messages");

    private final XBCUpdatePHPHandler wsHandler;
    /*
     private enum commandTypeEnum {
     SERVICE, INFO
     };
     private enum serviceCommandEnum {
     STOP, PING, LOGIN, RESTART
     };
     private enum serviceInfoEnum {
     VERSION
     };
     */

    public XBServiceServer(final XBAECatalog catalog) {
        super(catalog);

        wsHandler = new XBCUpdatePHPHandler((XBAECatalog) catalog);
        wsHandler.init();
        wsHandler.getPort().getLanguageId("en");
        ((XBAECatalog) catalog).setUpdateHandler(wsHandler);
        wsHandler.fireUsageEvent(false);

        // Register procedures
        addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.STOP_SERVICE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                source.matchEndXBT();
                Logger.getLogger(XBServiceServer.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, myBundle.getString("stop_service"));
                setStop(true);
            }
        });

        addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PING_SERVICE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.endXBT();
            }
        });

        addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LOGIN_SERVICE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
//            XBString loginName = new XBString();
//            XBString loginPass = new XBString();
//            XBL1SerialPullConsumer pullConsumer = new XBL1SerialPullConsumer(loginName);
/*            pullConsumer.attachXBL1PullProvider((XBL1PullProvider) source.getStream());
                 pullConsumer.processXBL1Pulls();
                 pullConsumer = new XBL1SerialPullConsumer(loginPass);
                 pullConsumer.attachXBL1PullProvider((XBL1PullProvider) source.getStream());
                 pullConsumer.processXBL1Pulls();*/
//            source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(1)); // Ok
                result.endXBT();
            }
        });

        addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.VERSION_INFO_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(1));
                result.endXBT();
            }
        });

        new XBPItemSkeleton(catalog).registerProcedures(this);
        new XBPNodeSkeleton(catalog).registerProcedures(this);
        new XBPSpecSkeleton(catalog).registerProcedures(this);
        new XBPRevSkeleton(catalog).registerProcedures(this);

        new XBPXLangSkeleton(catalog).registerProcedures(this);
        new XBPXNameSkeleton(catalog).registerProcedures(this);
        new XBPXDescSkeleton(catalog).registerProcedures(this);
        new XBPInfoSkeleton(catalog).registerProcedures(this);
        new XBPXFileSkeleton(catalog).registerProcedures(this);
        new XBPXIconSkeleton(catalog).registerProcedures(this);
        new XBPXPlugSkeleton(catalog).registerProcedures(this);
        new XBPXLineSkeleton(catalog).registerProcedures(this);
        new XBPXPaneSkeleton(catalog).registerProcedures(this);
        new XBPXStriSkeleton(catalog).registerProcedures(this);
    }

    private static final String defaultBundle = "sun.util.logging.resources.logging";
    public static final Level XB_SERVICE_STATUS = new XBServiceStatus("XB_SERVICE_STATUS", 758, defaultBundle);

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
        return (localLastUpdate == null || localLastUpdate.before(lastUpdate));
    }

    public void update() {
        // TODO: Update only new items, currently removes all
        ((XBAECatalog) catalog).clear();
        ((XBAECatalog) catalog).getUpdateHandler().processAllData(new Long[0]);
        // TODO: Support for sending full image latter...
    }

    /**
     * @return the wsHandler
     */
    public XBCUpdatePHPHandler getWsHandler() {
        return wsHandler;
    }
}
