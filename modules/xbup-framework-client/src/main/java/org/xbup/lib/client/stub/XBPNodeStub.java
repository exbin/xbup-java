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
package org.xbup.lib.client.stub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.XBRRoot;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRoot;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRNode catalog items.
 *
 * @version 0.1.25 2015/03/09
 * @author XBUP Project (http://xbup.org)
 */
public class XBPNodeStub implements XBPManagerStub<XBRNode> {

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
    public static long[] ROOT_PROCEDURE = {0, 2, 4, 29, 0};

    private final XBCatalogServiceClient client;

    public XBPNodeStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRNode getRootNode() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ROOT_NODE_PROCEDURE);
            if (message == null) {
                return null;
            }
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRNode(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCNode> getSubNodes(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SUBNODES_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCNode> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRNode(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode getSubNode(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long subnode = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (subnode == 0) {
                return null;
            }
            return new XBRNode(client, subnode);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getSubNodesCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SUBNODESCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public XBRNode findNodeByXBPath(Long[] catalogPath) {
        XBRNode node = (XBRNode) getRootNode();
        for (Long catalogPathNode : catalogPath) {
            node = (XBRNode) getSubNode(node, catalogPathNode);
            if (node == null) {
                break;
            }
        }
        return node;
        /*        try {
         XBCatalogServiceMessage message = client.executeProcedure(XBCatalogServiceClient.messageTypeEnum.NODE.ordinal(), XBCatalogServiceClient.nodeMessageEnum.FINDNODE.ordinal());
         XBListener listener = message.getXBOutput();
         listener.attribXB(new UBNat32(xbCatalogPath.length));
         for (int i = 0; i < xbCatalogPath.length; i++) {
         listener.attribXB(new UBNat32(xbCatalogPath[i]));
         }
         listener.endXB();
         XBMatchingProvider checker = message.getXBInput();
         long index = checker.matchAttribXB().getNaturalLong();
         checker.matchEndXB();
         message.close();
         if (index == 0) return null;
         return new XBRNode(client, index);
         } catch (XBProcessingException ex) {
         Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;*/
    }

    public XBRNode findParentByXBPath(Long[] xbCatalogPath) {
        if (xbCatalogPath.length == 0) {
            return null;
        }
        XBRNode node = (XBRNode) getRootNode();
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
            XBCatalogServiceMessage message = client.executeProcedure(PATHNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long count = checker.matchAttribXB().getNaturalLong();
            Long[] result = new Long[(int) count];
            for (int i = 0; i < count; i++) {
                result[i] = checker.matchAttribXB().getNaturalLong();
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode findOwnerByXBPath(Long[] catalogPath) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FINDOWNER_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(catalogPath.length));
            for (Long catalogPathNode : catalogPath) {
                listener.attribXB(new UBNat32(catalogPathNode));
            }
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRNode(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode findSubNodeByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long subNodeId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRNode(client, subNodeId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxSubNodeXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(MAXSUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode getSubNodeSeq(XBCNode node, long seq) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SUBNODESEQ_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(seq));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long subnode = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (subnode == 0) {
                return null;
            }
            return new XBRNode(client, subnode);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getSubNodesSeq(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SUBNODESEQCNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public XBCRoot getRoot() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ROOT_PROCEDURE);
            if (message == null) {
                return null;
            }
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRRoot(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPNodeStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRNode createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRNode item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRNode item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRNode getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRNode> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(NODESCOUNT_NODE_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
