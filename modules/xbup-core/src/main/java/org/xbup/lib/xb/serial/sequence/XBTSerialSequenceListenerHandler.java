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

import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.event.XBTEventListener;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerialState;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.XBTOutputTokenSerialHandler;
import org.xbup.lib.xb.ubnumber.UBENatural;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBENat32;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * XBUP level 1 XBTChildListener handler.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSerialSequenceListenerHandler implements XBTSerialSequence, XBTOutputTokenSerialHandler {

    private XBTEventListener eventListener;
    private XBSerialState state;

    public XBTSerialSequenceListenerHandler() {
        state = XBSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        eventListener = listener;
    }

    @Override
    public void sequenceXB(XBSerialSequence sequence) throws XBProcessingException, IOException {
        XBTChildListenerSerialHandler handler = new XBTChildListenerSerialHandler();
        handler.attachXBTEventListener(eventListener);

        handler.begin(sequence.getTerminationMode());
        handler.setType(sequence.getXBBlockType());

        List<XBSerializable> params = new ArrayList<>();
        serializeToXBSequence(sequence, handler, params);
        for (Iterator<XBSerializable> it = params.iterator(); it.hasNext();) {
            handler.addChild(it.next(), 0);
        }
        handler.end();
    }

    public void serializeToXBSequence(XBSerialSequence sequence, XBTChildListener serial, List<XBSerializable> params) throws XBProcessingException, IOException {
        for (Iterator<XBSerialSequenceItem> it = sequence.getItems().iterator(); it.hasNext();) {
            XBSerialSequenceItem item = it.next();
            switch (item.getSequenceOp()) {
                case JOIN: {
                    if (item.getItem() instanceof XBSerialSequence) {
                        serializeToXBSequence((XBSerialSequence) item.getItem(), serial, params);
                    } else {
                        // TODO process serial methods
                        // XBSerialMethod serialMethod = item.getItem().getSerializationMethods(XBSerializationType.TO_XB);
                        //if (serialMethod instanceof XBTSerialMethodStream) {
                        XBJoinOutputSerial joinSerial = new XBJoinOutputSerial(serial, params);
                        joinSerial.attachXBTEventListener(eventListener);
                        item.getItem().serializeXB(XBSerializationType.TO_XB, 0, joinSerial);
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
                while (count.getLong() != 0) {
                    serial.nextChild(next(), 0);
                    count = new UBNat32(count.getLong() - 1);
                }
            } else {
                XBTChildListener serial = (XBTChildListener) serializationHandler;
                UBNatural count = getSize();
                while (count.getLong() != 0) {
                    serial.addChild(next(), 0);
                    count = new UBNat32(count.getLong() - 1);
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
                    while (count.getLong() != 0) {
                        serial.nextChild(next(), 0);
                        count = new UBENat32(count.getLong() - 1);
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
                    while (count.getLong() != 0) {
                        serial.addChild(next(), 0);
                        count = new UBENat32(count.getLong() - 1);
                    }
                }
            }
        }
    }

    private class XBJoinOutputSerial implements XBTChildListener, XBTOutputTokenSerialHandler {

        private XBTEventListener eventListener;
        private XBTChildListener serial;
        private List<XBSerializable> params;

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
        public void addChild(XBSerializable child, int methodIndex) throws XBProcessingException, IOException {
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
