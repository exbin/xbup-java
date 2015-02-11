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

import org.xbup.lib.core.block.declaration.XBBlockDecl;

/**
 * Procedure declaration.
 *
 * @version 0.1.25 2015/02/11
 * @author XBUP Project (http://xbup.org)
 */
public interface XBProcedureDecl {

    /**
     * Returns block declaration representing this procedure.
     *
     * @return block declaration
     */
    public XBBlockDecl getBlockDecl();

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
