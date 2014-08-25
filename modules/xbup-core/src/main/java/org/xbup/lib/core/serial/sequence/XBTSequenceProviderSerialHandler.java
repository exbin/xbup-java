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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.child.XBChildSerialState;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProvider;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 serialization handler using serialization sequence parser mapping to token provider.
 *
 * @version 0.1 wr24.0 2014/08/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSequenceProviderSerialHandler implements XBTSequenceSerialHandler, XBTSequenceInputSerialHandler, XBTSerialSequence, XBTTokenInputSerialHandler {

    private XBTPullProvider pullProvider;
    private final XBChildSerialState state;

    public XBTSequenceProviderSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void sequenceXB(XBSerialSequence sequence) throws XBProcessingException, IOException {
        XBTChildInputSerialHandler handler = new XBTChildProviderSerialHandler();
        handler.attachXBTPullProvider(pullProvider);

        handler.begin();
        List<XBSerializable> params = new ArrayList<>();
        serializeFromXBSequence(sequence, handler, params);
        for (Iterator<XBSerializable> it = params.iterator(); it.hasNext();) {
            handler.nextChild(it.next());
        }
        handler.end();
    }

    public void serializeFromXBSequence(XBSerialSequence sequence, XBTChildProvider serial, List<XBSerializable> params) throws XBProcessingException, IOException {
        for (XBSerialSequenceItem item : sequence.getItems()) {
            switch (item.getSequenceOp()) {
                case JOIN: {
                    if (item.getItem() instanceof XBSerialSequence) {
                        serializeFromXBSequence((XBSerialSequence) item.getItem(), serial, params);
                    } else {
                        // TODO Process method
                        /*
                         XBTSerialMethod serialMethod = item.getItem().getXBTSerializationMethod();
                         if (serialMethod instanceof XBTSerialMethodStream) {
                         ((XBTSerialMethodStream) serialMethod).serializeFromXBT(new XBSerialSequence.XBJoinInputStream(serial, params));
                         } else if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
                         throw new UnsupportedOperationException("Not supported yet.");
                         } else {
                         throw new XBProcessingException("Unknown serialization object");
                         } */
                        if (item.getItem() instanceof XBTChildSerializable) {
                            XBJoinInputStream joinSerial = new XBJoinInputStream(serial, params);
                            joinSerial.attachXBTPullProvider(pullProvider);
                            ((XBTChildSerializable) item.getItem()).serializeFromXB(joinSerial);
                        } else {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                    }
                    break;
                }
                case CONSIST: {
                    params.add(item.getItem());
                    break;
                }
                case LIST_JOIN: {
                    XBSerialSequenceList list = (XBSerialSequenceList) item.getItem();
                    UBNatural count = serial.nextAttribute();
                    list.setSize(count);
                    params.add(new XBSequenceSerializableList(list));
                    break;
                }
                case LIST_CONSIST: {
                    XBSerialSequenceIList list = (XBSerialSequenceIList) item.getItem();
                    UBENatural count = new UBENat32(serial.nextAttribute().getInt()); // TODO: Handle infinity
                    list.setSize(count);
                    params.add(new XBSequenceSerializableIList(list));
                    break;
                }
            }
        }
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

    private class XBJoinInputStream implements XBTChildInputSerialHandler, XBTChildProvider, XBTTokenInputSerialHandler {

        private XBTPullProvider pullProvider;
        private final XBTChildProvider serial;
        private final List<XBSerializable> params;

        public XBJoinInputStream(XBTChildProvider serial, List<XBSerializable> params) {
            this.serial = serial;
            this.params = params;
        }

        @Override
        public void attachXBTPullProvider(XBTPullProvider provider) {
            pullProvider = provider;
            // TODO: Unused?
        }

        @Override
        public XBBlockTerminationMode begin() throws XBProcessingException, IOException {
            throw new XBProcessingException("Unexpected serial begin event");
        }

        @Override
        public XBBlockType getType() throws XBProcessingException, IOException {
            return serial.getType();
        }

        @Override
        public UBNatural nextAttribute() throws XBProcessingException, IOException {
            return serial.nextAttribute();
        }

        @Override
        public void nextChild(XBSerializable child) throws XBProcessingException, IOException {
            params.add(child);
        }

        @Override
        public InputStream nextData() throws XBProcessingException, IOException {
            return serial.nextData();
        }

        @Override
        public void end() throws XBProcessingException, IOException {
            throw new XBProcessingException("Unexpected serial end event");
        }
    }
}
