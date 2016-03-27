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
package org.exbin.framework.client.api;

import org.exbin.framework.api.XBModuleRepositoryUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.api.XBApplicationModule;

/**
 * Interface for XBUP framework client module.
 *
 * @version 0.2.0 2016/02/14
 * @author ExBin Project (http://exbin.org)
 */
public interface ClientModuleApi extends XBApplicationModule {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(ClientModuleApi.class);

    /**
     * Attempts to connect to running service.
     *
     * @return true if connection was estabilished
     */
    public boolean connectToService();

    /**
     * Attempts to connect to fallback service using database connection.
     *
     * @return true if connection was estabilished
     */
    boolean connectToFallbackService();

    /**
     * Run internal service.
     */
    void useBuildInService();

    XBACatalog getCatalog();

    XBPluginRepository getPluginRepository();

    boolean isDevMode();

    void setDevMode(boolean devMode);

    void addClientConnectionListener(ClientConnectionListener listener);

    void removeClientConnectionListener(ClientConnectionListener listener);
}
