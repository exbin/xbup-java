/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.catalog.base.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Interface for XBCNode items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCNodeService extends XBCService<XBCNode> {

    /**
     * Returns specification tree root node.
     *
     * @return root node
     */
    @Nonnull
    Optional<XBCNode> getMainRootNode();

    /**
     * Gets list of subnodes.
     *
     * @param parentNode parent node
     * @return list of child nodes
     */
    @Nonnull
    List<XBCNode> getSubNodes(XBCNode parentNode);

    /**
     * Returns subnode of given index.
     *
     * @param parentNode parent node
     * @param xbIndex sub node index
     * @return node or null
     */
    XBCNode getSubNode(XBCNode parentNode, long xbIndex);

    /**
     * Returns count of direct subnodes.
     *
     * @param parentNode parent node
     * @return count of child nodes
     */
    long getSubNodesCount(XBCNode parentNode);

    /**
     * Returns n-th node from all subnodes sequence.
     *
     * @param parentNode parent node
     * @param sequenceIndex sequence index
     * @return node or null
     */
    XBCNode getSubNodeSeq(XBCNode parentNode, long sequenceIndex);

    /**
     * Returns size of all subnodes sequence.
     *
     * @param parentNode parent node
     * @return size of all subnodes
     */
    long getSubNodesSeq(XBCNode parentNode);

    /**
     * Traverses array of XB indexes and return node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node or null
     */
    XBCNode findNodeByXBPath(Long[] xbCatalogPath);

    /**
     * Traverses array of XB indexes except last one and returns node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node or null
     */
    XBCNode findParentByXBPath(Long[] xbCatalogPath);

    /**
     * Returns path of XBIndexes for given node.
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
     * @return node or null
     */
    XBCNode findOwnerByXBPath(Long[] xbCatalogPath);

    /**
     * Finds maximum subnode XB index.
     *
     * @param node parent node
     * @return XB index or null
     */
    Long findMaxSubNodeXB(XBCNode node);

    /**
     * Removes node including all dependencies.
     *
     * @param node target node
     */
    void removeNodeFully(XBCNode node);
}
