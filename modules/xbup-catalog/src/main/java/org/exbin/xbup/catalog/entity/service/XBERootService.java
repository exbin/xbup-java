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
import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBERoot;
import org.exbin.xbup.catalog.entity.manager.XBERootManager;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCRootManager;
import org.exbin.xbup.core.catalog.base.service.XBCRootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBERoot items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBERootService extends XBEDefaultService<XBCRoot> implements XBCRootService, Serializable {

    @Autowired
    private XBERootManager manager;

    public XBERootService() {
        super();
    }

    public XBERootService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBERootManager(catalog);
        catalog.addCatalogManager(XBCRootManager.class, (XBCRootManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Nonnull
    @Override
    public XBERoot getMainRoot() {
        return ((XBERootManager) itemManager).getMainRoot();
    }

    @Nonnull
    @Override
    public Optional<Date> getMainLastUpdate() {
        return ((XBERootManager) itemManager).getMainLastUpdate();
    }

    @Override
    public boolean isMainPresent() {
        return ((XBERootManager) itemManager).isMainPresent();
    }

    @Override
    public void setMainLastUpdateToNow() {
        ((XBERootManager) itemManager).setMainLastUpdateToNow();
    }
}
