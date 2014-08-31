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
package org.xbup.lib.core.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.declaration.catalog.XBPBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.local.XBDBlockType;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Encapsulation class for natural numbers - 8 bits (known as byte).
 *
 * @version 0.1.24 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBByte implements XBTChildSerializable {

    private byte value;

    public static int MAXIMUM_VALUE = 0xFF;
    public static long[] XB_BLOCK_PATH = {0, 1, 3, 1, 2, 2}; // Testing only
    public static long[] XB_FORMAT_PATH = {0, 1, 3, 1, 2, 0}; // Testing only

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
        serial.begin();
        if (!serial.getType().equals(new XBDBlockType(new XBPBlockDecl(XB_BLOCK_PATH)))) {
            throw new XBProcessingException("Unexpected type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
        }

        serial.nextChild(new DataBlockSerializator());
        serial.end();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.setType(new XBDBlockType(new XBPBlockDecl(XB_BLOCK_PATH)));
        serial.addChild(new DataBlockSerializator());
        serial.end();
    }

    public class DataBlockSerializator implements XBTChildSerializable {

        @Override
        public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin();
            InputStream source = serial.nextData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                CopyStreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBByte.class.getName()).log(Level.SEVERE, null, ex);
            }

            byte[] newValue = stream.toByteArray();
            setValue(newValue[0]);
            serial.end();
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            byte[] data = new byte[1];
            data[0] = value;
            serial.addData(new ByteArrayInputStream(data));
            serial.end();
        }
    }
}
