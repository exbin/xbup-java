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
package org.xbup.lib.client.stub;

import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRBlockRev;
import org.xbup.lib.client.catalog.remote.XBRXBlockPane;
import org.xbup.lib.client.catalog.remote.XBRXPlugPane;
import org.xbup.lib.client.catalog.remote.XBRXPlugin;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXPlugPane;
import org.xbup.lib.core.catalog.base.XBCXPlugin;

/**
 * RPC stub class for XBRXBlockPane catalog items.
 *
 * @version 0.1.25 2015/03/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXPaneStub extends XBPBaseStub<XBRXBlockPane> {

    public static long[] REV_PANE_PROCEDURE = {0, 2, 16, 0, 0};
    public static long[] PLUGIN_PANE_PROCEDURE = {0, 2, 16, 1, 0};
    public static long[] PRIORITY_PANE_PROCEDURE = {0, 2, 16, 2, 0};
    public static long[] PANESCOUNT_PANE_PROCEDURE = {0, 2, 16, 3, 0};
    public static long[] REVPANE_PANE_PROCEDURE = {0, 2, 16, 4, 0};
    public static long[] PLUGPANESCOUNT_PANE_PROCEDURE = {0, 2, 16, 5, 0};
    public static long[] PLUGPANE_PANE_PROCEDURE = {0, 2, 16, 6, 0};
    public static long[] PANEPLUGIN_PLUGIN_PROCEDURE = {0, 2, 14, 5, 0};
    public static long[] PANEINDEX_PLUGIN_PROCEDURE = {0, 2, 14, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXPaneStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXBlockPane>() {
            @Override
            public XBRXBlockPane itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXBlockPane(client, itemId);
            }
        }, null);
        this.client = client;
    }

    public XBCXPlugin getPlugin(long lineId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PANEPLUGIN_PLUGIN_PROCEDURE), lineId);
        return index == null ? null : new XBRXPlugin(client, index);
    }

    public Long getPaneIndex(long lineId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PANEINDEX_PLUGIN_PROCEDURE), lineId);
    }

    public XBCBlockRev getBlockRev(long blockPaneId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(REV_PANE_PROCEDURE), blockPaneId);
        return index == null ? null : new XBRBlockRev(client, index);
    }

    public XBCXPlugPane getPane(long blockPaneId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGIN_PANE_PROCEDURE), blockPaneId);
        return index == null ? null : new XBRXPlugPane(client, index);
    }

    public Long getPriority(long blockPaneId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PRIORITY_PANE_PROCEDURE), blockPaneId);
    }

    public XBRXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGIN_PANE_PROCEDURE), rev.getId(), priority);
        return index == null ? null : new XBRXBlockPane(client, index);
    }

    public XBRXPlugPane getPlugPane(XBCXPlugin plugin, long lineIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGPANE_PANE_PROCEDURE), plugin.getId(), lineIndex);
        return index == null ? null : new XBRXPlugPane(client, index);
    }

    public Long getAllPlugPanesCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGPANESCOUNT_PANE_PROCEDURE));
    }
}
