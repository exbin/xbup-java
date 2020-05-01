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
package org.exbin.xbup.core.serial.sequence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 serialization list for finite list.
 *
 * @version 0.2.1 2017/06/04
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBListJoinSerializable extends XBSerializable {

    /**
     * Returns size of the list.
     *
     * @return size of list
     */
    @Nonnull
    UBNatural getSize();

    /**
     * Sets size of the list.
     *
     * @param size target size
     */
    void setSize(UBNatural size);

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
