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
package org.exbin.xbup.client.catalog.remote.service;

import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.manager.XBRLimiManager;
import org.exbin.xbup.core.catalog.base.XBCItemLimi;
import org.exbin.xbup.core.catalog.base.manager.XBCLimiManager;
import org.exbin.xbup.core.catalog.base.service.XBCLimiService;

/**
 * Remote service for XBRItemLimi items.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBRLimiService extends XBRDefaultService<XBCItemLimi> implements XBCLimiService {

    public XBRLimiService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRLimiManager(catalog);
        catalog.addCatalogManager(XBCLimiManager.class, (XBCLimiManager) itemManager);
    }
}
