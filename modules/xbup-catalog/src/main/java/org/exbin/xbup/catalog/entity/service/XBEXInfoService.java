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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXItemInfo;
import org.exbin.xbup.catalog.entity.manager.XBEXInfoManager;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCXInfoManager;
import org.exbin.xbup.core.catalog.base.service.XBCXInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface forXBEXItemInfoo items service.
 *
 * @version 0.1.21 2012/01/01
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEXInfoService extends XBEDefaultService<XBEXItemInfo> implements XBCXInfoService<XBEXItemInfo>, Serializable {

    @Autowired
    private XBEXInfoManager manager;

    public XBEXInfoService() {
        super();
    }

    public XBEXInfoService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXInfoManager(catalog);
        catalog.addCatalogManager(XBCXInfoManager.class, (XBCXInfoManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXItemInfo getNodeInfo(XBCNode dir) {
        return ((XBEXInfoManager) itemManager).getNodeInfo(dir);
    }
}
