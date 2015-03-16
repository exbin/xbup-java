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

import java.io.IOException;
import java.net.Socket;
import org.xbup.lib.core.remote.XBServiceClient;

/**
 * Connection client handler for remote catalogs.
 *
 * @version 0.1.25 2015/03/16
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCatalogServiceClient extends XBServiceClient {

    /**
     * Logins to catalog service.
     *
     * @param user user name
     * @param password password
     * @return TODO: 1 if successful, 0 if fails
     * @throws IOException
     */
    public int login(String user, char[] password) throws IOException;

    /**
     * Returns version of service.
     *
     * @return
     */
    public String getVersion();

    /**
     * Closes connection to service.
     */
    public void close();

    /**
     * Performs ping to remote service.
     *
     * @return true if sucessful
     */
    public boolean ping();

    /**
     * Returns service host string.
     *
     * @return service host string
     */
    public String getHost();

    /**
     * Returns port number.
     *
     * @return port number
     */
    public int getPort();

    /**
     * Returns local address.
     *
     * @return local address
     */
    public String getLocalAddress();

    /**
     * Returns host address.
     *
     * @return host address
     */
    public String getHostAddress();

    /**
     * Performs validation of the connection.
     *
     * @return true if successful
     */
    public boolean validate();

    /**
     * Returns socket.
     *
     * @return socket
     */
    public Socket getSocket();
}
