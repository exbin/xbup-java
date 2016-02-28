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
package org.xbup.lib.client.catalog.remote.manager;

import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.manager.XBCItemManager;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.stub.XBPItemStub;

/**
 * Remote manager class for XBRItem catalog items.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBRItemManager extends XBRDefaultManager<XBRItem> implements XBCItemManager<XBRItem> {

    private final XBPItemStub itemStub;

    public XBRItemManager(XBRCatalog catalog) {
        super(catalog);
        itemStub = new XBPItemStub(client);
    }

    @Override
    public long getItemsCount() {
        return itemStub.getItemsCount();
    }
}
