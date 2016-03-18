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

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.catalog.entity.XBEXHDoc;
import org.xbup.web.xbcatalogweb.base.XBCFullItemRecord;
import org.xbup.web.xbcatalogweb.base.XBCItemRecord;
import org.xbup.web.xbcatalogweb.base.manager.XBCItemRecordManager;
import org.xbup.web.xbcatalogweb.base.service.XBCItemRecordService;
import org.xbup.web.xbcatalogweb.entity.XBEItemRecord;

/**
 * XBEItemRecord service.
 *
 * @version 0.1.24 2015/01/16
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEItemRecordService implements XBCItemRecordService {

    @Autowired
    private XBCItemRecordManager manager;

    public XBEItemRecordService() {
        super();
    }

    @Override
    public XBCItemRecord createItem() {
        return manager.createItem();
    }

    @Override
    public void persistItem(XBCItemRecord item) {
        manager.persistItem(item);
    }

    @Override
    public void removeItem(XBCItemRecord item) {
        manager.removeItem(item);
    }

    @Override
    public XBCItemRecord getItem(long itemId) {
        return manager.getItem(itemId);
    }

    @Override
    public List<XBCItemRecord> getAllItems() {
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
    public List<XBCItemRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition) {
        return manager.findAllPaged(startFrom, maxResults, filterCondition, orderCondition);
    }

    @Override
    public XBCFullItemRecord findForEditById(Long selectedItemId) {
        return manager.findForEditById(selectedItemId);
    }

    public XBCFullItemRecord createForEdit() {
        return manager.createForEdit();
    }

    @Override
    public List<XBCItemRecord> findAllByParent(Long selectedPackageId) {
        return manager.findAllByParent(selectedPackageId);
    }

    @Override
    public XBEXHDoc getItemDoc(XBEItemRecord selectedItem) {
        return manager.getItemDoc(selectedItem);
    }

    public Long[] stringToPath(String path) {
        List<Long> pathList = new ArrayList<Long>();
        int pos = 0;
        while (pos < path.length()) {
            int nextPos = path.indexOf("/", pos);
            if (nextPos < 0) {
                nextPos = path.length();
            }
            pathList.add(Long.valueOf(path.substring(pos, nextPos)));
            pos = nextPos + 1;
        }
        return pathList.toArray(new Long[0]);
    }

    public XBCFullItemRecord findNodeByPath(Long[] path) {
        return manager.findNodeByPath(path);
    }

    public XBCFullItemRecord findFormatSpecByPath(Long[] path) {
        return manager.findFormatSpecByPath(path);
    }

    public XBCFullItemRecord findGroupSpecByPath(Long[] path) {
        return manager.findGroupSpecByPath(path);
    }

    public XBCFullItemRecord findBlockSpecByPath(Long[] path) {
        return manager.findBlockSpecByPath(path);
    }
}
