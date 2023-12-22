/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.parser_tree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBEditableDocument;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.core.ubnumber.UBStreamable;

/**
 * Basic object model parser XBUP level 0 document representation.
 *
 * @author ExBin Project (https://exbin.org)
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

    @Nonnull
    @Override
    public Optional<InputStream> getTailData() {
        if (tailData == null) {
            return Optional.empty();
        }
        return Optional.of(tailData.getDataInputStream());
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
    public Optional<XBBlock> getRootBlock() {
        return Optional.ofNullable(getRoot());
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
        XBTreeNode rootBlock = getRoot();
        if (rootBlock != null) {
            documentSize += rootBlock.getBlockSize();
        }
        return documentSize;
    }
}
