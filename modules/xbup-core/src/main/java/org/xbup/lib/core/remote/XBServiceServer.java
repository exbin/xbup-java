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

import org.xbup.lib.core.block.XBBlockType;

/**
 * XBUP RPC server interface.
 *
 * @version 0.1.25 2015/02/14
 * @author XBUP Project (http://xbup.org)
 */
public interface XBServiceServer {

    /**
     * Registers procedure handling.
     *
     * @param procedureType procedure type
     * @param procedure procedure handler to add
     */
    public void addXBProcedure(XBBlockType procedureType, XBProcedure procedure);

    /**
     * Unregisters procedure handling.
     *
     * @param procedureType procedure type
     */
    public void removeXBProcedure(XBBlockType procedureType);

}
