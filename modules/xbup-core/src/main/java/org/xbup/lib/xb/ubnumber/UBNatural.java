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
import org.xbup.lib.xb.block.declaration.XBDBlockType;
import org.xbup.lib.xb.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 * Abstract class for UBNatural attribute.
 *
 * @version 0.1 wr18.0 2009/07/26
 * @author XBUP Project (http://xbup.org)
 */
public abstract class UBNatural extends UBNumber {

    public static long[] xbBlockType = {0, 12, 0};
//    static UBNatural ZERO;

    /** Setting integer value */
    public abstract void setValue(int value) throws UBOverFlowException;

    /** Setting long integer value */
    public abstract void setValue(long value) throws UBOverFlowException;

    /** Setting Integer class value */
    public abstract void setValue(Integer value) throws UBOverFlowException;

    /** Getting short integer value */
    public abstract int getInt() throws UBOverFlowException;

    /** Getting long integer value */
    public abstract long getLong() throws UBOverFlowException;

    // Basic Predicates

    /** Zero predicate */
    public abstract boolean isZero();

    /** Equal predicate */
    public abstract boolean isEqual(UBNatural val);

    /** Value comparison predicate */
    public abstract boolean isGreater(UBNatural val);

    /** Short integer form indicator */
    public abstract boolean isShort();

    /** Long integer form indicator */
    public abstract boolean isLong();

    // Basic mathematical operators

    /** incrementation */
    public abstract void inc() throws UBOverFlowException;

    /** decrementation */
    public abstract void dec() throws UBOverFlowException;

    /** additing */
    public abstract void add(UBNatural val) throws UBOverFlowException;

    public abstract void sub(UBNatural val) throws UBOverFlowException;

    public abstract void shiftLeft(UBNatural val);

    public abstract void shiftRight(UBNatural val);

    public abstract void multiply(UBNatural val);

    public abstract void divide(UBNatural val);

    public abstract void divMod(UBNatural val, UBNatural rest);

    public abstract void modDiv(UBNatural val, UBNatural quot);

    public abstract void modulate(UBNatural val);

    public abstract void power(UBNatural val);

    public abstract void sqrt();

    // Basic Mathematical Functions

    public abstract void sum(UBNatural op1, UBNatural op2);

    public abstract void dif(UBNatural op1, UBNatural op2);

    public abstract void product(UBNatural op1, UBNatural op2);

    public abstract void quot(UBNatural op1, UBNatural op2);

    public abstract void quotRest(UBNatural op1, UBNatural op2, UBNatural rest);

    public abstract void restQuot(UBNatural op1, UBNatural op2, UBNatural quot);

    public abstract void rest(UBNatural op1, UBNatural op2);

    public abstract void invol(UBNatural op1, UBNatural op2);

    // Logical Mathematical Functions

    /** Logical addition */
    public abstract void doOr(UBNatural val) throws UBOverFlowException;

    /** Logical multiplication */
    public abstract void doAnd(UBNatural val) throws UBOverFlowException;

    /** Exclusive Logical addition */
    public abstract void doXor(UBNatural val) throws UBOverFlowException;

    /** Exclusive Negation
     * @param val of bits to negate
     */
    public abstract void doNot(UBNatural val) throws UBOverFlowException;

    @Override
    public UBNatural toNatural() {
        return this;
    }

    public XBBlockType getXBBlockType() {
        return new XBDBlockType(new XBCPBlockDecl(xbBlockType));
    }

    @Override
    public abstract UBNatural clone();
}
