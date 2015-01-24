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
import org.xbup.lib.core.parser.param.XBParamProcessingState;
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
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.token.XBTTokenOutputSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization handler using parameter mapping to listener.
 *
 * @version 0.1.24 2015/01/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBPListenerSerialHandler implements XBPOutputSerialHandler, XBPSequenceSerialHandler, XBTTokenOutputSerialHandler {

    private XBPWriteSerialHandler childHandler = null;
    private XBPSequenceEventProducer eventListener;

    private final List<List<XBSerializable>> childSequences = new ArrayList<>();
    private XBPSequencingListener sequencingListener = null;
    private XBParamProcessingState processingState = XBParamProcessingState.START;

    private static final String PULL_NOT_ALLOWED_EXCEPTION = "Pulling data not allowed in pushing mode";

    public XBPListenerSerialHandler() {
    }

    public XBPListenerSerialHandler(XBPWriteSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeToXB(this);
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(this);
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
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

        if (processingState == XBParamProcessingState.START) {
            eventListener.putToken(new XBTBeginToken(terminationMode));
            processingState = XBParamProcessingState.BEGIN;
        } else {
            sequencingListener = new XBPSequencingListener();
            sequencingListener.putBegin(terminationMode);
        }
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putType(type);
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN) {
            eventListener.putToken(new XBTTypeToken(type));
            processingState = XBParamProcessingState.TYPE;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBSerialException, XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putAttribute(attribute);
            return;
        }

        if (processingState == XBParamProcessingState.BEGIN || processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES) {
            eventListener.putToken(new XBTAttributeToken(attribute));
            processingState = XBParamProcessingState.ATTRIBUTES;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
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

        if (processingState == XBParamProcessingState.BEGIN) {
            eventListener.putToken(new XBTDataToken(data));
            processingState = XBParamProcessingState.DATA;
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putEnd();
            checkSequencingListener();
            return;
        }

        if (processingState == XBParamProcessingState.TYPE || processingState == XBParamProcessingState.ATTRIBUTES || processingState == XBParamProcessingState.DATA) {
            eventListener.putToken(new XBTEndToken());
            processingState = XBParamProcessingState.END;
            List<XBSerializable> childSequence = eventListener.getChildSequence();
            childSequences.add(childSequence);

            while (childSequence.isEmpty()) {
                childSequences.remove(childSequences.size() - 1);
                eventListener.putEnd();
                if (childSequences.isEmpty()) {
                    break;
                } else {
                    childSequence = childSequences.get(childSequences.size() - 1);
                }
            }

            if (!childSequence.isEmpty()) {
                XBSerializable child = childSequence.remove(childSequence.size() - 1);
                processingState = XBParamProcessingState.START;
                childHandler.write(child);
            }
        } else {
            throw new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
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

        eventListener.putToken(token);
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        if (sequencingListener != null) {
            sequencingListener.putConsist(serial);
            return;
        }

        eventListener.putChild(serial);
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
        putType(blockType);
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
