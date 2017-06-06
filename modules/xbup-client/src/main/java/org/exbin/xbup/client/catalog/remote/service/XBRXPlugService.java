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
package org.exbin.xbup.client.catalog.remote.service;

import java.io.InputStream;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXPlugin;
import org.exbin.xbup.client.catalog.remote.manager.XBRXPlugManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPlugManager;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;

/**
 * Remote service for XBRXPlugin items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXPlugService extends XBRDefaultService<XBRXPlugin> implements XBCXPlugService<XBRXPlugin> {

    public XBRXPlugService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXPlugManager(catalog);
        catalog.addCatalogManager(XBCXPlugManager.class, (XBCXPlugManager) itemManager);
    }

    @Override
    public XBRXPlugin findById(long id) {
        return ((XBRXPlugManager) itemManager).findById(id);
    }

    @Override
    public XBRXPlugin findPlugin(XBCNode node, Long index) {
        return ((XBRXPlugManager) itemManager).findPlugin(node, index);
    }

    @Override
    public Long getAllPluginCount() {
        return ((XBRXPlugManager) itemManager).getAllPluginCount();
    }

    @Override
    public InputStream getPlugin(XBCXPlugin plugin) {
        return ((XBRXPlugManager) itemManager).getPlugin(plugin);
    }

    @Override
    public Long[] getPluginXBPath(XBCXPlugin plugin) {
        return ((XBRXPlugManager) itemManager).getPluginXBPath(plugin);
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
