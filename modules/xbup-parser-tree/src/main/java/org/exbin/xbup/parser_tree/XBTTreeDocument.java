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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.exbin.xbup.core.block.XBBlockData;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic object model parser XBUP level 1 document representation.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTreeDocument extends XBTTree implements XBTEditableDocument {

    private boolean modified;
    private String fileName;
    private XBData extendedAreaData;

    public XBTTreeDocument() {
        super(null);
        extendedAreaData = null;
    }

    public XBTTreeDocument(XBTTreeNode rootNode) {
        this();
        super.setRootBlock(rootNode);
    }

    public XBTTreeDocument(XBCatalog catalog) {
        super(catalog);
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        int size = XBHead.writeXBUPHead(stream);
        size += super.toStreamUB(stream);
        if (extendedAreaData != null) {
            size += extendedAreaData.getDataSize();
            if (!extendedAreaData.isEmpty()) {
                extendedAreaData.saveToStream(stream);
            }
        }
        return size;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        int size = XBHead.checkXBUPHead(stream);
        clear();
        if (stream.available() > 0) {
            size += super.fromStreamUB(stream);
            setExtendedArea(stream);
        }
        return (int) (size + getExtendedAreaSize());
    }

    @Override
    public int getSizeUB() {
        int size = XBHead.getXBUPHeadSize();
        size += super.getSizeUB();
        if (getExtendedArea() != null) {
            size += getExtendedAreaSize();
        }
        return size;
    }

    @Override
    public InputStream getExtendedArea() {
        if (extendedAreaData == null) {
            return null;
        }
        return extendedAreaData.getDataInputStream();
    }

    public XBBlockData getExtendedArray() {
        return extendedAreaData;
    }

    @Override
    public void setExtendedArea(InputStream source) {
        if (source == null) {
            extendedAreaData = null;
        } else {
            extendedAreaData = new XBData();
            extendedAreaData.loadFromStream(source);
        }
    }

    @Override
    public long getExtendedAreaSize() {
        if (extendedAreaData == null) {
            return 0;
        }
        return extendedAreaData.getDataSize();
    }

    @Override
    public void clear() {
        super.clear();
        if (extendedAreaData != null) {
            extendedAreaData = null;
        }
    }

    @Override
    public void setRootBlock(XBTBlock block) {
        if (block != null && !(block instanceof XBTTreeNode)) {
            throw new IllegalArgumentException("Unsupported type of root block");
        }

        super.setRootBlock((XBTTreeNode) block);
    }

    public boolean wasModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int fromFileUB() throws IOException, XBProcessingException {
        int size;
        try (InputStream stream = new FileInputStream(fileName)) {
            size = fromStreamUB(stream);
        }
        modified = false;
        return size;
    }

    public int toFileUB() throws IOException {
        int size;
        try (OutputStream stream = new FileOutputStream(fileName)) {
            size = toStreamUB(stream);
        }
        modified = false;
        return size;
    }

    @Override
    public void processSpec() {
        super.processSpec();
    }

    @Override
    public XBTBlock findBlockByIndex(long index) {
        return (XBTBlock) super.findNodeByIndex(index);
    }

    @Override
    public XBTBlock createNewBlock(XBTBlock parent) {
        return newNodeInstance((XBTTreeNode) parent);
    }

    @Override
    public long getDocumentSize() {
        long documentSize = getExtendedAreaSize();
        if (getRootBlock() != null) {
            documentSize += getRootBlock().getBlockSize();
        }
        return documentSize;
    }
}
