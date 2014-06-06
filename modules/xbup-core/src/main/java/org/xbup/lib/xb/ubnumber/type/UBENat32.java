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
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.ubnumber.UBENatural;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 * UBENatural with limited value capacity to 32 bits.
 *
 * @version 0.1 wr23.0 2013/12/15
 * @author XBUP Project (http://xbup.org)
 */
public class UBENat32 extends UBENatural {

    private static long MAX_VALUE = 4294967295l;
    private long value;
    private boolean infinity;

    public UBENat32() {
        value = 0;
        infinity = false;
    }

    public UBENat32(int value) {
        this.value = value;
        infinity = false;
    }

    public UBENat32(long value) {
        this.value = value;
        infinity = false;
    }

    private UBENat32(UBENat32 value) {
        infinity = value.isInfinity();
        this.value = value.getLong();
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        byte buf[] = new byte[1];
        if (stream.read(buf) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        long input = (char) buf[0] & 0xFF;
        infinity = false;
        if (input < 0x7F) {
            value = buf[0];
            return 1;
        } else if (input == 0x7F) {
            infinity = true;
            return 1;
        } else if (input < 0xC0) {
            value = (input & 0x7F) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += ((char) buf[0] & 0xFF) + 0x7F;
            return 2;
        } else if (input < 0xE0) {
            value = (input & 0x3F) << 16;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += ((char) buf[0] & 0xFF) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += ((char) buf[0] & 0xFF) + 0x407F;
            return 3;
        } else if (input < 0xF0) {
            value = (input & 0x1F) << 24;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += ((char) buf[0] & 0xFF) << 16;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += ((char) buf[0] & 0xFF) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += ((char) buf[0] & 0xFF) + 0x20407F;
            return 4;
        } else if (input < 0xF8) {
            value = (input & 0x0F) << 32;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) << 24;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) << 16;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) + 0x1020407F;
            if (value >= 0x1020407F) {
                return 5;
            }
        }
        throw new XBProcessingException("Value is too big", XBProcessingExceptionType.UNSUPPORTED);
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        char output;
        if (isInfinity()) {
            output = 127;
            stream.write(output);
            return 1;
        } else if (value < 0x7F) {
            stream.write((char) value);
            return 1;
        } else if (value < 0x407F) {
            long pom = ((value - 0x7F) >> 8) + 128;
            stream.write((char) pom);
            pom = (value - 0x7F) & 0xFF;
            stream.write((char) pom);
            return 2;
        } else if (value < 0x20407F) {
            long pom = ((value - 0x407F) >> 16) + 192;
            stream.write((char) pom);
            pom = ((value - 0x407F) >> 8) & 0xFF;
            stream.write((char) pom);
            pom = (value - 0x407F) & 0xFF;
            stream.write((char) pom);
            return 3;
        } else if (value < 0x1020407F) {
            long pom = ((value - 0x20407F) >> 24) + 224;
            stream.write((char) pom);
            pom = ((value - 0x20407F) >> 16) & 0xFF;
            stream.write((char) pom);
            pom = ((value - 0x20407F) >> 8) & 0xFF;
            stream.write((char) pom);
            pom = (value - 0x20407F) & 0xFF;
            stream.write((char) pom);
            return 4;
        } else {
            long outValue = value - 0x1020407F;
            byte[] out = new byte[5];
            out[0] = (byte) ((outValue >> 32) + 0xF0);
            out[1] = (byte) ((outValue >> 24) & 0xFF);
            out[2] = (byte) ((outValue >> 16) & 0xFF);
            out[3] = (byte) ((outValue >> 8) & 0xFF);
            out[4] = (byte) (outValue & 0xFF);
            stream.write(out);
            return 5;
        }
    }

    @Override
    public int getSizeUB() {
        if (!infinity) {
            if (value < 0x7F) {
                return 1;
            } else if (value < 0x407F) {
                return 2;
            } else if (value < 0x20407F) {
                return 3;
            } else if (value < 0x1020407F) {
                return 4;
            } else {
                return 5;
            }
        } else {
            return 1;
        }
    }

    @Override
    public void setValue(int value) throws UBOverFlowException {
        if (value < 0) {
            throw new UBOverFlowException("Wrong negative value");
        } else if (value > MAX_VALUE) {
            throw new UBOverFlowException("Value capacity overreached");
        } else {
            this.value = value;
        }
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
        if (value < 0) {
            throw new UBOverFlowException("Wrong negative value");
        } else if (value > MAX_VALUE) {
            throw new UBOverFlowException("Value capacity overreached");
        } else {
            this.value = value;
        }
    }

    @Override
    public void setValue(Integer value) throws UBOverFlowException {
        this.value = value.intValue();
    }

    @Override
    public int getInt() throws UBOverFlowException {
        return (int) value;
    }

    @Override
    public long getLong() throws UBOverFlowException {
        return (long) value;
    }

    //
    @Override
    public void inc() throws UBOverFlowException {
        if (value < MAX_VALUE) {
            value++;
        } else {
            throw new UBOverFlowException("Value capacity overreached");
        }
    }

    @Override
    public void dec() throws UBOverFlowException {
        if (value > 0) {
            value--;
        } else {
            throw new UBOverFlowException("Value underflow");
        }
    }

    // Logical Mathematical Functions
    /**
     * Logical addition
     */
    @Override
    public void doOr(UBNatural val) throws UBOverFlowException {
        setValue(value | val.getInt());
    }

    /**
     * Logical multiplication
     */
    @Override
    public void doAnd(UBNatural val) throws UBOverFlowException {
        setValue(value & val.getInt());
    }

    /**
     * Exclusive Logical addition
     */
    @Override
    public void doXor(UBNatural val) throws UBOverFlowException {
        setValue(value ^ val.getInt());
    }

    /**
     * Exclusive Negation
     *
     * @param val of bits to negate
     */
    @Override
    public void doNot(UBNatural val) throws UBOverFlowException {
        if (val.getInt() < 32) {
            long mask = (1 >> val.getInt()) - 1;
            setValue(value ^ mask);
        } else {
            throw new UBOverFlowException("");
        }
    }

    // Basic Predicates
    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean isShort() {
        return true;
    }

    @Override
    public boolean isLong() {
        return true;
    }

    @Override
    public boolean isEqual(UBNatural val) {
        try {
            return (value == val.getInt());
        } catch (UBOverFlowException e) {
            return false;
        }
    }

    @Override
    public boolean isGreater(UBNatural val) {
        try {
            return (value > val.getInt());
        } catch (UBOverFlowException e) {
            return false;
        }
    }

    @Override
    public void add(UBNatural val) {
        setValue(value + val.getLong());
    }

    @Override
    public void sub(UBNatural val) {
        setValue(value - val.getLong());
    }

    @Override
    public void shiftLeft(UBNatural val) {

    }

    @Override
    public void shiftRight(UBNatural val) {
    }

    @Override
    public void multiply(UBNatural val) {
        setValue(value * val.getLong());
    }

    @Override
    public void divide(UBNatural val) {
        setValue(value / val.getLong());
    }

    @Override
    public void divMod(UBNatural val, UBNatural rest) {
        setValue(value % val.getLong());
    }

    @Override
    public void modDiv(UBNatural val, UBNatural quot) {

    }

    @Override
    public void modulate(UBNatural val) {
        setValue(value - val.getLong());

    }

    @Override
    public void power(UBNatural val) {
    }

    @Override
    public void sum(UBNatural op1, UBNatural op2) {
        setValue(op1.getLong());
        setValue(value + op2.getLong());
    }

    @Override
    public void dif(UBNatural op1, UBNatural op2) {
        setValue(op1.getLong());
        setValue(value - op2.getLong());
    }

    @Override
    public void product(UBNatural op1, UBNatural op2) {
        setValue(op1.getLong());
        setValue(value * op2.getLong());
    }

    @Override
    public void quot(UBNatural op1, UBNatural op2) {
        setValue(op1.getLong());
        setValue(value / op2.getLong());
    }

    @Override
    public void quotRest(UBNatural op1, UBNatural op2, UBNatural rest) {
    }

    @Override
    public void restQuot(UBNatural op1, UBNatural op2, UBNatural quot) {
    }

    @Override
    public void rest(UBNatural op1, UBNatural op2) {
        setValue(op1.getLong());
        setValue(value % op2.getLong());
    }

    @Override
    public void invol(UBNatural op1, UBNatural op2) {
    }

    @Override
    public void sqrt() {
        setValue((long) Math.sqrt(value));
    }

    @Override
    public UBNatural clone() {
        return new UBENat32(this);
    }

    @Override
    public boolean isInfinity() {
        return infinity;
    }

    @Override
    public void setInfinity() {
        infinity = true;
    }

    @Override
    public int toInt() throws UBOverFlowException {
        if (infinity) {
            throw new UBOverFlowException("Value is infinity");
        }
        return (int) value;
    }

    @Override
    public long toLong() throws UBOverFlowException {
        if (infinity) {
            throw new UBOverFlowException("Value is infinity");
        }
        return value;
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

    public static UBENat32 ConvertFromNatural(UBNatural value) {
        if (value.getLong() == 127) {
            return UBENat32.Infinity();
        }
        return new UBENat32(value.getLong());
    }

    // TODO: Or should be singleton?
    public static UBENat32 Infinity() {
        UBENat32 value = new UBENat32();
        value.infinity = true;
        return value;
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
            setValue(newValue.getLong());
            serial.end();
        } else {
            XBTChildListener serial = (XBTChildListener) serializationHandler;
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            serial.addAttribute(this);
            serial.end();
        }
    }
}
