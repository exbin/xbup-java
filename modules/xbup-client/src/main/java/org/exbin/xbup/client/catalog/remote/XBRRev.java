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
import org.exbin.xbup.client.stub.XBPRevStub;
import org.exbin.xbup.core.catalog.base.XBCRev;

/**
 * Catalog remote revision entity.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRRev extends XBRItem implements XBCRev {

    private final XBPRevStub revStub;

    public XBRRev(XBCatalogServiceClient client, long id) {
        super(client, id);
        revStub = new XBPRevStub(client);
    }

    @Override
    public Long getXBLimit() {
        return revStub.getXBLimit(getId());
    }

    @Override
    public XBRSpec getParent() {
        XBRItem item = super.getParent();
        if (item == null) {
            return null;
        }
        return new XBRSpec(client, item.getId());
    }
}
