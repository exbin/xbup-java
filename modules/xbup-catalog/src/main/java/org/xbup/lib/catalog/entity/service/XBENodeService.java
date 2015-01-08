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
package org.xbup.lib.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERoot;
import org.xbup.lib.catalog.entity.manager.XBENodeManager;

/**
 * Interface for XBENode items service.
 *
 * @version 0.1.22 2013/08/17
 * @author XBUP Project (http://xbup.org)
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
}
