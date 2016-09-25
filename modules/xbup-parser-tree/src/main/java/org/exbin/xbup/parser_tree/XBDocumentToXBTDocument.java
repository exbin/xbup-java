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
public class XBDocumentToXBTDocument implements XBTEditableDocument {

    private final XBDocument document;

    public XBDocumentToXBTDocument(XBDocument document) {
        this.document = document;
    }

    @Override
    public XBTBlock getRootBlock() {
        if (document.getRootBlock() instanceof XBTBlockToXBBlock) {
            return ((XBTBlockToXBBlock) document.getRootBlock()).getBlock();
        }

        return new XBBlockToXBTBlock(document.getRootBlock());
    }

    @Override
    public InputStream getTailData() {
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

    @Override
    public XBTBlock findBlockByIndex(long index) {
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
