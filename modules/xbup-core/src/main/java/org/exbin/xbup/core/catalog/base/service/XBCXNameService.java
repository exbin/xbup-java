/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.catalog.base.service;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;

/**
 * Interface for XBCXName items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCXNameService extends XBCService<XBCXName>, XBCExtension {

    /**
     * Returns default name for given item.
     *
     * @param item item
     * @return name
     */
    XBCXName getDefaultItemName(XBCItem item);

    /**
     * Returns name for given item and language.
     *
     * @param item item
     * @param language language
     * @return name
     */
    XBCXName getItemName(XBCItem item, XBCXLanguage language);

    /**
     * Returns list of names for given item.
     *
     * @param item item
     * @return list of names
     */
    List<XBCXName> getItemNames(XBCItem item);

    /**
     * Gets default name text for default language.
     *
     * @param item item to get caption of
     * @return string caption
     */
    String getDefaultText(XBCItem item);

    /**
     * Gets name path as a list of dot separated name sequence.
     *
     * @param item item
     * @return name path string
     */
    String getItemNamePath(XBCItem item);
}
