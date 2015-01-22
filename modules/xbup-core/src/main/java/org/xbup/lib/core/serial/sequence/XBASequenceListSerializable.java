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
package org.xbup.lib.core.serial.sequence;

import org.xbup.lib.core.serial.sequence.XBListJoinSerializable;
import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.param.XBPInputSerialHandler;
import org.xbup.lib.core.serial.param.XBPOutputSerialHandler;
import org.xbup.lib.core.serial.param.XBPSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization sequence serializable list.
 *
 * @version 0.1.24 2014/11/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBASequenceListSerializable implements XBListJoinSerializable, XBPSerializable {

    private final XBListJoinSerializable list;

    public XBASequenceListSerializable(XBListJoinSerializable list) {
        this.list = list;
    }

    @Override
    public void setSize(UBNatural count) {
        list.setSize(count);
    }

    @Override
    public UBNatural getSize() {
        return list.getSize();
    }

    @Override
    public XBSerializable next() {
        return list.next();
    }

    @Override
    public void reset() {
        list.reset();
    }

    @Override
    public void serializeFromXB(XBPInputSerialHandler serial) throws XBProcessingException, IOException {
        UBNatural count = getSize();
        while (count.getLong() != 0) {
            // TODO serial.pullChild(next());
            count = new UBNat32(count.getLong() - 1);
        }
    }

    @Override
    public void serializeToXB(XBPOutputSerialHandler serial) throws XBProcessingException, IOException {
        UBNatural count = getSize();
        while (count.getLong() != 0) {
            // TODO serial.putChild(next());
            count = new UBNat32(count.getLong() - 1);
        }
    }
}
