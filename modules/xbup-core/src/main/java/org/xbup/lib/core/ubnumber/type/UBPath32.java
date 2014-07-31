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
package org.xbup.lib.core.ubnumber.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerialHandler;
import org.xbup.lib.core.serial.XBSerialMethod;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.XBSerializationType;
import org.xbup.lib.core.serial.child.XBTChildListener;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.core.serial.child.XBTChildProvider;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.UBStreamable;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * UBPath with 32bit long items. TODO: move to types
 *
 * @version 0.1 wr24.0 2014/06/07
 * @author XBUP Project (http://xbup.org)
 */
public class UBPath32 implements UBStreamable, XBSerializable {

    private long[] path;

    public UBPath32() {
        path = new long[0];
    }

    public UBPath32(long[] path) {
        setValue(path);
    }

    public UBPath32(Long[] catalogPath) {
        setValue(catalogPath);
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        UBNat32 arrayLength = new UBNat32();
        int length = arrayLength.fromStreamUB(stream);
        path = new long[arrayLength.getInt()];
        for (int i = 0; i < path.length; i++) {
            UBNat32 value = new UBNat32();
            length += value.fromStreamUB(stream);
            path[i] = value.getLong();
        }
        return length;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        int length = (new UBNat32(path.length)).toStreamUB(stream);
        for (int i = 0; i < path.length; i++) {
            length += (new UBNat32(path[i])).toStreamUB(stream);
        }
        return length;
    }

    @Override
    public int getSizeUB() {
        int length = (new UBNat32(path.length)).getSizeUB();
        for (int i = 0; i < path.length; i++) {
            length += (new UBNat32(path[i])).getSizeUB();
        }
        return length;
    }

    public void setValue(long[] path) throws UBOverFlowException {
        this.path = path;
    }

    private void setValue(Long[] catalogPath) {
        path = new long[catalogPath.length];
        for (int i = 0; i < catalogPath.length; i++) {
            path[i] = catalogPath[i].longValue();
        }
    }

    public long[] getPath() throws UBOverFlowException {
        return path;
    }

    public Long[] getLongPath() throws UBOverFlowException {
        Long[] longPath = new Long[path.length];
        for (int i = 0; i < path.length; i++) {
            longPath[i] = new Long(path[i]);
        }
        return longPath;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        if (serialType == XBSerializationType.FROM_XB) {
            XBTChildProvider serial = (XBTChildProvider) serializationHandler;
            serial.begin();
            UBNatural length = serial.nextAttribute();
            path = new long[length.getInt()];
            for (int i = 0; i < path.length; i++) {
                path[i] = serial.nextAttribute().getLong();
            }
            serial.end();
        } else {
            XBTChildListener serial = (XBTChildListener) serializationHandler;
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            serial.addAttribute(new UBNat32(path.length));
            for (int i = 0; i < path.length; i++) {
                serial.addAttribute(new UBNat32(path[i]));
            }
            serial.end();
        }
    }

}
