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
package org.xbup.lib.core.ubnumber.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.ubnumber.UBInteger;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * UBInteger stored as int value (limited value capacity to 32 bits).
 *
 * @version 0.1.24 2015/01/07
 * @author XBUP Project (http://xbup.org)
 */
public class UBInt32 implements UBInteger, XBPSequenceSerializable {

    private long value;

    public UBInt32() {
        value = 0;
    }

    public UBInt32(int value) {
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
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCK_TYPE));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            convertFromNatural(serial.pullAttribute());
        } else {
            serial.putAttribute(convertToNatural());
        }
        serial.end();
    }
    
    public void convertFromNatural(UBNatural nat) {
        long natLong = nat.getLong();
        if (natLong < 0x40) {
            value = natLong;
        } else if (natLong < 0x80) {
            value = 0x80 - natLong;
        } else if (natLong < 0x2080) {
            value = natLong - 0x40;
        } else if (natLong < 0x4080) {
            value = 0x4080 + 0x40 - natLong;
        } else if (natLong < 0x104080) {
            value = natLong - 0x2080;
        } else if (natLong < 0x204080) {
            value = 0x204080 + 0x2080 - natLong;
        } else if (natLong < 0x8204080) {
            value = natLong - 0x104080;
        } else if (natLong < 0x10204080) {
            value = 0x10204080 + 0x104080 - natLong;
        } else {
            value = natLong - 0x8204080;
        }
    }
    
    public UBNatural convertToNatural() {
        if (value < 0) {
            if (value >= -0x40) {
                return new UBNat32(0x80 - value);
            } else if (value >= -0x2040) {
                return new UBNat32(0x2080 - value);
            } else if (value >= -0x102040) {
                return new UBNat32(0x102080 - value);
            } else if (value >= -0x8102040) {
                return new UBNat32(0x8102080 - value);
            } else {
                throw new UBOverFlowException("Unable to convert big negative value to natural");
            }
        } else {
            if (value < 0x40) {
                return new UBNat32(value);
            } else if (value < 0x2040) {
                return new UBNat32(value + 0x40);
            } else if (value < 0x8102040) {
                return new UBNat32(value + 0x2040);
            } else {
                return new UBNat32(value + 0x8102040);
            }
        }
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        byte buf[] = new byte[1];
        if (stream.read(buf) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        long input = (char) buf[0] & 0xFF;
        if (input < 0x80) {
            boolean negativeValue = (input & 0x40) > 0;
            value = input & 0x3F;
            value = negativeValue ? value -= 0x40 : value;
            return 1;
        } else if (input < 0xC0) {
            boolean negativeValue = (input & 0x20) > 0;
            value = (input & 0x1F) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF);
            value = negativeValue ? value -= 0x2040 : value + 0x40;
            return 2;
        } else if (input < 0xE0) {
            boolean negativeValue = (input & 0x10) > 0;
            value = (input & 0xF) << 16;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF);
            value = negativeValue ? value -= 0x102040 : value + 0x2040;
            return 3;
        } else if (input < 0xF0) {
            boolean negativeValue = (input & 0x8) > 0;
            value = (input & 0x7) << 24;
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
            value += (buf[0] & 0xFF);
            value = negativeValue ? value -= 0x8102040 : value + 0x102040;
            return 4;
        } else if (input < 0xF8) {
            boolean negativeValue = (input & 0x4) > 0;
            if (negativeValue) {
                throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
            }
            value = (input & 0x3) << 32;
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
            value += (buf[0] & 0xFF);
            value += 0x8102040;
            return 5;
        }

        throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
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
        } else {
            if (value < 0x40) {
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
        } else {
            if (value < 0x40) {
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
    }
}
