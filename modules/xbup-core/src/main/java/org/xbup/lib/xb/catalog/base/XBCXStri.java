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
package org.xbup.lib.xb.catalog.base;

/**
 * Interface for catalog item string identification entity.
 *
 * @version 0.1 wr21.0 2012/04/18
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCXStri extends XBCBase {

    /**
     * Get relevant item.
     *
     * @return item
     */
    public XBCItem getItem();

    /**
     * Get relevant text.
     *
     * @return text
     */
    public String getText();

    /**
     * Set relevant item.
     *
     * @param item
     */
    public void setItem(XBCItem item);

    /**
     * Set item text.
     *
     * @param text
     */
    public void setText(String text);

    /**
     * Get node path.
     *
     * @return the nodePath
     */
    public String getNodePath();

    /**
     * Set node path.
     *
     * @param nodePath the nodePath to set
     */
    public void setNodePath(String nodePath);
}
