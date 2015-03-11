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
import java.util.Iterator;
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERoot;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.client.stub.XBPNodeStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRNode catalog items.
 *
 * @version 0.1.25 2015/03/11
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
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBENode result2 = (XBENode) nodeService.getSubNode(node, index);
                if (result2 != null) {
                    result.attribXBT(new UBNat32(result2.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODES_NODE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                List<XBCNode> itemList = nodeService.getSubNodes(node);
                result.attribXBT(new UBNat32(itemList.size()));
                for (Iterator<XBCNode> it = itemList.iterator(); it.hasNext();) {
                    result.attribXBT(new UBNat32(((XBENode) it.next()).getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODESCOUNT_NODE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(nodeService.getSubNodesCount(node)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.FINDOWNER_NODE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                long count = source.matchAttribXBT().getNaturalLong();
                Long[] nodePath = new Long[(int) count];
                for (int i = 0; i < count; i++) {
                    nodePath[i] = source.matchAttribXBT().getNaturalLong();
                }
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBENode owner = (XBENode) (nodeService.findOwnerByXBPath(nodePath)).getParent();
                if (owner != null) {
                    result.attribXBT(new UBNat32(owner.getId()));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.PATHNODE_NODE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                Long[] itemPath = nodeService.getNodeXBPath(node);
                result.attribXBT(new UBNat32(itemPath.length));
                for (Long itemPathSegment : itemPath) {
                    result.attribXBT(new UBNat32(itemPathSegment));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.FINDNODE_NODE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                long count = source.matchAttribXBT().getNaturalLong();
                Long[] nodePath = new Long[(int) count];
                for (int i = 0; i < count; i++) {
                    nodePath[i] = source.matchAttribXBT().getNaturalLong();
                }
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBENode node = (XBENode) nodeService.findNodeByXBPath(nodePath);
                if (node != null) {
                    result.attribXBT(new UBNat32(node.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.MAXSUBNODE_NODE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(nodeService.findMaxSubNodeXB(node)));
                result.endXBT();
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
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBENode result2 = (XBENode) nodeService.getSubNodeSeq(node, index);
                if (result2 != null) {
                    result.attribXBT(new UBNat32(result2.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPNodeStub.SUBNODESEQCNT_NODE_PROCEDURE), new XBMultiProcedure() {

            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBENode node = (XBENode) itemService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(nodeService.getSubNodesSeq(node)));
                result.endXBT();
            }
        });

    }
}
