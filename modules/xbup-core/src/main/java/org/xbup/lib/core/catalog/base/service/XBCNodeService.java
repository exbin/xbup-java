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

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRoot;

/**
 * Interface for XBCNode items service.
 *
 * @version 0.1.22 2013/08/17
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCNodeService<T extends XBCNode> extends XBCService<T> {

    /** Returns specification tree root node */
    public XBCNode getRootNode();

    /** Get list of subnodes */
    public List<XBCNode> getSubNodes(XBCNode node);

    /** Returns subnode of given index */
    public XBCNode getSubNode(XBCNode node, long index);

    /** Return count of direct subnodes */
    public long getSubNodesCount(XBCNode node);

    /** Returns n-th node from subnode's sequence */
    public XBCNode getSubNodeSeq(XBCNode node, long seq);

    /** Return size of subnode sequence */
    public long getSubNodesSeq(XBCNode node);

    /** Travers array of XB indexes and returns node if exists */
    public XBCNode findNodeByXBPath(Long[] xbCatalogPath);

    /** Travers array of XB indexes except last one and returns node if exists */
    public XBCNode findParentByXBPath(Long[] xbCatalogPath);

    /** Returns Path of XBIndexes for given node */
    public Long[] getNodeXBPath(XBCNode node);

    /** Ignores last member of path and returns parent node */
    public XBCNode findOwnerByXBPath(Long[] xbCatalogPath);

    public Long findMaxSubNodeXB(XBCNode node);

    public XBCRoot getRoot();
}
