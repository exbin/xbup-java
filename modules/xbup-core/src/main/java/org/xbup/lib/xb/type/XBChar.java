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
import org.xbup.lib.xb.parser.tree.XBTreeNode;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBChildListener;
import org.xbup.lib.xb.serial.child.XBChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.xb.serial.child.XBChildProviderSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.util.CopyStreamUtils;

/**
 * Encapsulation class for single character.
 *
 * @version 0.1 wr23.0 2014/03/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBChar implements XBSerializable, XBDeclared {

    private Character value;
    public static long[] xbBlockPath = {1, 3, 1, 2, 2}; // Testing only
    public static long[] xbFormatPath = {1, 3, 1, 2, 0}; // Testing only

    // TODO: Encoding support
    /**
     * Creates a new instance of XBString
     */
    public XBChar() {
        this.value = null;
    }

    public XBChar(Character value) {
        this.value = value;
    }

    @Override
    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBCPBlockDecl(xbBlockPath));
    }

    public Character getValue() {
        return value;
    }

    public void setValue(Character value) {
        this.value = value;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBChildProviderSerialMethod(), new XBTChildProviderSerialMethod(1)})
                : Arrays.asList(new XBSerialMethod[]{new XBChildListenerSerialMethod(), new XBTChildListenerSerialMethod(1)});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        if (serialType == XBSerializationType.FROM_XB) {
            switch (methodIndex) {
                case 0: {
                    XBChildProviderSerialHandler serial = (XBChildProviderSerialHandler) serializationHandler;
                    InputStream source = serial.nextData();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    try {
                        CopyStreamUtils.copyInputStreamToOutputStream(source, stream);
                    } catch (IOException ex) {
                        Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    setValue(new String(stream.toByteArray()).charAt(0));
                    serial.end();
                    break;
                }
                case 1: {
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
                                Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            setValue(new String(stream.toByteArray()).charAt(0));
                            serial.end();
                        }
                    }, 0);
                    serial.end();

                    break;
                }
            }
        } else {
            switch (methodIndex) {
                case 0: {
                    XBChildListener serial = (XBChildListener) serializationHandler;
                    if (getValue() != null) {
                        serial.addData(new ByteArrayInputStream(getValue().toString().getBytes()));
                    } else {
                        serial.addData(new ByteArrayInputStream(new byte[0]));
                    }
                    serial.end();
                    break;
                }
                case 1: {
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
                            serial.addData(new ByteArrayInputStream(getValue().toString().getBytes()));
                            serial.end();
                        }

                    }, 0);
                    serial.end();

                    break;
                }
            }
        }
    }
}
