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
package org.xbup.lib.core.block;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for read access to binary data.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public interface XBBlockData {

    /**
     * Returns true if data are empty.
     *
     * @return true if data empty
     */
    public boolean isEmpty();

    /**
     * Returns size of data or -1 if size is not available.
     *
     * @return size of data in bytes
     */
    public long getDataSize();

    /**
     * Returns particular byte from data.
     *
     * @param position position
     * @return byte on requested position
     */
    public byte getByte(long position);

    /**
     * Creates copy of all data.
     *
     * @return copy of data
     */
    public XBBlockData copy();

    /**
     * Creates copy of given area.
     *
     * @param startFrom position to start copy from
     * @param length length of area
     * @return copy of data
     */
    public XBBlockData copy(long startFrom, long length);

    /**
     * Copies data from given area to target area.
     *
     * @param targetData data to copy to
     * @param startFrom position to start copy from
     * @param length length of area
     * @param targetPos target position to copy to
     */
    public void copyTo(XBEditableBlockData targetData, long startFrom, long length, long targetPos);

    /**
     * Provides handler for input stream generation.
     *
     * @return new instance of input stream
     */
    public InputStream getDataInputStream();

    /**
     * Saves data to given stream.
     *
     * @param outputStream output stream
     * @throws java.io.IOException if input/output error
     */
    public void saveToStream(OutputStream outputStream) throws IOException;
}
