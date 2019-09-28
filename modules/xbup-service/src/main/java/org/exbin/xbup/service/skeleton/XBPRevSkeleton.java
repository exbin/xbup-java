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
package org.exbin.xbup.service.skeleton;

import java.io.IOException;
import java.util.List;
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
 * @version 0.1.25 2015/03/25
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
                XBCSpec spec = (XBCSpec) specService.getItem(specId);
                XBERev rev = spec == null ? null : (XBERev) revService.findRevByXB(spec, xbIndex);

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
                XBCSpec spec = (XBCSpec) specService.getItem(specId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(revService.getRevsCount(spec)));
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
                XBCSpec spec = (XBCSpec) specService.getItem(specId);
                XBERev rev = spec == null ? null : (XBERev) revService.getRev(spec, orderIndex);

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
                XBCSpec spec = (XBCSpec) specService.getItem(specId);
                List<XBCRev> revs = revService.getRevs(spec);

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
                XBCRev rev = (XBCRev) revService.getItem(revId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(rev == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(rev.getXBLimit()));
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
