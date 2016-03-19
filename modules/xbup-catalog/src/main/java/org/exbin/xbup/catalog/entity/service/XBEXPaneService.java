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

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        catalog.addCatalogManager(XBCXPaneManager.class, itemManager);
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
