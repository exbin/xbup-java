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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.manager.XBEItemManager;
import org.exbin.xbup.catalog.entity.manager.XBItemWithDetail;
import org.exbin.xbup.core.catalog.base.manager.XBCItemManager;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEItem items service.
 *
 * @version 0.1.24 2014/12/11
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEItemService extends XBEDefaultService<XBEItem> implements XBCItemService<XBEItem>, Serializable {

    @Autowired
    private XBEItemManager manager;

    public XBEItemService() {
        super();
    }

    public XBEItemService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEItemManager(catalog);
        catalog.addCatalogManager(XBCItemManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    public List<XBItemWithDetail> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition, String specType) {
        return ((XBEItemManager) itemManager).findAllPaged(startFrom, maxResults, filterCondition, orderCondition, specType);
    }

    public int findAllPagedCount(String filterCondition, String specType) {
        return ((XBEItemManager) itemManager).findAllPagedCount(filterCondition, specType);
    }
}
