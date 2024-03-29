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

import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.manager.XBRRootManager;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.manager.XBCRootManager;
import org.exbin.xbup.core.catalog.base.service.XBCRootService;

/**
 * Remote service for XBRRoot items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRRootService extends XBRDefaultService<XBCRoot> implements XBCRootService {

    public XBRRootService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRRootManager(catalog);
        catalog.addCatalogManager(XBCRootManager.class, (XBCRootManager) itemManager);
    }

    @Override
    public XBCRoot getMainRoot() {
        return ((XBRRootManager) itemManager).getMainRoot();
    }

    @Nonnull
    @Override
    public Optional<Date> getMainLastUpdate() {
        return ((XBRRootManager) itemManager).getMainLastUpdate();
    }

    @Override
    public boolean isMainPresent() {
        return ((XBRRootManager) itemManager).isMainPresent();
    }

    @Override
    public void setMainLastUpdateToNow() {
        ((XBRRootManager) itemManager).setMainLastUpdateToNow();
    }

    @Override
    public void setMainLastUpdate(Date updateDate) {
        ((XBRRootManager) itemManager).setMainLastUpdate(updateDate);
    }

    @Override
    public void removeAllForRoot(XBCRoot root) {
        XBCNode rootNode = root.getNode();
        XBCNodeManager nodeManager = catalog.getCatalogManager(XBCNodeManager.class);
        nodeManager.removeNodeFully(rootNode);
    }
}
