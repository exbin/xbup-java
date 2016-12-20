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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBETran;
import org.exbin.xbup.catalog.entity.manager.XBETranManager;
import org.exbin.xbup.core.catalog.base.manager.XBCTranManager;
import org.exbin.xbup.core.catalog.base.service.XBCTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBETran items service.
 *
 * @version 0.1.21 2012/01/01
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBETranService extends XBEDefaultService<XBETran> implements XBCTranService<XBETran>, Serializable {

    @Autowired
    private XBETranManager manager;

    public XBETranService() {
        super();
    }

    public XBETranService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBETranManager(catalog);
        catalog.addCatalogManager(XBCTranManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }
}
