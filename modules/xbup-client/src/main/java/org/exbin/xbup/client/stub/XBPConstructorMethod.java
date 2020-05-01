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

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.catalog.base.XBCBase;

/**
 * Default constructor method.
 *
 * @version 0.1.25 2015/03/20
 * @author ExBin Project (http://exbin.org)
 * @param <T> base entity
 */
public interface XBPConstructorMethod<T extends XBCBase> {

    /**
     * Returns new instance of item using service client.
     *
     * @param client service client
     * @param itemId item ID
     * @return new instance of item
     */
    public T itemConstructor(XBCatalogServiceClient client, long itemId);
}
