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
import org.exbin.xbup.client.catalog.remote.XBRXFile;
import org.exbin.xbup.client.catalog.remote.XBRXHDoc;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * RPC stub class for XBRXHDoc catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPXHDocStub extends XBPBaseStub<XBRXHDoc> {

    public static long[] OWNER_HDOC_PROCEDURE = {0, 2, 17, 0, 0};
    public static long[] ITEM_HDOC_PROCEDURE = {0, 2, 17, 1, 0};
    public static long[] FILE_HDOC_PROCEDURE = {0, 2, 17, 2, 0};

    private final XBCatalogServiceClient client;

    public XBPXHDocStub(XBCatalogServiceClient client) {
        super(client, XBRXHDoc::new, null);
        this.client = client;
    }

    public XBRItem getHDocItem(long hdocId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(OWNER_HDOC_PROCEDURE), hdocId);
        return index == null ? null : new XBRItem(client, index);
    }

    public XBCXFile getDocFile(long hdocId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(FILE_HDOC_PROCEDURE), hdocId);
        return index == null ? null : new XBRXFile(client, index);
    }

    public XBRXHDoc getItemHDoc(long itemId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ITEM_HDOC_PROCEDURE), itemId);
        return index == null ? null : new XBRXHDoc(client, index);
    }
}
