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
package org.exbin.xbup.client.catalog.remote.service;

import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.catalog.base.service.XBCDefaultItemService;
import org.exbin.xbup.core.catalog.base.service.XBCService;

/**
 * Default remote service.
 *
 * @version 0.1.25 2015/03/20
 * @author ExBin Project (http://exbin.org)
 * @param <T> base entity
 */
public abstract class XBRDefaultService<T extends XBCBase> extends XBCDefaultItemService<T> implements XBCService<T> {

    protected XBRCatalog catalog;

    public XBRDefaultService(XBRCatalog catalog) {
        this.catalog = catalog;
    }
}
