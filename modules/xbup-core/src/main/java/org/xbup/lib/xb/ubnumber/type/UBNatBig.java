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
package org.xbup.lib.xb.ubnumber.type;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 *
 * @version 0.1 wr21.0 2011/12/01
 * @author XBUP Project (http://xbup.org)
 */
public class UBNatBig extends UBNatural {

    BigInteger value;
//    UBNat extend;

    public UBNatBig() {
//        extend = null;
//        BigInteger value = new BigInteger();
    }

    public UBNatBig(int value) {
        if (value<0) {
            throw new UBOverFlowException("Wrong negative value");
        }
        this.value = BigInteger.valueOf(value);
    }

    public UBNatBig(String val) {
        this.value = new BigInteger(val);
    }

    /** Number incrementation */
    @Override
    public void inc() {
        value.add(BigInteger.ONE);
    }

    /** Number decrementation */
    @Override
    public void dec() {
        if (value.equals(BigInteger.ZERO)) {
            throw new UBOverFlowException("Wrong negative value");
        }
        value.add(BigInteger.ONE);
    }

    public void add(UBNatBig val) {
        value.add(val.value);
    }

    /** Function for conversion into data stream */
    public void toStream(DataOutputStream os) {
        throw new UnsupportedOperationException("Not yet implemented");
/*        BigInteger BI128 = BigInteger.ONE.shiftLeft(7);
        BigInteger Length = BigInteger.ONE;
        BigInteger Output = BigInteger.ZERO;
        BigInteger Head = BigInteger.ZERO;
        BigInteger Temp = this.abs();
        BigInteger CurMaxVal = BI128;
        while (Temp.compareTo(CurMaxVal) == 1) {
            Temp = Temp.add(CurMaxVal.negate());
            CurMaxVal = CurMaxVal.shiftLeft(7);
            Head = Head.shiftRight(1);
            Head = Head.add(BI128);
            Length.add(BigInteger.ONE);
        }
        while (Length.compareTo(BigInteger.ZERO)==1) {

        }*/
    }

    /** Function for conversion getting from stream */
    public void fromStream(DataInputStream is) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public UBNatBig clone() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int toStreamUB(OutputStream stream) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int fromStreamUB(InputStream stream) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int getSizeUB() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getAttributeType() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int toInt() throws UBOverFlowException {
        return (int) value.longValue();
    }

    @Override
    public long toLong() throws UBOverFlowException {
        return value.longValue();
    }

    @Override
    public UBNatural toNatural() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getValueSize() {
        return 1;
    }

    @Override
    public List<UBNatural> getValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockType getXBBlockType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(int value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(Integer value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInt() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLong() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isZero() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEqual(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isGreater(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isShort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLong() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add(UBNatural val) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sub(UBNatural val) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void shiftLeft(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void shiftRight(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void multiply(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void divide(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void divMod(UBNatural val, UBNatural rest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void modDiv(UBNatural val, UBNatural quot) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void modulate(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void power(UBNatural val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sqrt() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sum(UBNatural op1, UBNatural op2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dif(UBNatural op1, UBNatural op2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void product(UBNatural op1, UBNatural op2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void quot(UBNatural op1, UBNatural op2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void quotRest(UBNatural op1, UBNatural op2, UBNatural rest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void restQuot(UBNatural op1, UBNatural op2, UBNatural quot) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rest(UBNatural op1, UBNatural op2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void invol(UBNatural op1, UBNatural op2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doOr(UBNatural val) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doAnd(UBNatural val) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doXor(UBNatural val) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doNot(UBNatural val) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
