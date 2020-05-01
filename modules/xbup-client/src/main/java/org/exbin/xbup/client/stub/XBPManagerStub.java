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
package org.exbin.xbup.client.stub;

import java.util.List;
import org.exbin.xbup.core.catalog.base.XBCBase;

/**
 * Stub generics interface for catalog manager classes.
 *
 * @version 0.1.25 2015/03/20
 * @author ExBin Project (http://exbin.org)
 * @param <T> entity class
 */
public interface XBPManagerStub<T extends XBCBase> {

    /**
     * Constructs remote representation linked to item id.
     *
     * @param itemId item index
     * @return new item
     */
    public T constructItem(long itemId);

    /**
     * Creates instance of new item and set it to default state.
     *
     * @return new item
     */
    public T createItem();

    /**
     * Updates item state to persistent repository.
     *
     * @param item to update
     */
    public void persistItem(T item);

    /**
     * Deletes item from persistent repository.
     *
     * @param item item
     */
    public void removeItem(T item);

    /**
     * Gets item from persistent repository.
     *
     * @param itemId item id
     * @return instance of item or null
     */
    public T getItem(long itemId);

    /**
     * Gets list of all items from persistent repository.
     *
     * @return list of items
     */
    public List<T> getAllItems();

    /**
     * Returns count of items in persistent repository.
     *
     * @return count of items
     */
    public long getItemsCount();
}
