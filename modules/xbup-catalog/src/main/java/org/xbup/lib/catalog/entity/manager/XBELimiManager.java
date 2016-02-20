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
package org.xbup.lib.catalog.entity.manager;

import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.manager.XBCLimiManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEItemLimi;

/**
 * XBUP catalog limitation manager.
 *
 * @version 0.1.21 2011/12/29
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBELimiManager extends XBEDefaultCatalogManager<XBEItemLimi> implements XBCLimiManager<XBEItemLimi>, Serializable {

    public XBELimiManager() {
        super();
    }
    
    public XBELimiManager(XBECatalog catalog) {
        super(catalog);
    }
}
