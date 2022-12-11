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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.manager.XBELimiManager;
import org.exbin.xbup.core.catalog.base.XBCItemLimi;
import org.exbin.xbup.core.catalog.base.manager.XBCLimiManager;
import org.exbin.xbup.core.catalog.base.service.XBCLimiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEItemLimi items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBELimiService extends XBEDefaultService<XBCItemLimi> implements XBCLimiService, Serializable {

    @Autowired
    private XBELimiManager manager;

    public XBELimiService() {
        super();
    }

    public XBELimiService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBELimiManager(catalog);
        catalog.addCatalogManager(XBCLimiManager.class, (XBCLimiManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }
}
