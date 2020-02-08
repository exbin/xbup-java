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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * UBNatural stored as long value (limited value capacity to 32 bits).
 *
 * @version 0.2.1 2017/05/09
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class UBNat32 implements UBNatural, XBPSequenceSerializable {

    public static final long MAX_VALUE = 4294967295l;
    private long value;

    public UBNat32() {
        value = 0;
    }

    public UBNat32(int value) {
        this.value = value;
    }

    public UBNat32(long value) {
        this.value = value;
    }

    public UBNat32(UBNatural nat) {
        this.value = nat.getLong();
    }

    @Override
    public void setValue(int value) throws UBOverFlowException {
        if (value < 0) {
            throw new UBOverFlowException("Can't set negative value to natural number");
        }

        this.value = value;
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
        if (value < 0) {
            throw new UBOverFlowException("Can't set negative value to natural number");
        } else if (value > MAX_VALUE) {
            throw new UBOverFlowException("Value too big");
        } else {
            this.value = value;
        }
    }

    @Override
    public int getInt() throws UBOverFlowException {
        return (int) value;
    }

    @Override
    public long getLong() throws UBOverFlowException {
        return value;
    }

    @Override
    public boolean isZero() {
        return value == 0;
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
    public int hashCode() {
        return Long.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final UBNat32 other = (UBNat32) obj;
        return this.value == other.value;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        byte buf[] = new byte[1];
        readBuf(stream, buf);
        long input = (char) buf[0] & 0xFF;
        if (input < 0x80) {
            value = input;
            return 1;
        } else if (input < 0xC0) {
            value = (input & 0x3F) << 8;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) + 0x80;
            return 2;
        } else if (input < 0xE0) {
            value = (input & 0x1F) << 16;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) << 8;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) + 0x4080;
            return 3;
        } else if (input < 0xF0) {
            value = (input & 0xF) << 24;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) << 16;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) << 8;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) + 0x204080;
            return 4;
        } else if (input < 0xF8) {
            value = (input & 0x7) << 32;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) << 24;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) << 16;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) << 8;
            readBuf(stream, buf);
            value += (buf[0] & 0xFF) + 0x10204080;
            return 5;
        }

        throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
    }

    private void readBuf(InputStream stream, byte[] buf) throws IOException {
        if (stream.read(buf) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (value < 0x80) {
            stream.write((char) value);
            return 1;
        } else if (value < 0x4080) {
            byte[] out = new byte[2];
            long outValue = value - 0x80;
            out[0] = (byte) ((outValue >> 8) + 0x80);
            out[1] = (byte) (outValue & 0xFF);
            stream.write(out);
            return 2;
        } else if (value < 0x204080) {
            long outValue = value - 0x4080;
            byte[] out = new byte[3];
            out[0] = (byte) ((outValue >> 16) + 0xC0);
            out[1] = (byte) ((outValue >> 8) & 0xFF);
            out[2] = (byte) (outValue & 0xFF);
            stream.write(out);
            return 3;
        } else if (value < 0x10204080) {
            long outValue = value - 0x204080;
            byte[] out = new byte[4];
            out[0] = (byte) ((outValue >> 24) + 0xE0);
            out[1] = (byte) ((outValue >> 16) & 0xFF);
            out[2] = (byte) ((outValue >> 8) & 0xFF);
            out[3] = (byte) (outValue & 0xFF);
            stream.write(out);
            return 4;
        } else {
            long outValue = value - 0x10204080;
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
        if (value < 0x80) {
            return 1;
        } else if (value < 0x4080) {
            return 2;
        } else if (value < 0x204080) {
            return 3;
        } else if (value < 0x10204080) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        serial.attribute(this);
        serial.end();
    }

    @Override
    public void setNaturalZero() {
        value = 0;
    }

    @Override
    public void setNaturalInt(int intValue) throws UBOverFlowException {
        setValue(intValue);
    }

    @Override
    public void setNaturalLong(long longValue) throws UBOverFlowException {
        setValue(longValue);
    }

    @Override
    public void convertFromNatural(UBNatural natural) {
        setValue(natural.getLong());
    }

    @Override
    public boolean isNaturalZero() {
        return isZero();
    }

    @Override
    public int getNaturalInt() throws UBOverFlowException {
        return getInt();
    }

    @Override
    public long getNaturalLong() throws UBOverFlowException {
        return getLong();
    }

    @Nonnull
    @Override
    public UBNatural convertToNatural() {
        return this;
    }
}
