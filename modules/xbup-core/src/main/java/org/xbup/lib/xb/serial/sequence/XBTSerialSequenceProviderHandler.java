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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.pull.XBTPullProvider;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerialState;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.XBTInputTokenSerialHandler;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.ubnumber.UBENatural;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBENat32;

/**
 * XBUP level 1 XBTChildListener handler.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSerialSequenceProviderHandler implements XBTSerialSequence, XBTInputTokenSerialHandler {
    
    private XBTPullProvider pullProvider;
    private XBSerialState state;

    public XBTSerialSequenceProviderHandler() {
        state = XBSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void sequenceXB(XBSerialSequence sequence) throws XBProcessingException, IOException {
        XBTChildProviderSerialHandler handler = new XBTChildProviderSerialHandler();
        handler.attachXBTPullProvider(pullProvider);

        handler.begin();
        List<XBSerializable> params = new ArrayList<>();
        serializeFromXBSequence(sequence, handler, params);
        for (Iterator<XBSerializable> it = params.iterator(); it.hasNext();) {
            handler.nextChild(it.next(), 0);
        }
        handler.end();
    }

    public void serializeFromXBSequence(XBSerialSequence sequence, XBTChildProvider serial, List<XBSerializable> params) throws XBProcessingException, IOException {
        for (Iterator<XBSerialSequenceItem> it = sequence.getItems().iterator(); it.hasNext();) {
            XBSerialSequenceItem item = it.next();
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
                        XBJoinInputStream joinSerial = new XBJoinInputStream(serial, params);
                        joinSerial.attachXBTPullProvider(pullProvider);
                        item.getItem().serializeXB(XBSerializationType.FROM_XB, 0, joinSerial);

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

    private class XBJoinInputStream implements XBTChildProvider, XBTInputTokenSerialHandler {

        private XBTPullProvider pullProvider;
        private XBTChildProvider serial;
        private List<XBSerializable> params;

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
        public void nextChild(XBSerializable child, int methodIndex) throws XBProcessingException, IOException {
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
