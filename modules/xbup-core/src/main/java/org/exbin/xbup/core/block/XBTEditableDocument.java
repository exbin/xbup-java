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
package org.exbin.xbup.core.block;

import java.io.InputStream;

/**
 * Basic interface for editable XBUP level 1 document.
 *
 * @version 0.2.0 2016/02/29
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTEditableDocument extends XBTDocument {

    /**
     * Sets root block of the document.
     *
     * @param block the block to use as root block for this document
     */
    void setRootBlock(XBTBlock block);

    /**
     * Sets extended data area.
     *
     * @param source data stream
     */
    void setExtendedArea(InputStream source);

    /**
     * Clears all data in this document.
     */
    void clear();

    /**
     * Finds node using depth-first traversal index.
     *
     * @param index order of the block in document
     * @return block if found else null
     */
    XBTBlock findBlockByIndex(long index);

    /**
     * Creates new instance of block as next child for given block.
     *
     * @param parent block which would be set as parent block, null for root
     * node
     * @return newly created instance of this document
     */
    XBTBlock createNewBlock(XBTBlock parent);
}