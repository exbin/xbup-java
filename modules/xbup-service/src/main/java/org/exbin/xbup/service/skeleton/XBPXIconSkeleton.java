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
import org.exbin.xbup.client.stub.XBPXIconStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXIcon catalog items.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXIconSkeleton {

    private final XBAECatalog catalog;

    public XBPXIconSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.OWNER_ICON_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
                Optional<XBCXIcon> icon = iconService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon.isPresent() ? new UBNat32(icon.get().getParent().getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.MODE_ICON_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
                Optional<XBCXIcon> icon = iconService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon.isPresent() ? new UBNat32(icon.get().getMode().getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.XBINDEX_ICON_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.DEFAULTITEM_ICON_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
                XBCItemService itemService = catalog.getCatalogService(XBCItemService.class);
                Optional<XBCItem> item = itemService.getItem(index.getNaturalLong());
                XBCXIcon icon = item.isPresent() ? iconService.getDefaultIcon(item.get()) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(icon.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.FILE_ICON_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
                Optional<XBCXIcon> icon = iconService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon.isPresent() ? new UBNat32(icon.get().getIconFile().getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.DEFAULTITEMBIG_ICON_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
                XBCItemService itemService = catalog.getCatalogService(XBCItemService.class);
                Optional<XBCItem> item = itemService.getItem(index.getNaturalLong());
                XBCXIcon icon = item.isPresent() ? iconService.getDefaultBigIcon(item.get()) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(icon.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.DEFAULTITEMSMALL_ICON_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
                XBCItemService itemService = catalog.getCatalogService(XBCItemService.class);
                Optional<XBCItem> item = itemService.getItem(index.getNaturalLong());
                XBCXIcon icon = item.isPresent() ? iconService.getDefaultSmallIcon(item.get()) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(icon.getId()));
            }
        });
    }
}
