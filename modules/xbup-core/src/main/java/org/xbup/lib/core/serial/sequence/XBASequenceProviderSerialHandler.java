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
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.XBReadSerialHandler;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBAChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildProvider;
import org.xbup.lib.core.serial.child.XBAChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildSerializable;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization handler using serialization sequence parser
 * mapping to token provider.
 *
 * @version 0.1.24 2014/12/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBASequenceProviderSerialHandler implements XBASequenceSerialHandler, XBASequenceInputSerialHandler, XBASerialSequenceable, XBTTokenInputSerialHandler {

    private XBTPullProvider pullProvider;
    private XBReadSerialHandler childHandler = null;

    public XBASequenceProviderSerialHandler() {
    }

    public XBASequenceProviderSerialHandler(XBReadSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void appendSequence(XBSerialSequence sequence) throws XBProcessingException, IOException {
        XBAChildInputSerialHandler handler = new XBAChildProviderSerialHandler();
        handler.attachXBTPullProvider(pullProvider);

        handler.pullBegin();
        XBBlockType blockType = handler.pullType();
        List<XBSerializable> children = serializeFromXBSequence(sequence, handler);

        for (XBSerializable child : children) {
            handler.pullChild(child);
        }

        // TODO: SKIP additional blocks and don't report them?
        // handler.pullEnd();
    }

    public List<XBSerializable> serializeFromXBSequence(XBSerialSequence sequence, XBAChildProvider handler) throws XBProcessingException, IOException {
        List<XBSerializable> children = new ArrayList<>();
        for (XBSerialSequenceItem seqItem : sequence.getItems()) {
            XBSerializable item = seqItem.getItem();
            switch (seqItem.getSequenceOp()) {
                case JOIN: {
                    if (item instanceof XBSerialSequence) {
                        children.addAll(serializeFromXBSequence((XBSerialSequence) item, handler));
                    } else if (item instanceof XBAChildSerializable) {
                        XBJoinInputStream joinSerial = new XBJoinInputStream(handler);
                        ((XBTChildSerializable) item).serializeFromXB(joinSerial);
                        children.addAll(joinSerial.getChildren());
                    } else {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

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
                    break;
                }
                case CONSIST: {
                    children.add(item);
                    break;
                }
                case LIST_JOIN: {
                    XBSerialSequenceList list = (XBSerialSequenceList) seqItem.getItem();
                    UBNatural count = handler.pullAttribute();
                    list.setSize(count);
                    children.add(new XBASequenceListSerializable(list));
                    break;
                }
                case LIST_CONSIST: {
                    XBSerialSequenceIList list = (XBSerialSequenceIList) seqItem.getItem();
                    UBENatural count = new UBENat32(handler.pullAttribute().getInt()); // TODO: Handle infinity
                    list.setSize(count);
                    children.add(new XBASequenceIListSerializable(list));
                    break;
                }
            }
        }

        return children;
    }

    @Override
    public void begin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void end() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void matchType(XBBlockType blockType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attribute(UBNatural attributeValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SerializationMode getSerializationMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void child(XBSerializable child) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void append(XBSerializable child) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void matchChild(XBSerializable child) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putType(XBBlockType type, XBBlockType targetType) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putChild(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockType pullMatchingType(XBBlockType blockTypes) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockType pullMatchingType(List<XBBlockType> blockTypes) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UBNatural pullAttribute() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream pullData() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class XBJoinInputStream implements XBTChildInputSerialHandler {

        private int depth = 0;
        private final XBAChildProvider serial;
        private final List<XBSerializable> children = new ArrayList<>();

        public XBJoinInputStream(XBAChildProvider serial) {
            this.serial = serial;
        }

        @Override
        public void attachXBTPullProvider(XBTPullProvider pullProvider) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
            if (depth > 0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            depth++;

            return XBBlockTerminationMode.SIZE_SPECIFIED;
        }

        @Override
        public XBBlockType pullType() throws XBProcessingException, IOException {
            // TODO type
            return new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK);
        }

        @Override
        public UBNatural pullAttribute() throws XBProcessingException, IOException {
            return serial.pullAttribute();
        }

        @Override
        public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
            children.add(child);
        }

        @Override
        public InputStream pullData() throws XBProcessingException, IOException {
            throw new UnsupportedOperationException("Not supported yet.");
            // return serial.pullData();
        }

        @Override
        public void pullEnd() throws XBProcessingException, IOException {
            if (depth < 0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            depth--;
        }

        public List<XBSerializable> getChildren() {
            return children;
        }
    }
}
