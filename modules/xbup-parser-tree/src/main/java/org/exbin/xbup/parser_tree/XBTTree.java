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
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.ubnumber.UBStreamable;

/**
 * XBUP level 1 object model parser tree.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTTree implements UBStreamable {

    @Nullable
    private XBTTreeNode rootNode;
    @Nullable
    private XBCatalog catalog;
    @Nullable
    protected XBContext rootContext;

    public XBTTree(XBCatalog catalog) {
        this(catalog, null);
    }

    public XBTTree(@Nullable XBCatalog catalog, @Nullable XBContext rootContext) {
        rootNode = null;
        this.catalog = catalog;
        this.rootContext = rootContext;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        clear();
        XBTTreeNode newRoot = new XBTTreeNode();
        setRoot(newRoot);
        return newRoot.fromStreamUB(stream);
    }

    public void setCatalog(@Nullable XBCatalog catalog) {
        this.catalog = catalog;
        if (catalog != null) {
            rootContext = catalog.getRootContext();
        } else {
            rootContext = null;
        }
    }

    public void processSpec() {
        if (rootNode != null) {
            rootNode.processSpec(catalog, rootContext);
        }
    }

    @Nonnull
    public XBTTreeNode newNodeInstance(@Nullable XBTTreeNode parent) {
        return new XBTTreeNode(parent);
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
    public int getSizeUB() {
        if (rootNode != null) {
            return rootNode.getSizeUB();
        } else {
            return 0;
        }
    }

    @Nonnull
    public Optional<XBTBlock> findNodeByIndex(long index) {
        return Optional.ofNullable(rootNode.findNodeByIndex(index).orElse(null));
    }

    @Nullable
    public XBTTreeNode getRoot() {
        return rootNode;
    }

    @Nonnull
    public Optional<XBTBlock> getRootBlock() {
        return Optional.ofNullable(rootNode);
    }

    public void setRoot(@Nullable XBTTreeNode rootNode) {
        this.rootNode = rootNode;
        processSpec();
    }

    @Nullable
    public XBContext getRootContext() {
        return rootContext;
    }

    public void setRootContext(@Nullable XBContext rootContext) {
        this.rootContext = rootContext;
        processSpec();
    }

    @Nullable
    public XBCatalog getCatalog() {
        return catalog;
    }
}
