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
 * Basic interface for XBUP level 0 document.
 *
 * @version 0.1.23 2013/11/03
 * @author XBUP Project (http://xbup.org)
 */
public interface XBDocument {

    /**
     * Get root block of the document.
     *
     * @return root block if exits or null.
     */
    public XBBlock getRootBlock();

    /**
     * Get extended data area.
     *
     * @return InputStream of providing read access to data area
     */
    public InputStream getExtended();

    /**
     * Get size of the whole document.
     *
     * It is sum of the size of the root block if present + size of the extended
     * area.
     *
     * @return length of whole document in bytes
     */
    public long getDocumentSize();

    /**
     * Get size of extended data area.
     *
     * @return length of extended area in bytes.
     */
    public long getExtendedSize();
}
