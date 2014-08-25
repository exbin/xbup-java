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
package org.xbup.lib.core.block.declaration;

import org.xbup.lib.core.block.XBBlockType;

/**
 * Interface for declared block type.
 *
 * @version 0.1.20 2010/11/13
 * @author XBUP Project (http://xbup.org)
 */
public interface XBDeclBlockType extends XBBlockType {

    /**
     * Current declaration of block type.
     *
     * @return declaration of block type
     */
    public XBBlockDecl getBlockDecl();
}
