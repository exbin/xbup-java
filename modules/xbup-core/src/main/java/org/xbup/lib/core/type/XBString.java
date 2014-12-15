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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBPBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildSerializable;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Encapsulation class for UTF-8 String.
 *
 * @version 0.1.24 2014/12/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBString implements XBTSequenceSerializable {

    private String value;
    public static long[] XB_BLOCK_PATH = {1, 3, 1, 2, 2}; // Testing only
    public static long[] XB_FORMAT_PATH = {1, 3, 1, 2, 0}; // Testing only

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
    public void serializeXB(XBTSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(new XBPBlockDecl(XB_BLOCK_PATH)));
        serial.child(new DataBlockSerializator());
        serial.end();
   }

    public class DataBlockSerializator implements XBChildSerializable {

        @Override
        public void serializeFromXB(XBChildInputSerialHandler serial) throws XBProcessingException, IOException {
            InputStream source = serial.nextData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                CopyStreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBString.class.getName()).log(Level.SEVERE, null, ex);
            }

            setValue(new String(stream.toByteArray(), Charset.forName("UTF-8")));
            serial.end();
        }

        @Override
        public void serializeToXB(XBChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            if (getValue() != null) {
                serial.addData(new ByteArrayInputStream(getValue().getBytes(Charset.forName("UTF-8"))));
            } else {
                serial.addData(new ByteArrayInputStream(new byte[0]));
            }

            serial.end();
        }
    }
}
