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

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXStri;

/**
 * Interface for XBCXStri items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCXStriService extends XBCService<XBCXStri>, XBCExtension {

    /**
     * Returns StringId for given item.
     *
     * @param item item
     * @return stringId
     */
    XBCXStri getItemStringId(XBCItem item);

    /**
     * Gets string id text for given item.
     *
     * @param item item to get Stri of
     * @return text or null if Stri is not set
     */
    String getItemStringIdText(XBCItem item);

    /**
     * Returns full stringId path including leading slash symbol.
     *
     * @param itemString stringId item
     * @return String representation of the path
     */
    String getFullPath(XBCXStri itemString);

    /**
     * Returns full stringId path including leading slash symbol.
     *
     * @param item item to get stri of
     * @return string representation of the path
     */
    String getItemFullPath(XBCItem item);

    /**
     * Gets specification for give full path.
     *
     * @param fullPath fullPath to specification
     * @return specification
     */
    XBCSpec getSpecByFullPath(String fullPath);
}
