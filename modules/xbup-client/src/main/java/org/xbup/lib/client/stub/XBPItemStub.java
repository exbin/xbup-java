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
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;

/**
 * RPC stub class for XBRItem catalog items.
 *
 * @version 0.1.25 2015/03/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBPItemStub extends XBPBaseStub<XBRItem> {

    public static long[] OWNER_ITEM_PROCEDURE = {0, 2, 3, 0, 0};
    public static long[] XBINDEX_ITEM_PROCEDURE = {0, 2, 3, 1, 0};
    public static long[] ITEMSCOUNT_ITEM_PROCEDURE = {0, 2, 3, 2, 0};

    private final XBCatalogServiceClient client;

    public XBPItemStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRItem>() {
            @Override
            public XBRItem itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRItem(client, itemId);
            }
        }, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(ITEMSCOUNT_ITEM_PROCEDURE)));
        this.client = client;
    }

    public XBRItem getParent(Long itemId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(OWNER_ITEM_PROCEDURE), itemId);
        return index == null ? null : constructItem(index);
    }

    public Long getXBIndex(Long itemId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(XBINDEX_ITEM_PROCEDURE), itemId);
    }
}
