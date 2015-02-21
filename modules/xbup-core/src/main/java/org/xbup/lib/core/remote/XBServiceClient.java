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
package org.xbup.lib.core.remote;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * XBService client interface.
 *
 * @version 0.1.21 2012/04/18
 * @author XBUP Project (http://xbup.org)
 */
public interface XBServiceClient {

    public static long[] XBSERVICE_FORMAT = {0, 2, 0, 0};

    public static long[] LOGIN_SERVICE_PROCEDURE = {0, 2, 0, 0, 0};
    public static long[] STOP_SERVICE_PROCEDURE = {0, 2, 0, 1, 0};
    public static long[] RESTART_SERVICE_PROCEDURE = {0, 2, 0, 2, 0};
    public static long[] PING_SERVICE_PROCEDURE = {0, 2, 0, 3, 0};

    public static long[] VERSION_INFO_PROCEDURE = {0, 2, 1, 0, 0};

    /**
     * Performs login to the server.
     *
     * @param user user name
     * @param password password
     * @return
     * @throws IOException
     */
    public int login(String user, char[] password) throws IOException;

    public void init() throws IOException, ConnectException;

    public String getVersion();

    public void close();

    public void ping();

    public String getHost();

    public int getPort();

    public String getLocalAddress();

    public String getHostAddress();

    public boolean validate();

    public Socket getSocket();
}
