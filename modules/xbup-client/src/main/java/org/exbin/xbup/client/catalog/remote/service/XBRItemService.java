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
package org.exbin.xbup.client.catalog.remote.service;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRItem;
import org.exbin.xbup.client.catalog.remote.manager.XBRItemManager;
import org.exbin.xbup.core.catalog.base.manager.XBCItemManager;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBItemWithDetail;

/**
 * Remote service for XBRItem items.
 *
 * @version 0.2.1 2020/02/03
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRItemService extends XBRDefaultService<XBRItem> implements XBCItemService<XBRItem> {

    public XBRItemService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRItemManager(catalog);
        catalog.addCatalogManager(XBCItemManager.class, (XBCItemManager) itemManager);
    }

    @Nonnull
    @Override
    public List<XBItemWithDetail> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition, String specType) {
        return ((XBRItemManager) itemManager).findAllPaged(startFrom, maxResults, filterCondition, orderCondition, specType);
    }

    @Override
    public int findAllPagedCount(String filterCondition, String specType) {
        return ((XBRItemManager) itemManager).findAllPagedCount(filterCondition, specType);
    }
}
