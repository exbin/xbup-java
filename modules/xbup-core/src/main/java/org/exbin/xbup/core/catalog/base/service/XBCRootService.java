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
package org.exbin.xbup.core.catalog.base.service;

import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Interface for XBCRoot items service.
 *
 * @version 0.2.1 2020/08/18
 * @author ExBin Project (http://exbin.org)
 * @param <T> root class
 */
@ParametersAreNonnullByDefault
public interface XBCRootService<T extends XBCRoot> extends XBCService<T> {

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
     * Resutrn true if main root is available.
     *
     * @return true if available
     */
    boolean isMainPresent();

    /**
     * Sets main last update to now.
     */
    void setMainLastUpdateToNow();
}