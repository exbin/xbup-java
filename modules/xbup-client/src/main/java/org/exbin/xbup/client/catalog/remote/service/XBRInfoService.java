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
package org.exbin.xbup.client.catalog.remote.service;

import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXItemInfo;
import org.exbin.xbup.client.catalog.remote.manager.XBRInfoManager;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCXInfoManager;
import org.exbin.xbup.core.catalog.base.service.XBCXInfoService;

/**
 * Remote service for XBRXItemInfo items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRInfoService extends XBRDefaultService<XBRXItemInfo> implements XBCXInfoService<XBRXItemInfo> {

    public XBRInfoService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRInfoManager(catalog);
        catalog.addCatalogManager(XBCXInfoManager.class, (XBCXInfoManager) itemManager);
    }

    @Override
    public XBRXItemInfo getNodeInfo(XBCNode dir) {
        return ((XBRInfoManager) itemManager).getNodeInfo(dir);
    }
}
