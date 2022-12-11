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
package org.exbin.xbup.core.serial.sequence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.ubnumber.UBENatural;

/**
 * XBUP level 1 serialization interface for potentionally infinite list.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBListConsistSerializable extends XBSerializable {

    /**
     * Returns size of the list.
     *
     * @return size of list
     */
    @Nonnull
    UBENatural getSize();

    /**
     * Sets size of the list.
     *
     * @param size target size
     */
    void setSize(UBENatural size);

    /**
     * Resets position of the list order.
     */
    void reset();

    /**
     * Gets next item from the list.
     *
     * @return next item
     */
    @Nullable
    XBSerializable next();
}
