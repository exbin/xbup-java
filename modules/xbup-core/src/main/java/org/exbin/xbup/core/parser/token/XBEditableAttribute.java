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
package org.exbin.xbup.core.parser.token;

import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for single attribute.
 *
 * @version 0.1.25 2015/02/09
 * @author ExBin Project (http://exbin.org)
 */
public interface XBEditableAttribute extends XBAttribute {

    /**
     * Sets zero value in natural form.
     */
    public void setNaturalZero();

    /**
     * Gets short integer value of natural form.
     *
     * @param intValue integer value to set
     * @throws UBOverFlowException if value is out of range
     */
    public void setNaturalInt(int intValue) throws UBOverFlowException;

    /**
     * Gets long integer value of natural form.
     *
     * @param longValue long value to set
     * @throws UBOverFlowException if value is out of range
     */
    public void setNaturalLong(long longValue) throws UBOverFlowException;

    /**
     * Sets this value using conversion from UBNatural form.
     *
     * @param natural natural value
     */
    public void convertFromNatural(UBNatural natural);
}
