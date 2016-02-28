/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.client.catalog.remote.service;

import java.util.List;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXBlockPane;
import org.xbup.lib.core.catalog.base.XBCXPlugPane;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.manager.XBCXPaneManager;
import org.xbup.lib.core.catalog.base.service.XBCXPaneService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.client.catalog.remote.XBRXBlockPane;
import org.xbup.lib.client.catalog.remote.XBRXPlugPane;
import org.xbup.lib.client.catalog.remote.manager.XBRXPaneManager;

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
        catalog.addCatalogManager(XBCXPaneManager.class, itemManager);
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
