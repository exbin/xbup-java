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
package org.xbup.lib.core.catalog.base.manager;

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXStri catalog manager.
 *
 * @version 0.1.24 2014/11/17
 * @author XBUP Project (http://xbup.org)
 * @param <T> string index entity
 */
public interface XBCXStriManager<T extends XBCXStri> extends XBCManager<T>, XBCExtension {

    /**
     * Returns string index for given item.
     *
     * @param item
     * @return string index
     */
    public XBCXStri getItemStringId(XBCItem item);

    /**
     * Returns list of string indices for given item.
     *
     * @param item
     * @return list of string indices
     */
    public List<XBCXStri> getItemStringIds(XBCItem item);

    /**
     * Gets stri text for given item.
     *
     * @param item item to get caption of
     * @return string caption
     */
    public String getItemStringIdText(XBCItem item);

    /**
     * Returns full stringId path including leading slash symbol.
     *
     * @param itemString stringId item
     * @return string representation of the path
     */
    public String getFullPath(XBCXStri itemString);
}
