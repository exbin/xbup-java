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
package org.exbin.xbup.catalog.entity.manager;

import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default manager for catalog items.
 *
 * @version 0.2.0 2016/02/20
 * @author ExBin Project (http://exbin.org)
 * @param <T> entity class
 */
public abstract class XBEDefaultCatalogManager<T extends XBCBase> extends XBEDefaultManager<T> {

    @Autowired
    protected XBECatalog catalog;

    public XBEDefaultCatalogManager() {
    }

    public XBEDefaultCatalogManager(XBECatalog catalog) {
        super(catalog.getEntityManager());
        this.catalog = catalog;
    }
}
