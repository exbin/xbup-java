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

import java.util.Date;

/**
 * Interface for catalog root node entity.
 *
 * @version 0.1.22 2013/08/18
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCRoot extends XBCBase {

    /**
     * Gets root node for this catalog root.
     *
     * @return the node
     */
    public XBCNode getNode();

    /**
     * Gets URL for this catalog root.
     *
     * @return the URL string
     */
    public String getUrl();

    /**
     * Gets last update for this catalog root.
     *
     * @return date
     */
    public Date getLastUpdate();
}
