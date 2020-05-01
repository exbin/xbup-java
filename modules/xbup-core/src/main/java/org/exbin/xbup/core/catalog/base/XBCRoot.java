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
    XBCNode getNode();

    /**
     * Gets URL for this catalog root.
     *
     * @return the URL string
     */
    String getUrl();

    /**
     * Gets last update for this catalog root.
     *
     * @return date
     */
    Date getLastUpdate();
}
