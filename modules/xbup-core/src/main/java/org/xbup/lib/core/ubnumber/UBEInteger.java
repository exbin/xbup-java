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
package org.xbup.lib.core.ubnumber;

import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for LRUB-encoded integer value with infinity constants.
 *
 * @version 0.1.24 2014/06/07
 * @author XBUP Project (http://xbup.org)
 */
public interface UBEInteger {

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
     * Get count of long value segments.
     *
     * @return count of long segments
     */
    public long getSegmentCount();

    /**
     * Get long integer segment of value.
     *
     * @param segmentIndex index of segment, 0 for lowest value
     * @return long integer
     */
    public long getValueSegment(long segmentIndex);

    /**
     * Read positive or negative infinity flag.
     *
     * @return true if value represents infinity
     */
    public boolean isInfinity();

    /**
     * Read positive infinity flag.
     *
     * @return true if value represents infinity
     */
    public boolean isPositiveInfinity();

    /**
     * Read negative infinity flag.
     *
     * @return true if value represents infinity
     */
    public boolean isNegativeInfinity();

    /**
     * Set value to positive infinity constant.
     */
    public void setPositiveInfinity();

    /**
     * Set value to negative infinity constant.
     */
    public void setNegativeInfinity();
}
