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
 * @version 0.2.1 2017/05/27
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
                XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(icon.getParent().getId()));
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
                XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(icon.getMode().getId()));
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
                XBCItem item = (XBCItem) itemService.getItem(index.getNaturalLong());
                XBCXIcon icon = item == null ? null : iconService.getDefaultIcon(item);

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
                XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(icon.getIconFile().getId()));
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
                XBCItem item = (XBCItem) itemService.getItem(index.getNaturalLong());
                XBCXIcon icon = item == null ? null : iconService.getDefaultBigIcon(item);

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
                XBCItem item = (XBCItem) itemService.getItem(index.getNaturalLong());
                XBCXIcon icon = item == null ? null : iconService.getDefaultSmallIcon(item);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(icon == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(icon.getId()));
            }
        });
    }
}
