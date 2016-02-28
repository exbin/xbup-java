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
package org.xbup.lib.core.parser.token;

import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.UBStreamable;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for single attribute.
 *
 * @version 0.1.25 2015/02/09
 * @author ExBin Project (http://exbin.org)
 */
public interface XBAttribute extends UBStreamable {

    /**
     * Returns if value in natural form is zero.
     *
     * @return true if value equals zero
     */
    public boolean isNaturalZero();

    /**
     * Gets short integer value of natural form.
     *
     * @return integer value
     * @throws UBOverFlowException if value is out of range
     */
    public int getNaturalInt() throws UBOverFlowException;

    /**
     * Gets long integer value of natural form.
     *
     * @return long integer value
     * @throws UBOverFlowException if value is out of range
     */
    public long getNaturalLong() throws UBOverFlowException;

    /**
     * Converts this value to UBNatural form.
     *
     * If value is UBNatural it returns itself, otherwise it returns new
     * instance.
     *
     * @return natural value
     */
    public UBNatural convertToNatural();
}
