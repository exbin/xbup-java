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
package org.exbin.xbup.core.catalog.base;

import java.sql.Time;
import javax.annotation.Nonnull;

/**
 * Interface for item information entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBCXItemInfo extends XBCBase {

    /**
     * Gets related item.
     *
     * @return item
     */
    @Nonnull
    XBCItem getItem();

    /**
     * Gets item owner.
     *
     * @return user
     */
    @Nonnull
    XBCXUser getOwner();

    /**
     * Gets created by user.
     *
     * @return user
     */
    @Nonnull
    XBCXUser getCreatedByUser();

    /**
     * Gets creation date.
     *
     * @return creation date
     */
    @Nonnull
    Time getCreationDate();
}
