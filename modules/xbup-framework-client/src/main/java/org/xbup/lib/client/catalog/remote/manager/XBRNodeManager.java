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
package org.xbup.lib.client.catalog.remote.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRoot;
import org.xbup.lib.core.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.core.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.XBRRoot;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRNode catalog items.
 *
 * @version 0.1.22 2013/08/17
 * @author XBUP Project (http://xbup.org)
 */
public class XBRNodeManager extends XBRDefaultManager<XBRNode> implements XBCNodeManager<XBRNode> {

    public XBRNodeManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public XBRNode getRootNode() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ROOT_NODE_PROCEDURE);
            if (message == null) {
                return null;
            }
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRNode(client,index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCNode> getSubNodes(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODES_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCNode> result = new ArrayList<XBCNode>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRNode(client,checker.attribXB().getLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRNode getSubNode(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long subnode = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (subnode == 0) {
                return null;
            }
            return new XBRNode(client,subnode);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getSubNodesCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODESCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public XBRNode findNodeByXBPath(Long[] xbCatalogPath) {
        XBRNode node = (XBRNode) getRootNode();
        for (int i = 0; i < xbCatalogPath.length; i++) {
            node = (XBRNode) getSubNode(node,xbCatalogPath[i]);
            if (node==null) {
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
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (index == 0) return null;
            return new XBRNode(client, index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;*/
    }

    @Override
    public XBRNode findParentByXBPath(Long[] xbCatalogPath) {
        if (xbCatalogPath.length == 0) {
            return null;
        }
        XBRNode node = (XBRNode) getRootNode();
        for (int i = 0; i < xbCatalogPath.length-1; i++) {
            node = (XBRNode) getSubNode(node,xbCatalogPath[i]);
            if (node==null) {
                break;
            }
        }
        return node;
    }

    @Override
    public Long[] getNodeXBPath(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.PATHNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long count = checker.attribXB().getLong();
            Long[] result = new Long[(int) count];
            for (int i = 0; i < count; i++) {
                result[i] = checker.attribXB().getLong();
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRNode findOwnerByXBPath(Long[] xbCatalogPath) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDOWNER_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(xbCatalogPath.length));
            for (int i = 0; i < xbCatalogPath.length; i++) {
                listener.attribXB(new UBNat32(xbCatalogPath[i]));
            }
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRNode(client, index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRNode findSubNodeByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long spec = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRNode(client,spec);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long findMaxSubNodeXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.MAXSUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.NODESCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public XBRNode getSubNodeSeq(XBCNode node, long seq) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODESEQ_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(seq));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long subnode = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (subnode == 0) {
                return null;
            }
            return new XBRNode(client,subnode);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getSubNodesSeq(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODESEQCNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public XBCRoot getRoot() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ROOT_PROCEDURE);
            if (message == null) {
                return null;
            }
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            long timeStamp = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRRoot(client,index, timeStamp);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
