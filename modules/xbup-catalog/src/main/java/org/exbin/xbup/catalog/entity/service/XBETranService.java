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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.manager.XBETranManager;
import org.exbin.xbup.core.catalog.base.XBCTran;
import org.exbin.xbup.core.catalog.base.manager.XBCTranManager;
import org.exbin.xbup.core.catalog.base.service.XBCTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBETran items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBETranService extends XBEDefaultService<XBCTran> implements XBCTranService, Serializable {

    @Autowired
    private XBETranManager manager;

    public XBETranService() {
        super();
    }

    public XBETranService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBETranManager(catalog);
        catalog.addCatalogManager(XBCTranManager.class, (XBCTranManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }
}
