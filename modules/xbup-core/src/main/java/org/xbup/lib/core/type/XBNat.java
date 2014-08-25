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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.XBDeclared;
import org.xbup.lib.core.catalog.declaration.XBCDeclaration;
import org.xbup.lib.core.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Encapsulation class for natural numbers.
 *
 * @version 0.1.24 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBNat implements XBTChildSerializable, XBDeclared {

    private UBNatural value;
    private UBNatural bitSize;
    public static long[] XB_BLOCK_PATH = {1, 1, 1, 0}; // Testing only
    public static long[] XB_FORMAT_PATH = {0, 1, 3, 1, 2, 0}; // Testing only

    public XBNat() {
        this.value = new UBNat32();
        this.bitSize = new UBNat32(16);
    }

    public XBNat(UBNatural value) {
        this.value = value;
    }

    @Override
    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBCPBlockDecl(XB_BLOCK_PATH));
    }

    public UBNatural getValue() {
        return value;
    }

    public void setValue(UBNatural value) {
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
                Logger.getLogger(XBNat.class.getName()).log(Level.SEVERE, null, ex);
            }
            //                setValue(new String(stream.toByteArray()));
            serial.end();
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            //                serial.addData(new ByteArrayInputStream(getValue().getBytes()));
            serial.end();

        }
    }

    /*
     public void readXBL2Stream(XBL2InputStream stream) throws XBProcessingException, XBParseException {
     try {
     XBL2StreamChecker checker = new XBL2StreamChecker(stream);
     checker.beginXBL2();
     checker.typeXBL2(new XBL1CPBlockDecl(xbBlockPath));
     checker.attribXBL2((UBNatural) new UBNat32(1)); // Data block pointer
     checker.beginXBL2();
     setValue(new String(checker.dataXBL2()));
     checker.endXBL2();
     checker.endXBL2();
     } catch (XBParseException ex) {
     Logger.getLogger(XBString.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
     Logger.getLogger(XBString.class.getName()).log(Level.SEVERE, null, ex);
     }
     }

     public void writeXBL2Stream(XBL2OutputStream stream) throws XBProcessingException, IOException {
     try {
     XBL2Listener listener = XBL2DefaultEventListener.toXBL2Listener(stream);
     listener.beginXBL2(false);
     listener.typeXBL2(new XBL1CPBlockDecl(xbBlockPath));
     listener.attribXBL2((UBNatural) new UBNat32(1)); // Data block pointer
     listener.beginXBL2(false);
     listener.dataXBL2(getValue().getBytes());
     listener.endXBL2();
     listener.endXBL2();
     } catch (XBParseException ex) {
     Logger.getLogger(XBString.class.getName()).log(Level.SEVERE, null, ex);
     }
     }
     */
}
