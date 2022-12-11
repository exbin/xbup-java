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
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.client.stub.XBPNodeStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRootService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRNode catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPNodeSkeleton implements XBPCatalogSkeleton {

    private XBAECatalog catalog;

    public XBPNodeSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.ROOT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long rootId = provider.pullLongAttribute();
                provider.end();

                XBCRootService rootService = catalog.getCatalogService(XBCRootService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                Optional<XBCRoot> root = rootService.getItem(rootId);
                listener.process(root.isPresent() ? new UBNat32(root.get().getNode() .getId()) : XBTEmptyBlock.getEmptyBlock());
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.MAINROOT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBCNode node = nodeService.getMainRootNode().get();
                listener.process(new UBNat32(node.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODE_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long xbIndex = provider.pullLongAttribute();
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBCNode subNode = nodeService.getSubNode(node.get(), xbIndex);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(subNode == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(subNode.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODES_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                List<XBCNode> subNodes = nodeService.getSubNodes(node.get());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(subNodes.size());
                for (XBCNode subNode : subNodes) {
                    listener.putAttribute(subNode.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODESCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nodeService.getSubNodesCount(node.get())));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.FINDOWNER_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long count = provider.pullLongAttribute();
                Long[] nodePath = new Long[(int) count];
                for (int i = 0; i < count; i++) {
                    nodePath[i] = provider.pullLongAttribute();
                }
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                XBENode owner = (XBENode) (nodeService.findOwnerByXBPath(nodePath)).getParent().orElse(null);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(owner == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(owner.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.PATHNODE_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                Long[] nodePath = nodeService.getNodeXBPath(node.get());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(nodePath.length);
                for (Long pathId : nodePath) {
                    listener.putAttribute(pathId);
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.FINDNODE_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long count = provider.pullLongAttribute();
                Long[] nodePath = new Long[(int) count];
                for (int i = 0; i < count; i++) {
                    nodePath[i] = provider.pullLongAttribute();
                }
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                XBENode node = (XBENode) (nodeService.findNodeByXBPath(nodePath));

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(node == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(node.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.MAXSUBNODE_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nodeService.findMaxSubNodeXB(node.get())));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.NODESCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nodeService.getItemsCount()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODESEQ_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                long orderIndex = provider.pullLongAttribute();
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);
                XBENode subNode = node.isPresent() ? (XBENode) nodeService.getSubNodeSeq(node.get(), orderIndex) : null;

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(subNode == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(subNode.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODESEQCNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                Optional<XBCNode> node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nodeService.getSubNodesSeq(node.get())));
            }
        });
    }

    @Override
    public void setCatalog(XBAECatalog catalog) {
        this.catalog = catalog;
    }
}
