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
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXBlockPane;
import org.exbin.xbup.catalog.entity.XBEXPlugPane;
import org.exbin.xbup.catalog.entity.manager.XBEXPaneManager;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPaneManager;
import org.exbin.xbup.core.catalog.base.service.XBCXPaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXBlockPane items service.
 *
 * @version 0.1.21 2011/12/31
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEXPaneService extends XBEDefaultService<XBEXBlockPane> implements XBCXPaneService<XBEXBlockPane>, Serializable {

    @Autowired
    private XBEXPaneManager manager;

    public XBEXPaneService() {
        super();
    }

    public XBEXPaneService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXPaneManager(catalog);
        catalog.addCatalogManager(XBCXPaneManager.class, (XBCXPaneManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXBlockPane findById(long id) {
        return ((XBEXPaneManager) itemManager).findById(id);
    }

    @Override
    public XBEXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        return ((XBEXPaneManager) itemManager).findPaneByPR(rev, priority);
    }

    @Override
    public XBEXPlugPane findPlugPaneById(long id) {
        return ((XBEXPaneManager) itemManager).findPlugPaneById(id);
    }

    @Override
    public Long getAllPlugPanesCount() {
        return ((XBEXPaneManager) itemManager).getAllPlugPanesCount();
    }

    @Override
    public List<XBCXBlockPane> getPanes(XBCBlockRev rev) {
        return ((XBEXPaneManager) itemManager).getPanes(rev);
    }

    @Override
    public long getPanesCount(XBCBlockRev rev) {
        return ((XBEXPaneManager) itemManager).getPanesCount(rev);
    }

    @Override
    public XBEXPlugPane getPlugPane(XBCXPlugin plugin, long pane) {
        return ((XBEXPaneManager) itemManager).getPlugPane(plugin, pane);
    }

    @Override
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin) {
        return ((XBEXPaneManager) itemManager).getPlugPanes(plugin);
    }

    @Override
    public long getPlugPanesCount(XBCXPlugin plugin) {
        return ((XBEXPaneManager) itemManager).getPlugPanesCount(plugin);
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
