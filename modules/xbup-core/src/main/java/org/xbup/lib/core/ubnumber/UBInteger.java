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
package org.xbup.lib.core.ubnumber;

import java.io.Serializable;
import org.xbup.lib.core.parser.token.XBEditableAttribute;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for LRUB-encoded integer value.
 *
 * @version 0.1.24 2015/01/07
 * @author ExBin Project (http://exbin.org)
 */
public interface UBInteger extends Serializable, UBStreamable, XBEditableAttribute {

    static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 0, 2, 0};
    
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
     * Gets count of long value segments.
     *
     * @return count of long segments
     */
    public long getSegmentCount();

    /**
     * Gets long integer segment of value.
     *
     * @param segmentIndex index of segment, 0 for lowest value
     * @return long integer
     */
    public long getValueSegment(long segmentIndex);
}
