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
package org.exbin.xbup.service.skeleton;

import java.io.IOException;
import java.util.Optional;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.catalog.entity.service.XBEItemService;
import org.exbin.xbup.catalog.entity.service.XBEXStriService;
import org.exbin.xbup.client.stub.XBPXStriStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
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
 * RPC skeleton class for XBRXStri catalog items.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXStriSkeleton implements XBPCatalogSkeleton {

    private XBAECatalog catalog;

    public XBPXStriSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXStriStub.ITEM_STRI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXStriService striService = catalog.getCatalogService(XBCXStriService.class);
                Optional<XBCXStri> stri = striService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(stri.isPresent() ? new UBNat32(stri.get().getItem().getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXStriStub.TEXT_STRI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXStriService striService = catalog.getCatalogService(XBCXStriService.class);
                Optional<XBCXStri> stri = striService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(stri.isPresent() ? new XBString(stri.get().getText()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXStriStub.NODEPATH_STRI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXStriService striService = catalog.getCatalogService(XBCXStriService.class);
                Optional<XBCXStri> stri = striService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(stri.isPresent() ? new XBString(stri.get().getNodePath()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXStriStub.ITEMSTRI_STRI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXStriService striService = (XBEXStriService) catalog.getCatalogService(XBCXStriService.class);
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                Optional<XBCItem> item = itemService.getItem(index.getNaturalLong());
                XBEXStri stri = item.isPresent() ? (XBEXStri) striService.getItemStringId(item.get()) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(stri == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(stri.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXStriStub.STRISCOUNT_STRI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBEXStriService striService = (XBEXStriService) catalog.getCatalogService(XBCXStriService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(striService.getItemsCount()));
            }
        });
    }

    @Override
    public void setCatalog(XBAECatalog catalog) {
        this.catalog = catalog;
    }
}
