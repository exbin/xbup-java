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
import org.exbin.xbup.client.stub.XBPXStriStub;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXStri;

/**
 * Catalog remote item string identificator entity.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXStri implements XBCXStri {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXStriStub striStub;

    public XBRXStri(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        striStub = new XBPXStriStub(client);
    }

    @Override
    public XBCItem getItem() {
        return striStub.getStriItem(id);
    }

    @Override
    public String getText() {
        return striStub.getText(id);
    }

    @Override
    public void setItem(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getNodePath() {
        return striStub.getNodePath(id);
    }

    @Override
    public void setNodePath(String nodePath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getId() {
        return id;
    }
}
