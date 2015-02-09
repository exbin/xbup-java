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
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXItemInfo;
import org.xbup.lib.core.catalog.base.XBCJoinDef;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.service.XBCXInfoService;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.service.XBCXIconService;
import org.xbup.lib.core.catalog.base.service.XBCXLangService;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.service.XBCXPaneService;
import org.xbup.lib.core.catalog.base.service.XBCXPlugService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.remote.XBTCPRemoteServer;
import org.xbup.lib.core.stream.XBTStreamChecker;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBEBlockJoin;
import org.xbup.lib.catalog.entity.XBEBlockListCons;
import org.xbup.lib.catalog.entity.XBEBlockListJoin;
import org.xbup.lib.catalog.entity.XBEBlockRev;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatJoin;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupJoin;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBESpecDef;
import org.xbup.lib.catalog.entity.XBEXBlockLine;
import org.xbup.lib.catalog.entity.XBEXBlockPane;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXFile;
import org.xbup.lib.catalog.entity.XBEXLanguage;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.XBEXPlugLine;
import org.xbup.lib.catalog.entity.XBEXPlugPane;
import org.xbup.lib.catalog.entity.XBEXPlugin;
import org.xbup.lib.catalog.entity.XBEXStri;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.catalog.entity.service.XBERevService;
import org.xbup.lib.catalog.entity.service.XBESpecService;
import org.xbup.lib.catalog.entity.service.XBEXDescService;
import org.xbup.lib.catalog.entity.service.XBEXFileService;
import org.xbup.lib.catalog.entity.service.XBEXInfoService;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.catalog.entity.service.XBEXNameService;
import org.xbup.lib.catalog.entity.service.XBEXStriService;
import org.xbup.lib.catalog.update.XBCUpdatePHPHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;

/**
 * XBUP service server.
 *
 * @version 0.1.24 2015/01/22
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

    public XBServiceServer(XBAECatalog catalog) {
        super(catalog);

        wsHandler = new XBCUpdatePHPHandler((XBAECatalog) catalog);
        wsHandler.init();
        wsHandler.getPort().getLanguageId("en");
        ((XBAECatalog) catalog).setUpdateHandler(wsHandler);
        wsHandler.fireUsageEvent(false);

        // Register procedures
        addXBProcedure(new StopServiceProcedure());
        addXBProcedure(new PingServiceProcedure());
        addXBProcedure(new LoginServiceProcedure());

        addXBProcedure(new VersionInfoProcedure());

        addXBProcedure(new OwnerItemProcedure());
        addXBProcedure(new XBIndexItemProcedure());
        addXBProcedure(new ItemsCountItemProcedure());

        addXBProcedure(new RootNodeProcedure());
        addXBProcedure(new SubNodeNodeProcedure());
        addXBProcedure(new SubNodesNodeProcedure());
        addXBProcedure(new SubNodesCountNodeProcedure());
        addXBProcedure(new SpecNodeProcedure());
        addXBProcedure(new SpecsNodeProcedure());
        addXBProcedure(new FormatSpecNodeProcedure());
        addXBProcedure(new FormatSpecsNodeProcedure());
        addXBProcedure(new GroupSpecNodeProcedure());
        addXBProcedure(new GroupSpecsNodeProcedure());
        addXBProcedure(new BlockSpecNodeProcedure());
        addXBProcedure(new BlockSpecsNodeProcedure());
        addXBProcedure(new FindOwnerNodeProcedure());
        addXBProcedure(new PathNodeNodeProcedure());
        addXBProcedure(new FindNodeNodeProcedure());
        addXBProcedure(new MaxSubNodeNodeProcedure());
        addXBProcedure(new FindBlockSpecNodeProcedure());
        addXBProcedure(new MaxBlockSpecNodeProcedure());
        addXBProcedure(new FindGroupSpecNodeProcedure());
        addXBProcedure(new MaxGroupSpecNodeProcedure());
        addXBProcedure(new FindFormatSpecNodeProcedure());
        addXBProcedure(new MaxFormatSpecNodeProcedure());
        addXBProcedure(new SpecsCountNodeProcedure());
        addXBProcedure(new BlockSpecsCountNodeProcedure());
        addXBProcedure(new GroupSpecsCountNodeProcedure());
        addXBProcedure(new FormatSpecsCountNodeProcedure());
        addXBProcedure(new NodesCountNodeProcedure());
        addXBProcedure(new SubNodeSeqNodeProcedure());
        addXBProcedure(new SubNodesSeqCountNodeProcedure());

        addXBProcedure(new BindSpecProcedure());
        addXBProcedure(new BindsSpecProcedure());
        addXBProcedure(new BindsCountSpecProcedure());
        addXBProcedure(new FindBindSpecProcedure());
        addXBProcedure(new FindRevSpecProcedure());
        addXBProcedure(new FormatSpecsCountSpecProcedure());
        addXBProcedure(new GroupSpecsCountSpecProcedure());
        addXBProcedure(new BlockSpecsCountSpecProcedure());
        addXBProcedure(new SpecsCountSpecProcedure());
        addXBProcedure(new RevsCountSpecProcedure());
        addXBProcedure(new RevSpecProcedure());

        addXBProcedure(new BindsCountBindProcedure());
        addXBProcedure(new TargetBindProcedure());

        addXBProcedure(new XBLimitRevProcedure());
        addXBProcedure(new RevsCountRevProcedure());

        addXBProcedure(new CodeLangProcedure());
        addXBProcedure(new DefaultLangProcedure());
        addXBProcedure(new LangsLangProcedure());
        addXBProcedure(new LangsCountLangProcedure());

        addXBProcedure(new ItemNameProcedure());
        addXBProcedure(new TextNameProcedure());
        addXBProcedure(new LangNameProcedure());
        addXBProcedure(new ItemNameNameProcedure());
        addXBProcedure(new LangNameNameProcedure());
        addXBProcedure(new ItemNamesNameProcedure());
        addXBProcedure(new NamesCountNameProcedure());

        addXBProcedure(new ItemDescProcedure());
        addXBProcedure(new TextDescProcedure());
        addXBProcedure(new LangDescProcedure());
        addXBProcedure(new ItemDescDescProcedure());
        addXBProcedure(new LangNameDescProcedure());
        addXBProcedure(new ItemDescsDescProcedure());
        addXBProcedure(new DescsCountDescProcedure());

        addXBProcedure(new NodeInfoProcedure());
        addXBProcedure(new InfosCountInfoProcedure());
        addXBProcedure(new FileNameInfoProcedure());
        addXBProcedure(new PathInfoProcedure());

        addXBProcedure(new OwnerFileProcedure());
        addXBProcedure(new FileNameFileProcedure());
        addXBProcedure(new DataFileProcedure());

        addXBProcedure(new OwnerIconProcedure());
        addXBProcedure(new ModeIconProcedure());
        addXBProcedure(new XBIndexIconProcedure());
        addXBProcedure(new DefaultItemIconProcedure());
        addXBProcedure(new FileIconProcedure());

        addXBProcedure(new OwnerPluginProcedure());
        addXBProcedure(new FilePluginProcedure());
        addXBProcedure(new IndexPluginProcedure());
        addXBProcedure(new LineIndexPluginProcedure());
        addXBProcedure(new LinePluginPluginProcedure());
        addXBProcedure(new PaneIndexPluginProcedure());
        addXBProcedure(new PanePluginPluginProcedure());

        addXBProcedure(new RevLineProcedure());
        addXBProcedure(new PlugLineProcedure());
        addXBProcedure(new PriorityLineProcedure());
        addXBProcedure(new LinesCountLineProcedure());
        addXBProcedure(new RevLineLineProcedure());
        addXBProcedure(new PlugLinesCountLineProcedure());
        addXBProcedure(new PlugLineLineProcedure());

        addXBProcedure(new RevPaneProcedure());
        addXBProcedure(new PlugPaneProcedure());
        addXBProcedure(new PriorityPaneProcedure());
        addXBProcedure(new PanesCountPaneProcedure());
        addXBProcedure(new RevPanePaneProcedure());
        addXBProcedure(new PlugPanesCountPaneProcedure());
        addXBProcedure(new PlugPanePaneProcedure());

        addXBProcedure(new ItemStriProcedure());
        addXBProcedure(new TextStriProcedure());
        addXBProcedure(new NodePathStriProcedure());
        addXBProcedure(new ItemStriStriProcedure());
        addXBProcedure(new StrisCountStriProcedure());
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

    private class StopServiceProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.STOP_SERVICE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            Logger.getLogger(XBServiceServer.class.getName()).log(XBServiceServer.XB_SERVICE_STATUS, myBundle.getString("stop_service"));
            setStop(true);
        }
    }

    private class PingServiceProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PING_SERVICE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.endXBT();
        }
    }

    private class LoginServiceProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LOGIN_SERVICE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
//            XBString loginName = new XBString();
//            XBString loginPass = new XBString();
//            XBL1SerialPullConsumer pullConsumer = new XBL1SerialPullConsumer(loginName);
/*            pullConsumer.attachXBL1PullProvider((XBL1PullProvider) source.getStream());
             pullConsumer.processXBL1Pulls();
             pullConsumer = new XBL1SerialPullConsumer(loginPass);
             pullConsumer.attachXBL1PullProvider((XBL1PullProvider) source.getStream());
             pullConsumer.processXBL1Pulls();*/
//            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1)); // Ok
            result.endXBT();
        }
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

    private class VersionInfoProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.VERSION_INFO_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            result.endXBT();
        }
    }

    private class OwnerItemProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.OWNER_ITEM_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (item != null) {
                XBEItem owner = (XBEItem) item.getParent();
                if (owner != null) {
                    result.attribXBT(new UBNat32(owner.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class XBIndexItemProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.XBINDEX_ITEM_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (item != null) {
                result.attribXBT(new UBNat32(item.getXBIndex()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class ItemsCountItemProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEMSCOUNT_ITEM_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(itemService.getItemsCount()));
            result.endXBT();
        }
    }

    private class RootNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ROOT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(nodeService.getRootNode().getId()));
            result.endXBT();
        }
    }

    private class RootProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ROOT_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(nodeService.getRoot().getId()));
            result.attribXBT(new UBNat32(nodeService.getRoot().getLastUpdate().getTime()));
            result.endXBT();
        }
    }

    private class SubNodeNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SUBNODE_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBENode result2 = (XBENode) nodeService.getSubNode(node, index);
            if (result2 != null) {
                result.attribXBT(new UBNat32(result2.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class SubNodesNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SUBNODES_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCNode> itemList = nodeService.getSubNodes(node);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCNode> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBENode) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class SubNodesCountNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SUBNODESCOUNT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(nodeService.getSubNodesCount(node)));
            result.endXBT();
        }
    }

    private class SpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBESpec) specService.getSpecByOrder(node, index)).getId())); // TODO: General spec has no xbIndex
            result.endXBT();
        }
    }

    private class SpecsNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPECS_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCSpec> itemList = specService.getSpecs(node);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCSpec> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBESpec) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class FormatSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (node != null) {
                XBEFormatSpec spec = (XBEFormatSpec) specService.getFormatSpec(node, index);
                if (spec != null) {
                    result.attribXBT(new UBNat32(spec.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class FormatSpecsNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPECS_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCFormatSpec> itemList = specService.getFormatSpecs(node);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCFormatSpec> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBEFormatSpec) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class GroupSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (node != null) {
                XBEGroupSpec spec = (XBEGroupSpec) specService.findGroupSpecByXB(node, index);
                if (spec != null) {
                    result.attribXBT(new UBNat32(spec.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class GroupSpecsNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPECS_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCGroupSpec> itemList = specService.getGroupSpecs(node);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCGroupSpec> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBEGroupSpec) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class BlockSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (node != null) {
                XBEBlockSpec spec = (XBEBlockSpec) specService.getBlockSpec(node, index);
                if (spec != null) {
                    result.attribXBT(new UBNat32(spec.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class BlockSpecsNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPECS_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCBlockSpec> itemList = specService.getBlockSpecs(node);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCBlockSpec> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBEBlockSpec) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class FindOwnerNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDOWNER_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            long count = source.attribXBT().getNaturalLong();
            Long[] nodePath = new Long[(int) count];
            for (int i = 0; i < count; i++) {
                nodePath[i] = source.attribXBT().getNaturalLong();
            }
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBENode owner = (XBENode) (nodeService.findOwnerByXBPath(nodePath)).getParent();
            if (owner != null) {
                result.attribXBT(new UBNat32(owner.getId()));
            }
            result.endXBT();
        }
    }

    private class PathNodeNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PATHNODE_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            Long[] itemPath = nodeService.getNodeXBPath(node);
            result.attribXBT(new UBNat32(itemPath.length));
            for (Long itemPathSegment : itemPath) {
                result.attribXBT(new UBNat32(itemPathSegment));
            }
            result.endXBT();
        }
    }

    private class FindNodeNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDNODE_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            long count = source.attribXBT().getNaturalLong();
            Long[] nodePath = new Long[(int) count];
            for (int i = 0; i < count; i++) {
                nodePath[i] = source.attribXBT().getNaturalLong();
            }
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBENode node = (XBENode) nodeService.findNodeByXBPath(nodePath);
            if (node != null) {
                result.attribXBT(new UBNat32(node.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class MaxSubNodeNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MAXSUBNODE_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(nodeService.findMaxSubNodeXB(node)));
            result.endXBT();
        }
    }

    private class FindBlockSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDBLOCKSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (node != null) {
                XBEBlockSpec spec = (XBEBlockSpec) specService.findBlockSpecByXB(node, index);
                if (spec != null) {
                    result.attribXBT(new UBNat32(spec.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class MaxBlockSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MAXBLOCKSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.findMaxBlockSpecXB(node)));
            result.endXBT();
        }
    }

    private class FindGroupSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDGROUPSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (node != null) {
                XBEGroupSpec spec = (XBEGroupSpec) specService.findGroupSpecByXB(node, index);
                if (spec != null) {
                    result.attribXBT(new UBNat32(spec.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class MaxGroupSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MAXGROUPSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.findMaxGroupSpecXB(node)));
            result.endXBT();
        }
    }

    private class FindFormatSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDFORMATSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (node != null) {
                XBEFormatSpec spec = (XBEFormatSpec) specService.findFormatSpecByXB(node, index);
                if (spec != null) {
                    result.attribXBT(new UBNat32(spec.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class MaxFormatSpecNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MAXFORMATSPEC_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.findMaxFormatSpecXB(node)));
            result.endXBT();
        }
    }

    private class SpecsCountNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPECSCOUNT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getSpecsCount(node)));
            result.endXBT();
        }
    }

    private class BlockSpecsCountNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPECSCOUNT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getBlockSpecsCount(node)));
            result.endXBT();
        }
    }

    private class GroupSpecsCountNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPECSCOUNT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getGroupSpecsCount(node)));
            result.endXBT();
        }
    }

    private class FormatSpecsCountNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPECSCOUNT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getFormatSpecsCount(node)));
            result.endXBT();
        }
    }

    private class NodesCountNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.NODESCOUNT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(nodeService.getItemsCount()));
            result.endXBT();
        }
    }

    private class SubNodeSeqNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SUBNODESEQ_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBENode result2 = (XBENode) nodeService.getSubNodeSeq(node, index);
            if (result2 != null) {
                result.attribXBT(new UBNat32(result2.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class SubNodesSeqCountNodeProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SUBNODESEQCNT_NODE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBENode node = (XBENode) itemService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(nodeService.getSubNodesSeq(node)));
            result.endXBT();
        }
    }

    private class BindSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BIND_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long itemIndex = source.attribXBT().getNaturalLong();
            Long index = source.attribXBT().getNaturalLong();
            source.endXBT();
            XBESpec item = specService.getItem(itemIndex);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBESpecDef bind = (XBESpecDef) specService.findSpecDefByXB(item, index);
            if (bind == null) {
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
            } else {
                result.attribXBT(new UBNat32(bind.getId()));
                if (bind instanceof XBCJoinDef) {
                    result.attribXBT(new UBNat32(0));
                } else {
                    result.attribXBT(new UBNat32(1));
                }
            }
            result.endXBT();
        }
    }

    private class BindsSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BINDS_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBESpec spec = specService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCSpecDef> itemList = specService.getSpecDefs(spec);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCSpecDef> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBESpecDef) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class BindsCountSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BINDSCOUNT_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBESpec spec = specService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getSpecDefsCount(spec)));
            result.endXBT();
        }
    }

    private class FindBindSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDBIND_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBESpec spec = specService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBESpecDef bind = (XBESpecDef) specService.findSpecDefByXB(spec, index);
            if (bind != null) {
                result.attribXBT(new UBNat32(bind.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            int specDefType = 0;
            if (bind instanceof XBEBlockJoin || bind instanceof XBEGroupJoin || bind instanceof XBEFormatJoin) {
                specDefType = 1;
            } else if (bind instanceof XBEBlockListCons) {
                specDefType = 2;
            } else if (bind instanceof XBEBlockListJoin) {
                specDefType = 3;
            }
            result.attribXBT(new UBNat32(specDefType));
            result.endXBT();
        }
    }

    private class FindRevSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDREV_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBESpec spec = specService.getItem(index);
            index = source.attribXBT().getNaturalLong();
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBERev rev = (XBERev) revService.findRevByXB(spec, index);
            if (rev != null) {
                result.attribXBT(new UBNat32(rev.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class FormatSpecsCountSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPECSCOUNT_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getAllFormatSpecsCount()));
            result.endXBT();
        }
    }

    private class GroupSpecsCountSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPECSCOUNT_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getAllGroupSpecsCount()));
            result.endXBT();
        }
    }

    private class BlockSpecsCountSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPECSCOUNT_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getAllBlockSpecsCount()));
            result.endXBT();
        }
    }

    private class SpecsCountSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPECSCOUNT_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getAllSpecsCount()));
            result.endXBT();
        }
    }

    /*                        case XBLIMIT: {
     UBNat32 index = (UBNat32) source.attribXBT();
     source.endXBT();
     XBEItem item = catalog.getItemService().getItem(index.getLong());
     Long xbIndex = ((XBESpec) item).getXBIndex();
     target.beginXBT(false);
     target.attribXBT(new UBNat32(1));
     target.attribXBT(new UBNat32(0));
     if (xbIndex != null) {
     target.attribXBT(new UBNat32(xbIndex));
     }
     target.endXBT();
     break;
     } */
    private class RevsCountSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.REVSCOUNT_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
            Long index = source.attribXBT().getNaturalLong();
            XBESpec spec = specService.getItem(index);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(revService.getRevsCount(spec)));
            result.endXBT();
        }
    }

    private class RevSpecProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.REV_SPEC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
            Long itemIndex = source.attribXBT().getNaturalLong();
            Long index = source.attribXBT().getNaturalLong();
            source.endXBT();
            XBESpec item = specService.getItem(itemIndex);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (item == null) {
                result.attribXBT(new UBNat32(0));
            } else {
                XBERev rev = ((XBERev) revService.getRev(item, index));
                if (rev != null) {
                    result.attribXBT(new UBNat32(rev.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            }
            result.endXBT();
        }
    }

    private class TargetBindProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.TARGET_BIND_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBESpecDef specDef = (XBESpecDef) specService.getSpecDef(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (specDef != null) {
                XBERev rev = (XBERev) ((XBESpecDef) specDef).getTarget();
                if (rev != null) {
                    result.attribXBT(new UBNat32(rev.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class BindsCountBindProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BINDSCOUNT_BIND_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(specService.getItemsCount()));
            result.endXBT();
        }
    }

    private class XBLimitRevProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.XBLIMIT_REV_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (item != null) {
                Long xbLimit = ((XBERev) item).getXBLimit();
                if (xbLimit != null) {
                    result.attribXBT(new UBNat32(xbLimit));
                } else {
                    result.attribXBT(new UBNat32(1));
                }
            } else {
                result.attribXBT(new UBNat32(1));
            }
            result.endXBT();
        }
    }

    private class RevsCountRevProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.REVSCOUNT_REV_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(revService.getAllRevisionsCount()));
            result.endXBT();
        }
    }

    private class CodeLangProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.CODE_LANG_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXLanguage lang = ((XBEXLangService) getCatalog().getCatalogService(XBCXLangService.class)).getItem(index.getLong());
            XBString text = new XBString(((XBEXLanguage) lang).getLangCode());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            XBPListenerSerialHandler handler = new XBPListenerSerialHandler();
            handler.attachXBTEventListener(new XBTListenerToEventListener(result));
            text.serializeXB(handler);
//                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            result.endXBT();
        }
    }

    private class DefaultLangProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.DEFAULT_LANG_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBEXLanguage) ((XBEXLangService) getCatalog().getCatalogService(XBCXLangService.class)).getDefaultLang()).getId()));
            result.endXBT();
        }
    }

    private class LangsLangProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LANGS_LANG_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBEXLanguage> itemList = (List<XBEXLanguage>) (((XBEXLangService) getCatalog().getCatalogService(XBCXLangService.class)).getAllItems());
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBEXLanguage> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBEXLanguage) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class LangsCountLangProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LANGSCOUNT_LANG_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBEXLangService) getCatalog().getCatalogService(XBCXLangService.class)).getItemsCount()));
            result.endXBT();
        }
    }

    private class ItemNameProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEM_NAME_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXName name = (XBEXName) ((XBEXNameService) getCatalog().getCatalogService(XBCXNameService.class)).getItem(index.getLong());
            XBEItem item = (XBEItem) ((XBEXName) name).getItem();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (item != null) {
                result.attribXBT(new UBNat32(item.getId()));
            }
            result.endXBT();
        }
    }

    private class TextNameProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.TEXT_NAME_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXName name = (XBEXName) ((XBEXNameService) getCatalog().getCatalogService(XBCXNameService.class)).getItem(index.getLong());
            XBString text;
            if (name != null) {
                text = new XBString(((XBEXName) name).getText());
            } else {
                text = new XBString("");
            }
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            XBPListenerSerialHandler handler = new XBPListenerSerialHandler();
            handler.attachXBTEventListener(new XBTListenerToEventListener(result));
            text.serializeXB(handler);
//                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            result.endXBT();
        }
    }

    private class LangNameProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LANG_NAME_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXName name = (XBEXName) ((XBEXNameService) getCatalog().getCatalogService(XBCXNameService.class)).getItem(index.getLong());
            XBEXLanguage lang = (XBEXLanguage) ((XBEXName) name).getLang();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (lang != null) {
                result.attribXBT(new UBNat32(lang.getId()));
            }
            result.endXBT();
        }
    }

    private class ItemNameNameProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEMNAME_NAME_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBEXName itemName = ((XBEXName) ((XBEXNameService) getCatalog().getCatalogService(XBCXNameService.class)).getDefaultItemName(item));
            if (itemName != null) {
                result.attribXBT(new UBNat32(itemName.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class LangNameNameProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LANGNAME_NAME_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            UBNat32 langIndex = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            XBEXLanguage language = ((XBEXLangService) getCatalog().getCatalogService(XBCXLangService.class)).getItem(langIndex.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBEXName) ((XBEXNameService) getCatalog().getCatalogService(XBCXNameService.class)).getItemName(item, language)).getId()));
            result.endXBT();
        }
    }

    private class ItemNamesNameProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEMNAMES_NAME_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCXName> itemList = ((XBEXNameService) getCatalog().getCatalogService(XBCXNameService.class)).getItemNames(item);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCXName> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBEXName) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class NamesCountNameProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.NAMESCOUNT_NAME_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBEXNameService) getCatalog().getCatalogService(XBCXNameService.class)).getItemsCount()));
            result.endXBT();
        }
    }

    private class ItemDescProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEM_DESC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXDesc desc = ((XBEXDescService) getCatalog().getCatalogService(XBCXDescService.class)).getItem(index.getLong());
            XBEItem item = (XBEItem) ((XBEXDesc) desc).getItem();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (item != null) {
                result.attribXBT(new UBNat32(item.getId()));
            }
            result.endXBT();
        }
    }

    private class TextDescProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.TEXT_DESC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXDesc desc = ((XBEXDescService) getCatalog().getCatalogService(XBCXDescService.class)).getItem(index.getLong());
            String descText = ((XBEXDesc) desc).getText();
            if (descText == null) {
                descText = "";
            }
            XBString text = new XBString(descText);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            XBPListenerSerialHandler handler = new XBPListenerSerialHandler();
            handler.attachXBTEventListener(new XBTListenerToEventListener(result));
            text.serializeXB(handler);
//                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            result.endXBT();
        }
    }

    private class LangDescProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LANG_DESC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXDesc desc = ((XBEXDescService) getCatalog().getCatalogService(XBCXDescService.class)).getItem(index.getLong());
            XBEXLanguage lang = (XBEXLanguage) ((XBEXDesc) desc).getLang();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (lang != null) {
                result.attribXBT(new UBNat32(lang.getId()));
            }
            result.endXBT();
        }
    }

    private class ItemDescDescProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEMDESC_DESC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBEXDesc desc = ((XBEXDesc) ((XBEXDescService) getCatalog().getCatalogService(XBCXDescService.class)).getDefaultItemDesc(item));
            if (desc != null) {
                result.attribXBT(new UBNat32(desc.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class LangNameDescProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LANGNAME_DESC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            UBNat32 langIndex = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            XBEXLanguage language = ((XBEXLangService) getCatalog().getCatalogService(XBCXLangService.class)).getItem(langIndex.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBEXDesc) ((XBEXDescService) getCatalog().getCatalogService(XBCXDescService.class)).getItemDesc(item, language)).getId()));
            result.endXBT();
        }
    }

    private class ItemDescsDescProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEMDESCS_DESC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            List<XBCXDesc> itemList = ((XBEXDescService) getCatalog().getCatalogService(XBCXDescService.class)).getItemDescs(item);
            result.attribXBT(new UBNat32(itemList.size()));
            for (Iterator<XBCXDesc> it = itemList.iterator(); it.hasNext();) {
                result.attribXBT(new UBNat32(((XBEXDesc) it.next()).getId()));
            }
            result.endXBT();
        }
    }

    private class DescsCountDescProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.DESCSCOUNT_DESC_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBEXDescService) getCatalog().getCatalogService(XBCXDescService.class)).getItemsCount()));
            result.endXBT();
        }
    }

    private class NodeInfoProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.NODE_INFO_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBEXInfoService infoService = (XBEXInfoService) catalog.getCatalogService(XBCXInfoService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBENode node = (XBENode) itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBCXItemInfo info = infoService.getNodeInfo(node);
            result.attribXBT(new UBNat32(info.getId()));
            result.endXBT();
        }
    }

    private class InfosCountInfoProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.INFOSCOUNT_INFO_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEXInfoService infoService = (XBEXInfoService) catalog.getCatalogService(XBCXInfoService.class);
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(infoService.getItemsCount()));
            result.endXBT();
        }
    }

    private class FileNameInfoProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FILENAME_INFO_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            /*            XBEInfoService infoService = (XBEInfoService) catalog.getCatalogService(XBCXInfoService.class);
             UBNat32 index = (UBNat32) source.attribXBT();
             source.endXBT();
             XBEItemInfo info = (XBEItemInfo) infoService.getItem(index.getLong());
             String descText = info.getFilename();
             if (descText == null) descText = "";
             XBString text = new XBString(descText);
             result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
             result.attribXBT(new UBNat32(0));
             result.attribXBT(new UBNat32(0));
             result.attribXBT(new UBNat32(1));
             XBTSerialEventProducer producer = new XBTSerialEventProducer(text);
             producer.attachXBTEventListener(XBTDefaultEventListener.getXBTEventListener(result));
             producer.generateXBTEvents();
             //                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
             result.endXBT(); */
        }
    }

    private class PathInfoProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PATH_INFO_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            /*            XBEInfoService infoService = (XBEInfoService) catalog.getCatalogService(XBCXInfoService.class);
             UBNat32 index = (UBNat32) source.attribXBT();
             source.endXBT();
             XBEItemInfo info = (XBEItemInfo) infoService.getItem(index.getLong());
             String descText = info.getPath();
             if (descText == null) descText = "";
             XBString text = new XBString(descText);
             result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
             result.attribXBT(new UBNat32(0));
             result.attribXBT(new UBNat32(0));
             result.attribXBT(new UBNat32(1));
             XBTSerialEventProducer producer = new XBTSerialEventProducer(text);
             producer.attachXBTEventListener(XBTDefaultEventListener.getXBTEventListener(result));
             producer.generateXBTEvents();
             //                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
             result.endXBT(); */
        }
    }

    private class OwnerFileProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.OWNER_FILE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXFile file = (XBEXFile) ((XBEXFileService) getCatalog().getCatalogService(XBCXFileService.class)).getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(file.getNode().getId()));
            result.endXBT();
        }
    }

    private class FileNameFileProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FILENAME_FILE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXFileService fileService = (XBEXFileService) getCatalog().getCatalogService(XBCXFileService.class);
            XBEXFile file = fileService.getItem(index.getLong());
            String fileName = file.getFilename();
            if (fileName == null) {
                fileName = "";
            }
            XBString text = new XBString(fileName);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            XBPListenerSerialHandler handler = new XBPListenerSerialHandler();
            handler.attachXBTEventListener(new XBTListenerToEventListener(result));
            text.serializeXB(handler);
//                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            result.endXBT();
        }
    }

    private class DataFileProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.DATA_FILE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXFileService fileService = (XBCXFileService) getCatalog().getCatalogService(XBCXFileService.class);
            XBCXFile file = (XBCXFile) fileService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.dataXBT(fileService.getFile(file));
            result.endXBT();
            result.endXBT();
        }
    }

    private class OwnerIconProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.OWNER_ICON_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXIconService iconService = (XBCXIconService) getCatalog().getCatalogService(XBCXIconService.class);
            XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(icon.getParent().getId()));
            result.endXBT();
        }
    }

    private class ModeIconProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MODE_ICON_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXIconService iconService = (XBCXIconService) getCatalog().getCatalogService(XBCXIconService.class);
            XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(icon.getMode().getId()));
            result.endXBT();
        }
    }

    private class XBIndexIconProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.XBINDEX_ICON_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXIconService iconService = (XBCXIconService) getCatalog().getCatalogService(XBCXIconService.class);
            XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0)); // icon.getXBIndex()
            result.endXBT();
        }
    }

    private class DefaultItemIconProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.DEFAULTITEM_ICON_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCItem item = (XBCItem) itemService.getItem(index.getLong());
            XBCXIconService iconService = (XBCXIconService) getCatalog().getCatalogService(XBCXIconService.class);
            Long iconId = (long) 0;
            XBCXIcon icon = iconService.getDefaultIcon(item);
            if (icon != null) {
                iconId = icon.getId();
            }
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(iconId));
            result.endXBT();
        }
    }

    private class FileIconProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FILE_ICON_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXIconService iconService = (XBCXIconService) getCatalog().getCatalogService(XBCXIconService.class);
            XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (icon != null) {
                XBCXFile file = icon.getIconFile();
                if (file != null) {
                    result.attribXBT(new UBNat32(file.getId()));
                } else {
                    result.attribXBT(new UBNat32());
                }
            } else {
                result.attribXBT(new UBNat32());
            }
            result.endXBT();
        }
    }

    private class OwnerPluginProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.OWNER_PLUGIN_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPlugService pluginService = (XBCXPlugService) getCatalog().getCatalogService(XBCXPlugService.class);
            XBEXPlugin plugin = (XBEXPlugin) pluginService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(plugin.getOwner().getId()));
            result.endXBT();
        }
    }

    private class FilePluginProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FILE_PLUGIN_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPlugService pluginService = (XBCXPlugService) getCatalog().getCatalogService(XBCXPlugService.class);
            XBCXPlugin plugin = pluginService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (plugin != null) {
                XBCXFile file = plugin.getPluginFile();
                if (file != null) {
                    result.attribXBT(new UBNat32(file.getId()));
                } else {
                    result.attribXBT(new UBNat32());
                }
            } else {
                result.attribXBT(new UBNat32());
            }
            result.endXBT();
        }
    }

    private class IndexPluginProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.INDEX_PLUGIN_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPlugService pluginService = (XBCXPlugService) getCatalog().getCatalogService(XBCXPlugService.class);
            XBEXPlugin plugin = (XBEXPlugin) pluginService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(plugin.getPluginIndex()));
            result.endXBT();
        }
    }

    private class LinePluginPluginProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LINEPLUGIN_PLUGIN_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            XBEXPlugLine plugLine = (XBEXPlugLine) lineService.findPlugLineById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(plugLine.getPlugin().getId()));
            result.endXBT();
        }
    }

    private class LineIndexPluginProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LINEINDEX_PLUGIN_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            XBEXPlugLine plugLine = (XBEXPlugLine) lineService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(plugLine.getLineIndex()));
            result.endXBT();
        }
    }

    private class PanePluginPluginProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PANEPLUGIN_PLUGIN_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            XBEXPlugPane plugPane = (XBEXPlugPane) paneService.findPlugPaneById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(plugPane.getPlugin().getId()));
            result.endXBT();
        }
    }

    private class PaneIndexPluginProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PANEINDEX_PLUGIN_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            XBEXPlugPane plugPane = (XBEXPlugPane) paneService.findPlugPaneById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(plugPane.getPaneIndex()));
            result.endXBT();
        }
    }

    private class RevLineProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.REV_LINE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            XBEXBlockLine blockLine = (XBEXBlockLine) lineService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(blockLine.getBlockRev().getId()));
            result.endXBT();
        }
    }

    private class PlugLineProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PLUGIN_LINE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            XBEXBlockLine blockLine = (XBEXBlockLine) lineService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(blockLine.getLine().getId()));
            result.endXBT();
        }
    }

    private class PriorityLineProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PRIORITY_LINE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            XBEXBlockLine blockLine = (XBEXBlockLine) lineService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(blockLine.getPriority()));
            result.endXBT();
        }
    }

    private class LinesCountLineProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.LINESCOUNT_LINE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(lineService.getAllLinesCount()));
            result.endXBT();
        }
    }

    private class RevLineLineProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.REVLINE_LINE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            UBNat32 priority = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEBlockRev rev = (XBEBlockRev) revService.getItem(index.getLong());
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            XBEXBlockLine blockLine = (XBEXBlockLine) lineService.findLineByPR(rev, priority.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (blockLine == null) {
                result.attribXBT(new UBNat32(0));
            } else {
                result.attribXBT(new UBNat32(blockLine.getId()));
            }
            result.endXBT();
        }
    }

    private class PlugLinesCountLineProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PLUGLINESCOUNT_LINE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(lineService.getAllPlugLinesCount()));
            result.endXBT();
        }
    }

    private class PlugLineLineProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PLUGLINE_LINE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            UBNat32 line = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPlugService pluginService = (XBCXPlugService) getCatalog().getCatalogService(XBCXPlugService.class);
            XBCXPlugin plugin = pluginService.findById(index.getLong());
            XBCXLineService lineService = (XBCXLineService) getCatalog().getCatalogService(XBCXLineService.class);
            XBEXBlockLine blockLine = (XBEXBlockLine) lineService.getPlugLine(plugin, line.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(blockLine.getId()));
            result.endXBT();
        }
    }

    private class RevPaneProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.REV_PANE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(blockPane.getBlockRev().getId()));
            result.endXBT();
        }
    }

    private class PlugPaneProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PLUGIN_PANE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBEXPlugPane pane = blockPane.getPane();
            if (pane != null) {
                result.attribXBT(new UBNat32(pane.getId()));
            } else {
                result.attribXBT(new UBNat32());
            }
            result.endXBT();
        }
    }

    private class PriorityPaneProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PRIORITY_PANE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(blockPane.getPriority()));
            result.endXBT();
        }
    }

    private class PanesCountPaneProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PANESCOUNT_PANE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(paneService.getAllPanesCount()));
            result.endXBT();
        }
    }

    private class RevPanePaneProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.REVPANE_PANE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            UBNat32 priority = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEBlockRev rev = (XBEBlockRev) revService.getItem(index.getLong());
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findPaneByPR(rev, priority.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (blockPane == null) {
                result.attribXBT(new UBNat32(0));
            } else {
                result.attribXBT(new UBNat32(blockPane.getId()));
            }
            result.endXBT();
        }
    }

    private class PlugPanesCountPaneProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PLUGPANESCOUNT_PANE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(paneService.getAllPlugPanesCount()));
            result.endXBT();
        }
    }

    private class PlugPanePaneProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.PLUGPANE_PANE_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            UBNat32 pane = (UBNat32) source.attribXBT();
            source.endXBT();
            XBCXPlugService pluginService = (XBCXPlugService) getCatalog().getCatalogService(XBCXPlugService.class);
            XBCXPlugin plugin = pluginService.findById(index.getLong());
            XBCXPaneService paneService = (XBCXPaneService) getCatalog().getCatalogService(XBCXPaneService.class);
            XBEXBlockPane blockPane = (XBEXBlockPane) paneService.getPlugPane(plugin, pane.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(blockPane.getId()));
            result.endXBT();
        }
    }

    private class ItemStriProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEM_STRI_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXStri stri = (XBEXStri) ((XBEXStriService) getCatalog().getCatalogService(XBCXStriService.class)).getItem(index.getLong());
            XBEItem item = (XBEItem) ((XBEXStri) stri).getItem();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            if (item != null) {
                result.attribXBT(new UBNat32(item.getId()));
            }
            result.endXBT();
        }
    }

    private class TextStriProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.TEXT_STRI_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXStri stri = (XBEXStri) ((XBEXStriService) getCatalog().getCatalogService(XBCXStriService.class)).getItem(index.getLong());
            XBString text;
            if (stri != null) {
                text = new XBString(((XBEXStri) stri).getText());
            } else {
                text = new XBString("");
            }
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            XBPListenerSerialHandler handler = new XBPListenerSerialHandler();
            handler.attachXBTEventListener(new XBTListenerToEventListener(result));
            text.serializeXB(handler);
//                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            result.endXBT();
        }
    }

    private class NodePathStriProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.NODEPATH_STRI_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEXStri stri = (XBEXStri) ((XBEXStriService) getCatalog().getCatalogService(XBCXStriService.class)).getItem(index.getLong());
            XBString text;
            if (stri != null) {
                text = new XBString(((XBEXStri) stri).getNodePath());
            } else {
                text = new XBString("");
            }
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(1));
            XBPListenerSerialHandler handler = new XBPListenerSerialHandler();
            handler.attachXBTEventListener(new XBTListenerToEventListener(result));
            text.serializeXB(handler);
//                            new XBL1ToL0StreamConvertor.XBCL1ToL0DefaultStreamConvertor((XBCL1Streamable) new XBL2ToL1StreamConvertor.XBCL2ToL1DefaultStreamConvertor(text)).writeXBStream(output);
            result.endXBT();
        }
    }

    private class ItemStriStriProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.ITEMSTRI_STRI_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            UBNat32 index = (UBNat32) source.attribXBT();
            source.endXBT();
            XBEItem item = itemService.getItem(index.getLong());
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            XBEXStri itemStri = ((XBEXStri) ((XBEXStriService) getCatalog().getCatalogService(XBCXStriService.class)).getItemStringId(item));
            if (itemStri != null) {
                result.attribXBT(new UBNat32(itemStri.getId()));
            } else {
                result.attribXBT(new UBNat32(0));
            }
            result.endXBT();
        }
    }

    private class StrisCountStriProcedure implements XBProcedure {

        @Override
        public XBBlockType getType() {
            return new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.STRISCOUNT_STRI_PROCEDURE));
        }

        @Override
        public XBLFormatDecl getResultDecl() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void execute(XBTStreamChecker source, XBTListener result) throws XBProcessingException, IOException {
            source.endXBT();
            result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(0));
            result.attribXBT(new UBNat32(((XBEXStriService) getCatalog().getCatalogService(XBCXStriService.class)).getItemsCount()));
            result.endXBT();
        }
    }
}
