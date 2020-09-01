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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRBlockRev;
import org.exbin.xbup.client.catalog.remote.XBRXBlockUi;
import org.exbin.xbup.client.catalog.remote.XBRXPlugUi;
import org.exbin.xbup.client.catalog.remote.XBRXPlugin;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * RPC stub class for XBRXBlockUi catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPXUiStub extends XBPBaseStub<XBRXBlockUi> {

    // Previously line 15, pane 16
    public static long[] REV_UI_PROCEDURE = {0, 2, 18, 0, 0};
    public static long[] PLUGIN_UI_PROCEDURE = {0, 2, 18, 1, 0};
    public static long[] PRIORITY_UI_PROCEDURE = {0, 2, 18, 2, 0};
    public static long[] UISCOUNT_UI_PROCEDURE = {0, 2, 18, 3, 0};
    public static long[] REVUI_UI_PROCEDURE = {0, 2, 18, 4, 0};
    public static long[] PLUGUISCOUNT_UI_PROCEDURE = {0, 2, 18, 5, 0};
    public static long[] PLUGUI_UI_PROCEDURE = {0, 2, 18, 6, 0};
    public static long[] UIPLUGIN_PLUGIN_PROCEDURE = {0, 2, 14, 7, 0};
    public static long[] METHODINDEX_PLUGIN_PROCEDURE = {0, 2, 14, 8, 0};
    public static long[] UITYPE_PLUGIN_PROCEDURE = {0, 2, 14, 9, 0};

    private final XBCatalogServiceClient client;

    public XBPXUiStub(XBCatalogServiceClient client) {
        super(client, XBRXBlockUi::new, null);
        this.client = client;
    }

    public XBCXPlugin getPlugin(long uiId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(UIPLUGIN_PLUGIN_PROCEDURE), uiId);
        return index == null ? null : new XBRXPlugin(client, index);
    }

    public long getMethodIndex(long uiId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(METHODINDEX_PLUGIN_PROCEDURE), uiId);
    }

    public XBCXPlugUiType getUiType(long uiId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(UITYPE_PLUGIN_PROCEDURE), uiId);
        if (index == null) {
            return null;
        }
        XBCXPlugUiType result = new XBCXPlugUiType() {
            @Override
            public String getName() {
                return XBPlugUiType.findByDbIndex((int) (long) index).getName();
            }

            @Override
            public long getId() {
                return index;
            }
        };
        return result;
    }

    public XBCBlockRev getBlockRev(long blockUiId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(REV_UI_PROCEDURE), blockUiId);
        return index == null ? null : new XBRBlockRev(client, index);
    }

    public XBCXPlugUi getUi(long blockUiId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGIN_UI_PROCEDURE), blockUiId);
        return index == null ? null : new XBRXPlugUi(client, index);
    }

    public Long getPriority(long blockUiId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(PRIORITY_UI_PROCEDURE), blockUiId);
    }

    public Long getBlockUiCount(XBCBlockRev rev) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(UISCOUNT_UI_PROCEDURE), rev.getId());
    }

    public XBRXBlockUi findUiByPR(XBCBlockRev rev, XBPlugUiType type, long priority) {
        Long index = XBPStubUtils.threeLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGIN_UI_PROCEDURE), rev.getId(), (long) type.getDbIndex(), priority);
        return index == null ? null : new XBRXBlockUi(client, index);
    }

    @Nullable
    public XBCXPlugUi getPlugUi(XBCXPlugin plugin, XBPlugUiType type, long methodIndex) {
        Long index = XBPStubUtils.threeLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGUI_UI_PROCEDURE), plugin.getId(), (long) type.getDbIndex(), methodIndex);
        return index == null ? null : new XBRXPlugUi(client, index);
    }

    public long getAllPlugUisCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(PLUGUISCOUNT_UI_PROCEDURE));
    }

    public String getName(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
