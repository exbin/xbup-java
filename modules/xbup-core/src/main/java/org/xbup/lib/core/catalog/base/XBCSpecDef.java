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
package org.xbup.lib.core.catalog.base;

/**
 * Interface for catalog specification definition entity.
 *
 * @version 0.1 wr22.0 2013/01/11
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCSpecDef extends XBCItem {

    /**
     * Get specification which is also owner.
     *
     * @return specification
     */
    public XBCSpec getSpec();

    /**
     * Get target specification.
     *
     * @return revision
     */
    public XBCRev getTarget();

    /**
     * Get binding type.
     *
     * @return specification definition type
     */
    public XBCSpecDefType getType();
}
