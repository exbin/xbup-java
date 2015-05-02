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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for editable binary data.
 *
 * @version 0.1.25 2015/05/01
 * @author XBUP Project (http://xbup.org)
 */
public interface XBEditableBlockData extends XBBlockData {

    /**
     * Sets data size.
     *
     * If size is bigger than current size, it will fill it with zeros,
     * otherwise it will shrink current data.
     *
     * @param size target size
     */
    public void setDataSize(long size);

    /**
     * Sets byte to given position.
     *
     * @param position position
     * @param value byte value to be set
     */
    public void setByte(long position, byte value);

    /**
     * Inserts empty data of given length to given position.
     *
     * @param startFrom position to insert to
     * @param length length of data
     */
    public void insert(long startFrom, long length);

    /**
     * Inserts given data to given position.
     *
     * @param startFrom position to insert to
     * @param insertedData data to insert
     */
    public void insert(long startFrom, XBBlockData insertedData);

    /**
     * Replaces all data with given data.
     *
     * @param newData source data
     */
    public void setData(XBBlockData newData);

    /**
     * Performs insertion of data but doesn't initialize it's value.
     *
     * @param startFrom position to insert to
     * @param length length of data
     */
    public void insertUninitialized(long startFrom, long length);

    /**
     * Fills given area with empty data.
     *
     * @param startFrom position to fill data to
     * @param length length of area
     */
    public void fillData(long startFrom, long length);

    /**
     * Fills given area with bytes of given value.
     *
     * @param startFrom position to fill data to
     * @param length length of area
     * @param fill value to fill with
     */
    public void fillData(long startFrom, long length, byte fill);

    /**
     * Removes area of data.
     *
     * @param startFrom position to start removal from
     * @param length length of area
     */
    public void remove(long startFrom, long length);

    /**
     * Removes all existing data.
     *
     * Simply releases all references to data pages.
     */
    public void clear();

    /**
     * Loads data from given stream.
     *
     * @param inputStream input stream
     * @throws java.io.IOException if input/output error
     */
    public void loadFromStream(InputStream inputStream) throws IOException;

    /**
     * Provides handler for output stream generation.
     *
     * @return new instance of output stream
     */
    public OutputStream getDataOutputStream();

    /**
     * Loads data from given stream expecting given size.
     *
     * @param inputStream input stream
     * @param dataSize data size
     * @throws java.io.IOException if input/output error
     */
    public void loadFromStream(InputStream inputStream, long dataSize) throws IOException;
}
