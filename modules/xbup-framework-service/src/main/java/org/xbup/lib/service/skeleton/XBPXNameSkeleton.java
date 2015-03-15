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
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBEXLanguage;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.catalog.entity.service.XBEXNameService;
import org.xbup.lib.client.stub.XBPXNameStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCXLangService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
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
 * RPC skeleton class for XBRXName catalog items.
 *
 * @version 0.1.25 2015/03/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXNameSkeleton {

    private final XBAECatalog catalog;

    public XBPXNameSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXNameStub.ITEM_NAME_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);
                XBEXName name = (XBEXName) nameService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(name.getId()));
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

                XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);
                XBEXName name = (XBEXName) nameService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name == null ? XBTEmptyBlock.getEmptyBlock() : new XBString(name.getText()));
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

                XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);
                XBEXName name = (XBEXName) nameService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(name == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(name.getLang().getId()));
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

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                XBEXName name = (XBEXName) nameService.getDefaultItemName(item);

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

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBEXLangService langService = (XBEXLangService) catalog.getCatalogService(XBCXLangService.class);
                XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                XBEXLanguage lang = langService.getItem(langIndex.getNaturalLong());
                XBEXName name = (XBEXName) nameService.getItemName(item, lang);

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

                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);
                XBEItem item = itemService.getItem(index.getNaturalLong());
                List<XBCXName> itemNames = nameService.getItemNames(item);

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
}
