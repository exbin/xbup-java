/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.client.stub.XBPNodeStub;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;

/**
 * Remote manager class for XBRNode catalog items.
 *
 * @version 0.1.25 2015/03/11
 * @author ExBin Project (http://exbin.org)
 */
public class XBRNodeManager extends XBRDefaultManager<XBRNode> implements XBCNodeManager<XBRNode> {

    private final XBPNodeStub nodeStub;

    public XBRNodeManager(XBRCatalog catalog) {
        super(catalog);
        nodeStub = new XBPNodeStub(client);
        setManagerStub(nodeStub);
    }

    @Override
    public XBRNode getRootNode() {
        return nodeStub.getRootNode();
    }

    @Override
    public List<XBCNode> getSubNodes(XBCNode node) {
        return nodeStub.getSubNodes(node);
    }

    @Override
    public XBRNode getSubNode(XBCNode node, long index) {
        return nodeStub.getSubNode(node, index);
    }

    @Override
    public long getSubNodesCount(XBCNode node) {
        return nodeStub.getSubNodesCount(node);
    }

    @Override
    public XBRNode findNodeByXBPath(Long[] catalogPath) {
        return nodeStub.findNodeByXBPath(catalogPath);
    }

    @Override
    public XBRNode findParentByXBPath(Long[] catalogPath) {
        return nodeStub.findParentByXBPath(catalogPath);
    }

    @Override
    public Long[] getNodeXBPath(XBCNode node) {
        return nodeStub.getNodeXBPath(node);
    }

    @Override
    public XBRNode findOwnerByXBPath(Long[] catalogPath) {
        return nodeStub.findOwnerByXBPath(catalogPath);
    }

    public XBRNode findSubNodeByXB(XBCNode node, long xbIndex) {
        return nodeStub.findSubNodeByXB(node, xbIndex);
    }

    @Override
    public Long findMaxSubNodeXB(XBCNode node) {
        return nodeStub.findMaxSubNodeXB(node);
    }

    @Override
    public XBRNode getSubNodeSeq(XBCNode node, long seq) {
        return nodeStub.getSubNodeSeq(node, seq);
    }

    @Override
    public long getSubNodesSeq(XBCNode node) {
        return nodeStub.getSubNodesSeq(node);
    }

    @Override
    public XBCRoot getRoot() {
        return nodeStub.getRoot();
    }

    @Override
    public XBCRoot getRoot(long rootId) {
        return nodeStub.getRoot(rootId);
    }
}
