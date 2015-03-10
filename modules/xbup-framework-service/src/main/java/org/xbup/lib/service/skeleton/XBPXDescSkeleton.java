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
import java.util.Iterator;
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXLanguage;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBEXDescService;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.client.stub.XBPXDescStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXLangService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXDesc catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXDescSkeleton {

    private final XBAECatalog catalog;

    public XBPXDescSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.ITEM_DESC_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEXDesc desc = ((XBEXDescService) catalog.getCatalogService(XBCXDescService.class)).getItem(index.getLong());
                XBEItem item = (XBEItem) ((XBEXDesc) desc).getItem();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (item != null) {
                    result.attribXBT(new UBNat32(item.getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.TEXT_DESC_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEXDesc desc = ((XBEXDescService) catalog.getCatalogService(XBCXDescService.class)).getItem(index.getLong());
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
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.LANG_DESC_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEXDesc desc = ((XBEXDescService) catalog.getCatalogService(XBCXDescService.class)).getItem(index.getLong());
                XBEXLanguage lang = (XBEXLanguage) ((XBEXDesc) desc).getLang();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (lang != null) {
                    result.attribXBT(new UBNat32(lang.getId()));
                }
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.ITEMDESC_DESC_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEItem item = itemService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBEXDesc desc = ((XBEXDesc) ((XBEXDescService) catalog.getCatalogService(XBCXDescService.class)).getDefaultItemDesc(item));
                if (desc != null) {
                    result.attribXBT(new UBNat32(desc.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.LANGNAME_DESC_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                UBNat32 langIndex = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEItem item = itemService.getItem(index.getLong());
                XBEXLanguage language = ((XBEXLangService) catalog.getCatalogService(XBCXLangService.class)).getItem(langIndex.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(((XBEXDesc) ((XBEXDescService) catalog.getCatalogService(XBCXDescService.class)).getItemDesc(item, language)).getId()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.ITEMDESCS_DESC_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEItem item = itemService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                List<XBCXDesc> itemList = ((XBEXDescService) catalog.getCatalogService(XBCXDescService.class)).getItemDescs(item);
                result.attribXBT(new UBNat32(itemList.size()));
                for (Iterator<XBCXDesc> it = itemList.iterator(); it.hasNext();) {
                    result.attribXBT(new UBNat32(((XBEXDesc) it.next()).getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.DESCSCOUNT_DESC_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(((XBEXDescService) catalog.getCatalogService(XBCXDescService.class)).getItemsCount()));
                result.endXBT();
            }
        });
    }
}
