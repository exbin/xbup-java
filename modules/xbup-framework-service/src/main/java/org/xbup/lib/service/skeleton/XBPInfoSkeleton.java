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
package org.xbup.lib.service.skeleton;

import java.io.IOException;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBEXInfoService;
import org.xbup.lib.client.stub.XBPInfoStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXItemInfo;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCXInfoService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRItemInfo catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPInfoSkeleton {

    private final XBAECatalog catalog;

    public XBPInfoSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPInfoStub.NODE_INFO_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBEXInfoService infoService = (XBEXInfoService) catalog.getCatalogService(XBCXInfoService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBENode node = (XBENode) itemService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBCXItemInfo info = infoService.getNodeInfo(node);
                result.attribXBT(new UBNat32(info.getId()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPInfoStub.INFOSCOUNT_INFO_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEXInfoService infoService = (XBEXInfoService) catalog.getCatalogService(XBCXInfoService.class);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(infoService.getItemsCount()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPInfoStub.FILENAME_INFO_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                /*            XBEInfoService infoService = (XBEInfoService) catalog.getCatalogService(XBCXInfoService.class);
                 UBNat32 index = (UBNat32) source.matchAttribXBT();
                 source.matchEndXBT();
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
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPInfoStub.PATH_INFO_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                /*            XBEInfoService infoService = (XBEInfoService) catalog.getCatalogService(XBCXInfoService.class);
                 UBNat32 index = (UBNat32) source.matchAttribXBT();
                 source.matchEndXBT();
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
        });

    }
}