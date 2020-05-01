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
import org.exbin.xbup.client.catalog.remote.XBRXBlockLine;
import org.exbin.xbup.client.catalog.remote.XBRXPlugLine;
import org.exbin.xbup.client.catalog.remote.XBRXPlugin;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * RPC stub class for XBRXBlockLine catalog items.
 *
 * @version 0.2.1 2020/04/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXLineStub extends XBPBaseStub<XBRXBlockLine> {

    public static long[] REV_LINE_PROCEDURE = {0, 2, 15, 0, 0};
    public static long[] PLUGIN_LINE_PROCEDURE = {0, 2, 15, 1, 0};
    public static long[] PRIORITY_LINE_PROCEDURE = {0, 2, 15, 2, 0};
    public static long[] LINESCOUNT_LINE_PROCEDURE = {0, 2, 15, 3, 0};
    public static long[] REVLINE_LINE_PROCEDURE = {0, 2, 15, 4, 0};
    public static long[] PLUGLINESCOUNT_LINE_PROCEDURE = {0, 2, 15, 5, 0};
    public static long[] PLUGLINE_LINE_PROCEDURE = {0, 2, 15, 6, 0};
    public static long[] LINEPLUGIN_PLUGIN_PROCEDURE = {0, 2, 14, 3, 0};
    public static long[] LINEINDEX_PLUGIN_PROCEDURE = {0, 2, 14, 4, 0};

    private final XBCatalogServiceClient client;

    public XBPXLineStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXBlockLine>() {
            @Override
            public XBRXBlockLine itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXBlockLine(client, itemId);
            }
        }, null);
        this.client = client;
    }

    public XBCXPlugin getPlugin(long lineId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(LINEPLUGIN_PLUGIN_PROCEDURE), lineId);
        return index == null ? null : new XBRXPlugin(client, index);
    }

    public Long getLineIndex(long lineId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(LINEINDEX_PLUGIN_PROCEDURE), lineId);
    }

    public XBCBlockRev getBlockRev(long blockLineId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(REV_LINE_PROCEDURE), blockLineId);
        return index == null ? null : new XBRBlockRev(client, index);
    }

    public XBCXPlugLine getLine(long blockLineId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGIN_LINE_PROCEDURE), blockLineId);
        return index == null ? null : new XBRXPlugLine(client, index);
    }

    public Long getPriority(long blockLineId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PRIORITY_LINE_PROCEDURE), blockLineId);
    }

    public Long getBlockLineCount(XBCBlockRev rev) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(LINESCOUNT_LINE_PROCEDURE), rev.getId());
    }

    public XBRXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGIN_LINE_PROCEDURE), rev.getId(), priority);
        return index == null ? null : new XBRXBlockLine(client, index);
    }

    public XBRXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGLINE_LINE_PROCEDURE), plugin.getId(), lineIndex);
        return index == null ? null : new XBRXPlugLine(client, index);
    }

    public Long getAllPlugLinesCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGLINESCOUNT_LINE_PROCEDURE));
    }
}
