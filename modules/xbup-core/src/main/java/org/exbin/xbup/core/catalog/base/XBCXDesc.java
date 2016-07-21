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

/**
 * Interface for catalog item description entity.
 *
 * @version 0.1.21 2011/12/16
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCXDesc extends XBCBase {

    /**
     * Gets relevant item.
     *
     * @return the item
     */
    public XBCItem getItem();

    /**
     * Gets description text.
     *
     * @return the description text
     */
    public String getText();

    /**
     * Gets description language.
     *
     * @return the language
     */
    public XBCXLanguage getLang();

    /**
     * Sets relevant item.
     *
     * @param item item
     */
    public void setItem(XBCItem item);

    /**
     * Sets description text.
     *
     * @param text text
     */
    public void setText(String text);

    /**
     * Sets description language.
     *
     * @param language language
     */
    public void setLang(XBCXLanguage language);
}
