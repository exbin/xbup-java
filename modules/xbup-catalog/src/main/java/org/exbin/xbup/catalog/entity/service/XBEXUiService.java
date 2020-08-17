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
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXBlockUi;
import org.exbin.xbup.catalog.entity.XBEXPlugUi;
import org.exbin.xbup.catalog.entity.manager.XBEXUiManager;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXUiManager;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXBlockLine items service.
 *
 * @version 0.2.1 2020/08/16
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXUiService extends XBEDefaultService<XBEXBlockUi> implements XBCXUiService<XBEXBlockUi>, Serializable {

    @Autowired
    private XBEXUiManager manager;

    public XBEXUiService() {
        super();
    }

    public XBEXUiService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXUiManager(catalog);
        catalog.addCatalogManager(XBCXUiManager.class, (XBCXUiManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXBlockUi findById(long id) {
        return ((XBEXUiManager) itemManager).findById(id);
    }

    @Override
    public XBEXBlockUi findUiByPR(XBCBlockRev rev, long priority) {
        return ((XBEXUiManager) itemManager).findUiByPR(rev, priority);
    }

    @Override
    public XBEXPlugUi findPlugUiById(long id) {
        return ((XBEXUiManager) itemManager).findPlugUiById(id);
    }

    @Override
    public long getAllPlugUisCount() {
        return ((XBEXUiManager) itemManager).getAllPlugUisCount();
    }

    @Override
    public List<XBCXBlockUi> getUis(XBCBlockRev rev) {
        return ((XBEXUiManager) itemManager).getUis(rev);
    }

    @Override
    public long getUisCount(XBCBlockRev rev) {
        return ((XBEXUiManager) itemManager).getUisCount(rev);
    }

    @Override
    public XBEXPlugUi getPlugUi(XBCXPlugin plugin, long methodIndex) {
        return ((XBEXUiManager) itemManager).getPlugUi(plugin, methodIndex);
    }

    @Override
    public List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin) {
        return ((XBEXUiManager) itemManager).getPlugUis(plugin);
    }

    @Override
    public long getPlugUisCount(XBCXPlugin plugin) {
        return ((XBEXUiManager) itemManager).getPlugUisCount(plugin);
    }

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }
}
