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
package org.xbup.lib.xb.parser.tree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xbup.lib.xb.block.XBTBlock;
import org.xbup.lib.xb.block.declaration.XBContext;
import org.xbup.lib.xb.block.declaration.XBContextBlockType;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.ubnumber.UBStreamable;

/**
 * XBUP level 1 object model parser tree.
 *
 * @version 0.1 wr23.0 2014/02/11
 * @author XBUP Project (http://xbup.org)
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
        if (newRoot.getBlockType() instanceof XBContextBlockType) {
            ((XBContextBlockType) newRoot.getBlockType()).setContext(rootContext);
        }

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
        if (getRootBlock()!=null) {
            ((XBTTreeNode) getRootBlock()).processSpec(catalog, rootContext);
        }
    }

    /**
     * TODO: This is stub, because I'm to lazy to think about proper solution
     * 
     * @param parent
     * @return
     */
    public XBTTreeNode newNodeInstance(XBTTreeNode parent) {
        return new XBTTreeNode(parent);
    }

    public void clear() {
        rootNode = null;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (getRootBlock()!=null) {
            return rootNode.toStreamUB(stream);
        } else {
            return 0;
        }
    }

    @Override
    public int getSizeUB() {
        if (getRootBlock()!=null) {
            return rootNode.getSizeUB();
        } else {
            return 0;
        }
    }

    public XBTBlock findNodeByIndex(long index) {
        return rootNode.findNodeByIndex(index);
    }

    public XBTBlock getRootBlock() {
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
