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
package org.xbup.lib.core.block.declaration;

import java.util.Map;
import java.util.TreeMap;

/**
 * Specification's header declaration using catalog link.
 *
 * TODO: this should be finished later
 *
 * @version 0.1.19 2010/06/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBCTransNode {

    private Map<Integer, XBCTransNode> subNodes;
    private XBBlockDecl block;

    public XBCTransNode() {
        subNodes = new TreeMap<>();
        block = null;
    }

    public Map<Integer, XBCTransNode> getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(Map<Integer, XBCTransNode> subNodes) {
        this.subNodes = subNodes;
    }

    public XBBlockDecl getBlock() {
        return block;
    }

    public void setBlock(XBBlockDecl block) {
        this.block = block;
    }

    public void addBlock(Long[] path, XBBlockDecl block) {
        int pos = 0;
        XBCTransNode parent = this;
        while (path.length > pos) {
            XBCTransNode node = parent.subNodes.get(path[pos].intValue());
            if (node == null) {
                node = new XBCTransNode();
                parent.subNodes.put(path[pos].intValue(), node);
            }
            parent = node;
            pos++;
        }

        parent.block = block;
    }

    public XBBlockDecl findBlock(Long[] path) {
        if (path.length == 0) {
            return block;
        }

        int pos = 0;
        XBCTransNode node = this;
        while (path.length > pos) {
            node = node.subNodes.get(path[pos].intValue());
            if (node == null) {
                return null;
            }
            pos++;
        }
        if (node == null) {
            return null;
        }

        return node.getBlock();
    }
}
