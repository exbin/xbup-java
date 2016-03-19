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
package org.exbin.xbup.core.catalog.base;

/**
 * Interface for catalog icon mode entity.
 *
 * @version 0.1.21 2012/05/01
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCXIconMode extends XBCBase {

    /**
     * Gets type of icon.
     *
     * @return icon type index
     */
    public Long getType();

    /**
     * Gets MIME string.
     *
     * @return MIME string
     */
    public String getMIME();

    /**
     * Gets icon mode text caption.
     *
     * @return caption string
     */
    public String getCaption();
}
