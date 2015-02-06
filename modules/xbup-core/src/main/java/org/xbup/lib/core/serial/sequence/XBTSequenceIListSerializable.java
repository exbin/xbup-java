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

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;

/**
 * XBUP level 1serialization sequence serializable list.
 *
 * @version 0.1.24 2014/10/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSequenceIListSerializable implements XBListConsistSerializable, XBTChildSerializable {

    private final XBListConsistSerializable list;

    public XBTSequenceIListSerializable(XBListConsistSerializable list) {
        this.list = list;
    }

    @Override
    public void setSize(UBENatural count) {
        list.setSize(count);
    }

    @Override
    public UBENatural getSize() {
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
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        UBENatural count = getSize();
        if (count.isInfinity()) {
            XBSerializable block;
            do {
                block = next();
                // TODO: Handle infinite lists (Process termination by empty data block)
                serial.pullChild(next());
            } while (block != null);
        } else {
            while (count.getLong() != 0) {
                serial.pullChild(next());
                count = new UBENat32(count.getLong() - 1);
            }
        }
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        UBENatural count = getSize();
        if (count.isInfinity()) {
            XBSerializable block;
            do {
                block = next();
                if (block == null) {
                    serial.putChild(block);
                } else {
                    serial.putChild(null); // TODO: Add empty block as terminator
                }
            } while (block != null);
        } else {
            while (count.getLong() != 0) {
                serial.putChild(next());
                count = new UBENat32(count.getLong() - 1);
            }
        }
    }
}
