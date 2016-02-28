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
package org.xbup.web.xbcatalogweb.service;

import org.xbup.web.xbcatalogweb.base.service.XBCUserRecordService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.catalog.entity.XBEXUser;
import org.xbup.web.xbcatalogweb.base.XBCUserRecord;
import org.xbup.web.xbcatalogweb.base.manager.XBCUserRecordManager;

/**
 * XBEUserRecord service.
 *
 * @version 0.1.23 2014/04/12
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEUserRecordService implements XBCUserRecordService {

    @Autowired
    private XBCUserRecordManager manager;

    public XBEUserRecordService() {
        super();
    }

    @Override
    public XBCUserRecord createItem() {
        return manager.createItem();
    }

    @Override
    public void persistItem(XBCUserRecord item) {
        manager.persistItem(item);
    }

    @Override
    public void removeItem(XBCUserRecord item) {
        manager.removeItem(item);
    }

    @Override
    public XBCUserRecord getItem(long itemId) {
        return manager.getItem(itemId);
    }

    @Override
    public List<XBCUserRecord> getAllItems() {
        return manager.getAllItems();
    }

    @Override
    public long getItemsCount() {
        return manager.getItemsCount();
    }

    @Override
    public int findAllPagedCount(String filterCondition) {
        return manager.findAllPagedCount(filterCondition);
    }

    @Override
    public List<XBCUserRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition) {
        return manager.findAllPaged(startFrom, maxResults, filterCondition, orderCondition);
    }

    @Override
    public void persistUser(XBEXUser user) {
        manager.persistUser(user);
    }
}
