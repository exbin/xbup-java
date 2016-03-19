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
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.ubnumber.UBStreamable;

/**
 * XBUP level 1 object model parser tree.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTree implements UBStreamable {

    private XBTTreeNode rootNode;
    private XBCatalog catalog;
    protected XBContext rootContext;

    public XBTTree(XBCatalog catalog) {
        this(catalog, null);
    }

    public XBTTree(XBCatalog catalog, XBContext rootContext) {
        rootNode = null;
        this.catalog = catalog;
        this.rootContext = rootContext;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        clear();
        XBTTreeNode newRoot = new XBTTreeNode();
        setRootBlock(newRoot);
        return newRoot.fromStreamUB(stream);
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
        if (catalog != null) {
            rootContext = catalog.getRootContext();
        } else {
            rootContext = null;
        }
    }

    public void processSpec() {
        if (getRootBlock() != null) {
            ((XBTTreeNode) getRootBlock()).processSpec(catalog, rootContext);
        }
    }

    public XBTTreeNode newNodeInstance(XBTTreeNode parent) {
        return new XBTTreeNode(parent);
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
    public int getSizeUB() {
        if (getRootBlock() != null) {
            return rootNode.getSizeUB();
        } else {
            return 0;
        }
    }

    public XBTBlock findNodeByIndex(long index) {
        return rootNode.findNodeByIndex(index);
    }

    public XBTTreeNode getRootBlock() {
        return rootNode;
    }

    public void setRootBlock(XBTTreeNode rootNode) {
        this.rootNode = rootNode;
        processSpec();
    }

    public XBContext getRootContext() {
        return rootContext;
    }

    public void setRootContext(XBContext rootContext) {
        this.rootContext = rootContext;
        processSpec();
    }

    public XBCatalog getCatalog() {
        return catalog;
    }
}
