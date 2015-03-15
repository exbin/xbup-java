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

import java.util.List;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRItemInfo;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXItemInfo;

/**
 * RPC Stub for Info extension related operations.
 *
 * @version 0.1.25 2015/03/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBPUserStub implements XBPManagerStub<XBCXItemInfo> {

    public static long[] NODE_INFO_PROCEDURE = {0, 2, 11, 0, 0};
    public static long[] INFOSCOUNT_INFO_PROCEDURE = {0, 2, 11, 1, 0};
    public static long[] FILENAME_INFO_PROCEDURE = {0, 2, 12, 2, 0};
    public static long[] PATH_INFO_PROCEDURE = {0, 2, 12, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPUserStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItemInfo getNodeInfo(XBCNode node) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(NODE_INFO_PROCEDURE), node.getId());
        return index == null ? null : new XBRItemInfo(client, index);
    }

    @Override
    public XBCXItemInfo createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBCXItemInfo item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBCXItemInfo item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXItemInfo getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCXItemInfo> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(INFOSCOUNT_INFO_PROCEDURE));
    }
}
