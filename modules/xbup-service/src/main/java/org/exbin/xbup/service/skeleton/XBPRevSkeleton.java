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
import java.util.List;
import java.util.Optional;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.entity.XBERev;
import org.exbin.xbup.client.stub.XBPRevStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRRev catalog items.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 */
public class XBPRevSkeleton {

    private final XBAECatalog catalog;

    public XBPRevSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRevStub.FINDREV_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                long xbIndex = provider.pullLongAttribute();
                provider.end();

                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);
                XBERev rev = spec.isPresent() ? (XBERev) revService.findRevByXB(spec.get(), xbIndex) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(rev == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(rev.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRevStub.REVSCOUNT_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                provider.end();

                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(revService.getRevsCount(spec.get())));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRevStub.REV_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                long orderIndex = provider.pullLongAttribute();
                provider.end();

                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);
                XBERev rev = spec.isPresent() ? (XBERev) revService.getRev(spec.get(), orderIndex) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(rev == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(rev.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRevStub.REVS_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                provider.end();

                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);
                List<XBCRev> revs = revService.getRevs(spec.get());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(revs.size());
                for (XBCRev rev : revs) {
                    listener.putAttribute(rev.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRevStub.XBLIMIT_REV_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long revId = provider.pullLongAttribute();
                provider.end();

                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                Optional<XBCRev> rev = revService.getItem(revId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(rev.isPresent() ? new UBNat32(rev.get().getXBLimit()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPRevStub.REVSCOUNT_REV_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(revService.getItemsCount()));
            }
        });
    }
}
