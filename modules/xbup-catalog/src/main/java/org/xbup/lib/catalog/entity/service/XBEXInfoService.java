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
package org.xbup.lib.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.manager.XBCXInfoManager;
import org.xbup.lib.core.catalog.base.service.XBCXInfoService;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEXItemInfo;
import org.xbup.lib.catalog.entity.manager.XBEXInfoManager;

/**
 * Interface forXBEXItemInfoo items service.
 *
 * @version 0.1.21 2012/01/01
 * @author XBUP Project (http://xbup.org)
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
        catalog.addCatalogManager(XBCXInfoManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXItemInfo getNodeInfo(XBCNode dir) {
        return ((XBEXInfoManager)itemManager).getNodeInfo(dir);
    }
}