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
package org.xbup.lib.client.stub;

import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.core.catalog.base.XBCBase;

/**
 * Default constructor method.
 *
 * @version 0.1.25 2015/03/20
 * @author XBUP Project (http://xbup.org)
 * @param <T> base entity
 */
public interface XBPConstructorMethod<T extends XBCBase> {

    /**
     * Returns new instance of item using service client.
     *
     * @param client service client
     * @param itemId item ID
     * @return new instance of item
     */
    public T itemConstructor(XBCatalogServiceClient client, long itemId);
}
