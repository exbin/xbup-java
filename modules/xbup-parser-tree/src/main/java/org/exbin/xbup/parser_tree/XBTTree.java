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

    @Nullable
    public XBTBlock findNodeByIndex(long index) {
        return rootNode.findNodeByIndex(index);
    }

    @Nullable
    public XBTTreeNode getRoot() {
        return rootNode;
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
