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
package org.exbin.xbup.core.catalog.base.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;

/**
 * Persistence manager for catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 * @param <T> entity class
 */
@ParametersAreNonnullByDefault
public class XBCDefaultItemService<T extends XBCBase> implements XBCService<T> {

    protected XBCManager<T> itemManager;

    @Nonnull
    @Override
    public T createItem() {
        return itemManager.createItem();
    }

    @Override
    public void removeItem(T item) {
        itemManager.removeItem(item);
    }

    @Nonnull
    @Override
    public List<T> getAllItems() {
        return itemManager.getAllItems();
    }

    @Nonnull
    @Override
    public Optional<T> getItem(long itemId) {
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
