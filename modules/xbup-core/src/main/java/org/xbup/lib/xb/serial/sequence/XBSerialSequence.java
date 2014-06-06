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
package org.xbup.lib.xb.serial.sequence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.ubnumber.UBENatural;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * XBUP level 1 serialization sequence.
 *
 * @version 0.1 wr23.0 2014/03/04
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

    /**
     * @return the type
     */
    public XBBlockType getXBBlockType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setXBBlockType(XBBlockType type) {
        this.type = type;
    }

    /**
     * @return the terminationMode
     */
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    /**
     * @param terminationMode the terminationMode to set
     */
    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        this.terminationMode = terminationMode;
    }

    /**
     * @return the items
     */
    public List<XBSerialSequenceItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<XBSerialSequenceItem> items) {
        this.items = items;
    }

    private class XBSequenceSerializableList implements XBSerialSequenceList, XBSerializable {

        private XBSerialSequenceList list;

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
        public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
            return serialType == XBSerializationType.FROM_XB
                    ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                    : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
        }

        @Override
        public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
            if (serialType == XBSerializationType.FROM_XB) {
                XBTChildProvider serial = (XBTChildProvider) serializationHandler;
                UBNatural count = getSize();
                while (!count.isZero()) {
                    serial.nextChild(next(), 0);
                    count.dec();
                }
            } else {
                XBTChildListener serial = (XBTChildListener) serializationHandler;
                UBNatural count = getSize();
                while (!count.isZero()) {
                    serial.addChild(next(), 0);
                    count.dec();
                }
            }
        }
    }

    private class XBSequenceSerializableIList implements XBSerialSequenceIList, XBSerializable {

        private XBSerialSequenceIList list;

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
        public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
            return serialType == XBSerializationType.FROM_XB
                    ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                    : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
        }

        @Override
        public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
            if (serialType == XBSerializationType.FROM_XB) {
                XBTChildProvider serial = (XBTChildProvider) serializationHandler;
                UBENatural count = getSize();
                if (count.isInfinity()) {
                    XBSerializable block;
                    do {
                        block = next();
                        // TODO: Handle infinite lists (Process termination by empty data block)
                        serial.nextChild(next(), 0);
                    } while (block != null);
                } else {
                    while (!count.isZero()) {
                        serial.nextChild(next(), 0);
                        count.dec();
                    }
                }
            } else {
                XBTChildListener serial = (XBTChildListener) serializationHandler;
                UBENatural count = getSize();
                if (count.isInfinity()) {
                    XBSerializable block;
                    do {
                        block = next();
                        if (block == null) {
                            serial.addChild(block, 0);
                        } else {
                            serial.addChild(null, 0); // TODO: Add empty block as terminator
                        }
                    } while (block != null);
                } else {
                    while (!count.isZero()) {
                        serial.addChild(next(), 0);
                        count.dec();
                    }
                }
            }
        }
    }
}
