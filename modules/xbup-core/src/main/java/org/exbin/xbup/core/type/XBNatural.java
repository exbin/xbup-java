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

import java.io.IOException;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.child.XBTChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Encapsulation class for natural numbers.
 *
 * @version 0.1.24 2014/08/23
 * @author ExBin Project (http://exbin.org)
 */
public class XBNatural implements XBTChildSerializable {

    private UBNatural value;

    static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 0, 0, 0};

    public XBNatural() {
        this.value = new UBNat32();
    }

    public XBNatural(UBNatural value) {
        this.value = value;
    }

    public UBNatural getValue() {
        return value;
    }

    public void setValue(UBNatural value) {
        this.value = value;
    }

    public List<XBBlockType> getXBTransTypes() {
        return null;
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.pullBegin();
        serial.pullType();
        value = serial.pullAttribute().convertToNatural();
        serial.pullEnd();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.putType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        serial.putAttribute(value);
        serial.putEnd();
    }
}
