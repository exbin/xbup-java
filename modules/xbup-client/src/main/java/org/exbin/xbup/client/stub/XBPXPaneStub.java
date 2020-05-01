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
package org.exbin.xbup.client.stub;

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRBlockRev;
import org.exbin.xbup.client.catalog.remote.XBRXBlockPane;
import org.exbin.xbup.client.catalog.remote.XBRXPlugPane;
import org.exbin.xbup.client.catalog.remote.XBRXPlugin;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * RPC stub class for XBRXBlockPane catalog items.
 *
 * @version 0.2.1 2020/04/18
 * @author ExBin Project (http://exbin.org)
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

    public XBCBlockRev getBlockRev(long blockRevId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(REV_PANE_PROCEDURE), blockRevId);
        return index == null ? null : new XBRBlockRev(client, index);
    }

    public XBCXPlugPane getPane(long pluginPaneId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGIN_PANE_PROCEDURE), pluginPaneId);
        return index == null ? null : new XBRXPlugPane(client, index);
    }

    public Long getPriority(long blockPaneId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PRIORITY_PANE_PROCEDURE), blockPaneId);
    }

    public Long getBlockPanesCount(XBCBlockRev rev) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PANESCOUNT_PANE_PROCEDURE), rev.getId());
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
