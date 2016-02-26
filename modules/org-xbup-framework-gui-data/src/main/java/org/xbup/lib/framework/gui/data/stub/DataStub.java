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
package org.xbup.lib.framework.gui.data.stub;

import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRXItemInfo;
import org.xbup.lib.core.catalog.base.XBCNode;

/**
 * RPC Stub for data operations.
 *
 * @version 0.2.0 2016/02/26
 * @author XBUP Project (http://xbup.org)
 */
public class DataStub {

    // public static long[] NODE_INFO_PROCEDURE = {0, 2, 11, 0, 0};

    private final XBCatalogServiceClient client;

    public DataStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRXItemInfo getNodeInfo(XBCNode node) {
//        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(NODE_INFO_PROCEDURE), node.getId());
//        return index == null ? null : new XBRXItemInfo(client, index);
        return null;
    }
    
    // getSchema()
    // getItems()
}
