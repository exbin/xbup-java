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
 * Interface for catalog item string identification entity.
 *
 * @version 0.1.21 2012/04/18
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCXStri extends XBCBase {

    /**
     * Gets relevant item.
     *
     * @return item
     */
    XBCItem getItem();

    /**
     * Gets relevant text.
     *
     * @return text
     */
    String getText();

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
     * Gets node path.
     *
     * @return the nodePath
     */
    String getNodePath();

    /**
     * Sets node path.
     *
     * @param nodePath the nodePath to set
     */
    void setNodePath(String nodePath);
}
