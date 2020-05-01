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
import org.exbin.xbup.client.catalog.remote.XBRXBlockLine;
import org.exbin.xbup.client.catalog.remote.XBRXPlugLine;
import org.exbin.xbup.client.catalog.remote.manager.XBRXLineManager;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXLineManager;
import org.exbin.xbup.core.catalog.base.service.XBCXLineService;

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
        catalog.addCatalogManager(XBCXLineManager.class, (XBCXLineManager) itemManager);
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
