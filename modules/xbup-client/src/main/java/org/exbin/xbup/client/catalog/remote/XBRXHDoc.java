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
import org.exbin.xbup.client.stub.XBPXHDocStub;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;

/**
 * Catalog remote item HTML documentation entity.
 *
 * @version 0.1.25 2015/04/04
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXHDoc implements XBCXHDoc {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXHDocStub hdocStub;

    public XBRXHDoc(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        hdocStub = new XBPXHDocStub(client);
    }

    @Override
    public XBRItem getItem() {
        return hdocStub.getHDocItem(id);
    }

    @Override
    public XBCXFile getDocFile() {
        return hdocStub.getDocFile(id);
    }

    @Override
    public XBCXLanguage getLang() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getId() {
        return id;
    }
}
