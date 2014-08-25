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

import org.xbup.lib.core.ubnumber.UBInteger;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * UBInteger stored as int value (limited value capacity to 32 bits).
 *
 * @version 0.1.24 2014/06/07
 * @author XBUP Project (http://xbup.org)
 */
public class UBInt32 implements UBInteger {

    private int value;

    public UBInt32() {
        value = 0;
    }

    public UBInt32(int value) {
        this.value = value;
    }

    @Override
    public int getInt() throws UBOverFlowException {
        return value;
    }

    @Override
    public long getLong() throws UBOverFlowException {
        return value;
    }

    @Override
    public void setValue(int value) throws UBOverFlowException {
        this.value = value;
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
        this.value = (int) value;
    }

    @Override
    public long getSegmentCount() {
        return 1;
    }

    @Override
    public long getValueSegment(long segmentIndex) {
        if (segmentIndex != 0) {
            throw new IndexOutOfBoundsException();
        }

        return value;
    }

    // TODO
    /*
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
     UBNatural newValue = serial.nextAttribute();
     // TODO setValue(newValue.getLong());
     serial.end();
     } else {
     XBTChildListener serial = (XBTChildListener) serializationHandler;
     serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
     // TODO serial.addAttribute(this);
     serial.end();
     }
     }
     */
}
