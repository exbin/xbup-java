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
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.XBSerialHandler;
import org.xbup.lib.core.serial.XBSerialMethod;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.XBSerializationType;
import org.xbup.lib.core.serial.child.XBTChildListener;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.core.serial.child.XBTChildProvider;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * UBNatural stored as long value (limited value capacity to 32 bits).
 *
 * @version 0.1 wr24.0 2014/08/12
 * @author XBUP Project (http://xbup.org)
 */
public class UBNat32 implements UBNatural, XBSerializable {

    private static long MAX_VALUE = 4294967295l;
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
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        byte buf[] = new byte[1];
        if (stream.read(buf) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        long input = (char) buf[0] & 0xFF;
        if (input < 0x80) {
            value = buf[0];
            return 1;
        } else if (input < 0xC0) {
            value = (input & 0x7F) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) + 0x80;
            return 2;
        } else if (input < 0xE0) {
            value = (input & 0x3F) << 16;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) << 8;
            if (stream.read(buf) < 0) {
                throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
            value += (buf[0] & 0xFF) + 0x4080;
            return 3;
        } else if (input < 0xF0) {
            value = (input & 0x1F) << 24;
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
            value += (buf[0] & 0xFF) + 0x204080;
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
            value += (buf[0] & 0xFF) + 0x10204080;
            if (value >= 0x10204080) {
                return 5;
            }
        }

        throw new XBProcessingException("Value is too big for 32-bit value", XBProcessingExceptionType.UNSUPPORTED);
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

    // TODO Create XBNatural
    /*
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
     */
}
