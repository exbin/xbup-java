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

import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXHDoc;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXHDoc items service.
 *
 * @version 0.1.21 2011/02/05
 * @author XBUP Project (http://xbup.org)
 * @param <T> documentation entity
 */
public interface XBCXHDocService<T extends XBCXHDoc> extends XBCService<T>, XBCExtension {

    /**
     * Gets documentation in default language for given item.
     *
     * @param item catalog item
     * @return hypertext documentation structure
     */
    public XBCXHDoc getDocumentation(XBCItem item);

    /**
     * Gets text for documentation in default language for given item.
     *
     * @param item catalog item
     * @return hypertext documentation
     */
    public String getDocumentationText(XBCItem item);

}