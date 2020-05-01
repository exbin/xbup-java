/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client;

import java.io.IOException;
import java.net.Socket;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.xbup.core.remote.XBServiceClient;

/**
 * Connection client handler for remote catalogs.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCatalogServiceClient extends XBServiceClient {

    /**
     * Logins to catalog service.
     *
     * @param user user name
     * @param password password
     * @return TODO: 1 if successful, 0 if fails
     * @throws IOException if input/output error
     */
    int login(@Nonnull String user, @Nonnull char[] password) throws IOException;

    /**
     * Returns version of service.
     *
     * @return version
     */
    @Nonnull
    String getVersion();

    /**
     * Closes connection to service.
     */
    void close();

    /**
     * Performs ping to remote service.
     *
     * @return true if sucessful
     */
    boolean ping();

    /**
     * Returns service host string.
     *
     * @return service host string
     */
    @Nullable
    String getHost();

    /**
     * Returns port number.
     *
     * @return port number
     */
    int getPort();

    /**
     * Returns local address.
     *
     * @return local address
     */
    @Nullable
    String getLocalAddress();

    /**
     * Returns host address.
     *
     * @return host address
     */
    @Nullable
    String getHostAddress();

    /**
     * Performs validation of the connection.
     *
     * @return true if successful
     */
    boolean validate();

    /**
     * Returns socket.
     *
     * @return socket
     */
    @Nullable
    Socket getSocket();
}
