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
package org.exbin.xbup.core.remote;

import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;

/**
 * XBUP level 1 RPC procedure definition interface.
 *
 * @version 0.1.21 2011/10/30
 * @author ExBin Project (http://exbin.org)
 */
public interface XBProcedureDef {

    /**
     * Returns type of this procedure.
     * 
     * @return type of this procedure
     */
    public XBBlockType getType();

    /**
     * Returns type of input data parameter.
     * 
     * @return declaration of input type
     */
    public XBBlockDecl getParameterType();

    /**
     * Returns type of output data returned as result.
     * 
     * @return declaration of output type
     */
    public XBBlockDecl getReturnType();

    /**
     * Returns type of status data for reporting proper execution or exception.
     * 
     * @return declaration of execution status type
     */
    public XBBlockDecl getStatusType();
}
