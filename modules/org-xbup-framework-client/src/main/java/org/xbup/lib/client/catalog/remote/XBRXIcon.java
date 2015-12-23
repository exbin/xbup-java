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
package org.xbup.lib.client.catalog.remote;

import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.XBCXIconMode;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.stub.XBPXIconStub;

/**
 * Catalog remote item icon entity.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXIcon implements XBCXIcon {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXIconStub iconStub;

    public XBRXIcon(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        iconStub = new XBPXIconStub(client);
    }

    @Override
    public XBRItem getParent() {
        return iconStub.getParent(id);
    }

    @Override
    public XBCXIconMode getMode() {
        return iconStub.getMode(id);
    }

    @Override
    public XBCXFile getIconFile() {
        return iconStub.getIconFile(id);
    }

    @Override
    public Long getId() {
        return id;
    }
}
