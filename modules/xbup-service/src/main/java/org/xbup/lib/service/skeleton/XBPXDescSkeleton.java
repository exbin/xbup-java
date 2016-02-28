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
package org.xbup.lib.service.skeleton;

import java.io.IOException;
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXLanguage;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBEXDescService;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.client.stub.XBPXDescStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXLangService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXDesc catalog items.
 *
 * @version 0.1.25 2015/03/16
 * @author ExBin Project (http://exbin.org)
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
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);
                XBEXDesc desc = descService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(desc == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(desc.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.TEXT_DESC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);
                XBEXDesc desc = descService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(desc == null ? XBTEmptyBlock.getEmptyBlock() : new XBString(desc.getText()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.LANG_DESC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);
                XBEXDesc desc = descService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(desc == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(desc.getLang().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.ITEMDESC_DESC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                XBEXDesc desc = descService.getDefaultItemDesc(item);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(desc == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(desc.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.LANGDESC_DESC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute langIndex = provider.pullAttribute();
                provider.end();

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBEXLangService langService = (XBEXLangService) catalog.getCatalogService(XBCXLangService.class);
                XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                XBEXLanguage lang = langService.getItem(langIndex.getNaturalLong());
                XBEXDesc desc = descService.getItemDesc(item, lang);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(desc == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(desc.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.ITEMDESCS_DESC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                List<XBCXDesc> itemDescs = descService.getItemDescs(item);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.putType(new XBFixedBlockType());
                listener.putAttribute(itemDescs.size());
                for (XBCXDesc itemDesc : itemDescs) {
                    listener.putAttribute(itemDesc.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXDescStub.DESCSCOUNT_DESC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(descService.getItemsCount()));
            }
        });
    }
}
