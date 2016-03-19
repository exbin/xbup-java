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
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.client.stub.XBPXHDocStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
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
 * RPC skeleton class for XBRXHDoc catalog items.
 *
 * @version 0.1.25 2015/04/04
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXHDocSkeleton {

    private final XBAECatalog catalog;

    public XBPXHDocSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXHDocStub.OWNER_HDOC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXHDocService hdocService = (XBCXHDocService) catalog.getCatalogService(XBCXHDocService.class);
                XBCXHDoc hdoc = (XBCXHDoc) hdocService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(hdoc == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(hdoc.getItem().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXHDocStub.ITEM_HDOC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCItemService itemService = (XBCItemService) catalog.getCatalogService(XBCItemService.class);
                XBCXHDocService hdocService = (XBCXHDocService) catalog.getCatalogService(XBCXHDocService.class);
                XBCItem item = (XBCItem) itemService.getItem(index.getNaturalLong());
                XBCXHDoc hdoc = item == null ? null : (XBCXHDoc) hdocService.getDocumentation(item);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(hdoc == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(hdoc.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXHDocStub.FILE_HDOC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXHDocService hdocService = (XBCXHDocService) catalog.getCatalogService(XBCXHDocService.class);
                XBCXHDoc hdoc = (XBCXHDoc) hdocService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(hdoc == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(hdoc.getDocFile().getId()));
            }
        });
    }
}
