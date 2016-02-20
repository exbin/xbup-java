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
package org.xbup.lib.client.catalog.remote.manager;

import java.util.List;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXBlockPane;
import org.xbup.lib.core.catalog.base.XBCXPlugPane;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.manager.XBCXPaneManager;
import org.xbup.lib.client.catalog.remote.XBRXBlockPane;
import org.xbup.lib.client.catalog.remote.XBRXPlugPane;
import org.xbup.lib.client.stub.XBPXPaneStub;

/**
 * Remote manager class for XBRXBlockPane catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXPaneManager extends XBRDefaultManager<XBRXBlockPane> implements XBCXPaneManager<XBRXBlockPane> {

    private final XBPXPaneStub paneStub;

    public XBRXPaneManager(XBRCatalog catalog) {
        super(catalog);
        paneStub = new XBPXPaneStub(client);
        setManagerStub(paneStub);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Panel Extension";
    }

    @Override
    public List<XBCXBlockPane> getPanes(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPanesCount(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        return paneStub.findPaneByPR(rev, priority);
    }

    @Override
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugPanesCount(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugPane getPlugPane(XBCXPlugin plugin, long paneIndex) {
        return getPlugPane(plugin, paneIndex);
    }

    @Override
    public Long getAllPlugPanesCount() {
        return paneStub.getAllPlugPanesCount();
    }

    @Override
    public XBRXBlockPane findById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugPane findPlugPaneById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}