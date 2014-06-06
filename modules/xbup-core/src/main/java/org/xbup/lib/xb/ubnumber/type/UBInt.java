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
import java.util.List;
import java.util.Set;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.ubnumber.UBInteger;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.UBNumber;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 *
 * @version 0.1 wr21.0 2011/12/01
 * @author XBUP Project (http://xbup.org)
 */
public class UBInt extends UBInteger {
    private Set value;

    public static void UBInteger(int value) {
    }

    @Override
    public void inc() {

    }

    @Override
    public void dec() {
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
        return true;
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

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        return 0;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBParseException {
        return 0;
    }

    @Override
    public int getSizeUB() {
        return 0;
    }

    public void getAttributeType() {
    }

    public int getItemsCount() {
        return 1;
    }

    public List<UBNumber> getItems() {
        throw new UnsupportedOperationException("Not yet implemented");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UBNatural> getValues() {
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

