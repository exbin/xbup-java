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
import org.exbin.xbup.client.stub.XBPXNameStub;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;

/**
 * Catalog remote item name entity.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXName implements XBCXName {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXNameStub nameStub;

    public XBRXName(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        nameStub = new XBPXNameStub(client);
    }

    @Override
    public XBCItem getItem() {
        return nameStub.getNameItem(id);
    }

    @Override
    public String getText() {
        return nameStub.getText(id);
    }

    @Override
    public XBRXLanguage getLang() {
        return nameStub.getLang(id);
    }

    @Override
    public void setItem(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLang(XBCXLanguage language) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getId() {
        return id;
    }
}
