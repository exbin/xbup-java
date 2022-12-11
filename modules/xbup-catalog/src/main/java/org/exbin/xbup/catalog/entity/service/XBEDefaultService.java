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
package org.exbin.xbup.catalog.entity.service;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.catalog.base.service.XBCDefaultItemService;
import org.exbin.xbup.core.catalog.base.service.XBCService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default service for catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 * @param <T> entity class
 */
@ParametersAreNonnullByDefault
public class XBEDefaultService<T extends XBCBase> extends XBCDefaultItemService<T> implements XBCService<T> {

    @Autowired
    protected XBECatalog catalog;

    public XBEDefaultService() {
    }

    public XBEDefaultService(XBECatalog catalog) {
        this.catalog = catalog;
    }
}
