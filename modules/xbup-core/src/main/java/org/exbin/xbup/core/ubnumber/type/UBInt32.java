/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.ubnumber.UBInteger;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * UBInteger stored as int value (limited value capacity to 32 bits).
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class UBInt32 implements UBInteger, XBPSequenceSerializable {

    private long value;

    public UBInt32() {
        value = 0;
    }

    public UBInt32(int value) {
        this.value = value;
    }

    public UBInt32(long value) {
        this.value = value;
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
    public void setValue(int value) throws UBOverFlowException {
        this.value = value;
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
        this.value = (int) value;
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

        final UBInt32 other = (UBInt32) obj;
        return this.value == other.value;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            convertFromNatural(serial.pullAttribute().convertToNatural());
        } else {
            serial.putAttribute(convertToNatural());
        }
        serial.end();
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        byte[] buffer = new byte[1];
        readBuf(stream, buffer);
        long input = (char) buffer[0] & 0xFF;
        if (input < 0x80) {
            boolean negativeValue = (input & 0x40) > 0;
            value = input & 0x3F;
            value = negativeValue ? value -= 0x40 : value;
            return 1;
        } else if (input < 0xC0) {
            boolean negativeValue = (input & 0x20) > 0;
            value = (input & 0x1F) << 8;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF);
            value = negativeValue ? value -= 0x2040 : value + 0x40;
            return 2;
        } else if (input < 0xE0) {
            boolean negativeValue = (input & 0x10) > 0;
            value = (input & 0xF) << 16;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 8;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF);
            value = negativeValue ? value -= 0x102040 : value + 0x2040;
            return 3;
        } else if (input < 0xF0) {
            boolean negativeValue = (input & 0x8) > 0;
            value = (input & 0x7) << 24;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 16;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 8;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF);
            value = negativeValue ? value -= 0x8102040 : value + 0x102040;
            return 4;
        } else if (input < 0xF8) {
            boolean negativeValue = (input & 0x4) > 0;
            if (negativeValue) {
                throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
            }
            value = (input & 0x3) << 32;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 24;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 16;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF) << 8;
            readBuf(stream, buffer);
            value += (buffer[0] & 0xFF);
            value += 0x8102040;
            return 5;
        }

        throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
    }

    private void readBuf(InputStream stream, byte[] buffer) throws IOException {
        if (stream.read(buffer) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (value < 0) {
            if (value >= -0x40) {
                stream.write((char) (0x80 + value));
                return 1;
            } else if (value >= -0x2040) {
                byte[] out = new byte[2];
                long outValue = 0x4040 + value; // 4080 -40
                out[0] = (byte) ((outValue >> 8) + 0x80);
                out[1] = (byte) (outValue & 0xFF);
                stream.write(out);
                return 2;
            } else if (value >= -0x102040) {
                long outValue = 0x202040 + value;
                byte[] out = new byte[3];
                out[0] = (byte) ((outValue >> 16) + 0xC0);
                out[1] = (byte) ((outValue >> 8) & 0xFF);
                out[2] = (byte) (outValue & 0xFF);
                stream.write(out);
                return 3;
            } else if (value >= -0x8102040) {
                long outValue = 0x10102040 + value;
                byte[] out = new byte[4];
                out[0] = (byte) ((outValue >> 24) + 0xE0);
                out[1] = (byte) ((outValue >> 16) & 0xFF);
                out[2] = (byte) ((outValue >> 8) & 0xFF);
                out[3] = (byte) (outValue & 0xFF);
                stream.write(out);
                return 4;
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        } else if (value < 0x40) {
            stream.write((char) value);
            return 1;
        } else if (value < 0x2040) {
            byte[] out = new byte[2];
            long outValue = value - 0x40;
            out[0] = (byte) ((outValue >> 8) + 0x80);
            out[1] = (byte) (outValue & 0xFF);
            stream.write(out);
            return 2;
        } else if (value < 0x102040) {
            long outValue = value - 0x2040;
            byte[] out = new byte[3];
            out[0] = (byte) ((outValue >> 16) + 0xC0);
            out[1] = (byte) ((outValue >> 8) & 0xFF);
            out[2] = (byte) (outValue & 0xFF);
            stream.write(out);
            return 3;
        } else if (value < 0x8102040) {
            long outValue = value - 0x102040;
            byte[] out = new byte[4];
            out[0] = (byte) ((outValue >> 24) + 0xE0);
            out[1] = (byte) ((outValue >> 16) & 0xFF);
            out[2] = (byte) ((outValue >> 8) & 0xFF);
            out[3] = (byte) (outValue & 0xFF);
            stream.write(out);
            return 4;
        } else {
            long outValue = value - 0x8102040;
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
        if (value < 0) {
            if (value >= -0x40) {
                return 1;
            } else if (value >= -0x2040) {
                return 2;
            } else if (value >= -0x102040) {
                return 3;
            } else if (value >= -0x8102040) {
                return 4;
            } else {
                return 5;
            }
        } else if (value < 0x40) {
            return 1;
        } else if (value < 0x2040) {
            return 2;
        } else if (value < 0x102040) {
            return 3;
        } else if (value < 0x8102040) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public void setNaturalZero() {
        setNaturalLong(0);
    }

    @Override
    public void setNaturalInt(int intValue) throws UBOverFlowException {
        setNaturalLong(intValue);
    }

    @Override
    public void setNaturalLong(long longValue) throws UBOverFlowException {
        if (longValue < 0x40) {
            value = longValue;
        } else if (longValue < 0x80) {
            value = -0x80 + longValue;
        } else if (longValue < 0x2080) {
            value = longValue - 0x40;
        } else if (longValue < 0x4080) {
            value = -0x4080 - 0x40 + longValue;
        } else if (longValue < 0x104080) {
            value = longValue - 0x2080;
        } else if (longValue < 0x204080) {
            value = -0x204080 - 0x2080 + longValue;
        } else if (longValue < 0x8204080) {
            value = longValue - 0x104080;
        } else if (longValue < 0x10204080) {
            value = -0x10204080 - 0x104080 + longValue;
        } else {
            value = longValue - 0x8204080;
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
        if (value < 0) {
            if (value >= -0x40) {
                return 0x80 - value;
            } else if (value >= -0x2040) {
                return 0x2080 - value;
            } else if (value >= -0x102040) {
                return 0x102080 - value;
            } else if (value >= -0x8102040) {
                return 0x8102080 - value;
            } else {
                throw new UBOverFlowException("Unable to convert big negative value to natural");
            }
        } else if (value < 0x40) {
            return value;
        } else if (value < 0x2040) {
            return value + 0x40;
        } else if (value < 0x8102040) {
            return value + 0x2040;
        } else {
            return value + 0x8102040;
        }
    }

    @Override
    public void convertFromNatural(UBNatural nat) {
        setNaturalLong(nat.getLong());
    }

    @Nonnull
    @Override
    public UBNatural convertToNatural() {
        return new UBNat32(getNaturalLong());
    }
}
