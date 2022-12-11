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

import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.manager.XBEItemManager;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.service.XBItemWithDetail;
import org.exbin.xbup.core.catalog.base.manager.XBCItemManager;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEItem items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEItemService extends XBEDefaultService<XBCItem> implements XBCItemService, Serializable {

    @Autowired
    private XBEItemManager manager;

    public XBEItemService() {
        super();
    }

    public XBEItemService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEItemManager(catalog);
        catalog.addCatalogManager(XBCItemManager.class, (XBCItemManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Nonnull
    @Override
    public List<XBItemWithDetail> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition, String specType) {
        return ((XBEItemManager) itemManager).findAllPaged(startFrom, maxResults, filterCondition, orderCondition, specType);
    }

    @Override
    public int findAllPagedCount(String filterCondition, String specType) {
        return ((XBEItemManager) itemManager).findAllPagedCount(filterCondition, specType);
    }
}
