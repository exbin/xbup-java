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
package org.exbin.xbup.core.parser.token;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for single editable attribute.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBEditableAttribute extends XBAttribute {

    /**
     * Sets zero value in natural form.
     */
    void setNaturalZero();

    /**
     * Gets short integer value of natural form.
     *
     * @param intValue integer value to set
     * @throws UBOverFlowException if value is out of range
     */
    void setNaturalInt(int intValue) throws UBOverFlowException;

    /**
     * Gets long integer value of natural form.
     *
     * @param longValue long value to set
     * @throws UBOverFlowException if value is out of range
     */
    void setNaturalLong(long longValue) throws UBOverFlowException;

    /**
     * Sets this value using conversion from UBNatural form.
     *
     * @param natural natural value
     */
    void convertFromNatural(UBNatural natural);
}
