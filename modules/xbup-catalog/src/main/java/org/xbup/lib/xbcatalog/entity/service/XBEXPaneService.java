/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.xbcatalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.xb.catalog.base.XBCBlockRev;
import org.xbup.lib.xb.catalog.base.XBCXBlockPane;
import org.xbup.lib.xb.catalog.base.XBCXPlugPane;
import org.xbup.lib.xb.catalog.base.XBCXPlugin;
import org.xbup.lib.xb.catalog.base.manager.XBCXPaneManager;
import org.xbup.lib.xb.catalog.base.service.XBCXPaneService;
import org.xbup.lib.xb.catalog.base.XBCExtension;
import org.xbup.lib.xbcatalog.XBECatalog;
import org.xbup.lib.xbcatalog.entity.XBEXBlockPane;
import org.xbup.lib.xbcatalog.entity.XBEXPlugPane;
import org.xbup.lib.xbcatalog.entity.manager.XBEXPaneManager;

/**
 * Interface for XBEXBlockPane items service.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
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
        return ((XBEXPaneManager)itemManager).findById(id);
    }

    @Override
    public XBEXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        return ((XBEXPaneManager)itemManager).findPaneByPR(rev, priority);
    }

    @Override
    public XBEXPlugPane findPlugPaneById(long id) {
        return ((XBEXPaneManager)itemManager).findPlugPaneById(id);
    }

    @Override
    public Long getAllPanesCount() {
        return ((XBEXPaneManager)itemManager).getAllPanesCount();
    }

    @Override
    public Long getAllPlugPanesCount() {
        return ((XBEXPaneManager)itemManager).getAllPlugPanesCount();
    }

    @Override
    public List<XBCXBlockPane> getPanes(XBCBlockRev rev) {
        return ((XBEXPaneManager)itemManager).getPanes(rev);
    }

    @Override
    public long getPanesCount(XBCBlockRev rev) {
        return ((XBEXPaneManager)itemManager).getPanesCount(rev);
    }

    @Override
    public XBEXPlugPane getPlugPane(XBCXPlugin plugin, long pane) {
        return ((XBEXPaneManager)itemManager).getPlugPane(plugin, pane);
    }

    @Override
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin) {
        return ((XBEXPaneManager)itemManager).getPlugPanes(plugin);
    }

    @Override
    public long getPlugPanesCount(XBCXPlugin plugin) {
        return ((XBEXPaneManager)itemManager).getPlugPanesCount(plugin);
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
