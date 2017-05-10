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

import javax.annotation.Nonnull;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.UBStreamable;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for single attribute.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public interface XBAttribute extends UBStreamable {

    /**
     * Returns if value in natural form is zero.
     *
     * @return true if value equals zero
     */
    boolean isNaturalZero();

    /**
     * Gets short integer value of natural form.
     *
     * @return integer value
     * @throws UBOverFlowException if value is out of range
     */
    int getNaturalInt() throws UBOverFlowException;

    /**
     * Gets long integer value of natural form.
     *
     * @return long integer value
     * @throws UBOverFlowException if value is out of range
     */
    long getNaturalLong() throws UBOverFlowException;

    /**
     * Converts this value to UBNatural form.
     *
     * If value is UBNatural it returns itself, otherwise it returns new
     * instance.
     *
     * @return natural value
     */
    @Nonnull
    UBNatural convertToNatural();
}
