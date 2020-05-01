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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic object model parser XBUP level 1 document representation.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTTreeDocument extends XBTTree implements XBTEditableDocument {

    private boolean modified;
    @Nullable
    private String fileName;
    @Nullable
    private XBData tailData;

    public XBTTreeDocument() {
        super(null);
        tailData = null;
    }

    public XBTTreeDocument(@Nullable XBTTreeNode rootNode) {
        this();
        super.setRoot(rootNode);
    }

    public XBTTreeDocument(@Nullable XBCatalog catalog) {
        super(catalog);
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
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
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
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
        if (tailData != null) {
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

    @Nonnull
    @Override
    public Optional<XBTBlock> getRootBlock() {
        return Optional.ofNullable(getRoot());
    }

    @Override
    public void setRootBlock(@Nullable XBTBlock block) {
        if (block != null && !(block instanceof XBTTreeNode)) {
            throw new IllegalArgumentException("Unsupported type of root block");
        }

        super.setRoot((XBTTreeNode) block);
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
    public void processSpec() {
        super.processSpec();
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> findBlockByIndex(long index) {
        return super.findNodeByIndex(index);
    }

    @Nonnull
    @Override
    public XBTBlock createNewBlock(@Nullable XBTBlock parent) {
        return newNodeInstance((XBTTreeNode) parent);
    }

    @Override
    public long getDocumentSize() {
        long documentSize = getTailDataSize();
        XBTTreeNode rootBlock = getRoot();
        if (rootBlock != null) {
            documentSize += rootBlock.getBlockSize();
        }
        return documentSize;
    }
}
