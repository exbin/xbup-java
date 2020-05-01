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
import java.util.Optional;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBEditableDocument;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTDocument;
import org.exbin.xbup.core.block.XBTEditableDocument;

/**
 * Conversion from level 1 document to level 0 document
 *
 * @version 0.2.0 2015/09/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBTDocumentToXBDocument implements XBEditableDocument {

    private final XBTDocument document;

    public XBTDocumentToXBDocument(XBTDocument document) {
        this.document = document;
    }

    @Nonnull
    @Override
    public Optional<XBBlock> getRootBlock() {
        XBTBlock rootBlock = document.getRootBlock().get();
        if (rootBlock instanceof XBBlockToXBTBlock) {
            return Optional.of(((XBBlockToXBTBlock) rootBlock).getBlock());
        }

        return Optional.of(new XBTBlockToXBBlock(rootBlock));
    }

    @Nonnull
    @Override
    public Optional<InputStream> getTailData() {
        return document.getTailData();
    }

    @Override
    public long getTailDataSize() {
        return document.getTailDataSize();
    }

    @Override
    public void setRootBlock(XBBlock block) {
        if (!(document instanceof XBTEditableDocument)) {
            throw new IllegalStateException("Cannot set root of read only document");
        }

        if (block instanceof XBTBlockToXBBlock) {
            ((XBTEditableDocument) document).setRootBlock(((XBTBlockToXBBlock) block).getBlock());
        } else {
            ((XBTEditableDocument) document).setRootBlock(block == null ? null : new XBBlockToXBTBlock(block));
        }

    }

    @Override
    public void setTailData(InputStream source) throws IOException {
        if (!(document instanceof XBTEditableDocument)) {
            throw new IllegalStateException("Cannot set tail data of read only document");
        }

        ((XBTEditableDocument) document).setTailData(source);
    }

    @Override
    public void clear() {
        if (!(document instanceof XBTEditableDocument)) {
            throw new IllegalStateException("Cannot clear read only document");
        }

        ((XBTEditableDocument) document).clear();
    }

    @Override
    public long getDocumentSize() {
        return document.getDocumentSize();
    }
}
