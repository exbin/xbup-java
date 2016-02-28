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
package org.xbup.lib.core.catalog.base.service;

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXDesc items service.
 *
 * @version 0.1.24 2014/12/03
 * @author ExBin Project (http://exbin.org)
 * @param <T> description entity
 */
public interface XBCXDescService<T extends XBCXDesc> extends XBCService<T>, XBCExtension {

    /**
     * Returns description for given item in default language.
     *
     * @param item item
     * @return description
     */
    public XBCXDesc getDefaultItemDesc(XBCItem item);

    /**
     * Returns description for given item and language.
     *
     * @param item item
     * @param language language
     * @return description
     */
    public XBCXDesc getItemDesc(XBCItem item, XBCXLanguage language);

    /**
     * Returns list of names for given item.
     *
     * @param item item
     * @return description
     */
    public List<XBCXDesc> getItemDescs(XBCItem item);

    /**
     * Gets description text for default language and given item.
     *
     * @param item item to get name of
     * @return text or null if name is not set
     */
    public String getDefaultText(XBCItem item);
}
