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
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.client.stub.XBPItemStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRItem catalog items.
 *
 * @version 0.1.25 2015/03/10
 * @author XBUP Project (http://xbup.org)
 */
public class XBPItemSkeleton {

    private final XBAECatalog catalog;

    public XBPItemSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPItemStub.OWNER_ITEM_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                listener.process(item == null || item.getParent() == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(item.getParent().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPItemStub.XBINDEX_ITEM_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                listener.process(item == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(item.getXBIndex()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPItemStub.ITEMSCOUNT_ITEM_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(itemService.getItemsCount()));
            }
        });
    }
}
