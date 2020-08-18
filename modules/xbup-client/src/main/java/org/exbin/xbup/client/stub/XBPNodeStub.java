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
package org.exbin.xbup.client.stub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRNode catalog items.
 *
 * @version 0.2.1 2020/08/18
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPNodeStub extends XBPBaseStub<XBRNode> {

    public static long[] ROOT_NODE_PROCEDURE = {0, 2, 4, 0, 0};
    public static long[] SUBNODE_NODE_PROCEDURE = {0, 2, 4, 1, 0};
    public static long[] SUBNODES_NODE_PROCEDURE = {0, 2, 4, 2, 0};
    public static long[] SUBNODESCOUNT_NODE_PROCEDURE = {0, 2, 4, 3, 0};
    public static long[] FINDOWNER_NODE_PROCEDURE = {0, 2, 4, 12, 0};
    public static long[] PATHNODE_NODE_PROCEDURE = {0, 2, 4, 13, 0};
    public static long[] FINDNODE_NODE_PROCEDURE = {0, 2, 4, 14, 0};
    public static long[] MAXSUBNODE_NODE_PROCEDURE = {0, 2, 4, 15, 0};
    public static long[] NODESCOUNT_NODE_PROCEDURE = {0, 2, 4, 26, 0};
    public static long[] SUBNODESEQ_NODE_PROCEDURE = {0, 2, 4, 27, 0};
    public static long[] SUBNODESEQCNT_NODE_PROCEDURE = {0, 2, 4, 28, 0};
    public static long[] MAINROOT_NODE_PROCEDURE = {0, 2, 4, 29, 0};

    private final XBCatalogServiceClient client;

    public XBPNodeStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRNode>() {
            @Override
            public XBRNode itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRNode(client, itemId);
            }
        }, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(NODESCOUNT_NODE_PROCEDURE)));
        this.client = client;
    }

    public XBRNode getRootNode(long rootId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ROOT_NODE_PROCEDURE), rootId);
        return index == null ? null : new XBRNode(client, index);
    }

    public XBRNode getMainRootNode() {
        Long index = XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(MAINROOT_NODE_PROCEDURE));
        return index == null ? null : new XBRNode(client, index);
    }

    public List<XBCNode> getSubNodes(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(SUBNODES_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCNode> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRNode(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }
            procedureCall.execute();

            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode getSubNode(XBCNode node, long index) {
        return findSubNodeByXB(node, index);
    }

    public long getSubNodesCount(XBCNode node) {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(SUBNODESCOUNT_NODE_PROCEDURE));
    }

    public XBRNode findNodeByXBPath(Long[] catalogPath) {
        XBRNode node = (XBRNode) getMainRootNode();
        for (Long catalogPathNode : catalogPath) {
            node = (XBRNode) getSubNode(node, catalogPathNode);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    public XBRNode findParentByXBPath(Long[] xbCatalogPath) {
        if (xbCatalogPath.length == 0) {
            return null;
        }
        XBRNode node = (XBRNode) getMainRootNode();
        for (int i = 0; i < xbCatalogPath.length - 1; i++) {
            node = (XBRNode) getSubNode(node, xbCatalogPath[i]);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    public Long[] getNodeXBPath(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(PATHNODE_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType();
                long pathLength = serialOutput.pullLongAttribute();
                Long[] result = new Long[(int) pathLength];
                for (int i = 0; i < pathLength; i++) {
                    result[i] = serialOutput.pullLongAttribute();
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }
            procedureCall.execute();
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode findOwnerByXBPath(Long[] catalogPath) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FINDOWNER_NODE_PROCEDURE));
            serialInput.putAttribute(catalogPath.length);
            for (Long catalogPathNode : catalogPath) {
                serialInput.putAttribute(catalogPathNode);
            }
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);
            procedureCall.execute();

            return new XBRNode(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode findSubNodeByXB(XBCNode node, long xbIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(SUBNODE_NODE_PROCEDURE), node.getId(), xbIndex);
        return index == null ? null : new XBRNode(client, index);
    }

    public Long findMaxSubNodeXB(XBCNode node) {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(MAXSUBNODE_NODE_PROCEDURE));
    }

    public XBRNode getSubNodeSeq(XBCNode node, long seq) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(SUBNODESEQ_NODE_PROCEDURE), node.getId(), seq);
        return index == null ? null : new XBRNode(client, index);
    }

    public long getSubNodesSeq(XBCNode node) {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(SUBNODESEQCNT_NODE_PROCEDURE));
    }
}
