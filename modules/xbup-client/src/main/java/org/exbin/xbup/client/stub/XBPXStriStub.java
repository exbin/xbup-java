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
import org.exbin.xbup.client.catalog.remote.XBRItem;
import org.exbin.xbup.client.catalog.remote.XBRXStri;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * RPC stub class for XBRXStri catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPXStriStub extends XBPBaseStub<XBRXStri> {

    public static long[] ITEM_STRI_PROCEDURE = {0, 2, 14, 0, 0};
    public static long[] TEXT_STRI_PROCEDURE = {0, 2, 14, 1, 0};
    public static long[] NODEPATH_STRI_PROCEDURE = {0, 2, 14, 2, 0};
    public static long[] ITEMSTRI_STRI_PROCEDURE = {0, 2, 14, 3, 0};
    public static long[] STRISCOUNT_STRI_PROCEDURE = {0, 2, 14, 4, 0};

    private final XBCatalogServiceClient client;

    public XBPXStriStub(XBCatalogServiceClient client) {
        super(client, XBRXStri::new, null);
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
