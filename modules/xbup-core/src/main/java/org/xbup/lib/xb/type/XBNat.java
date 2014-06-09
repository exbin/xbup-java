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
package org.xbup.lib.xb.type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.block.declaration.XBDeclared;
import org.xbup.lib.xb.catalog.declaration.XBCDeclaration;
import org.xbup.lib.xb.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;
import org.xbup.lib.xb.util.CopyStreamUtils;

/**
 * Encapsulation class for natural numbers.
 *
 * @version 0.1 wr23.0 2014/03/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBNat implements XBSerializable, XBDeclared {

    private UBNatural value;
    private UBNatural bitSize;
    public static long[] xbBlockPath = {1, 1, 1, 0}; // Testing only
    public static long[] xbFormatPath = {0, 1, 3, 1, 2, 0}; // Testing only

    // TODO: Encoding support
    /**
     * Creates a new instance of XBString
     */
    public XBNat() {
        this.value = new UBNat32();
        this.bitSize = new UBNat32(16);
    }

    public XBNat(UBNatural value) {
        this.value = value;
    }

    @Override
    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBCPBlockDecl(xbBlockPath));
    }

    public UBNatural getValue() {
        return value;
    }

    public void setValue(UBNatural value) {
        this.value = value;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
        // TODO return new XBDBlockType(new XBCPBlockDecl(xbBlockPath));
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        if (serialType == XBSerializationType.FROM_XB) {
            XBTChildProvider serial = (XBTChildProvider) serializationHandler;
            serial.begin();
            serial.nextChild(new XBSerializable() {
                @Override
                public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                    return Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()});
                }

                @Override
                public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    XBTChildProvider serial = (XBTChildProvider) serializationHandler;
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
            }, 0);
            serial.end();
        } else {
            XBTChildListener serial = (XBTChildListener) serializationHandler;
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            serial.addChild(new XBSerializable() {
                @Override
                public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                    return Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
                }

                @Override
                public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    XBTChildListener serial = (XBTChildListener) serializationHandler;
                    serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
                    //                serial.addData(new ByteArrayInputStream(getValue().getBytes()));
                    serial.end();
                }
            }, 0);
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
