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
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.client.stub.XBPNodeStub;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;

/**
 * Remote manager class for XBRNode catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRNodeManager extends XBRDefaultManager<XBCNode> implements XBCNodeManager {

    private final XBPNodeStub nodeStub;

    public XBRNodeManager(XBRCatalog catalog) {
        super(catalog);
        nodeStub = new XBPNodeStub(client);
        setManagerStub(nodeStub);
    }

    @Nonnull
    @Override
    public Optional<XBCNode> getMainRootNode() {
        return nodeStub.getMainRootNode();
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
    public void removeNodeFully(XBCNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
