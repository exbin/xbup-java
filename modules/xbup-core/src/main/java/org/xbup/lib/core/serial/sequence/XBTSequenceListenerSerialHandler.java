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
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.XBWriteSerialHandler;
import org.xbup.lib.core.serial.token.XBTTokenOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 serialization handler using serialization sequence parser
 * mapping to token listener.
 *
 * @version 0.1.24 2014/12/16
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSequenceListenerSerialHandler implements XBTSequenceSerialHandler, XBTSequenceOutputSerialHandler, XBTSerialSequenceable, XBTTokenOutputSerialHandler {

    private final XBTChildListenerSerialHandler listener;
    private XBWriteSerialHandler childHandler = null;
    private XBTEventListener eventListener;

    public XBTSequenceListenerSerialHandler() {
        listener = new XBTChildListenerSerialHandler();
    }

    public XBTSequenceListenerSerialHandler(XBWriteSerialHandler childHandler) {
        listener = new XBTChildListenerSerialHandler(childHandler);
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
        listener.attachXBTEventListener(eventListener);
    }

    @Override
    public XBSerializationMode getSerializationMode() {
        return XBSerializationMode.PUSH;
    }

    @Override
    public void begin() throws XBProcessingException, IOException {
        putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        putEnd();
    }

    @Override
    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException {
        putType(blockType);
    }

    @Override
    public void attribute(UBNatural attributeValue) throws XBProcessingException, IOException {
        putAttribute(attributeValue);
    }

    @Override
    public void child(XBSerializable child) throws XBProcessingException, IOException {
        putChild(child);
    }

    @Override
    public void join(XBSerializable child) throws XBProcessingException, IOException {
        putJoin(child);
    }

    @Override
    public void append(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void matchChild(XBSerializable child) throws XBProcessingException, IOException {
        putChild(child);
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        listener.putBegin(terminationMode);
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        listener.putType(type);
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
        listener.putAttribute(attribute);
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        listener.putAttribute(attributeValue);
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        listener.putAttribute(attributeValue);
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        listener.putAttribute(attributeValue);
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        listener.putAttribute(attributeValue);
    }

    @Override
    public void putChild(XBSerializable child) throws XBProcessingException, IOException {
        listener.putChild(child);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        listener.putJoin(serial);
    }

    @Override
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        listener.putAppend(serial);
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        listener.putData(data);
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        listener.putEnd();
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public UBNatural pullAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public InputStream pullData() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pulling data not allowed in pushing mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void appendSequence(XBSerialSequence sequence) throws XBProcessingException, IOException {
        listener.putBegin(sequence.getTerminationMode());
        listener.putType(sequence.getBlockType());

        List<XBSerializable> params = new ArrayList<>();
        serializeToXBSequence(sequence, listener, params);
        for (Iterator<XBSerializable> it = params.iterator(); it.hasNext();) {
            listener.putChild(it.next());
        }
        listener.putEnd();
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
                        } else if (item.getItem() instanceof XBTSequenceSerializable) {
                            XBTSequenceListenerSerialHandler serialHandler = new XBTSequenceListenerSerialHandler();
                            serialHandler.attachXBTEventListener(eventListener);
                            ((XBTSequenceSerializable) item.getItem()).serializeXB(serialHandler);
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
                    serial.putAttribute(count);
                    params.add(new XBTSequenceListSerializable(list));
                    break;
                }
                case LIST_CONSIST: {
                    XBSerialSequenceIList list = (XBSerialSequenceIList) item.getItem();
                    UBENatural count = list.getSize();
                    serial.putAttribute(new UBNat32(count.getLong()));
                    params.add(new XBTSequenceIListSerializable(list));
                    break;
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
        public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        }

        @Override
        public void putType(XBBlockType type) throws XBProcessingException, IOException {
            serial.putType(type);
        }

        @Override
        public void putAttribute(UBNatural attr) throws XBProcessingException, IOException {
            serial.putAttribute(attr);
        }

        @Override
        public void putChild(XBSerializable child) throws XBProcessingException, IOException {
            params.add(child);
        }

        @Override
        public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void putData(InputStream data) throws XBProcessingException, IOException {
            serial.putData(data);
        }

        @Override
        public void putEnd() throws XBProcessingException, IOException {
        }

        @Override
        public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
            serial.putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
            serial.putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
            serial.putAttribute(new UBNat32(attributeValue));
        }

        @Override
        public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
            serial.putAttribute(new UBNat32(attributeValue));
        }
    }
}
