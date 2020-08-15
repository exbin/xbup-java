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
package org.exbin.xbup.catalog.modifiable;

import java.util.Date;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Interface for catalog root node entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMRoot extends XBCRoot, XBMBase {

    /**
     * Sets root node for this catalog root.
     *
     * @param node node
     */
    void setNode(XBCNode node);

    /**
     * Sets URL for this catalog root.
     *
     * @param url the URL string or empty string for build-in catalog
     */
    void setUrl(String url);

    /**
     * Sets last update for this catalog root.
     *
     * @param date date
     */
    void setLastUpdate(@Nullable Date date);
}
