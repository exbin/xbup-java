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
package org.xbup.lib.core.serial.param;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;

/**
 * XBUP level 2 serialization sequence serializable list.
 *
 * @version 0.1.24 2014/11/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBASequenceIListSerializable implements XBSerialSequenceIList, XBPChildSerializable {

    private final XBSerialSequenceIList list;

    public XBASequenceIListSerializable(XBSerialSequenceIList list) {
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
    public void serializeFromXB(XBPChildInputSerialHandler serial) throws XBProcessingException, IOException {
        UBENatural count = getSize();
        if (count.isInfinity()) {
            XBSerializable block;
            do {
                block = next();
                // TODO: Handle infinite lists (Process termination by empty data block)
                serial.pullChild(next());
            } while (block != null);
        } else {
            for (long i = 0; i < count.getLong(); i++) {
                serial.pullChild(next());
            }
        }
    }

    @Override
    public void serializeToXB(XBPChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        UBENatural count = getSize();
        if (count.isInfinity()) {
            XBSerializable block;
            do {
                block = next();
                if (block == null) {
                    // TODO serial.putChild(block);
                }
            } while (block != null);
        } else {
            for (long i = 0; i < count.getLong(); i++) {
                // TODO serial.putChild(next());
            }
        }
    }
}
