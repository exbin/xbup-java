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

/**
 * XBUP level 1 RPC server interface.
 *
 * @version 0.1.19 2010/06/04
 * @author XBUP Project (http://xbup.org)
 */
public interface XBRemoteServer {

    /**
     * Register procedure handling.
     *
     * @param procedure procedure handler to add
     */
    public void addXBProcedure(XBProcedure procedure);

    /**
     * Unregister procedure handling.
     *
     * @param procedure procedure handler to remove
     */
    public void removeXBProcedure(XBProcedure procedure);
}