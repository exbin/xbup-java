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
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.catalog.declaration.XBCDeclaration;
import org.xbup.lib.core.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDBlockType;
import org.xbup.lib.core.block.declaration.XBDeclared;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import static org.xbup.lib.core.type.XBInt.XB_BLOCK_PATH;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Encapsulation class for natural numbers.
 *
 * @version 0.1 wr24.0 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBNat16 implements XBTChildSerializable, XBDeclared { // XBCL2Streamable

    private int value;
    public static int maxValue = 0xFFFFFFFF;
    public static long[] XB_BLOCK_PATH = {0, 1, 3, 1, 2, 2}; // Testing only
    public static long[] XB_FORMAT_PATH = {0, 1, 3, 1, 2, 0}; // Testing only

    public XBNat16() {
        this.value = 0;
    }

    public XBNat16(int value) {
        this.value = value;
    }

    @Override
    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBCPBlockDecl(XB_BLOCK_PATH));
    }

    public UBNatural getValue() {
        return new UBNat32(value);
    }

    public void setValue(UBNatural value) throws XBProcessingException {
        int newValue = value.getInt();
        if (newValue > maxValue) {
            throw new XBProcessingException("Value too big");
        }

        this.value = newValue;
    }

    public void setValue(int value) throws XBProcessingException {
        this.value = value;
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.nextChild(new DataBlockSerializator());
        serial.end();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.setType(new XBDBlockType(new XBCPBlockDecl(XB_BLOCK_PATH)));
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
                Logger.getLogger(XBNat16.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] newValue = stream.toByteArray();
            setValue((newValue[3] >> 24) + (newValue[2] >> 16) + (newValue[1] >> 8) + newValue[0]);
            serial.end();
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            byte[] data = new byte[4];
            data[0] = (byte) (value & 0xFF);
            data[1] = (byte) ((value << 8) & 0xFF);
            data[2] = (byte) ((value << 16) & 0xFF);
            data[3] = (byte) ((value << 24) & 0xFF);
            serial.addData(new ByteArrayInputStream(data));
            serial.end();
        }
    }
}
