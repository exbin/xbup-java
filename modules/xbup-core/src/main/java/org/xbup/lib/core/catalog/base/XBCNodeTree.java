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
package org.xbup.lib.core.catalog.base;

/**
 * Interface for catalog node tree entity.
 *
 * @version 0.1.24 2014/09/07
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCNodeTree {

    /**
     * Gets index.
     *
     * @return the index
     */
    public Long getId();

    /**
     * Gets node.
     *
     * @return the node
     */
    public XBCNode getOwner();

    /**
     * Gets node child.
     *
     * @return the node
     */
    public XBCNode getNode();

    /**
     * Gets depth level.
     *
     * @return the depth
     */
    public Integer getDepthLevel();

    /**
     * Gets root record for this node.
     *
     * @return root record
     */
    public XBCRoot getRoot();
}
