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
import org.exbin.xbup.catalog.entity.XBEXUser;
import org.exbin.xbup.catalog.entity.manager.XBEXUserManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXUserManager;
import org.exbin.xbup.core.catalog.base.service.XBCXUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXUser items service.
 *
 * @version 0.1.21 2014/03/16
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEXUserService extends XBEDefaultService<XBEXUser> implements XBCXUserService<XBEXUser>, Serializable {

    @Autowired
    private XBEXUserManager manager;

    public XBEXUserService() {
        super();
    }

    public XBEXUserService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXUserManager(catalog);
        catalog.addCatalogManager(XBCXUserManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    public XBEXUser findByLogin(String userLogin) {
        return manager.findByLogin(userLogin);
    }
}
