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
 * Interface for read access to XBUP level 1 document.
 *
 * @version 0.1.24 2014/08/26
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTDocument {

    /**
     * Gets root block of the document.
     *
     * @return root block if exits or null
     */
    public XBTBlock getRootBlock();

    /**
     * Gets size of the whole document.
     *
     * It is sum of the size of the root node if present + size of extended
     * area.
     *
     * @return size of document in bytes
     */
    public long getDocumentSize();

    /**
     * Gets extended data area.
     *
     * @return data stream
     */
    public InputStream getExtendedArea();

    /**
     * Gets size of extended data area.
     *
     * @return length of extended area in bytes
     */
    public long getExtendedAreaSize();
}
