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
package org.exbin.xbup.core.catalog.base.manager;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;

/**
 * Interface for XBCXHDoc catalog manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCXHDocManager extends XBCManager<XBCXHDoc>, XBCExtension {

    /**
     * Gets documentation for item.
     *
     * @param item item
     * @return documentation
     */
    XBCXHDoc getDocumentation(XBCItem item);

    /**
     * Gets documentation by unique index.
     *
     * @param id unique index
     * @return documentation
     */
    XBCXHDoc findById(Long id);

    /**
     * Gets count of all HTML documentations.
     *
     * @return count of documentations
     */
    Long getAllHDocsCount();
}
