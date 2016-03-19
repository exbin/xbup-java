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
package org.exbin.xbup.core.serial.sequence;

import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 1 serialization sequence.
 *
 * @version 0.1.24 2015/01/22
 * @author ExBin Project (http://exbin.org)
 */
public class XBSerialSequenceItem {

    private XBSerialSequenceOp sequenceOp;
    private XBSerializable item;

    public XBSerialSequenceItem(XBSerialSequenceOp sequenceOp, XBSerializable item) {
        this.sequenceOp = sequenceOp;
        this.item = item;
    }

    public XBSerialSequenceOp getSequenceOp() {
        return sequenceOp;
    }

    public void setSequenceOp(XBSerialSequenceOp sequenceOp) {
        this.sequenceOp = sequenceOp;
    }

    public XBSerializable getItem() {
        return item;
    }

    public void setItem(XBSerializable item) {
        this.item = item;
    }
}
