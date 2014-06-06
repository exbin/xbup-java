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

import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 * Abstract class for UB form boolean value.
 *
 * @version 0.1 wr18.0 2009/08/24
 * @author XBUP Project (http://xbup.org)
 */
public abstract class UBBoolean extends UBNumber {

//    static UBNatural ZERO;

    /** Setting boolean value */
    public abstract void setValue(boolean value) throws UBOverFlowException;

    /** Setting Integer class value */
    public abstract void setValue(Boolean value) throws UBOverFlowException;

    /** Getting short integer value */
    public abstract int getInt() throws UBOverFlowException;

    /** Getting long integer value */
    public abstract int getLong() throws UBOverFlowException;

    /** Getting short boolean value */
    public abstract boolean getBool() throws UBOverFlowException;

    // Basic Predicates

    /** Zero predicate */
    public abstract boolean isFalse();

    /** Equal predicate */
    public abstract boolean isEqual(UBBoolean val);

    // Logical Mathematical Functions

    /** Logical addition */
    public abstract void doOr(UBBoolean val) throws UBOverFlowException;

    /** Logical multiplication */
    public abstract void doAnd(UBBoolean val) throws UBOverFlowException;

    /** Exclusive Logical addition */
    public abstract void doXor(UBBoolean val) throws UBOverFlowException;

    /**
     * Exclusive Negation
     * @param val of bits to negate
     */
    public abstract void doNot(UBBoolean val) throws UBOverFlowException;

    public XBBlockType getXBBlockType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
