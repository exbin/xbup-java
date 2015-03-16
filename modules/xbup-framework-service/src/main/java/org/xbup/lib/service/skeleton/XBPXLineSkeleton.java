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
import org.xbup.lib.catalog.entity.XBEBlockRev;
import org.xbup.lib.catalog.entity.XBEXBlockLine;
import org.xbup.lib.catalog.entity.XBEXPlugLine;
import org.xbup.lib.catalog.entity.service.XBERevService;
import org.xbup.lib.client.stub.XBPXLineStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.service.XBCXPlugService;
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
 * RPC skeleton class for XBRXBlockLine catalog items.
 *
 * @version 0.1.25 2015/03/16
 * @author XBUP Project (http://xbup.org)
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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);

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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
                XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
                XBEBlockRev rev = (XBEBlockRev) revService.getItem(index.getNaturalLong());
                XBEXBlockLine blockLine = rev == null ? null : (XBEXBlockLine) lineService.findLineByPR(rev, priority.getNaturalLong());

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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);

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

                XBCXLineService lineService = (XBCXLineService) catalog.getCatalogService(XBCXLineService.class);
                XBCXPlugService pluginService = (XBCXPlugService) catalog.getCatalogService(XBCXPlugService.class);
                XBCXPlugin plugin = pluginService.findById(index.getNaturalLong());
                XBEXPlugLine plugLine = plugin == null ? null : (XBEXPlugLine) lineService.getPlugLine(plugin, lineOrder.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugLine == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugLine.getId()));
            }
        });
    }
}
