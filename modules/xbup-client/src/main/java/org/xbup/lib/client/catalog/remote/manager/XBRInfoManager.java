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
import org.xbup.lib.client.catalog.remote.XBRXItemInfo;
import org.xbup.lib.client.stub.XBPInfoStub;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.manager.XBCXInfoManager;

/**
 * Remote manager class for XBRXItemInfo catalog items.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBRInfoManager extends XBRDefaultManager<XBRXItemInfo> implements XBCXInfoManager<XBRXItemInfo> {

    private final XBPInfoStub infoStub;

    public XBRInfoManager(XBRCatalog catalog) {
        super(catalog);
        infoStub = new XBPInfoStub(client);
    }

    @Override
    public XBRXItemInfo getNodeInfo(XBCNode node) {
        return infoStub.getNodeInfo(node);
    }
}
