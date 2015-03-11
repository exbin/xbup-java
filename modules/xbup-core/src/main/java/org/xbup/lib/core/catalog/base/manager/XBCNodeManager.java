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
package org.xbup.lib.core.catalog.base.manager;

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRoot;

/**
 * Interface for XBCNode catalog manager.
 *
 * @version 0.1.25 2015/03/11
 * @author XBUP Project (http://xbup.org)
 * @param <T> node entity
 */
public interface XBCNodeManager<T extends XBCNode> extends XBCCatalogManager<T> {

    /**
     * Returns specification tree root.
     *
     * @return specification root
     */
    public XBCRoot getRoot();

    /**
     * Gets root record.
     *
     * @param rootId root index
     * @return root record
     */
    public XBCRoot getRoot(long rootId);

    /**
     * Returns specification tree root node.
     *
     * @return root node
     */
    public XBCNode getRootNode();

    /**
     * Gets list of subnodes.
     *
     * @param node parent node
     * @return list of nodes
     */
    public List<XBCNode> getSubNodes(XBCNode node);

    /**
     * Returns subnode of given index.
     *
     * @param node parent node
     * @param index order
     * @return node
     */
    public XBCNode getSubNode(XBCNode node, long index);

    /**
     * Returns count of direct subnodes.
     *
     * @param node parent node
     * @return count of nodes
     */
    public long getSubNodesCount(XBCNode node);

    /**
     * Returns n-th node from subnode's sequence.
     *
     * @param node parent node
     * @param seq index in depth first order
     * @return node
     */
    public XBCNode getSubNodeSeq(XBCNode node, long seq);

    /**
     * Returns size of subnode sequence.
     *
     * @param node parent node
     * @return count of all sub nodes
     */
    public long getSubNodesSeq(XBCNode node);

    /**
     * Traverses array of XB indexes and returns node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node
     */
    public XBCNode findNodeByXBPath(Long[] xbCatalogPath);

    /**
     * Traverses array of XB indexes except last one and returns node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node
     */
    public XBCNode findParentByXBPath(Long[] xbCatalogPath);

    /**
     * Returns Path of XBIndexes for given node.
     *
     * @param node
     * @return catalog path
     */
    public Long[] getNodeXBPath(XBCNode node);

    /**
     * Ignores last member of path and returns parent node.
     *
     * @param xbCatalogPath catalog path
     * @return parent node for given path
     */
    public XBCNode findOwnerByXBPath(Long[] xbCatalogPath);

    /**
     * Gets maximum XB index of all direct child nodes for given node.
     *
     * @param node parent node
     * @return maximum XBIndex
     */
    public Long findMaxSubNodeXB(XBCNode node);
}
