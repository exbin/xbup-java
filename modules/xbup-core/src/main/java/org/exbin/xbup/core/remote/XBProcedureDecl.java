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

import org.exbin.xbup.core.block.declaration.XBBlockDecl;

/**
 * Procedure declaration.
 *
 * @version 0.2.1 2017/05/29
 * @author ExBin Project (http://exbin.org)
 */
public interface XBProcedureDecl {

    /**
     * Returns block declaration representing this procedure.
     *
     * @return block declaration
     */
    XBBlockDecl getBlockDecl();

    /**
     * Returns type of input data parameter.
     *
     * @return declaration of input type
     */
    XBBlockDecl getParameterType();

    /**
     * Returns type of output data returned as result.
     *
     * @return declaration of output type
     */
    XBBlockDecl getReturnType();

    /**
     * Returns type of status data for reporting proper execution or exception.
     *
     * @return declaration of execution status type
     */
    XBBlockDecl getStatusType();
}
