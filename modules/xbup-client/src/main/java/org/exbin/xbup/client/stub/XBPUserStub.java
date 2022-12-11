/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.client.catalog.remote.XBRXItemInfo;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXItemInfo;

/**
 * RPC Stub for Info extension related operations.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPUserStub extends XBPBaseStub<XBCXItemInfo> {

    public static long[] NODE_INFO_PROCEDURE = {0, 2, 11, 0, 0};
    public static long[] INFOSCOUNT_INFO_PROCEDURE = {0, 2, 11, 1, 0};
    public static long[] FILENAME_INFO_PROCEDURE = {0, 2, 12, 2, 0};
    public static long[] PATH_INFO_PROCEDURE = {0, 2, 12, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPUserStub(XBCatalogServiceClient client) {
        super(client, XBRXItemInfo::new, null);
        this.client = client;
    }

    public XBRXItemInfo getNodeInfo(XBCNode node) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(NODE_INFO_PROCEDURE), node.getId());
        return index == null ? null : new XBRXItemInfo(client, index);
    }
}
