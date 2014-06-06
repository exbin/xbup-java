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
package org.xbup.lib.xb.catalog.base.service;

import java.util.List;
import org.xbup.lib.xb.catalog.base.XBCBase;
import org.xbup.lib.xb.catalog.base.manager.XBCManager;

/**
 * Persistence manager for catalog items.
 *
 * @version 0.1 wr21.0 2011/12/27
 * @author XBUP Project (http://xbup.org)
 * @param <T> entity class
 */
public class XBCDefaultItemService<T extends XBCBase> implements XBCService<T> {

    protected XBCManager<T> itemManager;

    @Override
    public T createItem() {
        return itemManager.createItem();
    }

    @Override
    public void removeItem(T item) {
        itemManager.removeItem(item);
    }

    @Override
    public List<T> getAllItems() {
        return itemManager.getAllItems();
    }

    @Override
    public T getItem(long itemId) {
        return itemManager.getItem(itemId);
    }

    @Override
    public long getItemsCount() {
        return itemManager.getItemsCount();
    }

    @Override
    public void persistItem(T item) {
        itemManager.persistItem(item);
    }
}
