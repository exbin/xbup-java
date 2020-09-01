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

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.catalog.entity.manager.XBEXPlugManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPlugManager;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXPlugin items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXPlugService extends XBEDefaultService<XBEXPlugin> implements XBCXPlugService<XBEXPlugin>, Serializable {

    @Autowired
    private XBEXPlugManager manager;

    public XBEXPlugService() {
        super();
    }

    public XBEXPlugService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXPlugManager(catalog);
        catalog.addCatalogManager(XBCXPlugManager.class, (XBCXPlugManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXPlugin findById(long id) {
        return ((XBEXPlugManager) itemManager).findById(id);
    }

    @Override
    public XBEXPlugin findPlugin(XBCNode node, Long index) {
        return ((XBEXPlugManager) itemManager).findPlugin(node, index);
    }

    @Override
    public Long getAllPluginCount() {
        return ((XBEXPlugManager) itemManager).getAllPluginCount();
    }

    @Override
    public InputStream getPlugin(XBCXPlugin plugin) {
        return ((XBEXPlugManager) itemManager).getPlugin(plugin);
    }

    @Override
    public Long[] getPluginXBPath(XBCXPlugin plugin) {
        return ((XBEXPlugManager) itemManager).getPluginXBPath(plugin);
    }

    @Nonnull
    @Override
    public List<XBCXPlugin> findPluginsForNode(XBCNode node) {
        return ((XBEXPlugManager) itemManager).findPluginsForNode(node);
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
