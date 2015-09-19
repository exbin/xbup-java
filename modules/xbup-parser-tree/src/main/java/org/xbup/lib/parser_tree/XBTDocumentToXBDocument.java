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
package org.xbup.lib.parser_tree;

import java.io.InputStream;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBEditableDocument;
import org.xbup.lib.core.block.XBTDocument;
import org.xbup.lib.core.block.XBTEditableDocument;

/**
 * Conversion from level 1 document to level 0 document
 *
 * @version 0.1.25 2015/08/12
 * @author XBUP Project (http://xbup.org)
 */
public class XBTDocumentToXBDocument implements XBEditableDocument {

    private final XBTDocument document;

    public XBTDocumentToXBDocument(XBTDocument document) {
        this.document = document;
    }

    @Override
    public XBBlock getRootBlock() {
        if (document.getRootBlock() instanceof XBBlockToXBTBlock) {
            return ((XBBlockToXBTBlock) document.getRootBlock()).getBlock();
        }

        return new XBTBlockToXBBlock(document.getRootBlock());
    }

    @Override
    public InputStream getExtendedArea() {
        return document.getExtendedArea();
    }

    @Override
    public long getExtendedAreaSize() {
        return document.getExtendedAreaSize();
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
    public void setExtendedArea(InputStream source) {
        if (!(document instanceof XBTEditableDocument)) {
            throw new IllegalStateException("Cannot set extended area of read only document");
        }

        ((XBTEditableDocument) document).setExtendedArea(source);
    }

    @Override
    public void clear() {
        if (!(document instanceof XBTEditableDocument)) {
            throw new IllegalStateException("Cannot clear read only document");
        }

        ((XBTEditableDocument) document).clear();
    }
}
