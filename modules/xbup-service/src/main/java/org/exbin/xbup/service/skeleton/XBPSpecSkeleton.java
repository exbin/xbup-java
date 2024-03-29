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
import java.util.List;
import java.util.Optional;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockJoin;
import org.exbin.xbup.catalog.entity.XBEBlockListCons;
import org.exbin.xbup.catalog.entity.XBEBlockListJoin;
import org.exbin.xbup.catalog.entity.XBEFormatJoin;
import org.exbin.xbup.catalog.entity.XBEGroupJoin;
import org.exbin.xbup.catalog.entity.XBESpecDef;
import org.exbin.xbup.client.stub.XBPSpecStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRSpec catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPSpecSkeleton implements XBPCatalogSkeleton {

    private XBAECatalog catalog;

    public XBPSpecSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    @Override
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCSpec spec = node.isPresent() ? specService.getSpecByOrder(node.get(), orderIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCSpec spec = node.isPresent() ? specService.getFormatSpec(node.get(), orderIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCSpec spec = node.isPresent() ? specService.getGroupSpec(node.get(), orderIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCSpec spec = node.isPresent() ? specService.getBlockSpec(node.get(), orderIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                List<XBCSpec> specs = specService.getSpecs(node.get());

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                List<XBCFormatSpec> specs = specService.getFormatSpecs(node.get());

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                List<XBCGroupSpec> specs = specService.getGroupSpecs(node.get());

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                List<XBCBlockSpec> specs = specService.getBlockSpecs(node.get());

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCSpec spec = node.isPresent() ? specService.findFormatSpecByXB(node.get(), xbIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCSpec spec = node.isPresent() ? specService.findGroupSpecByXB(node.get(), xbIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCSpec spec = node.isPresent() ? specService.findBlockSpecByXB(node.get(), xbIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.findMaxFormatSpecXB(node.get())));
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.findMaxGroupSpecXB(node.get())));
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.findMaxBlockSpecXB(node.get())));
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getSpecsCount(node.get())));
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getFormatSpecsCount(node.get())));
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getGroupSpecsCount(node.get())));
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getBlockSpecsCount(node.get())));
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);
                XBCSpecDef def = spec.isPresent() ? specService.findSpecDefByXB(spec.get(), xbIndex) : null;

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);
                List<XBCSpecDef> itemList = specService.getSpecDefs(spec.get());

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(spec.isPresent() ? new UBNat32(specService.getSpecDefsCount(spec.get())) : XBTEmptyBlock.getEmptyBlock());
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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Optional<XBCSpec> spec = specService.getItem(specId);
                XBESpecDef specDef = (XBESpecDef) specService.findSpecDefByXB(spec.get(), xbIndex);

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);

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

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                XBESpecDef specDef = (XBESpecDef) specService.getSpecDef(index);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(specDef == null || !specDef.getTargetRev().isPresent() ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(specDef.getTargetRev().get().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPSpecStub.BINDSCOUNT_BIND_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(specService.getItemsCount()));
            }
        });

    }

    @Override
    public void setCatalog(XBAECatalog catalog) {
        this.catalog = catalog;
    }
}
