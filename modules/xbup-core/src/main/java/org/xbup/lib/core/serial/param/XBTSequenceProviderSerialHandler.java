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

import org.xbup.lib.core.serial.sequence.XBTSequenceListSerializable;
import org.xbup.lib.core.serial.sequence.XBTSequenceIListSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequence;
import org.xbup.lib.core.serial.sequence.XBListConsistSerializable;
import org.xbup.lib.core.serial.sequence.XBListJoinSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequenceItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.XBTReadSerialHandler;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProvider;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;

/**
 * XBUP level 1 serialization handler using serialization sequence parser
 * mapping to token provider.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSequenceProviderSerialHandler implements XBTSequenceSerialHandler, XBTSequenceInputSerialHandler, XBASerialSequenceable, XBTTokenInputSerialHandler {

    private final XBTChildProviderSerialHandler provider;
    private XBTReadSerialHandler childHandler = null;
    private XBTPullProvider pullProvider;

    public XBTSequenceProviderSerialHandler() {
        provider = new XBTChildProviderSerialHandler();
    }

    public XBTSequenceProviderSerialHandler(XBTReadSerialHandler childHandler) {
        provider = new XBTChildProviderSerialHandler(childHandler);
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
        provider.attachXBTPullProvider(pullProvider);
    }

    public XBSerializationMode getSerializationMode() {
        return XBSerializationMode.PULL;
    }

    public void begin() throws XBProcessingException, IOException {
        pullBegin();
    }

    public void end() throws XBProcessingException, IOException {
        pullEnd();
    }

    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException {
        XBBlockType pullType = pullType();
        if (!pullType.equals(blockType)) {
            throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
        }
    }

    public void attribute(UBNatural attributeValue) throws XBProcessingException, IOException {
        attributeValue.setValue(pullAttribute().getLong());
    }

    public void child(XBSerializable child) throws XBProcessingException, IOException {
        pullChild(child);
    }

    public void append(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void matchChild(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    public void putChild(XBSerializable child) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        throw new XBProcessingException("Pushing data not allowed in pulling mode", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        return provider.pullBegin();
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        return provider.pullType();
    }

    @Override
    public UBNatural pullAttribute() throws XBProcessingException, IOException {
        return provider.pullAttribute();
    }

    @Override
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        return (byte) pullAttribute().getInt();
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        return (short) pullAttribute().getInt();
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getInt();
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getLong();
    }

    public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
        provider.pullChild(child);
    }

    public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException {
        provider.pullAppend(serial);
    }

    @Override
    public InputStream pullData() throws XBProcessingException, IOException {
        return provider.pullData();
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        provider.pullEnd();
    }

    public void appendSequence(XBSerialSequence sequence) throws XBProcessingException, IOException {
        provider.pullBegin();
        XBBlockType blockType = provider.pullType();
        List<XBSerializable> children = serializeFromXBSequence(sequence, provider);

        for (XBSerializable child : children) {
            provider.pullChild(child);
        }

        provider.pullEnd();
    }

    public List<XBSerializable> serializeFromXBSequence(XBSerialSequence sequence, XBTChildProvider handler) throws XBProcessingException, IOException {
        List<XBSerializable> children = new ArrayList<>();
        for (XBSerialSequenceItem seqItem : sequence.getItems()) {
            XBSerializable item = seqItem.getItem();
            switch (seqItem.getSequenceOp()) {
                case JOIN: {
                    if (item instanceof XBSerialSequence) {
                        children.addAll(serializeFromXBSequence((XBSerialSequence) item, handler));
                    } else if (item instanceof XBTChildSerializable) {
                        XBJoinInputStream joinSerial = new XBJoinInputStream(handler);
                        joinSerial.attachXBTPullProvider(pullProvider);
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
                    XBListJoinSerializable list = (XBListJoinSerializable) seqItem.getItem();
                    UBNatural count = handler.pullAttribute();
                    list.setSize(count);
                    children.add(new XBTSequenceListSerializable(list));
                    break;
                }
                case LIST_CONSIST: {
                    XBListConsistSerializable list = (XBListConsistSerializable) seqItem.getItem();
                    UBENatural count = new UBENat32(handler.pullAttribute().getInt()); // TODO: Handle infinity
                    list.setSize(count);
                    children.add(new XBTSequenceIListSerializable(list));
                    break;
                }
            }
        }

        return children;
    }

    @Override
    public void join(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullConsist(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullListConsist(XBSerializable child) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class XBJoinInputStream implements XBTChildInputSerialHandler {

        private int depth = 0;
        private final XBTChildProvider serial;
        private final List<XBSerializable> children = new ArrayList<>();

        public XBJoinInputStream(XBTChildProvider serial) {
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
        public byte pullByteAttribute() throws XBProcessingException, IOException {
            return (byte) pullAttribute().getInt();
        }

        @Override
        public short pullShortAttribute() throws XBProcessingException, IOException {
            return (short) pullAttribute().getInt();
        }

        @Override
        public int pullIntAttribute() throws XBProcessingException, IOException {
            return pullAttribute().getInt();
        }

        @Override
        public long pullLongAttribute() throws XBProcessingException, IOException {
            return pullAttribute().getLong();
        }

        @Override
        public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
            children.add(child);
        }

        @Override
        public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException {
            throw new UnsupportedOperationException("Not supported yet.");
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
