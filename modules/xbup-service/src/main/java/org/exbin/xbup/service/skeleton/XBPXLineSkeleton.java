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
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEXBlockLine;
import org.exbin.xbup.catalog.entity.XBEXPlugLine;
import org.exbin.xbup.client.stub.XBPXLineStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCXLineService;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;
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
 * RPC skeleton class for XBRXBlockLine catalog items.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXLineSkeleton {

    private final XBAECatalog catalog;

    public XBPXLineSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.LINEPLUGIN_PLUGIN_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);
                XBEXPlugLine plugLine = (XBEXPlugLine) lineService.findPlugLineById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugLine.getPlugin().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.LINEINDEX_PLUGIN_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);
                XBEXPlugLine plugLine = (XBEXPlugLine) lineService.findPlugLineById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugLine.getLineIndex()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.REV_LINE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);
                XBEXBlockLine blockLine = (XBEXBlockLine) lineService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockLine.getBlockRev().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.PLUGIN_LINE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);
                XBEXBlockLine blockLine = (XBEXBlockLine) lineService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockLine.getLine().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.PRIORITY_LINE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);
                XBEXBlockLine blockLine = (XBEXBlockLine) lineService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockLine.getPriority()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.LINESCOUNT_LINE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(lineService.getItemsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.REVLINE_LINE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute priority = provider.pullAttribute();
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);
                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                Optional<XBEBlockRev> rev = revService.getItem(index.getNaturalLong());
                XBEXBlockLine blockLine = rev.isPresent() ? (XBEXBlockLine) lineService.findLineByPR(rev.get(), priority.getNaturalLong()) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockLine.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.PLUGLINESCOUNT_LINE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(lineService.getAllPlugLinesCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLineStub.PLUGLINE_LINE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute lineOrder = provider.pullAttribute();
                provider.end();

                XBCXLineService lineService = catalog.getCatalogService(XBCXLineService.class);
                XBCXPlugService pluginService = catalog.getCatalogService(XBCXPlugService.class);
                XBCXPlugin plugin = pluginService.findById(index.getNaturalLong());
                XBEXPlugLine plugLine = plugin == null ? null : (XBEXPlugLine) lineService.getPlugLine(plugin, lineOrder.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugLine.getId()));
            }
        });
    }
}
