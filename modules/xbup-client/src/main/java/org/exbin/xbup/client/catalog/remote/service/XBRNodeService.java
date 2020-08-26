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
package org.exbin.xbup.client.catalog.remote.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.client.catalog.remote.manager.XBRNodeManager;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;

/**
 * Remote service for XBRNode items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRNodeService extends XBRDefaultService<XBCNode> implements XBCNodeService {

    public XBRNodeService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRNodeManager(catalog);
        catalog.addCatalogManager(XBCNodeManager.class, (XBCNodeManager) itemManager);
    }

    @Nonnull
    @Override
    public Optional<XBCNode> getMainRootNode() {
        return ((XBRNodeManager) itemManager).getMainRootNode();
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
}
