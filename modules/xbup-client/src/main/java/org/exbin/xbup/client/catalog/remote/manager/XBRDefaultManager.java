/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.stub.XBPManagerStub;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;

/**
 * Default remote manager for catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 * @param <T> entity class
 */
@ParametersAreNonnullByDefault
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

    @Nonnull
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

    @Nonnull
    @Override
    public List<T> getAllItems() {
        if (managerStub != null) {
            return managerStub.getAllItems();
        }

        throw new IllegalStateException("Operation not supported");
    }

    @Nonnull
    @Override
    public Optional<T> getItem(long itemId) {
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

    @Override
    public boolean initCatalog() {
        return true;
    }
}
