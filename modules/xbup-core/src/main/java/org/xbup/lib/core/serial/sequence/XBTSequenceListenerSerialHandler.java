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

import org.xbup.lib.core.serial.child.XBTChildListener;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.child.XBChildSerialState;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.token.XBTTokenOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 serialization handler using serialization sequence parser
 * mapping to token listener.
 *
 * @version 0.1.24 2014/08/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSequenceListenerSerialHandler implements XBTSequenceSerialHandler, XBTSequenceOutputSerialHandler, XBTSerialSequence, XBTTokenOutputSerialHandler {

    private XBTEventListener eventListener;
    private final XBChildSerialState state;

    public XBTSequenceListenerSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        eventListener = listener;
    }

    @Override
    public void sequenceXB(XBSerialSequence sequence) throws XBProcessingException, IOException {
        XBTChildOutputSerialHandler handler = new XBTChildListenerSerialHandler();
        handler.attachXBTEventListener(eventListener);

        handler.begin(sequence.getTerminationMode());
        handler.setType(sequence.getXBBlockType());

        List<XBSerializable> params = new ArrayList<>();
        serializeToXBSequence(sequence, handler, params);
        for (Iterator<XBSerializable> it = params.iterator(); it.hasNext();) {
            handler.addChild(it.next());
        }
        handler.end();
    }

    public void serializeToXBSequence(XBSerialSequence sequence, XBTChildListener serial, List<XBSerializable> params) throws XBProcessingException, IOException {
        for (XBSerialSequenceItem item : sequence.getItems()) {
            switch (item.getSequenceOp()) {
                case JOIN: {
                    if (item.getItem() instanceof XBSerialSequence) {
                        serializeToXBSequence((XBSerialSequence) item.getItem(), serial, params);
                    } else {
                        // TODO process serial methods
                        // XBSerialMethod serialMethod = item.getItem().getSerializationMethods(XBSerializationType.TO_XB);
                        //if (serialMethod instanceof XBTSerialMethodStream) {
                        if (item.getItem() instanceof XBTChildSerializable) {
                            XBJoinOutputSerial joinSerial = new XBJoinOutputSerial(serial, params);
                            joinSerial.attachXBTEventListener(eventListener);
                            ((XBTChildSerializable) item.getItem()).serializeToXB(joinSerial);
                        } else {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        //} else if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
                        //    throw new UnsupportedOperationException("Not supported yet.");
                        //} else {
                        //    throw new XBProcessingException("Unknown serialization object");
                        //}
                    }
                    break;
                }
                case CONSIST: {
                    params.add(item.getItem());
                    break;
                }
                case LIST_JOIN: {
                    XBSerialSequenceList list = (XBSerialSequenceList) item.getItem();
                    UBNatural count = list.getSize();
                    serial.addAttribute(count);
                    params.add(new XBSequenceSerializableList(list));
                    break;
                }
                case LIST_CONSIST: {
                    XBSerialSequenceIList list = (XBSerialSequenceIList) item.getItem();
                    UBENatural count = list.getSize();
                    serial.addAttribute(new UBNat32(count.getLong()));
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

    private class XBJoinOutputSerial implements XBTChildOutputSerialHandler, XBTChildListener, XBTTokenOutputSerialHandler {

        private XBTEventListener eventListener;
        private final XBTChildListener serial;
        private final List<XBSerializable> params;

        public XBJoinOutputSerial(XBTChildListener serial, List<XBSerializable> params) {
            this.serial = serial;
            this.params = params;
        }

        @Override
        public void attachXBTEventListener(XBTEventListener listener) {
            eventListener = listener;
            // TODO: Unused?
        }

        @Override
        public void begin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        }

        @Override
        public void setType(XBBlockType type) throws XBProcessingException, IOException {
            serial.setType(type);
        }

        @Override
        public void addAttribute(UBNatural attr) throws XBProcessingException, IOException {
            serial.addAttribute(attr);
        }

        @Override
        public void addChild(XBSerializable child) throws XBProcessingException, IOException {
            params.add(child);
        }

        @Override
        public void addData(InputStream data) throws XBProcessingException, IOException {
            serial.addData(data);
        }

        @Override
        public void end() throws XBProcessingException, IOException {
        }
    }
}
