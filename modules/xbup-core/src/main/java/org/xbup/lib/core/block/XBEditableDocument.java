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
package org.xbup.lib.core.block;

import java.io.InputStream;

/**
 * Basic interface for editable XBUP level 0 document.
 *
 * @version 0.1 wr23.0 2013/11/25
 * @author XBUP Project (http://xbup.org)
 */
public interface XBEditableDocument extends XBDocument {

    /**
     * Set root block of the document.
     *
     * @param block the block to use as root block for this document
     */
    public void setRootBlock(XBBlock block);

    /**
     * Set extended data area.
     *
     * @param source data stream
     */
    public void setExtended(InputStream source);

    /**
     * Clear all data in this document.
     */
    public void clear();

    /**
     * Find block using depth-first traversal index.
     *
     * @param index order of the block in document
     * @return block if found else null
     */
    public XBBlock findBlockByIndex(long index);
}
