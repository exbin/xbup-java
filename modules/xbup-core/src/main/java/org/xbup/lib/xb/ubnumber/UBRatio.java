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

import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 * Interface for LRUB-encoded real value limited to range [0, 1].
 *
 * @version 0.1 wr24.0 2014/06/07
 * @author XBUP Project (http://xbup.org)
 */
public interface UBRatio {

    /**
     * Getting short integer value.
     *
     * @return integer value
     * @throws UBOverFlowException if value is out of range
     */
    public int getInt() throws UBOverFlowException;

    /**
     * Getting long integer value.
     *
     * @return long integer value
     * @throws UBOverFlowException if value is out of range
     */
    public long getLong() throws UBOverFlowException;

    /**
     * Getting float value.
     *
     * @return float value
     * @throws UBOverFlowException if value is out of range
     */
    public float getFloat() throws UBOverFlowException;

    /**
     * Getting double value.
     *
     * @return double value
     * @throws UBOverFlowException if value is out of range
     */
    public double getDouble() throws UBOverFlowException;

    /**
     * Setting integer value.
     *
     * @param value integer value
     */
    public void setValue(int value) throws UBOverFlowException;

    /**
     * Setting long integer value.
     *
     * @param value long integer value
     */
    public void setValue(long value) throws UBOverFlowException;

    /**
     * Setting float value.
     *
     * @param value float value
     */
    public void setValue(float value) throws UBOverFlowException;

    /**
     * Setting double value.
     *
     * @param value double value
     */
    public void setValue(double value) throws UBOverFlowException;
}
