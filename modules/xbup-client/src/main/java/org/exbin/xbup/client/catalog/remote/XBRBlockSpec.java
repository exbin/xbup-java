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
package org.exbin.xbup.client.catalog.remote;

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;

/**
 * Catalog remote block specification entity.
 *
 * @version 0.1.17 2009/02/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBRBlockSpec extends XBRSpec implements XBCBlockSpec {

    public XBRBlockSpec(XBCatalogServiceClient client, long id) {
        super(client,id);
    }
}
