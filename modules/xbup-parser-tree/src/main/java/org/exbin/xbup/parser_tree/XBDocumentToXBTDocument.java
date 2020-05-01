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
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.block.XBEditableDocument;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTEditableDocument;

/**
 * Conversion from level 0 document to level 1 document
 *
 * @version 0.2.0 2015/09/20
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDocumentToXBTDocument implements XBTEditableDocument {

    private final XBDocument document;

    public XBDocumentToXBTDocument(XBDocument document) {
        this.document = document;
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> getRootBlock() {
        XBBlock rootBlock = document.getRootBlock().get();
        if (rootBlock instanceof XBTBlockToXBBlock) {
            return Optional.of(((XBTBlockToXBBlock) rootBlock).getBlock());
        }

        return Optional.of(new XBBlockToXBTBlock(rootBlock));
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
    public void setRootBlock(XBTBlock block) {
        if (!(document instanceof XBEditableDocument)) {
            throw new IllegalStateException("Cannot set root of read only document");
        }

        if (block instanceof XBBlockToXBTBlock) {
            ((XBEditableDocument) document).setRootBlock(((XBBlockToXBTBlock) block).getBlock());
        } else {
            ((XBEditableDocument) document).setRootBlock(block == null ? null : new XBTBlockToXBBlock(block));
        }

    }

    @Override
    public void setTailData(InputStream source) throws IOException {
        if (!(document instanceof XBEditableDocument)) {
            throw new IllegalStateException("Cannot set tail data of read only document");
        }

        ((XBEditableDocument) document).setTailData(source);
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> findBlockByIndex(long index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBTBlock createNewBlock(XBTBlock parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        if (!(document instanceof XBEditableDocument)) {
            throw new IllegalStateException("Cannot clear read only document");
        }

        ((XBEditableDocument) document).clear();
    }

    @Override
    public long getDocumentSize() {
        return document.getDocumentSize();
    }
}
