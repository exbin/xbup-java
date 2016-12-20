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
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXBlockLine;
import org.exbin.xbup.catalog.entity.XBEXPlugLine;
import org.exbin.xbup.catalog.entity.manager.XBEXLineManager;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXLineManager;
import org.exbin.xbup.core.catalog.base.service.XBCXLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXBlockLine items service.
 *
 * @version 0.1.21 2011/12/31
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEXLineService extends XBEDefaultService<XBEXBlockLine> implements XBCXLineService<XBEXBlockLine>, Serializable {

    @Autowired
    private XBEXLineManager manager;

    public XBEXLineService() {
        super();
    }

    public XBEXLineService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXLineManager(catalog);
        catalog.addCatalogManager(XBCXLineManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXBlockLine findById(long id) {
        return ((XBEXLineManager) itemManager).findById(id);
    }

    @Override
    public XBEXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        return ((XBEXLineManager) itemManager).findLineByPR(rev, priority);
    }

    @Override
    public XBEXPlugLine findPlugLineById(long id) {
        return ((XBEXLineManager) itemManager).findPlugLineById(id);
    }

    @Override
    public Long getAllPlugLinesCount() {
        return ((XBEXLineManager) itemManager).getAllPlugLinesCount();
    }

    @Override
    public List<XBCXBlockLine> getLines(XBCBlockRev rev) {
        return ((XBEXLineManager) itemManager).getLines(rev);
    }

    @Override
    public long getLinesCount(XBCBlockRev rev) {
        return ((XBEXLineManager) itemManager).getLinesCount(rev);
    }

    @Override
    public XBEXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        return ((XBEXLineManager) itemManager).getPlugLine(plugin, lineIndex);
    }

    @Override
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin) {
        return ((XBEXLineManager) itemManager).getPlugLines(plugin);
    }

    @Override
    public long getPlugLinesCount(XBCXPlugin plugin) {
        return ((XBEXLineManager) itemManager).getPlugLinesCount(plugin);
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
