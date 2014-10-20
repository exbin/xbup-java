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
 * Interface for catalog item string identification entity.
 *
 * @version 0.1.21 2012/04/18
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCXStri extends XBCBase {

    /**
     * Gets relevant item.
     *
     * @return item
     */
    public XBCItem getItem();

    /**
     * Gets relevant text.
     *
     * @return text
     */
    public String getText();

    /**
     * Sets relevant item.
     *
     * @param item
     */
    public void setItem(XBCItem item);

    /**
     * Sets item text.
     *
     * @param text
     */
    public void setText(String text);

    /**
     * Gets node path.
     *
     * @return the nodePath
     */
    public String getNodePath();

    /**
     * Sets node path.
     *
     * @param nodePath the nodePath to set
     */
    public void setNodePath(String nodePath);
}
