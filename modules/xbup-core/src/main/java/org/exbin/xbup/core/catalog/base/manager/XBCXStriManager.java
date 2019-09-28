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
package org.exbin.xbup.core.catalog.base.manager;

import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXStri;

/**
 * Interface for XBCXStri catalog manager.
 *
 * @version 0.1.24 2014/11/18
 * @author ExBin Project (http://exbin.org)
 * @param <T> string index entity
 */
public interface XBCXStriManager<T extends XBCXStri> extends XBCManager<T>, XBCExtension {

    /**
     * Returns string index for given item.
     *
     * @param item item
     * @return string index
     */
    XBCXStri getItemStringId(XBCItem item);

    /**
     * Gets stri text for given item.
     *
     * @param item item to get caption of
     * @return string caption
     */
    String getItemStringIdText(XBCItem item);

    /**
     * Returns full stringId path including leading slash symbol.
     *
     * @param itemString stringId item
     * @return string representation of the path
     */
    String getFullPath(XBCXStri itemString);
}
