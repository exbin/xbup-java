/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.catalog.entity.XBEXBlockUi;
import org.exbin.xbup.catalog.entity.XBEXPlugUi;
import org.exbin.xbup.client.stub.XBPXUiStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
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
 * RPC skeleton class for XBRXBlockUi catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPXUiSkeleton implements XBPCatalogSkeleton {

    private XBAECatalog catalog;

    public XBPXUiSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.UIPLUGIN_PLUGIN_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
                XBEXPlugUi plugLine = (XBEXPlugUi) uiService.findPlugUiById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugLine.getPlugin().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.METHODINDEX_PLUGIN_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXUiService lineService = catalog.getCatalogService(XBCXUiService.class);
                XBEXPlugUi plugLine = (XBEXPlugUi) lineService.findPlugUiById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugLine.getMethodIndex()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.REV_UI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
                XBEXBlockUi blockUi = (XBEXBlockUi) uiService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockUi == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockUi.getBlockRev().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.PLUGIN_UI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
                XBEXBlockUi blockUi = (XBEXBlockUi) uiService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockUi == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockUi.getUi().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.PRIORITY_UI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
                XBEXBlockUi blockUi = (XBEXBlockUi) uiService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockUi == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockUi.getPriority()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.UISCOUNT_UI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(uiService.getItemsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.REVUI_UI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute type = provider.pullAttribute();
                XBAttribute priority = provider.pullAttribute();
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                Optional<XBCRev> rev = revService.getItem(index.getNaturalLong());
                XBEXBlockUi blockUi = rev.isPresent() ? (XBEXBlockUi) uiService.findUiByPR((XBCBlockRev) rev.get(), XBPlugUiType.findByDbIndex(type.getNaturalInt()), priority.getNaturalLong()) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockUi == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockUi.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.PLUGUISCOUNT_UI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(uiService.getAllPlugUisCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXUiStub.PLUGUI_UI_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute type = provider.pullAttribute();
                XBAttribute lineOrder = provider.pullAttribute();
                provider.end();

                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
                XBCXPlugService pluginService = catalog.getCatalogService(XBCXPlugService.class);
                XBCXPlugin plugin = pluginService.findById(index.getNaturalLong());
                XBEXPlugUi plugUi = plugin == null ? null : (XBEXPlugUi) uiService.getPlugUi(plugin, XBPlugUiType.findByDbIndex(type.getNaturalInt()), lineOrder.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugUi == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugUi.getId()));
            }
        });
    }

    @Override
    public void setCatalog(XBAECatalog catalog) {
        this.catalog = catalog;
    }
}
