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
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXBlockPane;
import org.exbin.xbup.client.catalog.remote.XBRXPlugPane;
import org.exbin.xbup.client.catalog.remote.manager.XBRXPaneManager;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPaneManager;
import org.exbin.xbup.core.catalog.base.service.XBCXPaneService;

/**
 * Remote service for XBRXBlockPane items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXPaneService extends XBRDefaultService<XBRXBlockPane> implements XBCXPaneService<XBRXBlockPane> {

    public XBRXPaneService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXPaneManager(catalog);
        catalog.addCatalogManager(XBCXPaneManager.class, (XBCXPaneManager) itemManager);
    }

    @Override
    public XBRXBlockPane findById(long id) {
        return ((XBRXPaneManager) itemManager).findById(id);
    }

    @Override
    public XBRXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        return ((XBRXPaneManager) itemManager).findPaneByPR(rev, priority);
    }

    @Override
    public XBRXPlugPane findPlugPaneById(long id) {
        return ((XBRXPaneManager) itemManager).findPlugPaneById(id);
    }

    @Override
    public Long getAllPlugPanesCount() {
        return ((XBRXPaneManager) itemManager).getAllPlugPanesCount();
    }

    @Override
    public List<XBCXBlockPane> getPanes(XBCBlockRev rev) {
        return ((XBRXPaneManager) itemManager).getPanes(rev);
    }

    @Override
    public long getPanesCount(XBCBlockRev rev) {
        return ((XBRXPaneManager) itemManager).getPanesCount(rev);
    }

    @Override
    public XBRXPlugPane getPlugPane(XBCXPlugin plugin, long pane) {
        return ((XBRXPaneManager) itemManager).getPlugPane(plugin, pane);
    }

    @Override
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin) {
        return ((XBRXPaneManager) itemManager).getPlugPanes(plugin);
    }

    @Override
    public long getPlugPanesCount(XBCXPlugin plugin) {
        return ((XBRXPaneManager) itemManager).getPlugPanesCount(plugin);
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
