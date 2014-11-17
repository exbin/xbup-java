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
package org.xbup.lib.core.catalog.base.service;

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXName items service.
 *
 * @version 0.1.24 2014/11/17
 * @author XBUP Project (http://xbup.org)
 * @param <T> name entity
 */
public interface XBCXNameService<T extends XBCXName> extends XBCService<T>, XBCExtension {

    /**
     * Returns default name for given item.
     *
     * @param item item
     * @return name
     */
    public XBCXName getItemName(XBCItem item);

    /**
     * Returns name for given item and language.
     *
     * @param item item
     * @param language language
     * @return name
     */
    public XBCXName getItemName(XBCItem item, XBCXLanguage language);

    /**
     * Returns list of names for given item.
     *
     * @param item item
     * @return list of names
     */
    public List<XBCXName> getItemNames(XBCItem item);

    /**
     * Gets default name text for default language.
     *
     * @param item item to get caption of
     * @return string caption
     */
    public String getDefaultText(XBCItem item);

    /**
     * Sets name text for default language and given item.
     *
     * @param item item to set name for
     * @param text text to set
     */
    public void setDefaultText(XBCItem item, String text);

    /**
     * Gets name path as a list of dot separated name sequence.
     *
     * @param item
     * @return name path string
     */
    public String getItemNamePath(XBCItem item);
}
