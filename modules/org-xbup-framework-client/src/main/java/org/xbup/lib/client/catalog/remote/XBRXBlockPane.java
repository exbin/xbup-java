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

import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXBlockPane;
import org.xbup.lib.core.catalog.base.XBCXPlugPane;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.stub.XBPXPaneStub;

/**
 * Catalog remote block panel editor entity.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXBlockPane implements XBCXBlockPane {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXPaneStub paneStub;

    public XBRXBlockPane(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        paneStub = new XBPXPaneStub(client);
    }

    @Override
    public XBCBlockRev getBlockRev() {
        return paneStub.getBlockRev(id);
    }

    @Override
    public XBCXPlugPane getPane() {
        return paneStub.getPane(id);
    }

    @Override
    public Long getPriority() {
        return paneStub.getPriority(id);
    }

    @Override
    public Long getId() {
        return id;
    }
}