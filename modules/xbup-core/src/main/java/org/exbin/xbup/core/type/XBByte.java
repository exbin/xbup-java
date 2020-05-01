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
package org.exbin.xbup.core.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.serial.child.XBTChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Encapsulation class for natural numbers - 8 bits.
 *
 * @version 0.1.24 2014/08/23
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBByte implements XBTChildSerializable {

    private byte value;

    public static final int MAXIMUM_VALUE = 0xFF;
    static final long[] XBUP_BLOCKREV_CATALOGPATH = {0, 1, 3, 1, 2, 2, 0};
    static final long[] XBUP_FORMATREV_CATALOGPATH = {0, 1, 3, 1, 2, 0, 0};

    public XBByte() {
        this.value = 0;
    }

    public XBByte(byte value) {
        this.value = value;
    }

    public UBNatural getValue() {
        return new UBNat32(value);
    }

    public void setValue(UBNatural value) throws XBProcessingException {
        int newValue = value.getInt();
        if (newValue > MAXIMUM_VALUE) {
            throw new UBOverFlowException("Value is too big");
        }

        this.value = (byte) newValue;
    }

    public void setValue(byte value) throws XBProcessingException {
        this.value = value;
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.pullBegin();
        if (!serial.pullType().equals(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH))) {
            throw new XBProcessingException("Unexpected type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
        }

        serial.pullChild(new DataBlockSerializator());
        serial.pullEnd();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.putType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        serial.putChild(new DataBlockSerializator());
        serial.putEnd();
    }

    @ParametersAreNonnullByDefault
    public class DataBlockSerializator implements XBTChildSerializable {

        @Override
        public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.pullBegin();
            InputStream source = serial.pullData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                StreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBByte.class.getName()).log(Level.SEVERE, null, ex);
            }

            byte[] newValue = stream.toByteArray();
            setValue(newValue[0]);
            serial.pullEnd();
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
            byte[] data = new byte[1];
            data[0] = value;
            serial.putData(new ByteArrayInputStream(data));
            serial.putEnd();
        }
    }
}
