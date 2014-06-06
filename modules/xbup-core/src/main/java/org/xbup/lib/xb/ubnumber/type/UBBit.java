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
package org.xbup.lib.xb.ubnumber.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.UBNumber;
import org.xbup.lib.xb.ubnumber.exception.UBOverFlowException;

/**
 * Bitfield value.
 *
 * @version 0.1 wr23.0 2014/03/09
 * @author XBUP Project (http://xbup.org)
 */
public class UBBit extends UBNumber {

    private Set value;
//    BNatural extend;

    public void UBNumber(int value) {

    }

    public void inc() {

    }

    public void dec() {

    }

    public void toStream(OutputStream stream) {

    }

    @Override
    public int toStreamUB(OutputStream stream) {
        return 0;
    }

    @Override
    public int fromStreamUB(InputStream stream) {
        return 0;
    }

    @Override
    public int getSizeUB() {
        return 0;
    }

    @Override
    public UBNumber clone() {
        return null;
    }

    public void getAttributeType() {
    }

    @Override
    public int toInt() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public long toLong() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public UBNatural toNatural() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getValueSize() {
        return 1;
    }

    @Override
    public List<UBNatural> getValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
