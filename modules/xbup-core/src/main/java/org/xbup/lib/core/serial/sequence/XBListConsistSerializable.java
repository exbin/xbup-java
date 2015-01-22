/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.core.serial.sequence;

import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;

/**
 * XBUP level 1 serialization interface for potentionally infinite list.
 *
 * @version 0.1.24 2015/01/22
 * @author XBUP Project (http://xbup.org)
 */
public interface XBListConsistSerializable extends XBSerializable {

    /**
     * Returns size of the list.
     *
     * @return size of list
     */
    public UBENatural getSize();

    /**
     * Sets size of the list.
     *
     * @param count
     */
    public void setSize(UBENatural count);

    /**
     * Resets position of the list order.
     */
    public void reset();

    /**
     * Gets next item from the list.
     *
     * @return next item
     */
    public XBSerializable next();
}
