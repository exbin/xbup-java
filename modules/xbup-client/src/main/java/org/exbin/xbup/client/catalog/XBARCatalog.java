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
package org.exbin.xbup.client.catalog;

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.service.XBRLimiService;
import org.exbin.xbup.client.catalog.remote.service.XBRTranService;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.service.XBCLimiService;
import org.exbin.xbup.core.catalog.base.service.XBCTranService;

/**
 * XBUP level 2 remote catalog.
 *
 * @version 0.1.21 2012/01/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBARCatalog extends XBRCatalog implements XBACatalog {

    /**
     * Creates a new instance of XBARCatalog.
     * 
     * @param client connection client
     */
    public XBARCatalog(XBCatalogServiceClient client) {
        super(client);
        catalogServices.put(XBCLimiService.class, new XBRLimiService(this));
        catalogServices.put(XBCTranService.class, new XBRTranService(this));
    }
}
