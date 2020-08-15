/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.catalog.base.manager;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCBase;

/**
 * Generic interface for persistence item management classes.
 *
 * @version 0.2.1 2020/08/11
 * @author ExBin Project (http://exbin.org)
 * @param <T> base entity
 */
@ParametersAreNonnullByDefault
public interface XBCManager<T extends XBCBase> {

    /**
     * Creates instance of new item and set it to default state.
     *
     * @return new item
     */
    @Nonnull
    T createItem();

    /**
     * Updates item state to persistent repository.
     *
     * @param item item to update
     */
    void persistItem(T item);

    /**
     * Deletes item from persistent repository.
     *
     * @param item item to remove
     */
    void removeItem(T item);

    /**
     * Gets item from persistent repository.
     *
     * @param itemId item id
     * @return instance of item or null
     */
    @Nullable
    T getItem(long itemId);

    /**
     * Gets list of all items from persistent repository.
     *
     * @return list of items
     */
    @Nonnull
    List<T> getAllItems();

    /**
     * Returns count of items in persistent repository.
     *
     * @return count of items
     */
    long getItemsCount();
}
