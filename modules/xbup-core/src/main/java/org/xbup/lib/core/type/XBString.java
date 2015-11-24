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
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildSerializable;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.util.StreamUtils;

/**
 * Encapsulation class for UTF-8 String.
 *
 * @version 0.2.0 2015/11/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBString implements XBPSequenceSerializable {

    private String value;
    static long[] XBUP_BLOCKREV_CATALOGPATH = {1, 3, 1, 2, 0, 0};

    public XBString() {
        this.value = "";
    }

    public XBString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        if (serial.getSerializationMode() == XBSerializationMode.PULL && serial.pullIfEmptyBlock()) {
            value = null;
        } else {
            serial.consist(new DataBlockSerializator());
        }
        serial.end();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBString) {
            return value.equals(((XBString) obj).value);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.value);
        return hash;
    }

    public class DataBlockSerializator implements XBChildSerializable, XBTChildSerializable {

        @Override
        public void serializeFromXB(XBChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin();
            InputStream source = serial.nextData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                StreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBString.class.getName()).log(Level.SEVERE, null, ex);
            }

            setValue(new String(stream.toByteArray(), Charset.forName("UTF-8")));
            serial.end();
        }

        @Override
        public void serializeToXB(XBChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            if (getValue() != null) {
                serial.addData(new ByteArrayInputStream(getValue().getBytes(Charset.forName("UTF-8"))));
            } else {
                serial.addData(new ByteArrayInputStream(new byte[0]));
            }

            serial.end();
        }

        @Override
        public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.pullBegin();
            InputStream source = serial.pullData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                StreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBString.class.getName()).log(Level.SEVERE, null, ex);
            }

            setValue(new String(stream.toByteArray(), Charset.forName("UTF-8")));
            serial.pullEnd();
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
            if (getValue() != null) {
                serial.putData(new ByteArrayInputStream(getValue().getBytes(Charset.forName("UTF-8"))));
            } else {
                serial.putData(new ByteArrayInputStream(new byte[0]));
            }

            serial.putEnd();
        }
    }
}
