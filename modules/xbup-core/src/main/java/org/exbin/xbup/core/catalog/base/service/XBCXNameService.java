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
package org.exbin.xbup.core.catalog.base.service;

import java.util.List;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;

/**
 * Interface for XBCXName items service.
 *
 * @version 0.1.24 2014/11/17
 * @author ExBin Project (http://exbin.org)
 * @param <T> name entity
 */
public interface XBCXNameService<T extends XBCXName> extends XBCService<T>, XBCExtension {

    /**
     * Returns default name for given item.
     *
     * @param item item
     * @return name
     */
    XBCXName getDefaultItemName(XBCItem item);

    /**
     * Returns name for given item and language.
     *
     * @param item item
     * @param language language
     * @return name
     */
    XBCXName getItemName(XBCItem item, XBCXLanguage language);

    /**
     * Returns list of names for given item.
     *
     * @param item item
     * @return list of names
     */
    List<XBCXName> getItemNames(XBCItem item);

    /**
     * Gets default name text for default language.
     *
     * @param item item to get caption of
     * @return string caption
     */
    String getDefaultText(XBCItem item);

    /**
     * Gets name path as a list of dot separated name sequence.
     *
     * @param item item
     * @return name path string
     */
    String getItemNamePath(XBCItem item);
}
