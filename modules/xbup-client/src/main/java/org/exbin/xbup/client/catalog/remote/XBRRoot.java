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

import java.util.Date;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.stub.XBPNodeStub;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalog remote root entity.
 *
 * @version 0.1.25 2015/03/11
 * @author ExBin Project (http://exbin.org)
 */
public class XBRRoot implements XBCRoot {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPNodeStub nodeStub;

    public XBRRoot(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        nodeStub = new XBPNodeStub(client);
    }

    @Override
    public XBCNode getNode() {
        return nodeStub.getRootNode(id);
    }

    @Override
    public String getUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getLastUpdate() {
        return nodeStub.getRootLastUpdate(id);
    }

    @Override
    public Long getId() {
        return id;
    }
}
