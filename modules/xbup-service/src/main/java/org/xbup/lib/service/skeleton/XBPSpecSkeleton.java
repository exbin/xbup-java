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
import org.xbup.lib.catalog.entity.XBEBlockJoin;
import org.xbup.lib.catalog.entity.XBEBlockListCons;
import org.xbup.lib.catalog.entity.XBEBlockListJoin;
import org.xbup.lib.catalog.entity.XBEFormatJoin;
import org.xbup.lib.catalog.entity.XBEGroupJoin;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBESpecDef;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.catalog.entity.service.XBESpecService;
import org.xbup.lib.client.stub.XBPSpecStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRSpec catalog items.
 *
 * @version 0.1.25 2015/03/16
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSpecSkeleton {

    private final XBAECatalog catalog;

    public XBPSpecSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.SPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long orderIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBESpec spec = node == null ? null : specService.getSpecByOrder(node, orderIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(spec.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FORMATSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long orderIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBESpec spec = node == null ? null : specService.getFormatSpec(node, orderIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(spec.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.GROUPSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long orderIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBESpec spec = node == null ? null : specService.getGroupSpec(node, orderIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(spec.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BLOCKSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long orderIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBESpec spec = node == null ? null : specService.getBlockSpec(node, orderIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(spec.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.SPECS_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                List<XBCSpec> specs = specService.getSpecs(node);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(specs.size());
                for (XBCSpec spec : specs) {
                    listener.putAttribute(spec.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FORMATSPECS_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                List<XBCFormatSpec> specs = specService.getFormatSpecs(node);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(specs.size());
                for (XBCSpec spec : specs) {
                    listener.putAttribute(spec.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.GROUPSPECS_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                List<XBCGroupSpec> specs = specService.getGroupSpecs(node);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(specs.size());
                for (XBCSpec spec : specs) {
                    listener.putAttribute(spec.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BLOCKSPECS_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                List<XBCBlockSpec> specs = specService.getBlockSpecs(node);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(specs.size());
                for (XBCSpec spec : specs) {
                    listener.putAttribute(spec.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FINDFORMATSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long xbIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBESpec spec = node == null ? null : specService.findFormatSpecByXB(node, xbIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(spec.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FINDGROUPSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long xbIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBESpec spec = node == null ? null : specService.findGroupSpecByXB(node, xbIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(spec.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FINDBLOCKSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long xbIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBESpec spec = node == null ? null : specService.findBlockSpecByXB(node, xbIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(spec.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.MAXFORMATSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.findMaxFormatSpecXB(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.MAXGROUPSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.findMaxGroupSpecXB(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.MAXBLOCKSPEC_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.findMaxBlockSpecXB(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.SPECSCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getSpecsCount(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FORMATSPECSCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getFormatSpecsCount(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.GROUPSPECSCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getGroupSpecsCount(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BLOCKSPECSCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getBlockSpecsCount(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BIND_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                long xbIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBESpec spec = specService.getItem(specId);
                XBESpecDef def = spec == null ? null : specService.findSpecDefByXB(spec, xbIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(def == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(def.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BINDS_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBESpec spec = specService.getItem(specId);
                List<XBCSpecDef> itemList = specService.getSpecDefs(spec);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(itemList.size());
                for (XBCSpecDef specDef : itemList) {
                    listener.putAttribute(specDef.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BINDSCOUNT_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBESpec spec = specService.getItem(specId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(specService.getSpecDefsCount(spec)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FINDBIND_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long specId = provider.pullLongAttribute();
                long xbIndex = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBESpec spec = specService.getItem(specId);
                XBESpecDef specDef = (XBESpecDef) specService.findSpecDefByXB(spec, xbIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                if (specDef == null) {
                    listener.process(XBTEmptyBlock.getEmptyBlock());
                } else {
                    listener.begin();
                    listener.matchType();
                    listener.putAttribute(specDef.getId());

                    int specDefType = 0;
                    if (specDef instanceof XBEBlockJoin || specDef instanceof XBEGroupJoin || specDef instanceof XBEFormatJoin) {
                        specDefType = 1;
                    } else if (specDef instanceof XBEBlockListCons) {
                        specDefType = 2;
                    } else if (specDef instanceof XBEBlockListJoin) {
                        specDefType = 3;
                    }
                    listener.putAttribute(specDefType);
                    listener.end();
                }
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.FORMATSPECSCOUNT_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getAllFormatSpecsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.GROUPSPECSCOUNT_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getAllGroupSpecsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BLOCKSPECSCOUNT_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getAllBlockSpecsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.SPECSCOUNT_SPEC_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getAllSpecsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.TARGET_BIND_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long index = provider.pullLongAttribute();
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBESpecDef specDef = (XBESpecDef) specService.getSpecDef(index);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(specDef == null || specDef.getTarget() == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(specDef.getTarget().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BINDSCOUNT_BIND_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getItemsCount()));
            }
        });

    }
}
