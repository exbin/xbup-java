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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * UBENatural stored as long value (limited value capacity to 32 bits).
 *
 * @version 0.1.24 2015/01/10
 * @author XBUP Project (http://xbup.org)
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
    private UBENat32(UBENat32 value) {
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
    public void convertFromNatural(UBNatural nat) {
        if (nat.getLong() < 127) {
            value = nat.getLong();
        } else if (nat.getLong() == 127) {
            setInfinity();
        }

        value = nat.getLong() - 1;
    }

    @Override
    public UBNatural convertToNatural() {
        if (infinity) {
            return new UBNat32(127);
        } else if (value < 127) {
            return new UBNat32(value);
        }

        return new UBNat32(value + 1);
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        infinity = false;
        byte buf[] = new byte[1];
        if (stream.read(buf) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        long input = (char) buf[0] & 0xFF;
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

        throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
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

    // TODO: Or should be singleton?
    public static UBENat32 getInfinity() {
        UBENat32 value = new UBENat32();
        value.infinity = true;
        return value;
    }
}
