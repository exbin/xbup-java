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
package org.exbin.xbup.client;

import java.io.IOException;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.catalog.XBPCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBPSerialReader;

/**
 * XBService catalog client using IP networking.
 *
 * @version 0.2.0 2015/10/30
 * @author ExBin Project (http://exbin.org)
 */
public class XBCatalogNetServiceClient extends XBTCPServiceClient implements XBCatalogServiceClient {

    public XBCatalogNetServiceClient(String host, int port) {
        super(host, port);
        init();
    }

    private void init() {
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
        XBPSerialReader reader = new XBPSerialReader(ClassLoader.class.getResourceAsStream("/org/exbin/xbup/client/resources/catalog_service_format.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        try {
            reader.read(formatDecl);
        } catch (XBProcessingException | IOException ex) {
            return null;
        }
        return formatDecl;
    }
}
