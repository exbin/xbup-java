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
package org.xbup.lib.parser_tree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.ubnumber.UBStreamable;

/**
 * XBUP level 0 object model parser tree.
 *
 * @version 0.1.20 2010/09/12
 * @author ExBin Project (http://exbin.org)
 */
public class XBTree implements UBStreamable {

    private XBTreeNode rootNode;

    public XBTree() {
        rootNode = null;
    }

    public void clear() {
        rootNode = null;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (getRootBlock() != null) {
            return rootNode.toStreamUB(stream);
        } else {
            return 0;
        }
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        clear();
        rootNode = new XBTreeNode();
        return rootNode.fromStreamUB(stream);
    }

    @Override
    public int getSizeUB() {
        if (getRootBlock() != null) {
            return rootNode.getSizeUB();
        } else {
            return 0;
        }
    }

    public XBTreeNode findBlockByIndex(long index) {
        return rootNode.findNodeByIndex(index);
    }

    public XBTreeNode newNodeInstance(XBTreeNode parent) {
        return new XBTreeNode(parent);
    }

    public XBTreeNode getRootBlock() {
        return rootNode;
    }

    public void setRootBlock(XBTreeNode rootNode) {
        this.rootNode = rootNode;
    }
}
