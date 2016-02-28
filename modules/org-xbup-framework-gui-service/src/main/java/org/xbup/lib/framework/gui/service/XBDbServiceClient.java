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
package org.xbup.lib.framework.gui.service;

import java.io.IOException;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.convert.XBTDefaultMatchingProvider;
import org.xbup.lib.core.remote.XBCallHandler;

/**
 * Fake XBService client using localhost database.
 *
 * @version 0.2.0 2016/02/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBDbServiceClient implements XBCatalogServiceClient {

    private final EntityManagerFactory entityManagerFactory;
//    private XBL2CatalogHandler catalog;
    private XBTDefaultMatchingProvider source;
    private XBTListener target;

    /**
     * Performs login to the server
     *
     * @param user
     * @param password
     * @return
     * @throws java.io.IOException
     */
    @Override
    public int login(String user, char[] password) throws IOException {
        return 0;
    }

    public XBDbServiceClient(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
//        catalog = new XBL2RemoteCatalog(new XBCatalogNetServiceClient(host, port));
    }

    /*    public XBL2CatalogHandler getCatalog() {
     return catalog;
     } */
    @Override
    public String getVersion() {
        return "0.2.0";
    }

    public void stop() {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getLocalAddress() {
        return "localhost";
    }

    @Override
    public String getHostAddress() {
        return "localhost";
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Socket getSocket() {
        return null;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public XBCallHandler procedureCall() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
