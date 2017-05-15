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
package org.exbin.xbup.core.block.definition;

import javax.annotation.Nullable;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 1 parameter definition interface.
 *
 * Parameter is one of four types: Join, Consist, Join List, Consist List.
 * Without declaration (blockDecl) it means single attribute for join and data
 * block for consist mode and their lists respectively.
 *
 * @version 0.2.1 2017/05/15
 * @author ExBin Project (http://exbin.org)
 */
public interface XBBlockParam extends XBSpecParam, XBSerializable {

    /**
     * Gets block declaration.
     *
     * @return block declaration
     */
    @Nullable
    XBBlockDecl getBlockDecl();
}
