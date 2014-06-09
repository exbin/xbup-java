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

import java.io.ByteArrayInputStream;
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
public class XBNat16 implements XBSerializable, XBDeclared { // XBCL2Streamable

    private int value;
    public static int maxValue = 0xFFFFFFFF;
    public static long[] xbBlockPath = {0, 1, 3, 1, 2, 2}; // Testing only
    public static long[] xbFormatPath = {0, 1, 3, 1, 2, 0}; // Testing only

    // TODO: Encoding support
    /**
     * Creates a new instance of XBString
     */
    public XBNat16() {
        this.value = 0;
    }

    public XBNat16(int value) {
        this.value = value;
    }

    @Override
    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBCPBlockDecl(xbBlockPath));
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
                        Logger.getLogger(XBNat16.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    byte[] newValue = stream.toByteArray();
                    setValue((newValue[3] >> 24) + (newValue[2] >> 16) + (newValue[1] >> 8) + newValue[0]);
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
                    byte[] data = new byte[4];
                    data[0] = (byte) (value & 0xFF);
                    data[1] = (byte) ((value << 8) & 0xFF);
                    data[2] = (byte) ((value << 16) & 0xFF);
                    data[3] = (byte) ((value << 24) & 0xFF);
                    serial.addData(new ByteArrayInputStream(data));
                    serial.end();
                }
            }, 0);
            serial.end();
        }
    }
}
