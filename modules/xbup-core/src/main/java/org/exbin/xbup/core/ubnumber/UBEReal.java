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

import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for LRUB-encoded real value with infinity constants.
 *
 * @version 0.1.24 2014/06/07
 * @author ExBin Project (http://exbin.org)
 */
public interface UBEReal {

    /**
     * Gets short integer value.
     *
     * @return integer value
     * @throws UBOverFlowException if value is out of range
     */
    public int getInt() throws UBOverFlowException;

    /**
     * Gets long integer value.
     *
     * @return long integer value
     * @throws UBOverFlowException if value is out of range
     */
    public long getLong() throws UBOverFlowException;

    /**
     * Returns if value is zero.
     *
     * @return true if value equals zero
     */
    public boolean isZero();

    /**
     * Gets float value.
     *
     * @return float value
     * @throws UBOverFlowException if value is out of range
     */
    public float getFloat() throws UBOverFlowException;

    /**
     * Gets double value.
     *
     * @return double value
     * @throws UBOverFlowException if value is out of range
     */
    public double getDouble() throws UBOverFlowException;

    /**
     * Sets integer value.
     *
     * @param value integer value
     */
    public void setValue(int value) throws UBOverFlowException;

    /**
     * Sets long integer value.
     *
     * @param value long integer value
     */
    public void setValue(long value) throws UBOverFlowException;

    /**
     * Sets float value.
     *
     * @param value float value
     */
    public void setValue(float value) throws UBOverFlowException;

    /**
     * Sets double value.
     *
     * @param value double value
     */
    public void setValue(double value) throws UBOverFlowException;

    /**
     * Gets base part of real value.
     *
     * @return base part
     */
    public UBNatural getBase();

    /**
     * Gets mantissa part of real value.
     *
     * @return mantissa part
     */
    public UBNatural getMantissa();

    /**
     * Reads positive or negative infinity flag.
     *
     * @return true if value represents infinity
     */
    public boolean isInfinity();

    /**
     * Reads positive infinity flag.
     *
     * @return true if value represents infinity
     */
    public boolean isPositiveInfinity();

    /**
     * Reads negative infinity flag.
     *
     * @return true if value represents infinity
     */
    public boolean isNegativeInfinity();

    /**
     * Sets value to positive infinity constant.
     */
    public void setPositiveInfinity();

    /**
     * Sets value to negative infinity constant.
     */
    public void setNegativeInfinity();
}
