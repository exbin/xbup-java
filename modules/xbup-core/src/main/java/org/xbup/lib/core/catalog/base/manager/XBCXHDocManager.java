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

import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXHDoc;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXHDoc catalog manager.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 * @param <T> HTML documentation entity
 */
public interface XBCXHDocManager<T extends XBCXHDoc> extends XBCManager<T>, XBCExtension {

    /**
     * Get documentation for item.
     *
     * @param item
     * @return documentation
     */
    public XBCXHDoc getDocumentation(XBCItem item);

    /**
     * Get documentation by unique index.
     *
     * @param id unique index
     * @return documentation
     */
    public XBCXHDoc findById(Long id);

    /**
     * Get count of all HTML documentations.
     *
     * @return count of documentations
     */
    public Long getAllHDocsCount();
}
