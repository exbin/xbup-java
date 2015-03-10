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
import org.xbup.lib.catalog.entity.XBEXBlockPane;
import org.xbup.lib.catalog.entity.XBEXPlugPane;
import org.xbup.lib.catalog.entity.service.XBERevService;
import org.xbup.lib.client.stub.XBPXPaneStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCXPaneService;
import org.xbup.lib.core.catalog.base.service.XBCXPlugService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXBlockPane catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
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
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXPlugPane plugPane = (XBEXPlugPane) paneService.findPlugPaneById(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(plugPane.getPlugin().getId()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PANEINDEX_PLUGIN_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXPlugPane plugPane = (XBEXPlugPane) paneService.findPlugPaneById(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(plugPane.getPaneIndex()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.REV_PANE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(blockPane.getBlockRev().getId()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PLUGIN_PANE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBEXPlugPane pane = blockPane.getPane();
                if (pane != null) {
                    result.attribXBT(new UBNat32(pane.getId()));
                } else {
                    result.attribXBT(new UBNat32());
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PRIORITY_PANE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findById(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(blockPane.getPriority()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PANESCOUNT_PANE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                source.matchEndXBT();
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(paneService.getItemsCount()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.REVPANE_PANE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                UBNat32 priority = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEBlockRev rev = (XBEBlockRev) revService.getItem(index.getLong());
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.findPaneByPR(rev, priority.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (blockPane == null) {
                    result.attribXBT(new UBNat32(0));
                } else {
                    result.attribXBT(new UBNat32(blockPane.getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PLUGPANESCOUNT_PANE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                source.matchEndXBT();
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(paneService.getAllPlugPanesCount()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXPaneStub.PLUGPANE_PANE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                UBNat32 pane = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXPlugService pluginService = (XBCXPlugService) catalog.getCatalogService(XBCXPlugService.class);
                XBCXPlugin plugin = pluginService.findById(index.getLong());
                XBCXPaneService paneService = (XBCXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                XBEXBlockPane blockPane = (XBEXBlockPane) paneService.getPlugPane(plugin, pane.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(blockPane.getId()));
                result.endXBT();
            }
        });

    }
}
