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
package org.xbup.web.xbcatalogweb.service;

import org.xbup.web.xbcatalogweb.base.service.XBCPackageRecordService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.web.xbcatalogweb.base.XBCPackageRecord;
import org.xbup.web.xbcatalogweb.base.manager.XBCPackageRecordManager;

/**
 * XBEPackageRecord service.
 *
 * @version 0.1.23 2014/05/08
 * @author XBUP Project (http://xbup.org)
 */
@Service
public class XBEPackageRecordService implements XBCPackageRecordService {

    @Autowired
    private XBCPackageRecordManager manager;

    public XBEPackageRecordService() {
        super();
    }

    @Override
    public XBCPackageRecord createItem() {
        return manager.createItem();
    }

    @Override
    public void persistItem(XBCPackageRecord item) {
        manager.persistItem(item);
    }

    @Override
    public void removeItem(XBCPackageRecord item) {
        manager.removeItem(item);
    }

    @Override
    public XBCPackageRecord getItem(long itemId) {
        return manager.getItem(itemId);
    }

    @Override
    public List<XBCPackageRecord> getAllItems() {
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
    public List<XBCPackageRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition) {
        return manager.findAllPaged(startFrom, maxResults, filterCondition, orderCondition);
    }

    @Override
    public List<XBCPackageRecord> findFullTree() {
        return manager.findFullTree();
    }
}
