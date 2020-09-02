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
package org.exbin.xbup.core.catalog.base.manager;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Interface for XBCNode catalog manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCNodeManager extends XBCCatalogManager<XBCNode> {

    /**
     * Returns specification tree root node.
     *
     * @return root node
     */
    Optional<XBCNode> getMainRootNode();

    /**
     * Gets list of subnodes.
     *
     * @param node parent node
     * @return list of nodes
     */
    @Nonnull
    List<XBCNode> getSubNodes(XBCNode node);

    /**
     * Returns subnode of given index.
     *
     * @param node parent node
     * @param xbIndex order
     * @return node
     */
    XBCNode getSubNode(XBCNode node, long xbIndex);

    /**
     * Returns count of direct subnodes.
     *
     * @param node parent node
     * @return count of nodes
     */
    long getSubNodesCount(XBCNode node);

    /**
     * Returns n-th node from subnode's sequence.
     *
     * @param node parent node
     * @param seq index in depth first order
     * @return node
     */
    XBCNode getSubNodeSeq(XBCNode node, long seq);

    /**
     * Returns size of subnode sequence.
     *
     * @param node parent node
     * @return count of all sub nodes
     */
    long getSubNodesSeq(XBCNode node);

    /**
     * Traverses array of XB indexes and returns node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node
     */
    XBCNode findNodeByXBPath(Long[] xbCatalogPath);

    /**
     * Traverses array of XB indexes except last one and returns node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node
     */
    XBCNode findParentByXBPath(Long[] xbCatalogPath);

    /**
     * Returns Path of XBIndexes for given node.
     *
     * @param node node
     * @return catalog path
     */
    @Nonnull
    Long[] getNodeXBPath(XBCNode node);

    /**
     * Ignores last member of path and returns parent node.
     *
     * @param xbCatalogPath catalog path
     * @return parent node for given path
     */
    XBCNode findOwnerByXBPath(Long[] xbCatalogPath);

    /**
     * Gets maximum XB index of all direct child nodes for given node.
     *
     * @param node parent node
     * @return maximum XBIndex
     */
    Long findMaxSubNodeXB(XBCNode node);
}
