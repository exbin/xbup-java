/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
 * Interface for LRUB-encoded boolean value.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface UBBoolean extends Serializable, UBStreamable, XBEditableAttribute {

    static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 0, 13, 0};

    /**
     * Gets boolean value.
     *
     * @return value
     */
    boolean getBoolean() throws UBOverFlowException;

    /**
     * Sets boolean value.
     *
     * @param value value
     */
    void setValue(boolean value) throws UBOverFlowException;

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
