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
import org.exbin.xbup.client.stub.XBPXLineStub;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;

/**
 * Catalog remote block line editor entity.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXBlockLine implements XBCXBlockLine {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXLineStub lineStub;

    public XBRXBlockLine(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        lineStub = new XBPXLineStub(client);
    }

    @Override
    public XBCBlockRev getBlockRev() {
        return lineStub.getBlockRev(id);
    }

    @Override
    public XBCXPlugLine getLine() {
        return lineStub.getLine(id);
    }

    @Override
    public Long getPriority() {
        return lineStub.getPriority(id);
    }

    @Override
    public Long getId() {
        return id;
    }
}
