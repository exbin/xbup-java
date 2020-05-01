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
package org.exbin.xbup.client.catalog.remote.manager;

import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXItemInfo;
import org.exbin.xbup.client.stub.XBPInfoStub;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCXInfoManager;

/**
 * Remote manager class for XBRXItemInfo catalog items.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBRInfoManager extends XBRDefaultManager<XBRXItemInfo> implements XBCXInfoManager<XBRXItemInfo> {

    private final XBPInfoStub infoStub;

    public XBRInfoManager(XBRCatalog catalog) {
        super(catalog);
        infoStub = new XBPInfoStub(client);
    }

    @Override
    public XBRXItemInfo getNodeInfo(XBCNode node) {
        return infoStub.getNodeInfo(node);
    }
}
