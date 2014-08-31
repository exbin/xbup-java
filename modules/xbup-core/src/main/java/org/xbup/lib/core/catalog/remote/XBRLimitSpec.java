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
package org.xbup.lib.core.catalog.remote;

import org.xbup.lib.core.catalog.base.XBCLimitSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.client.XBCatalogServiceClient;

/**
 *
 * @version 0.1.22 2013/07/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBRLimitSpec implements XBCLimitSpec {

    private long id;
    protected XBCatalogServiceClient client;

    public XBRLimitSpec(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public XBCNode getParent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getXBIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
