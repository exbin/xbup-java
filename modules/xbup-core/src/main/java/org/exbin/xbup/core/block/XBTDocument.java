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
import javax.annotation.Nullable;

/**
 * Interface for read access to XBUP level 1 document.
 *
 * @version 0.2.1 2017/05/08
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTDocument {

    /**
     * Returns root block of the document.
     *
     * @return root block if exits or null
     */
    @Nullable
    public XBTBlock getRootBlock();

    /**
     * Returns size of the whole document if available.
     *
     * @return length of whole document in bytes or -1 if not available or is
     * infinite
     */
    public long getDocumentSize();

    /**
     * Returns tail data input stream.
     *
     * @return data stream
     */
    @Nullable
    public InputStream getTailData();

    /**
     * Returns size of the tail data if available.
     *
     * @return length of tail data in bytes or -1 if not available or is
     * infinite
     */
    public long getTailDataSize();
}
