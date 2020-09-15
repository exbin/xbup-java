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
package org.exbin.xbup.client.catalog.remote.service;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXBlockUi;
import org.exbin.xbup.client.catalog.remote.XBRXPlugUi;
import org.exbin.xbup.client.catalog.remote.manager.XBRXUiManager;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXUiManager;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;

/**
 * Remote service for XBRXBlockUi items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXUiService extends XBRDefaultService<XBCXBlockUi> implements XBCXUiService {

    public XBRXUiService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXUiManager(catalog);
        catalog.addCatalogManager(XBCXUiManager.class, (XBCXUiManager) itemManager);
    }

    @Override
    public XBRXBlockUi findById(long id) {
        return ((XBRXUiManager) itemManager).findById(id);
    }

    @Override
    public XBCXBlockUi findUiByPR(XBCBlockRev revision, XBPlugUiType type, long priority) {
        return ((XBRXUiManager) itemManager).findUiByPR(revision, type, priority);
    }

    @Override
    public XBRXPlugUi findPlugUiById(long id) {
        return ((XBRXUiManager) itemManager).findPlugUiById(id);
    }

    @Override
    public XBCXPlugUiType findTypeById(int id) {
        return ((XBRXUiManager) itemManager).findTypeById(id);
    }

    @Override
    public long getAllPlugUisCount() {
        return ((XBRXUiManager) itemManager).getAllPlugUisCount();
    }

    @Override
    public List<XBCXBlockUi> getUis(XBCBlockRev rev) {
        return ((XBRXUiManager) itemManager).getUis(rev);
    }

    @Override
    public List<XBCXBlockUi> getUis(XBCBlockRev rev, XBPlugUiType type) {
        return ((XBRXUiManager) itemManager).getUis(rev, type);
    }

    @Override
    public long getUisCount(XBCBlockRev rev) {
        return ((XBRXUiManager) itemManager).getUisCount(rev);
    }

    @Override
    public long getUisCount(XBCBlockRev rev, XBPlugUiType type) {
        return ((XBRXUiManager) itemManager).getUisCount(rev, type);
    }

    @Override
    public XBRXPlugUi getPlugUi(XBCXPlugin plugin, XBPlugUiType type, long methodIndex) {
        return ((XBRXUiManager) itemManager).getPlugUi(plugin, type, methodIndex);
    }

    @Override
    public List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin) {
        return ((XBRXUiManager) itemManager).getPlugUis(plugin);
    }

    @Override
    public List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin, XBPlugUiType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugUisCount(XBCXPlugin plugin) {
        return ((XBRXUiManager) itemManager).getPlugUisCount(plugin);
    }

    @Override
    public long getPlugUisCount(XBCXPlugin plugin, XBPlugUiType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }

    @Override
    public XBCXPlugUi createPlugUi() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistPlugUi(XBCXPlugUi plugUi) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePlugUi(XBCXPlugUi plugUi) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
