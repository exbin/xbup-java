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
package org.xbup.lib.core.catalog.base.service;

import java.util.Date;
import java.util.List;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRoot;

/**
 * Interface for XBCNode items service.
 *
 * @version 0.1.24 2015/01/31
 * @author XBUP Project (http://xbup.org)
 * @param <T> node class
 */
public interface XBCNodeService<T extends XBCNode> extends XBCService<T> {

    /**
     * Returns specification tree root node.
     *
     * @return
     */
    public XBCNode getRootNode();

    /**
     * Gets list of subnodes.
     *
     * @param parentNode parent node
     * @return list of child nodes
     */
    public List<XBCNode> getSubNodes(XBCNode parentNode);

    /**
     * Returns subnode of given index.
     *
     * @param parentNode parent node
     * @param index
     * @return node or null
     */
    public XBCNode getSubNode(XBCNode parentNode, long index);

    /**
     * Returns count of direct subnodes.
     *
     * @param parentNode parent node
     * @return count of child nodes
     */
    public long getSubNodesCount(XBCNode parentNode);

    /**
     * Returns n-th node from all subnodes sequence.
     *
     * @param parentNode parent node
     * @param sequenceIndex
     * @return node or null
     */
    public XBCNode getSubNodeSeq(XBCNode parentNode, long sequenceIndex);

    /**
     * Returns size of all subnodes sequence.
     *
     * @param parentNode parent node
     * @return size of all subnodes
     */
    public long getSubNodesSeq(XBCNode parentNode);

    /**
     * Traverses array of XB indexes and return node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node or null
     */
    public XBCNode findNodeByXBPath(Long[] xbCatalogPath);

    /**
     * Traverses array of XB indexes except last one and returns node if exists.
     *
     * @param xbCatalogPath catalog path
     * @return node or null
     */
    public XBCNode findParentByXBPath(Long[] xbCatalogPath);

    /**
     * Returns path of XBIndexes for given node.
     *
     * @param node node
     * @return catalog path
     */
    public Long[] getNodeXBPath(XBCNode node);

    /**
     * Ignores last member of path and returns parent node.
     *
     * @param xbCatalogPath catalog path
     * @return node or null
     */
    public XBCNode findOwnerByXBPath(Long[] xbCatalogPath);

    /**
     * Finds maximum subnode XB index.
     *
     * @param node parent node
     * @return XB index or null
     */
    public Long findMaxSubNodeXB(XBCNode node);

    /**
     * Gets root record.
     *
     * @return root record
     */
    public XBCRoot getRoot();

    /**
     * Gets time of the last update.
     *
     * @return time of last update
     */
    public Date getLastUpdate();

    /**
     * Persist root node item.
     *
     * TODO: Root service?
     *
     * @param root root node item
     */
    public void persistRoot(XBCRoot root);
}
