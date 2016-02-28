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
import org.xbup.lib.core.catalog.base.XBCXBlockLine;
import org.xbup.lib.core.catalog.base.XBCXPlugLine;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.manager.XBCXLineManager;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.client.catalog.remote.XBRXBlockLine;
import org.xbup.lib.client.catalog.remote.XBRXPlugLine;
import org.xbup.lib.client.catalog.remote.manager.XBRXLineManager;

/**
 * Remote service for XBRXBlockLine items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXLineService extends XBRDefaultService<XBRXBlockLine> implements XBCXLineService<XBRXBlockLine> {

    public XBRXLineService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXLineManager(catalog);
        catalog.addCatalogManager(XBCXLineManager.class, itemManager);
    }

    @Override
    public XBRXBlockLine findById(long id) {
        return ((XBRXLineManager) itemManager).findById(id);
    }

    @Override
    public XBRXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        return ((XBRXLineManager) itemManager).findLineByPR(rev, priority);
    }

    @Override
    public XBRXPlugLine findPlugLineById(long id) {
        return ((XBRXLineManager) itemManager).findPlugLineById(id);
    }

    @Override
    public Long getAllPlugLinesCount() {
        return ((XBRXLineManager) itemManager).getAllPlugLinesCount();
    }

    @Override
    public List<XBCXBlockLine> getLines(XBCBlockRev rev) {
        return ((XBRXLineManager) itemManager).getLines(rev);
    }

    @Override
    public long getLinesCount(XBCBlockRev rev) {
        return ((XBRXLineManager) itemManager).getLinesCount(rev);
    }

    @Override
    public XBRXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        return ((XBRXLineManager) itemManager).getPlugLine(plugin, lineIndex);
    }

    @Override
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin) {
        return ((XBRXLineManager) itemManager).getPlugLines(plugin);
    }

    @Override
    public long getPlugLinesCount(XBCXPlugin plugin) {
        return ((XBRXLineManager) itemManager).getPlugLinesCount(plugin);
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
