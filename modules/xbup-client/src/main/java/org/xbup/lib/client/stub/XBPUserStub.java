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
package org.xbup.lib.client.stub;

import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRXItemInfo;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCNode;

/**
 * RPC Stub for Info extension related operations.
 *
 * @version 0.1.25 2015/03/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBPUserStub extends XBPBaseStub<XBRXItemInfo> {

    public static long[] NODE_INFO_PROCEDURE = {0, 2, 11, 0, 0};
    public static long[] INFOSCOUNT_INFO_PROCEDURE = {0, 2, 11, 1, 0};
    public static long[] FILENAME_INFO_PROCEDURE = {0, 2, 12, 2, 0};
    public static long[] PATH_INFO_PROCEDURE = {0, 2, 12, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPUserStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXItemInfo>() {
            @Override
            public XBRXItemInfo itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXItemInfo(client, itemId);
            }
        }, null);
        this.client = client;
    }

    public XBRXItemInfo getNodeInfo(XBCNode node) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(NODE_INFO_PROCEDURE), node.getId());
        return index == null ? null : new XBRXItemInfo(client, index);
    }
}
