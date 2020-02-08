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
package org.exbin.xbup.parser_tree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.ubnumber.UBStreamable;

/**
 * XBUP level 0 object model parser tree.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTree implements UBStreamable {

    @Nullable
    private XBTreeNode rootNode;

    public XBTree() {
        rootNode = null;
    }

    public void clear() {
        rootNode = null;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (rootNode != null) {
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
        if (rootNode != null) {
            return rootNode.getSizeUB();
        } else {
            return 0;
        }
    }

    @Nullable
    public XBTreeNode findBlockByIndex(long index) {
        return rootNode.findNodeByIndex(index);
    }

    @Nonnull
    public XBTreeNode newNodeInstance(@Nullable XBTreeNode parent) {
        return new XBTreeNode(parent);
    }

    @Nullable
    public XBTreeNode getRootBlock() {
        return rootNode;
    }

    public void setRootBlock(@Nullable XBTreeNode rootNode) {
        this.rootNode = rootNode;
    }
}
