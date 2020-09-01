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

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.client.catalog.remote.XBRXFile;
import org.exbin.xbup.client.catalog.remote.XBRXPlugin;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * RPC stub class for XBRXPlugin catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPXPlugStub extends XBPBaseStub<XBRXPlugin> {

    public static long[] OWNER_PLUGIN_PROCEDURE = {0, 2, 14, 0, 0};
    public static long[] FILE_PLUGIN_PROCEDURE = {0, 2, 14, 1, 0};
    public static long[] INDEX_PLUGIN_PROCEDURE = {0, 2, 14, 2, 0};

    private final XBCatalogServiceClient client;

    public XBPXPlugStub(XBCatalogServiceClient client) {
        super(client, XBRXPlugin::new, null);
        this.client = client;
    }

    public XBRNode getOwner(long pluginId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(OWNER_PLUGIN_PROCEDURE), pluginId);
        return index == null ? null : new XBRNode(client, index);
    }

    public XBCXFile getPluginFile(long pluginId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(FILE_PLUGIN_PROCEDURE), pluginId);
        return index == null ? null : new XBRXFile(client, index);
    }

    public Long getPluginIndex(long pluginId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(INDEX_PLUGIN_PROCEDURE), pluginId);
    }
}
