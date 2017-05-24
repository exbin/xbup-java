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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.utils.binary_data.BinaryData;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBEditableDocument;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.core.ubnumber.UBStreamable;

/**
 * Basic object model parser XBUP level 0 document representation.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
public class XBTreeDocument extends XBTree implements XBEditableDocument, UBStreamable {

    private boolean modified;
    @Nullable
    private String fileName;
    @Nullable
    private XBData tailData;

    public XBTreeDocument() {
        tailData = null;
    }

    public XBTreeDocument(@Nullable XBTreeNode rootNode) {
        this();
        super.setRootBlock(rootNode);
    }

    @Override
    public int toStreamUB(@Nonnull OutputStream stream) throws IOException {
        int size = XBHead.writeXBUPHead(stream);
        size += super.toStreamUB(stream);
        if (tailData != null) {
            size += tailData.getDataSize();
            if (!tailData.isEmpty()) {
                tailData.saveToStream(stream);
            }
        }
        return size;
    }

    @Override
    public int fromStreamUB(@Nonnull InputStream stream) throws IOException, XBProcessingException {
        int size = XBHead.checkXBUPHead(stream);
        clear();
        if (stream.available() > 0) {
            size += super.fromStreamUB(stream);
            setTailData(stream);
        }
        return (int) (size + getTailDataSize());
    }

    @Override
    public int getSizeUB() {
        int size = XBHead.getXBUPHeadSize();
        size += super.getSizeUB();
        if (getTailData() != null) {
            size += getTailDataSize();
        }
        return size;
    }

    @Override
    @Nullable
    public InputStream getTailData() {
        if (tailData == null) {
            return null;
        }
        return tailData.getDataInputStream();
    }

    @Nullable
    public BinaryData getTailDataArray() {
        return tailData;
    }

    @Override
    public void setTailData(@Nullable InputStream source) throws IOException {
        if (source == null) {
            tailData = null;
        } else {
            tailData = new XBData();
            tailData.loadFromStream(source);
        }
    }

    @Override
    public long getTailDataSize() {
        if (tailData == null) {
            return 0;
        }
        return tailData.getDataSize();
    }

    @Override
    public void clear() {
        super.clear();
        if (tailData != null) {
            tailData = null;
        }
    }

    @Override
    public void setRootBlock(@Nullable XBBlock block) {
        if (block != null && !(block instanceof XBTreeNode)) {
            throw new IllegalArgumentException("Unsupported type of root block");
        }
        setRootBlock((XBTreeNode) block);
    }

    public boolean wasModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Nullable
    public String getFileName() {
        return fileName;
    }

    public void setFileName(@Nullable String fileName) {
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
    public long getDocumentSize() {
        long documentSize = getTailDataSize();
        XBTreeNode rootBlock = getRootBlock();
        if (rootBlock != null) {
            documentSize += rootBlock.getBlockSize();
        }
        return documentSize;
    }
}
