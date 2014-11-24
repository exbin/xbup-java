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
package org.xbup.lib.client.catalog.remote.service;

import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.manager.XBCXInfoManager;
import org.xbup.lib.core.catalog.base.service.XBCXInfoService;
import org.xbup.lib.client.catalog.remote.XBRItemInfo;
import org.xbup.lib.client.catalog.remote.manager.XBRInfoManager;

/**
 * Interface for XBRItemInfo items service.
 *
 * @version 0.1.21 2012/01/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBRInfoService extends XBRDefaultService<XBRItemInfo> implements XBCXInfoService<XBRItemInfo> {

    public XBRInfoService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRInfoManager(catalog);
        catalog.addCatalogManager(XBCXInfoManager.class, itemManager);
    }

    @Override
    public XBRItemInfo getNodeInfo(XBCNode dir) {
        return ((XBRInfoManager)itemManager).getNodeInfo(dir);
    }

}
