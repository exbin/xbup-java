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
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBEXStri;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBEXStriService;
import org.xbup.lib.client.stub.XBPXStriStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBRemoteServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXStri catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXStriSkeleton {

    private final XBAECatalog catalog;

    public XBPXStriSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBRemoteServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPXStriStub.ITEM_STRI_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEXStri stri = (XBEXStri) ((XBEXStriService) catalog.getCatalogService(XBCXStriService.class)).getItem(index.getLong());
                XBEItem item = (XBEItem) ((XBEXStri) stri).getItem();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (item != null) {
                    result.attribXBT(new UBNat32(item.getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPXStriStub.TEXT_STRI_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEXStri stri = (XBEXStri) ((XBEXStriService) catalog.getCatalogService(XBCXStriService.class)).getItem(index.getLong());
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
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPXStriStub.NODEPATH_STRI_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEXStri stri = (XBEXStri) ((XBEXStriService) catalog.getCatalogService(XBCXStriService.class)).getItem(index.getLong());
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
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPXStriStub.ITEMSTRI_STRI_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEItem item = itemService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBEXStri itemStri = ((XBEXStri) ((XBEXStriService) catalog.getCatalogService(XBCXStriService.class)).getItemStringId(item));
                if (itemStri != null) {
                    result.attribXBT(new UBNat32(itemStri.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPXStriStub.STRISCOUNT_STRI_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(((XBEXStriService) catalog.getCatalogService(XBCXStriService.class)).getItemsCount()));
                result.endXBT();
            }
        });
    }
}
