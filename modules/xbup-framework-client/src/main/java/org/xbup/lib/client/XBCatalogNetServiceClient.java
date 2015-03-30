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
package org.xbup.lib.client;

import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.catalog.XBPCatalog;
import org.xbup.lib.core.serial.XBPSerialReader;

/**
 * XBService catalog client using IP networking.
 *
 * @version 0.1.25 2015/03/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBCatalogNetServiceClient extends XBTCPServiceClient implements XBCatalogServiceClient {

    public XBCatalogNetServiceClient(String host, int port) {
        super(host, port);
        XBPCatalog catalog = new XBPCatalog();
        catalog.addFormatDecl(getContextFormatDecl());
        catalog.generateContext();
        setCatalog(catalog);
    }

    /**
     * Returns local format declaration when catalog or service is not
     * available.
     *
     * @return local format declaration
     */
    private XBLFormatDecl getContextFormatDecl() {
        XBPSerialReader reader = new XBPSerialReader(ClassLoader.class.getResourceAsStream("/org/xbup/lib/client/resources/catalog_service_format.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        reader.read(formatDecl);
        return formatDecl;
    }
}
