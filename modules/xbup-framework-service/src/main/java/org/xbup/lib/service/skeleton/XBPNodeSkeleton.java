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
import java.util.Date;
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERoot;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.client.stub.XBPNodeStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.type.XBDateTime;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRNode catalog items.
 *
 * @version 0.1.25 2015/03/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBPNodeSkeleton {

    private final XBAECatalog catalog;

    public XBPNodeSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.ROOT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long rootId = provider.pullLongAttribute();
                provider.end();

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBENode node = nodeService.getRoot(rootId).getNode();
                listener.process(node == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(node.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.CATALOG_ROOT_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBERoot root = nodeService.getRoot();
                listener.process(root == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(root.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.LASTUPDATE_ROOT_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long rootId = provider.pullLongAttribute();
                provider.end();

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBERoot root = nodeService.getRoot(rootId);
                Date lastUpdate = root == null ? null : root.getLastUpdate();
                listener.process(lastUpdate == null ? XBTEmptyBlock.getEmptyBlock() : new XBDateTime(lastUpdate));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.ROOT_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long rootId = provider.pullLongAttribute();
                provider.end();

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBERoot root = nodeService.getRoot(rootId);
                listener.process(root == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(root.getId()));
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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBENode subNode = nodeService.getSubNode(node, xbIndex);

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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                List<XBCNode> subNodes = nodeService.getSubNodes(node);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType(new XBFixedBlockType());
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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nodeService.getSubNodesCount(node)));
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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode owner = (XBENode) (nodeService.findOwnerByXBPath(nodePath)).getParent();

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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                Long[] nodePath = nodeService.getNodeXBPath(node);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType(new XBFixedBlockType());
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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nodeService.findMaxSubNodeXB(node)));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.NODESCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);

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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                XBENode subNode = node == null ? null : (XBENode) nodeService.getSubNodeSeq(node, orderIndex);

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

                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(nodeService.getSubNodesSeq(node)));
            }
        });
    }
}
