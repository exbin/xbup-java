/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    XBCItem getItem();

    /**
     * Gets description text.
     *
     * @return the description text
     */
    String getText();

    /**
     * Gets description language.
     *
     * @return the language
     */
    XBCXLanguage getLang();

    /**
     * Sets relevant item.
     *
     * @param item item
     */
    void setItem(XBCItem item);

    /**
     * Sets description text.
     *
     * @param text text
     */
    void setText(String text);

    /**
     * Sets description language.
     *
     * @param language language
     */
    void setLang(XBCXLanguage language);
}
