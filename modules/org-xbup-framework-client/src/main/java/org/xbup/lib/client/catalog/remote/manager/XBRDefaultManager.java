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
package org.xbup.lib.client.catalog.remote.manager;

import java.util.List;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.stub.XBPManagerStub;

/**
 * Default remote manager for catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 * @param <T> entity class
 */
public abstract class XBRDefaultManager<T extends XBCBase> implements XBCManager<T> {

    protected XBRCatalog catalog;
    protected XBCatalogServiceClient client;
    private XBPManagerStub<T> managerStub = null;

    public XBRDefaultManager(XBRCatalog catalog) {
        this.catalog = catalog;
        this.client = catalog.getCatalogServiceClient();
    }

    public XBPManagerStub<T> getManagerStub() {
        return managerStub;
    }

    public void setManagerStub(XBPManagerStub<T> managerStub) {
        this.managerStub = managerStub;
    }

    @Override
    public T createItem() {
        if (managerStub != null) {
            return managerStub.createItem();
        }

        throw new IllegalStateException("Operation not supported");
    }

    @Override
    public void removeItem(T item) {
        if (managerStub != null) {
            managerStub.removeItem(item);
        }

        throw new IllegalStateException("Operation not supported");
    }

    @Override
    public List<T> getAllItems() {
        if (managerStub != null) {
            return managerStub.getAllItems();
        }

        throw new IllegalStateException("Operation not supported");
    }

    @Override
    public T getItem(long itemId) {
        if (managerStub != null) {
            return managerStub.getItem(itemId);
        }

        throw new IllegalStateException("Operation not supported");
    }

    @Override
    public long getItemsCount() {
        if (managerStub != null) {
            return managerStub.getItemsCount();
        }

        throw new IllegalStateException("Operation not supported");
    }

    @Override
    public void persistItem(T item) {
        if (managerStub != null) {
            managerStub.persistItem(item);
        }

        throw new IllegalStateException("Operation not supported");
    }
}
