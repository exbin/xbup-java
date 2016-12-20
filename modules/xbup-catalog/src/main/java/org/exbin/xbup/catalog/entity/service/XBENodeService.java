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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBERoot;
import org.exbin.xbup.catalog.entity.manager.XBENodeManager;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBENode items service.
 *
 * @version 0.1.24 2015/03/11
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBENodeService extends XBEDefaultService<XBENode> implements XBCNodeService<XBENode>, Serializable {

    @Autowired
    private XBENodeManager manager;

    public XBENodeService() {
        super();
    }

    public XBENodeService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBENodeManager(catalog);
        catalog.addCatalogManager(XBCNodeManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBENode getRootNode() {
        return ((XBENodeManager) itemManager).getRootNode();
    }

    @Override
    public XBERoot getRoot() {
        return ((XBENodeManager) itemManager).getRoot();
    }

    @Override
    public XBERoot getRoot(long rootId) {
        return ((XBENodeManager) itemManager).getRoot(rootId);
    }

    @Override
    public Long findMaxSubNodeXB(XBCNode node) {
        return ((XBENodeManager) itemManager).findMaxSubNodeXB(node);
    }

    @Override
    public XBENode findNodeByXBPath(Long[] catalogPath) {
        return ((XBENodeManager) itemManager).findNodeByXBPath(catalogPath);
    }

    @Override
    public XBENode findOwnerByXBPath(Long[] catalogPath) {
        return ((XBENodeManager) itemManager).findOwnerByXBPath(catalogPath);
    }

    @Override
    public XBENode findParentByXBPath(Long[] catalogPath) {
        return ((XBENodeManager) itemManager).findParentByXBPath(catalogPath);
    }

    @Override
    public Long[] getNodeXBPath(XBCNode node) {
        return ((XBENodeManager) itemManager).getNodeXBPath(node);
    }

    @Override
    public XBENode getSubNode(XBCNode node, long index) {
        return ((XBENodeManager) itemManager).getSubNode(node, index);
    }

    @Override
    public XBENode getSubNodeSeq(XBCNode node, long seq) {
        return ((XBENodeManager) itemManager).getSubNodeSeq(node, seq);
    }

    @Override
    public List<XBCNode> getSubNodes(XBCNode node) {
        return ((XBENodeManager) itemManager).getSubNodes(node);
    }

    @Override
    public long getSubNodesCount(XBCNode node) {
        return ((XBENodeManager) itemManager).getSubNodesCount(node);
    }

    @Override
    public long getSubNodesSeq(XBCNode node) {
        return ((XBENodeManager) itemManager).getSubNodesSeq(node);
    }

    @Override
    public Date getLastUpdate() {
        return getRoot().getLastUpdate();
    }

    public void setLastUpdateToNow() {
        ((XBENodeManager) itemManager).setLastUpdateToNow();
    }

    @Override
    public void persistRoot(XBCRoot root) {
        ((XBCManager) itemManager).persistItem(root);
    }
}
