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
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEXBlockPane;
import org.exbin.xbup.catalog.entity.XBEXPlugPane;
import org.exbin.xbup.catalog.entity.service.XBERevService;
import org.exbin.xbup.client.stub.XBPXPaneStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCXPaneService;
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
 * RPC skeleton class for XBRXBlockPane catalog items.
 *
 * @version 0.1.25 2015/03/16
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXPaneSkeleton {

    private final XBAECatalog catalog;

    public XBPXPaneSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PANEPLUGIN_PLUGIN_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXPlugPane plugPane = (XBEXPlugPane) paneService.findPlugPaneById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugPane == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugPane.getPlugin().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PANEINDEX_PLUGIN_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXPlugPane plugPane = (XBEXPlugPane) paneService.findPlugPaneById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugPane == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugPane.getPaneIndex()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.REV_PANE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockPane == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockPane.getBlockRev().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PLUGIN_PANE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockPane == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockPane.getPane().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PRIORITY_PANE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockPane == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockPane.getPriority()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PANESCOUNT_PANE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(paneService.getItemsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.REVPANE_PANE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute priority = provider.pullAttribute();
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
                XBEBlockRev rev = (XBEBlockRev) revService.getItem(index.getNaturalLong());
                XBEXBlockPane blockPane = rev == null ? null : (XBEXBlockPane) paneService.findPaneByPR(rev, priority.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(blockPane == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(blockPane.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PLUGPANESCOUNT_PANE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(paneService.getAllPlugPanesCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PLUGPANE_PANE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                XBAttribute paneOrder = provider.pullAttribute();
                provider.end();

                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBCXPlugService pluginService = (XBCXPlugService) catalog.getCatalogService(XBCXPlugService.class);
                XBCXPlugin plugin = pluginService.findById(index.getNaturalLong());
                XBEXPlugPane plugPane = plugin == null ? null : (XBEXPlugPane) paneService.getPlugPane(plugin, paneOrder.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(plugPane == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(plugPane.getId()));
            }
        });
    }
}
