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
package org.xbup.lib.service.remote.provider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBEBlockJoin;
import org.xbup.lib.catalog.entity.XBEBlockListCons;
import org.xbup.lib.catalog.entity.XBEBlockListJoin;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatJoin;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupJoin;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBESpecDef;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBESpecService;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.catalog.base.XBCJoinDef;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBRemoteServer;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRSpec catalog items.
 *
 * @version 0.1.25 2015/02/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSpecManager {

    public static void registerProcedures(XBRemoteServer remoteServer, final XBAECatalog catalog) {
        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(((XBESpec) specService.getSpecByOrder(node, index)).getId())); // TODO: General spec has no xbIndex
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPECS_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                List<XBCSpec> itemList = specService.getSpecs(node);
                result.attribXBT(new UBNat32(itemList.size()));
                for (Iterator<XBCSpec> it = itemList.iterator(); it.hasNext();) {
                    result.attribXBT(new UBNat32(((XBESpec) it.next()).getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (node != null) {
                    XBEFormatSpec spec = (XBEFormatSpec) specService.getFormatSpec(node, index);
                    if (spec != null) {
                        result.attribXBT(new UBNat32(spec.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPECS_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                List<XBCFormatSpec> itemList = specService.getFormatSpecs(node);
                result.attribXBT(new UBNat32(itemList.size()));
                for (Iterator<XBCFormatSpec> it = itemList.iterator(); it.hasNext();) {
                    result.attribXBT(new UBNat32(((XBEFormatSpec) it.next()).getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (node != null) {
                    XBEGroupSpec spec = (XBEGroupSpec) specService.findGroupSpecByXB(node, index);
                    if (spec != null) {
                        result.attribXBT(new UBNat32(spec.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPECS_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                List<XBCGroupSpec> itemList = specService.getGroupSpecs(node);
                result.attribXBT(new UBNat32(itemList.size()));
                for (Iterator<XBCGroupSpec> it = itemList.iterator(); it.hasNext();) {
                    result.attribXBT(new UBNat32(((XBEGroupSpec) it.next()).getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (node != null) {
                    XBEBlockSpec spec = (XBEBlockSpec) specService.getBlockSpec(node, index);
                    if (spec != null) {
                        result.attribXBT(new UBNat32(spec.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPECS_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                List<XBCBlockSpec> itemList = specService.getBlockSpecs(node);
                result.attribXBT(new UBNat32(itemList.size()));
                for (Iterator<XBCBlockSpec> it = itemList.iterator(); it.hasNext();) {
                    result.attribXBT(new UBNat32(((XBEBlockSpec) it.next()).getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDBLOCKSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (node != null) {
                    XBEBlockSpec spec = (XBEBlockSpec) specService.findBlockSpecByXB(node, index);
                    if (spec != null) {
                        result.attribXBT(new UBNat32(spec.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MAXBLOCKSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.findMaxBlockSpecXB(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDGROUPSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (node != null) {
                    XBEGroupSpec spec = (XBEGroupSpec) specService.findGroupSpecByXB(node, index);
                    if (spec != null) {
                        result.attribXBT(new UBNat32(spec.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MAXGROUPSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.findMaxGroupSpecXB(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDFORMATSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (node != null) {
                    XBEFormatSpec spec = (XBEFormatSpec) specService.findFormatSpecByXB(node, index);
                    if (spec != null) {
                        result.attribXBT(new UBNat32(spec.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.MAXFORMATSPEC_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.findMaxFormatSpecXB(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPECSCOUNT_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getSpecsCount(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPECSCOUNT_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getBlockSpecsCount(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPECSCOUNT_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getGroupSpecsCount(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPECSCOUNT_NODE_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getFormatSpecsCount(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BIND_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long itemIndex = source.matchAttribXBT().getNaturalLong();
                Long index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                XBESpec item = specService.getItem(itemIndex);
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBESpecDef bind = (XBESpecDef) specService.findSpecDefByXB(item, index);
                if (bind == null) {
                    result.attribXBT(new UBNat32(0));
                    result.attribXBT(new UBNat32(0));
                } else {
                    result.attribXBT(new UBNat32(bind.getId()));
                    if (bind instanceof XBCJoinDef) {
                        result.attribXBT(new UBNat32(0));
                    } else {
                        result.attribXBT(new UBNat32(1));
                    }
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BINDS_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBESpec spec = specService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                List<XBCSpecDef> itemList = specService.getSpecDefs(spec);
                result.attribXBT(new UBNat32(itemList.size()));
                for (Iterator<XBCSpecDef> it = itemList.iterator(); it.hasNext();) {
                    result.attribXBT(new UBNat32(((XBESpecDef) it.next()).getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BINDSCOUNT_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBESpec spec = specService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getSpecDefsCount(spec)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FINDBIND_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBESpec spec = specService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBESpecDef bind = (XBESpecDef) specService.findSpecDefByXB(spec, index);
                if (bind != null) {
                    result.attribXBT(new UBNat32(bind.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                int specDefType = 0;
                if (bind instanceof XBEBlockJoin || bind instanceof XBEGroupJoin || bind instanceof XBEFormatJoin) {
                    specDefType = 1;
                } else if (bind instanceof XBEBlockListCons) {
                    specDefType = 2;
                } else if (bind instanceof XBEBlockListJoin) {
                    specDefType = 3;
                }
                result.attribXBT(new UBNat32(specDefType));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.FORMATSPECSCOUNT_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getAllFormatSpecsCount()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.GROUPSPECSCOUNT_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getAllGroupSpecsCount()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BLOCKSPECSCOUNT_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getAllBlockSpecsCount()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.SPECSCOUNT_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getAllSpecsCount()));
                result.endXBT();
            }
        });

        /*                        case XBLIMIT: {
         UBNat32 index = (UBNat32) source.matchAttribXBT();
         source.matchEndXBT();
         XBEItem item = catalog.getItemService().getItem(index.getLong());
         Long xbIndex = ((XBESpec) item).getXBIndex();
         target.beginXBT(false);
         target.attribXBT(new UBNat32(1));
         target.attribXBT(new UBNat32(0));
         if (xbIndex != null) {
         target.attribXBT(new UBNat32(xbIndex));
         }
         target.endXBT();
         break;
         } */

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.TARGET_BIND_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBESpecDef specDef = (XBESpecDef) specService.getSpecDef(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (specDef != null) {
                    XBERev rev = (XBERev) ((XBESpecDef) specDef).getTarget();
                    if (rev != null) {
                        result.attribXBT(new UBNat32(rev.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBServiceClient.BINDSCOUNT_BIND_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(specService.getItemsCount()));
                result.endXBT();
            }
        });

    }
}