/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.ubnumber;

import java.io.Serializable;
import org.exbin.xbup.core.parser.token.XBEditableAttribute;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for UBNatural attribute.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public interface UBNatural extends Serializable, UBStreamable, XBEditableAttribute {

    static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 0, 0, 0};

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
     * Gets count of long value segments.
     *
     * @return count of long segments
     */
    long getSegmentCount();

    /**
     * Gets long integer segment of value.
     *
     * @param segmentIndex index of segment, 0 for lowest value
     * @return long integer
     */
    long getValueSegment(long segmentIndex);
}
