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
import org.exbin.xbup.client.catalog.remote.XBRItem;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;

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
