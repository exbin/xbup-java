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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
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
 * RPC skeleton class for XBRXStri catalog items.
 *
 * @version 0.1.25 2015/03/16
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXStriSkeleton {

    private final XBAECatalog catalog;

    public XBPXStriSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXStriStub.ITEM_STRI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXStriService striService = (XBEXStriService) catalog.getCatalogService(XBCXStriService.class);
                XBEXStri stri = striService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(stri == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(stri.getItem().getId()));
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

                XBEXStriService striService = (XBEXStriService) catalog.getCatalogService(XBCXStriService.class);
                XBEXStri stri = striService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(stri == null ? XBTEmptyBlock.getEmptyBlock() : new XBString(stri.getText()));
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

                XBEXStriService striService = (XBEXStriService) catalog.getCatalogService(XBCXStriService.class);
                XBEXStri stri = striService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(stri == null ? XBTEmptyBlock.getEmptyBlock() : new XBString(stri.getNodePath()));
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
                XBEItem item = itemService.getItem(index.getNaturalLong());
                XBEXStri stri = item == null ? null : (XBEXStri) striService.getItemStringId(item);

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
}