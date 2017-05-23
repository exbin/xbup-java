/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.ubnumber.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.ubnumber.UBENatural;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * UBENatural stored as long value (limited value capacity to 32 bits).
 *
 * @version 0.1.25 2015/02/09
 * @author ExBin Project (http://exbin.org)
 */
public class UBENat32 implements UBENatural {

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

    /**
     * This is copy constructor.
     */
    private UBENat32(@Nonnull UBENat32 value) {
        infinity = value.isInfinity();
        this.value = value.getLong();
    }

    @Override
    public void setValue(int value) throws UBOverFlowException {
        if (value < 0) {
            throw new UBOverFlowException("Can't set negative value to natural number");
        }

        infinity = false;
        this.value = value;
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
        if (value < 0) {
            throw new UBOverFlowException("Can't set negative value to natural number");
        } else if (value > MAX_VALUE) {
            throw new UBOverFlowException("Value too big");
        }

        this.value = value;
        infinity = false;
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
    public int getInt() throws UBOverFlowException {
        if (infinity) {
            throw new UBOverFlowException("Infinity value cannot be converted to int");
        }

        return (int) value;
    }

    @Override
    public long getLong() throws UBOverFlowException {
        if (infinity) {
            throw new UBOverFlowException("Infinity value cannot be converted to long");
        }

        return value;
    }

    @Override
    public boolean isZero() {
        return infinity == false && value == 0;
    }

    @Override
    public long getSegmentCount() {
        return 1;
    }

    @Override
    public long getValueSegment(long segmentIndex) {
        if (segmentIndex != 0) {
            throw new IndexOutOfBoundsException();
        }

        return value;
    }

    @Override
    public int fromStreamUB(@Nonnull InputStream stream) throws IOException, XBProcessingException {
        infinity = false;
        byte[] buffer = new byte[1];
        readBuf(stream, buffer);
        long input = (char) buffer[0] & 0xFF;
        if (input < 0x7F) {
            value = buffer[0];
            return 1;
        } else if (input == 0x7F) {
            infinity = true;
            return 1;
        } else if (input < 0xC0) {
            value = (input & 0x7F) << 8;
            readBuf(stream, buffer);
            value += ((char) buffer[0] & 0xFF) + 0x7F;
            return 2;
        } else if (input < 0xE0) {
            value = (input & 0x3F) << 16;
            readBuf(stream, buffer);
            value += ((char) buffer[0] & 0xFF) << 8;
            readBuf(stream, buffer);
            value += ((char) buffer[0] & 0xFF) + 0x407F;
            return 3;
        } else if (input < 0xF0) {
            value = (input & 0x1F) << 24;
            readBuf(stream, buffer);
            value += ((char) buffer[0] & 0xFF) << 16;
            readBuf(stream, buffer);
            value += ((char) buffer[0] & 0xFF) << 8;
            readBuf(stream, buffer);
            value += ((char) buffer[0] & 0xFF) + 0x20407F;
            return 4;
        } else if (input < 0xF8) {
            value = (input & 0x0F) << 32;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 24;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 16;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 8;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) + 0x1020407F;
            if (value >= 0x1020407F) {
                return 5;
            }
        }

        throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
    }

    private void readBuf(@Nonnull InputStream stream, @Nonnull byte[] buf) throws IOException {
        if (stream.read(buf) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
    }

    @Override
    public int toStreamUB(@Nonnull OutputStream stream) throws IOException {
        char output;
        if (infinity) {
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
        if (infinity) {
            return INFINITY_SIZE_UB;
        }

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
    }

    @Nonnull
    public static UBENat32 getInfinity() {
        UBENat32 value = new UBENat32();
        value.infinity = true;
        return value;
    }

    @Override
    public void setNaturalZero() {
        setNaturalLong(0);
    }

    @Override
    public void setNaturalInt(int intValue) throws UBOverFlowException {
        setNaturalLong(value);
    }

    @Override
    public void setNaturalLong(long longValue) throws UBOverFlowException {
        if (longValue < 127) {
            value = longValue;
        } else if (longValue == 127) {
            setInfinity();
        } else {
            value = longValue - 1;
        }
    }

    @Override
    public boolean isNaturalZero() {
        return value == 0;
    }

    @Override
    public int getNaturalInt() throws UBOverFlowException {
        return (int) getNaturalLong();
    }

    @Override
    public long getNaturalLong() throws UBOverFlowException {
        if (infinity) {
            return 127;
        } else if (value < 127) {
            return value;
        }

        return value + 1;
    }

    @Override
    public void convertFromNatural(@Nonnull UBNatural nat) {
        setNaturalLong(nat.getLong());
    }

    @Nonnull
    @Override
    public UBNatural convertToNatural() {
        return new UBNat32(getNaturalLong());
    }

}
