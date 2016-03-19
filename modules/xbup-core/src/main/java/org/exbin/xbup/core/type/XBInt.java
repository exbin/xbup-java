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
package org.exbin.xbup.core.type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.child.XBTChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.ubnumber.UBInteger;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBInt32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Encapsulation class for integer numbers.
 *
 * @version 0.1.24 2014/08/23
 * @author ExBin Project (http://exbin.org)
 */
public class XBInt implements XBTChildSerializable {

    private UBInteger value;
    private UBNatural bitSize;
    static long[] XBUP_BLOCKREV_CATALOGPATH = {1, 1, 1, 1, 0};
    static long[] XBUP_FORMATREV_CATALOGPATH = {0, 1, 3, 1, 2, 0, 0};

    public XBInt() {
        this.value = new UBInt32();
        this.bitSize = new UBNat32(16);
    }

    public XBInt(UBInteger value) {
        this.value = value;
    }

    public UBInteger getValue() {
        return value;
    }

    public void setValue(UBInteger value) {
        this.value = value;
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.pullBegin();
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

    public class DataBlockSerializator implements XBTChildSerializable {

        @Override
        public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.pullBegin();
            InputStream source = serial.pullData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                StreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBInt.class.getName()).log(Level.SEVERE, null, ex);
            }
            //                setValue(new String(stream.toByteArray()));
            serial.pullEnd();
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
            //                serial.putData(new ByteArrayInputStream(getValue().getBytes()));
            serial.putEnd();
        }
    }
}
