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
import java.util.Date;
import java.util.Optional;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.client.stub.XBPRootStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.service.XBCRootService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.type.XBDateTime;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRNode catalog items.
 *
 * @version 0.2.1 2020/08/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBPRootSkeleton {

    private final XBAECatalog catalog;

    public XBPRootSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRootStub.CATALOG_ROOT_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCRootService rootService = catalog.getCatalogService(XBCRootService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBCRoot root = rootService.getMainRoot();
                listener.process(new UBNat32(root.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRootStub.LASTUPDATE_ROOT_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long rootId = provider.pullLongAttribute();
                provider.end();

                XBCRootService rootService = catalog.getCatalogService(XBCRootService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                Optional<XBCRoot> root = rootService.getItem(rootId);
                Date lastUpdate = root.isPresent() ? root.get().getLastUpdate().orElse(null) : null;
                listener.process(lastUpdate == null ? XBTEmptyBlock.getEmptyBlock() : new XBDateTime(lastUpdate));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRootStub.ROOT_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long rootId = provider.pullLongAttribute();
                provider.end();

                XBCRootService rootService = catalog.getCatalogService(XBCRootService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                Optional<XBCRoot> root = rootService.getItem(rootId);
                listener.process(root.isPresent() ? new UBNat32(root.get().getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });
    }
}
