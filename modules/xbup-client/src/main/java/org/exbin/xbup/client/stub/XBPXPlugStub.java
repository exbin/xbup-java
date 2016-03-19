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
package org.exbin.xbup.client.stub;

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.client.catalog.remote.XBRXFile;
import org.exbin.xbup.client.catalog.remote.XBRXPlugin;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * RPC stub class for XBRXPlugin catalog items.
 *
 * @version 0.1.25 2015/03/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXPlugStub extends XBPBaseStub<XBRXPlugin> {

    public static long[] OWNER_PLUGIN_PROCEDURE = {0, 2, 14, 0, 0};
    public static long[] FILE_PLUGIN_PROCEDURE = {0, 2, 14, 1, 0};
    public static long[] INDEX_PLUGIN_PROCEDURE = {0, 2, 14, 2, 0};

    private final XBCatalogServiceClient client;

    public XBPXPlugStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXPlugin>() {
            @Override
            public XBRXPlugin itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXPlugin(client, itemId);
            }
        }, null);
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
