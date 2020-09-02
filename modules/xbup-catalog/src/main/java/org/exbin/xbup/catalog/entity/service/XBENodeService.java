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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.manager.XBENodeManager;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBENode items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBENodeService extends XBEDefaultService<XBCNode> implements XBCNodeService, Serializable {

    @Autowired
    private XBENodeManager manager;

    public XBENodeService() {
        super();
    }

    public XBENodeService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBENodeManager(catalog);
        catalog.addCatalogManager(XBCNodeManager.class, (XBCNodeManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Nonnull
    @Override
    public Optional<XBCNode> getMainRootNode() {
        return ((XBENodeManager) itemManager).getMainRootNode();
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
    public XBENode getSubNode(XBCNode node, long xbIndex) {
        return ((XBENodeManager) itemManager).getSubNode(node, xbIndex);
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
