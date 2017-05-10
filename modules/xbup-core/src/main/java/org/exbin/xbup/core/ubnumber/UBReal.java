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
package org.exbin.xbup.core.ubnumber;

import javax.annotation.Nonnull;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for LRUB-encoded real value.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public interface UBReal {

    static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 0, 8, 0};

    /**
     * Gets short integer value.
     *
     * @return integer value
     * @throws UBOverFlowException if value is out of range
     */
    int getInt() throws UBOverFlowException;

    /**
     * Gets long integer value.
     *
     * @return long integer value
     * @throws UBOverFlowException if value is out of range
     */
    long getLong() throws UBOverFlowException;

    /**
     * Gets float value.
     *
     * @return float value
     * @throws UBOverFlowException if value is out of range
     */
    float getFloat() throws UBOverFlowException;

    /**
     * Gets double value.
     *
     * @return double value
     * @throws UBOverFlowException if value is out of range
     */
    double getDouble() throws UBOverFlowException;

    /**
     * Returns if value is zero.
     *
     * @return true if value equals zero
     */
    boolean isZero();

    /**
     * Sets integer value.
     *
     * @param value integer value
     */
    void setValue(int value) throws UBOverFlowException;

    /**
     * Sets long integer value.
     *
     * @param value long integer value
     */
    void setValue(long value) throws UBOverFlowException;

    /**
     * Sets float value.
     *
     * @param value float value
     */
    void setValue(float value) throws UBOverFlowException;

    /**
     * Sets double value.
     *
     * @param value double value
     */
    void setValue(double value) throws UBOverFlowException;

    /**
     * Gets base part of real value.
     *
     * @return base part
     */
    @Nonnull
    UBInteger getBase();

    /**
     * Gets mantissa part of real value.
     *
     * @return mantissa part
     */
    @Nonnull
    UBInteger getMantissa();
}
