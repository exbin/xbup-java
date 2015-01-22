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

import org.xbup.lib.core.serial.sequence.XBSerialSequenceItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.XBPWriteSerialHandler;
import org.xbup.lib.core.serial.XBSerialException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBChildSerialState;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.token.XBTTokenOutputSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization handler using basic parser mapping to listener.
 *
 * @version 0.1.24 2015/01/22
 * @author XBUP Project (http://xbup.org)
 */
public class XBPListenerSerialHandler implements XBPOutputSerialHandler, XBPSequenceSerialHandler, XBTTokenOutputSerialHandler {

    private XBPWriteSerialHandler childHandler = null;
    private XBPSequenceEventProducer eventListener;

    private final List<List<XBSerializable>> childSequence = new ArrayList<>();
    private XBPSequencingListener sequencingListener = null;
    private XBChildSerialState state;
    private static final String PULL_NOT_ALLOWED_EXCEPTION = "Pulling data not allowed in pushing mode";

    public XBPListenerSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    public XBPListenerSerialHandler(XBPWriteSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        if (eventListener instanceof XBPSequenceEventProducer) {
            this.eventListener = (XBPSequenceEventProducer) eventListener;
        } else {
            this.eventListener = new XBPSequenceEventProducer(listener);
        }
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putBegin(terminationMode);
            return;
        }

        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state != XBChildSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Unable to set block terminated mode", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putToken(new XBTBeginToken(terminationMode));
        state = XBChildSerialState.ATTRIBUTE_PART;
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putType(type);
            return;
        }

        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            eventListener.putToken(new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putToken(new XBTTypeToken(type));
        state = XBChildSerialState.TYPE;
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBSerialException, XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attribute);
            return;
        }

        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_END) {
            throw new XBSerialException("Unable to add attributes after data or child blocks", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Missing block type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putToken(new XBTAttributeToken(attribute));
        state = XBChildSerialState.ATTRIBUTES;
    }

    public void putChild(XBSerializable child) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("At least one attribute is needed before child", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_END) {
            throw new XBSerialException("Unable to add child after data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if (child instanceof XBPSerializable) {
            XBPListenerSerialHandler childOutput = new XBPListenerSerialHandler();
            // TODO childOutput.attachXBTEventListener(eventListener);
            ((XBPSerializable) child).serializeToXB(childOutput);
        } else {
            if (childHandler != null) {
                childHandler.write(child);
            } else {
                throw new XBProcessingException("Unsupported child serialization", XBProcessingExceptionType.UNKNOWN);
            }
        }

        state = XBChildSerialState.CHILDREN;
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putJoin(serial);
            return;
        }

        if (serial instanceof XBTChildSerializable) {
            ((XBPSerializable) serial).serializeToXB(this);
        } else {
            if (childHandler != null) {
                childHandler.write(serial == null ? XBTEmptyBlock.getEmptyBlock() : serial);
            } else {
                throw new XBProcessingException("Unsupported child serialization", XBProcessingExceptionType.UNKNOWN);
            }
        }
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putData(data);
            return;
        }

        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.ATTRIBUTES) {
            throw new XBSerialException("Data block is not allowed after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.TYPE) {
            throw new XBSerialException("Data event is not allowed after block type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        // TODO test depth for extended area
        /*
         if (state == XBChildSerialState.DATA) {
         throw new XBSerialException("Data event is not allowed after another data event", XBProcessingExceptionType.UNEXPECTED_ORDER);
         }
         if (state == XBChildSerialState.CHILDREN) {
         throw new XBSerialException("Data block is not allowed after children", XBProcessingExceptionType.UNEXPECTED_ORDER);
         } */
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            eventListener.putToken(new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putToken(new XBTDataToken(data));
        state = XBChildSerialState.DATA;
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putEnd();
            checkSequencingListener();
            return;
        }

        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN || state == XBChildSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("At least one attribute or data required");
        }

        eventListener.putToken(new XBTEndToken());
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attributeValue);
            return;
        }

        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putToken(token);
            checkSequencingListener();
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putConsist(serial);
            return;
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putListConsist(serial);
            return;
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putListJoin(serial);
            return;
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putItem(item);
            checkSequencingListener();
            return;
        }

        throw new UnsupportedOperationException("Not supported yet.");
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
    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        putEnd();
    }

    @Override
    public void attribute(UBNatural attributeValue) throws XBProcessingException, IOException {
        putAttribute(attributeValue);
    }

    @Override
    public void consist(XBSerializable serial) throws XBProcessingException, IOException {
        putConsist(serial);
    }

    @Override
    public void join(XBSerializable serial) throws XBProcessingException, IOException {
        putJoin(serial);
    }

    @Override
    public void listConsist(XBSerializable serial) throws XBProcessingException, IOException {
        putListConsist(serial);
    }

    @Override
    public void listJoin(XBSerializable serial) throws XBProcessingException, IOException {
        putListJoin(serial);
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public UBNatural pullAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public InputStream pullData() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullConsist(XBSerializable child) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullListConsist(XBSerializable child) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void pullItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        throw new XBProcessingException(PULL_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    private void checkSequencingListener() throws IOException, XBProcessingException {
        if (sequencingListener.isFinished()) {
            eventListener.putChild(sequencingListener.getSequenceSerial());
            sequencingListener = null;
        }
    }
}
