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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.ubnumber.UBInteger;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.UBNumber;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 *
 * @version 0.1 wr21.0 2011/12/01
 * @author XBUP Project (http://xbup.org)
 */
public class UBInt32 extends UBInteger {

    private int value;

    /** Creates a new instance of UBInt32 */
    public UBInt32() {
//        value = 0;
    }

    @Override
    public int toStreamUB(OutputStream stream) {
/*        if (value<128) {
            stream.write(chr(0));
            return 1;
        } else if (value<32768) {

        }*/
        return 0;
    }

    @Override
    public int fromStreamUB(InputStream stream) {
        return 0;
    }

    @Override
    public int getSizeUB() {
        return 0;
    }

    @Override
    public void setValue(int value) throws UBOverFlowException {
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
    }

    @Override
    public void setValue(Integer value) throws UBOverFlowException {
    }

    @Override
    public int getInt() throws UBOverFlowException {
        return 0;
    }

    @Override
    public int getLong() throws UBOverFlowException {
        return 0;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isEqual(UBNatural val) {
        return false;
    }

    @Override
    public boolean isGreater(UBNatural val) {
        return false;
    }

    @Override
    public boolean isShort() {
        return false;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public void inc() throws UBOverFlowException {
    }

    @Override
    public void dec() throws UBOverFlowException {
    }

    @Override
    public void add(UBNatural val) throws UBOverFlowException {
    }

    @Override
    public void sub(UBNatural val) throws UBOverFlowException {
    }

    @Override
    public void shiftLeft(UBNatural val) {
    }

    @Override
    public void shiftRight(UBNatural val) {
    }

    @Override
    public void multiply(UBNatural val) {
    }

    @Override
    public void divide(UBNatural val) {
    }

    @Override
    public void divMod(UBNatural val, UBNatural rest) {
    }

    @Override
    public void modDiv(UBNatural val, UBNatural quot) {
    }

    @Override
    public void modulate(UBNatural val) {
    }

    @Override
    public void power(UBNatural val) {
    }

    @Override
    public void sqrt() {
    }

    @Override
    public void sum(UBNatural op1, UBNatural op2) {
    }

    @Override
    public void dif(UBNatural op1, UBNatural op2) {
    }

    @Override
    public void product(UBNatural op1, UBNatural op2) {
    }

    @Override
    public void quot(UBNatural op1, UBNatural op2) {
    }

    @Override
    public void quotRest(UBNatural op1, UBNatural op2, UBNatural rest) {
    }

    @Override
    public void restQuot(UBNatural op1, UBNatural op2, UBNatural quot) {
    }

    @Override
    public void rest(UBNatural op1, UBNatural op2) {
    }

    @Override
    public void invol(UBNatural op1, UBNatural op2) {
    }

    @Override
    public void doOr(UBNatural val) throws UBOverFlowException {
    }

    @Override
    public void doAnd(UBNatural val) throws UBOverFlowException {
    }

    @Override
    public void doXor(UBNatural val) throws UBOverFlowException {
    }

    @Override
    public void doNot(UBNatural val) throws UBOverFlowException {
    }

    @Override
    public UBNumber clone() {
        return null;
    }

    public void getAttributeType() {
    }

    @Override
    public int toInt() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public long toLong() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not yet implemented");
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
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        if (serialType == XBSerializationType.FROM_XB) {
            XBTChildProvider serial = (XBTChildProvider) serializationHandler;
            serial.begin();
            UBNatural newValue = serial.nextAttribute();
            // TODO setValue(newValue.getLong());
            serial.end();
        } else {
            XBTChildListener serial = (XBTChildListener) serializationHandler;
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            // TODO serial.addAttribute(this);
            serial.end();
        }
    }
}
