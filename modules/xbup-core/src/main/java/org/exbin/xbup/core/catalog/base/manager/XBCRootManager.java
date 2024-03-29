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

import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Interface for XBCRoot catalog manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCRootManager extends XBCCatalogManager<XBCRoot> {

    /**
     * Creates catalog's root with empty node.
     *
     * @param catalogUrl catalog URL
     * @return root record
     */
    @Nonnull
    XBCRoot createEmptyRoot(String catalogUrl);

    /**
     * Gets main catalog root record.
     *
     * @return root record
     */
    @Nonnull
    XBCRoot getMainRoot();

    /**
     * Gets time of the last update.
     *
     * @return time of last update
     */
    @Nonnull
    Optional<Date> getMainLastUpdate();

    /**
     * Fully removes given root with all it's content
     *
     * @param root root record
     */
    void removeFully(XBCRoot root);

    /**
     * Sets main last update to now.
     */
    void setMainLastUpdateToNow();

    /**
     * Resutrn true if main root is available.
     *
     * @return true if available
     */
    boolean isMainPresent();
}
