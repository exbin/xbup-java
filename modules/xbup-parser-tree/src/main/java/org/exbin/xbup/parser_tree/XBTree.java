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
package org.exbin.xbup.parser_tree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
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
    public XBTreeNode getRoot() {
        return rootNode;
    }

    public void setRootBlock(@Nullable XBTreeNode rootNode) {
        this.rootNode = rootNode;
    }
}
