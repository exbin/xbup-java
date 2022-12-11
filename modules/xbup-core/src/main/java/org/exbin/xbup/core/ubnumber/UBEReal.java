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

import javax.annotation.Nonnull;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * Interface for LRUB-encoded real value with infinity constants.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface UBEReal {

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
    UBNatural getBase();

    /**
     * Gets mantissa part of real value.
     *
     * @return mantissa part
     */
    @Nonnull
    UBNatural getMantissa();

    /**
     * Reads positive or negative infinity flag.
     *
     * @return true if value represents infinity
     */
    boolean isInfinity();

    /**
     * Reads positive infinity flag.
     *
     * @return true if value represents infinity
     */
    boolean isPositiveInfinity();

    /**
     * Reads negative infinity flag.
     *
     * @return true if value represents infinity
     */
    boolean isNegativeInfinity();

    /**
     * Sets value to positive infinity constant.
     */
    void setPositiveInfinity();

    /**
     * Sets value to negative infinity constant.
     */
    void setNegativeInfinity();
}
