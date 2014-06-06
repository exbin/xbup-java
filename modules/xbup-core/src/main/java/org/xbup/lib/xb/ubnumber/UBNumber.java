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
package org.xbup.lib.xb.ubnumber;

import java.io.Serializable;
import java.util.List;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 * Abstract class for numbers serializable to UB streams.
 *
 * UBNumber consists of one or more natural numbers with possible shifted
 * meaning.
 *
 * @version 0.1 wr18.0 2009/08/05
 * @author XBUP Project (http://xbup.org)
 */
public abstract class UBNumber implements Serializable, Cloneable, UBStreamable, XBSerializable {

    @Override
    public abstract UBNumber clone();

    /**
     * Converts value of object to non-negative integer.
     * @return 
     */
    public abstract int toInt() throws UBOverFlowException;

    /**
     * Converts value of object to non-negative integer
     */
    public abstract long toLong() throws UBOverFlowException;

    /**
     * Get size of value in UBNatural items
     */
    public abstract long getValueSize();

    /**
     * Get list of ubnatural values representation
     */
    public abstract List<UBNatural> getValues();

    /**
     * Converts value to UBNatural form
     */
    public abstract UBNatural toNatural();
}
