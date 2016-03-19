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

import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;

/**
 * Interface for XBCXHDoc items service.
 *
 * @version 0.1.24 2015/01/31
 * @author ExBin Project (http://exbin.org)
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

    /**
     * Gets text body part of documentation in default language for given item.
     *
     * @param item catalog item
     * @return hypertext documentation
     */
    public String getDocumentationBodyText(XBCItem item);
}
