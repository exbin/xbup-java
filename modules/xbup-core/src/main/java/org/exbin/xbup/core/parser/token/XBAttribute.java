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
