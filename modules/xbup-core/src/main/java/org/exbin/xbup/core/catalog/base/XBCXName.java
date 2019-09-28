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
 * Interface for catalog item name entity.
 *
 * @version 0.1.21 2011/12/16
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCXName extends XBCBase {

    /**
     * Gets relevant item.
     *
     * @return item
     */
    XBCItem getItem();

    /**
     * Gets item text.
     *
     * @return item text
     */
    String getText();

    /**
     * Gets name language.
     *
     * @return language
     */
    XBCXLanguage getLang();

    /**
     * Sets relevant item.
     *
     * @param item item
     */
    void setItem(XBCItem item);

    /**
     * Sets item text.
     *
     * @param text text
     */
    void setText(String text);

    /**
     * Sets language.
     *
     * @param language language
     */
    void setLang(XBCXLanguage language);
}
