/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.ubnumber.UBBoolean;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * UBBoolean stored as boolean value.
 *
 * @version 0.1.25 2015/02/09
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class UBBool implements UBBoolean, XBPSequenceSerializable {

    private boolean value;

    public UBBool() {
        value = false;
    }

    public UBBool(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getBoolean() throws UBOverFlowException {
        return value;
    }

    @Override
    public void setValue(boolean value) throws UBOverFlowException {
        this.value = value;
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

        return value ? 1 : 0;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        byte[] buffer = new byte[1];
        if (stream.read(buffer) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        long input = (char) buffer[0] & 0xFF;
        if (input > 1 || input < 0) {
            throw new XBProcessingException("Unsupported boolean value", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        value = input == 1;
        return 1;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        stream.write((char) (value ? 1 : 0));
        return 1;
    }

    @Override
    public int getSizeUB() {
        return 1;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            setNaturalInt(serial.pullByteAttribute());
        } else {
            serial.putAttribute(value ? 1 : 0);
        }
        serial.end();
    }

    @Override
    public void setNaturalZero() {
        value = false;
    }

    @Override
    public void setNaturalInt(int intValue) throws UBOverFlowException {
        if (intValue < 0 || intValue > 1) {
            throw new UBOverFlowException("Value must be in range 0..1 for boolean");
        }
    }

    @Override
    public void setNaturalLong(long longValue) throws UBOverFlowException {
        if (longValue < 0 || longValue > 1) {
            throw new UBOverFlowException("Value must be in range 0..1 for boolean");
        }
    }

    @Override
    public boolean isNaturalZero() {
        return !value;
    }

    @Override
    public int getNaturalInt() throws UBOverFlowException {
        return value ? 1 : 0;
    }

    @Override
    public long getNaturalLong() throws UBOverFlowException {
        return value ? 1 : 0;
    }

    @Override
    public void convertFromNatural(@Nonnull UBNatural natural) {
        setNaturalInt(natural.getInt());
    }

    @Nonnull
    @Override
    public UBNatural convertToNatural() {
        return new UBNat32(getNaturalInt());
    }
}
