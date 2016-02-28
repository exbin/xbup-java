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
package org.xbup.lib.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.core.catalog.base.manager.XBCLimiManager;
import org.xbup.lib.core.catalog.base.service.XBCLimiService;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEItemLimi;
import org.xbup.lib.catalog.entity.manager.XBELimiManager;

/**
 * Interface for XBEItemLimi items service.
 *
 * @version 0.1.21 2012/01/01
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBELimiService extends XBEDefaultService<XBEItemLimi> implements XBCLimiService<XBEItemLimi>, Serializable {

    @Autowired
    private XBELimiManager manager;

    public XBELimiService() {
        super();
    }

    public XBELimiService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBELimiManager(catalog);
        catalog.addCatalogManager(XBCLimiManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }
}
