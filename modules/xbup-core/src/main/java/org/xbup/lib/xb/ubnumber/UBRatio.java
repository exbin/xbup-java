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
public abstract class UBRatio extends UBNumber {

    public static long[] xbBlockType = {0, 12, 1000};
//    static UBNatural ZERO;

    /** Setting integer value */
    public abstract void setValue(int value) throws UBOverFlowException;

    /** Setting long integer value */
    public abstract void setValue(long value) throws UBOverFlowException;

    /** Setting long integer value */
    public abstract void setValue(float value) throws UBOverFlowException;

    /** Setting long integer value */
    public abstract void setValue(double value) throws UBOverFlowException;

    /** Setting Integer class value */
    public abstract void setValue(Integer value) throws UBOverFlowException;

    /** Setting Integer class value */
    public abstract void setValue(Float value) throws UBOverFlowException;

    /** Setting Integer class value */
    public abstract void setValue(Double value) throws UBOverFlowException;

    /** Getting short integer value */
    public abstract int getInt() throws UBOverFlowException;

    /** Getting long integer value */
    public abstract long getLong() throws UBOverFlowException;

    /** Getting long float value */
    public abstract float getFloat() throws UBOverFlowException;

    /** Getting long double value */
    public abstract double getDouble() throws UBOverFlowException;

    // Basic Predicates

    /** Zero predicate */
    public abstract boolean isZero();

    /** Zero predicate */
    public abstract boolean isOne();

    /** Equal predicate */
    public abstract boolean isEqual(UBRatio val);

    /** Value comparison predicate */
    public abstract boolean isGreater(UBRatio val);

    /** Short integer form indicator */
    public abstract boolean isShort();

    /** Long integer form indicator */
    public abstract boolean isLong();

    /** Long integer form indicator */
    public abstract boolean isFloat();

    /** Long integer form indicator */
    public abstract boolean isDouble();

    // Basic mathematical operators

    /** incrementation */
    public abstract void inc() throws UBOverFlowException;

    /** decrementation */
    public abstract void dec() throws UBOverFlowException;

    /** additing */
    public abstract void add(UBRatio val) throws UBOverFlowException;

    public abstract void sub(UBRatio val) throws UBOverFlowException;

    public abstract void shiftLeft(UBRatio val);

    public abstract void shiftRight(UBRatio val);

    public abstract void multiply(UBRatio val);

    public abstract void divide(UBRatio val);

    public abstract void divMod(UBRatio val, UBRatio rest);

    public abstract void modDiv(UBRatio val, UBRatio quot);

    public abstract void modulate(UBRatio val);

    public abstract void power(UBRatio val);

    public abstract void sqrt();

    // Basic Mathematical Functions

    public abstract void sum(UBRatio op1, UBRatio op2);

    public abstract void dif(UBRatio op1, UBRatio op2);

    public abstract void product(UBRatio op1, UBRatio op2);

    public abstract void quot(UBRatio op1, UBRatio op2);

    public abstract void quotRest(UBRatio op1, UBRatio op2, UBRatio rest);

    public abstract void restQuot(UBRatio op1, UBRatio op2, UBRatio quot);

    public abstract void rest(UBRatio op1, UBRatio op2);

    public abstract void invol(UBRatio op1, UBRatio op2);

    // Logical Mathematical Functions

    /** Logical addition */
    public abstract void doOr(UBRatio val) throws UBOverFlowException;

    /** Logical multiplication */
    public abstract void doAnd(UBRatio val) throws UBOverFlowException;

    /** Exclusive Logical addition */
    public abstract void doXor(UBRatio val) throws UBOverFlowException;

    /** Exclusive Negation
     * @param val of bits to negate
     */
    public abstract void doNot(UBRatio val) throws UBOverFlowException;

    @Override
    public abstract UBNatural toNatural();

    public XBBlockType getXBBlockType() {
        return new XBDBlockType(new XBCPBlockDecl(xbBlockType));
    }

    @Override
    public abstract UBRatio clone();
}
