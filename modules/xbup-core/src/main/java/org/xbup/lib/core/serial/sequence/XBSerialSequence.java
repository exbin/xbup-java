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
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 serialization sequence.
 *
 * @version 0.1.24 2014/09/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBSerialSequence {

    private XBBlockType type;
    private XBBlockTerminationMode terminationMode;
    private List<XBSerialSequenceItem> items;

    public XBSerialSequence() {
        items = new ArrayList<>();
    }

    public XBSerialSequence(XBBlockType type, XBBlockTerminationMode terminationMode) {
        this();
        this.type = type;
        this.terminationMode = terminationMode;
    }

    public XBSerialSequence(XBBlockType type) {
        this(type, XBBlockTerminationMode.SIZE_SPECIFIED);
    }

    public XBSerialSequence(XBBlockType type, XBSerializable join) {
        this(type, XBBlockTerminationMode.SIZE_SPECIFIED);
        privateJoin(join);
    }

    public XBSerialSequence(XBBlockType type, XBBlockTerminationMode terminationMode, XBSerializable join) {
        this(type, terminationMode);
        privateJoin(join);
    }

    public void add(XBSerialSequenceOp op, XBSerializable item) {
        items.add(new XBSerialSequenceItem(op, item));
    }

    private void privateJoin(XBSerializable item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.JOIN, item));
    }

    public void join(XBSerializable item) {
        privateJoin(item);
    }

    public void consist(XBSerializable item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.CONSIST, item));
    }

    public void listJoin(XBSerialSequenceList item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_JOIN, new XBSequenceSerializableList(item)));
    }

    public void listConsist(XBSerialSequenceIList item) {
        items.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_CONSIST, new XBSequenceSerializableIList(item)));
    }

    public XBBlockType getXBBlockType() {
        return type;
    }

    public void setXBBlockType(XBBlockType type) {
        this.type = type;
    }

    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        this.terminationMode = terminationMode;
    }

    public List<XBSerialSequenceItem> getItems() {
        return items;
    }

    public void setItems(List<XBSerialSequenceItem> items) {
        this.items = items;
    }

    public void append(XBSerialSequence seq) {
        items.addAll(seq.items);
    }

    private class XBSequenceSerializableList implements XBSerialSequenceList, XBTChildSerializable {

        private final XBSerialSequenceList list;

        public XBSequenceSerializableList(XBSerialSequenceList list) {
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
        public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
            UBNatural count = getSize();
            while (count.getLong() != 0) {
                serial.nextChild(next());
                count = new UBNat32(count.getLong() - 1);
            }
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            UBNatural count = getSize();
            while (count.getLong() != 0) {
                serial.addChild(next());
                count = new UBNat32(count.getLong() - 1);
            }
        }
    }

    private class XBSequenceSerializableIList implements XBSerialSequenceIList, XBTChildSerializable {

        private final XBSerialSequenceIList list;

        public XBSequenceSerializableIList(XBSerialSequenceIList list) {
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
                    serial.nextChild(next());
                } while (block != null);
            } else {
                while (count.getLong() != 0) {
                    serial.nextChild(next());
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
                        serial.addChild(block);
                    } else {
                        serial.addChild(null); // TODO: Add empty block as terminator
                    }
                } while (block != null);
            } else {
                while (count.getLong() != 0) {
                    serial.addChild(next());
                    count = new UBENat32(count.getLong() - 1);
                }
            }
        }
    }
}
