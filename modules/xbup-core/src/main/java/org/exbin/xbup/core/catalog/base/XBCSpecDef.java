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
package org.exbin.xbup.core.catalog.base;

import org.exbin.xbup.core.block.definition.XBParamType;

/**
 * Interface for catalog specification definition entity.
 *
 * @version 0.1.22 2013/01/11
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCSpecDef extends XBCItem {

    /**
     * Gets specification which is also owner.
     *
     * @return specification
     */
    XBCSpec getSpec();

    /**
     * Gets target specification.
     *
     * @return revision
     */
    XBCRev getTarget();

    /**
     * Gets specification definition's type.
     *
     * @return specification definition's type
     */
    XBParamType getType();
}
