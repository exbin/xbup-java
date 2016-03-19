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
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXBlockLine;
import org.exbin.xbup.client.catalog.remote.XBRXPlugLine;
import org.exbin.xbup.client.stub.XBPXLineStub;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXLineManager;

/**
 * Remote manager class for XBRXBlockLine catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXLineManager extends XBRDefaultManager<XBRXBlockLine> implements XBCXLineManager<XBRXBlockLine> {

    private final XBPXLineStub lineStub;

    public XBRXLineManager(XBRCatalog catalog) {
        super(catalog);
        lineStub = new XBPXLineStub(client);
        setManagerStub(lineStub);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Line Extension";
    }

    @Override
    public List<XBCXBlockLine> getLines(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLinesCount(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        return lineStub.findLineByPR(rev, priority);
    }

    @Override
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugLinesCount(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        return lineStub.getPlugLine(plugin, lineIndex);
    }

    @Override
    public Long getAllPlugLinesCount() {
        return lineStub.getAllPlugLinesCount();
    }

    @Override
    public XBRXBlockLine findById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugLine findPlugLineById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
