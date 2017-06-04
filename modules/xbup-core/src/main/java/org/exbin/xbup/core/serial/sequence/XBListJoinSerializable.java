/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.xbup.core.serial.sequence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 serialization list for finite list.
 *
 * @version 0.2.1 2017/06/04
 * @author ExBin Project (http://exbin.org)
 */
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
    void setSize(@Nonnull UBNatural size);

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
