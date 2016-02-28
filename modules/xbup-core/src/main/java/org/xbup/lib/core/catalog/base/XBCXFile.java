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
package org.xbup.lib.core.catalog.base;

/**
 * Interface for catalog item assesory file entity.
 *
 * @version 0.1.22 2013/07/28
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCXFile extends XBCBase {

    /**
     * Gets owner which is directory.
     *
     * @return the node
     */
    public XBCNode getNode();

    /**
     * Gets name of the file.
     *
     * @return filename string
     */
    public String getFilename();

    /**
     * Gets content of the file.
     *
     * @return byte array
     */
    public byte[] getContent();
}
