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
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.ubnumber.UBInteger;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.UBNumber;
import org.xbup.lib.xb.ubnumber.UBReal;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 *
 * @version 0.1 wr21.0 2011/12/01
 * @author XBUP Project (http://xbup.org)
 */
public class UBRea extends UBReal {

    private UBInteger value;
    private UBInteger mantissa;

    public UBRea(int value) {
    }

    public UBRea() {
        this(0);
    }

    public UBRea(UBReal value) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void inc() {
    }

    @Override
    public void dec() {
    }

    @Override
    public UBNumber clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int toInt() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long toLong() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getValueSize() {
        return 2;
    }

    @Override
    public List<UBNatural> getValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UBNatural toNatural() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSizeUB() {
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
    public int getLong() throws UBOverFlowException {
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

