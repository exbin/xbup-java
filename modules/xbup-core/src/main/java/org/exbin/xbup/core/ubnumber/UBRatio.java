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

import java.io.Serializable;
import org.exbin.xbup.core.parser.token.XBEditableAttribute;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for LRUB-encoded real value limited to range [0, 1].
 *
 * @version 0.1.25 2015/02/09
 * @author ExBin Project (http://exbin.org)
 */
public interface UBRatio extends Serializable, UBStreamable, XBEditableAttribute {

    static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 0, 5, 0};

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
     * Returns if value is zero.
     *
     * @return true if value equals zero
     */
    public boolean isZero();

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
}
