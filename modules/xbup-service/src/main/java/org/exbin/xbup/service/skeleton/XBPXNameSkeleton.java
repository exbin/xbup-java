/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.service.skeleton;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.client.stub.XBPXNameStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBCXLangService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXName catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPXNameSkeleton implements XBPCatalogSkeleton {

    private XBAECatalog catalog;

    public XBPXNameSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.ITEM_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                Optional<XBCXName> name = nameService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name.isPresent() ? new UBNat32(name.get().getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.TEXT_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                Optional<XBCXName> name = nameService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name.isPresent() ? new XBString(name.get().getText()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.LANG_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                Optional<XBCXName> name = nameService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name.isPresent() ? new UBNat32(name.get().getLang().getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.ITEMNAME_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCItemService itemService = catalog.getCatalogService(XBCItemService.class);
                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                Optional<XBCItem> item = itemService.getItem(index.getNaturalLong());
                XBCXName name = nameService.getDefaultItemName(item.get());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(name.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.LANGNAME_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute langIndex = provider.pullAttribute();
                provider.end();

                XBCItemService itemService = catalog.getCatalogService(XBCItemService.class);
                XBCXLangService langService = catalog.getCatalogService(XBCXLangService.class);
                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                Optional<XBCItem> item = itemService.getItem(index.getNaturalLong());
                Optional<XBCXLanguage> lang = langService.getItem(langIndex.getNaturalLong());
                XBCXName name = nameService.getItemName(item.get(), lang.get());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(name.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.ITEMNAMES_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCItemService itemService = catalog.getCatalogService(XBCItemService.class);
                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                Optional<XBCItem> item = itemService.getItem(index.getNaturalLong());
                List<XBCXName> itemNames = nameService.getItemNames(item.get());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.putType(new XBFixedBlockType());
                listener.putAttribute(itemNames.size());
                for (XBCXName itemName : itemNames) {
                    listener.putAttribute(itemName.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.NAMESCOUNT_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nameService.getItemsCount()));
            }
        });
    }

    @Override
    public void setCatalog(XBAECatalog catalog) {
        this.catalog = catalog;
    }
}
