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
package org.xbup.lib.client.catalog.remote.service;

import java.util.Date;
import java.util.List;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRoot;
import org.xbup.lib.core.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.manager.XBRNodeManager;

/**
 * Remote service for XBRNode items.
 *
 * @version 0.1.25 2015/03/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBRNodeService extends XBRDefaultService<XBRNode> implements XBCNodeService<XBRNode> {

    public XBRNodeService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRNodeManager(catalog);
        catalog.addCatalogManager(XBCNodeManager.class, itemManager);
    }

    @Override
    public XBRNode getRootNode() {
        return ((XBRNodeManager) itemManager).getRootNode();
    }

    @Override
    public Long findMaxSubNodeXB(XBCNode node) {
        return ((XBRNodeManager) itemManager).findMaxSubNodeXB(node);
    }

    @Override
    public XBRNode findNodeByXBPath(Long[] xbCatalogPath) {
        return ((XBRNodeManager) itemManager).findNodeByXBPath(xbCatalogPath);
    }

    @Override
    public XBRNode findOwnerByXBPath(Long[] xbCatalogPath) {
        return ((XBRNodeManager) itemManager).findOwnerByXBPath(xbCatalogPath);
    }

    @Override
    public XBRNode findParentByXBPath(Long[] xbCatalogPath) {
        return ((XBRNodeManager) itemManager).findParentByXBPath(xbCatalogPath);
    }

    @Override
    public Long[] getNodeXBPath(XBCNode node) {
        return ((XBRNodeManager) itemManager).getNodeXBPath(node);
    }

    @Override
    public XBRNode getSubNode(XBCNode node, long index) {
        return ((XBRNodeManager) itemManager).getSubNode(node, index);
    }

    @Override
    public XBRNode getSubNodeSeq(XBCNode node, long seq) {
        return ((XBRNodeManager) itemManager).getSubNodeSeq(node, seq);
    }

    @Override
    public List<XBCNode> getSubNodes(XBCNode node) {
        return ((XBRNodeManager) itemManager).getSubNodes(node);
    }

    @Override
    public long getSubNodesCount(XBCNode node) {
        return ((XBRNodeManager) itemManager).getSubNodesCount(node);
    }

    @Override
    public long getSubNodesSeq(XBCNode node) {
        return ((XBRNodeManager) itemManager).getSubNodesSeq(node);
    }

    @Override
    public XBCRoot getRoot() {
        return ((XBRNodeManager) itemManager).getRoot();
    }

    @Override
    public Date getLastUpdate() {
        return getRoot().getLastUpdate();
    }

    @Override
    public void persistRoot(XBCRoot root) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCRoot getRoot(long rootId) {
        return ((XBRNodeManager) itemManager).getRoot(rootId);
    }
}
