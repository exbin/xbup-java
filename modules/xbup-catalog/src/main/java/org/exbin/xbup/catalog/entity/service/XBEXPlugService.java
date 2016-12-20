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
package org.exbin.xbup.catalog.entity.service;

import java.io.InputStream;
import java.io.Serializable;
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
 * @version 0.1.21 2011/12/31
 * @author ExBin Project (http://exbin.org)
 */
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
        catalog.addCatalogManager(XBCXPlugManager.class, itemManager);
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

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }
}
