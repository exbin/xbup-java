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

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.manager.XBRItemManager;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.manager.XBCItemManager;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBItemWithDetail;

/**
 * Remote service for XBRItem items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRItemService extends XBRDefaultService<XBCItem> implements XBCItemService {

    public XBRItemService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRItemManager(catalog);
        catalog.addCatalogManager(XBCItemManager.class, (XBCItemManager) itemManager);
    }

    @Nonnull
    @Override
    public List<XBItemWithDetail> findAllPaged(int startFrom, int maxResults, @Nullable String filterCondition, @Nullable String orderCondition, @Nullable String specType) {
        return ((XBRItemManager) itemManager).findAllPaged(startFrom, maxResults, filterCondition, orderCondition, specType);
    }

    @Override
    public int findAllPagedCount(@Nullable String filterCondition, @Nullable String specType) {
        return ((XBRItemManager) itemManager).findAllPagedCount(filterCondition, specType);
    }
}
