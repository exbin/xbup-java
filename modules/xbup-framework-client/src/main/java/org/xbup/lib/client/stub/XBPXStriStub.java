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
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXStri;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCItem;

/**
 * RPC stub class for XBRXStri catalog items.
 *
 * @version 0.1.25 2015/03/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXStriStub extends XBPBaseStub<XBRXStri> {

    public static long[] ITEM_STRI_PROCEDURE = {0, 2, 14, 0, 0};
    public static long[] TEXT_STRI_PROCEDURE = {0, 2, 14, 1, 0};
    public static long[] NODEPATH_STRI_PROCEDURE = {0, 2, 14, 2, 0};
    public static long[] ITEMSTRI_STRI_PROCEDURE = {0, 2, 14, 3, 0};
    public static long[] STRISCOUNT_STRI_PROCEDURE = {0, 2, 14, 4, 0};

    private final XBCatalogServiceClient client;

    public XBPXStriStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXStri>() {
            @Override
            public XBRXStri itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXStri(client, itemId);
            }
        }, null);
        this.client = client;
    }

    public XBCItem getStriItem(long striId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ITEM_STRI_PROCEDURE), striId);
        return index == null ? null : new XBRItem(client, index);
    }

    public String getText(long striId) {
        return XBPStubUtils.longToStringMethod(client.procedureCall(), new XBDeclBlockType(TEXT_STRI_PROCEDURE), striId);
    }

    public String getNodePath(long striId) {
        return XBPStubUtils.longToStringMethod(client.procedureCall(), new XBDeclBlockType(NODEPATH_STRI_PROCEDURE), striId);
    }

    public XBRXStri getItemStringId(XBCItem item) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ITEMSTRI_STRI_PROCEDURE), item.getId());
        return index == null ? null : new XBRXStri(client, index);
    }
}
