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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.XBPReadSerialHandler;
import org.xbup.lib.core.serial.XBSerialException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBChildSerialState;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 2 serialization handler using parameter mapping to provider.
 *
 * @version 0.1.24 2015/01/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBPProviderSerialHandler implements XBPInputSerialHandler, XBPSequenceSerialHandler, XBTTokenInputSerialHandler {

    private XBPSequencePullConsumer pullProvider;
    private XBChildSerialState state;
    private XBPReadSerialHandler childHandler = null;

    private static final String PUSH_NOT_ALLOWED_EXCEPTION = "Pushing data not allowed in pulling mode";

    public XBPProviderSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    public XBPProviderSerialHandler(XBPReadSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    public void process(XBSerializable serial) throws IOException, XBProcessingException {
        if (serial instanceof XBPSerializable) {
            ((XBPSerializable) serial).serializeFromXB(this);
        } else if (serial instanceof XBPSequenceSerializable) {
            ((XBPSequenceSerializable) serial).serializeXB(this);
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        if (pullProvider instanceof XBPSequencePullConsumer) {
            this.pullProvider = (XBPSequencePullConsumer) pullProvider;
        } else {
            this.pullProvider = new XBPSequencePullConsumer(pullProvider);
        }
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        XBTToken token = pullProvider.pullToken(XBTTokenType.BEGIN);
        state = XBChildSerialState.ATTRIBUTE_PART;
        return ((XBTBeginToken) token).getTerminationMode();
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        if (state != XBChildSerialState.BLOCK_BEGIN && state != XBChildSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("Block type must be read before attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            pullProvider.pullToken(XBTTokenType.BEGIN);
        }
        XBTToken token = pullProvider.pullToken(XBTTokenType.TYPE);
        state = XBChildSerialState.TYPE;

        return ((XBTTypeToken) token).getBlockType();
    }

    @Override
    public UBNatural pullAttribute() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.DATA) {
            throw new XBSerialException("Unable to get attribute after data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.CHILDREN) {
            throw new XBSerialException("Unable to get attribute after children", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            pullProvider.pullToken(XBTTokenType.BEGIN);
        }

        XBTToken token = pullProvider.pullToken(XBTTokenType.ATTRIBUTE);
        return ((XBTAttributeToken) token).getAttribute();
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
    public InputStream pullData() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.ATTRIBUTES) {
            throw new XBSerialException("No data block allowed after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.DATA) {
            throw new XBSerialException("No data block allowed after data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            pullProvider.pullToken(XBTTokenType.BEGIN);
        }

        XBTToken token = pullProvider.pullToken(XBTTokenType.DATA);
        state = XBChildSerialState.DATA;
        return ((XBTDataToken) token).getData();
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        pullProvider.pullToken(XBTTokenType.END);
        state = XBChildSerialState.EOF;
    }

    @Override
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pullConsist(XBSerializable child) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_END) {
            throw new XBSerialException("No more children available", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.DATA) {
            throw new XBSerialException("No children after data allowed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        
        pullProvider.pullChild(child);
        state = XBChildSerialState.CHILDREN;
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

    @Override
    public XBSerializationMode getSerializationMode() {
        return XBSerializationMode.PULL;
    }

    @Override
    public void begin() throws XBProcessingException, IOException {
        pullBegin();
    }

    @Override
    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException {
        pullType();
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        pullEnd();
    }

    @Override
    public void attribute(UBNatural attributeValue) throws XBProcessingException, IOException {
        attributeValue.setValue(pullAttribute().getLong());
    }

    @Override
    public void consist(XBSerializable serial) throws XBProcessingException, IOException {
        pullConsist(serial);
    }

    @Override
    public void join(XBSerializable serial) throws XBProcessingException, IOException {
        pullJoin(serial);
    }

    @Override
    public void listConsist(XBSerializable serial) throws XBProcessingException, IOException {
        pullListConsist(serial);
    }

    @Override
    public void listJoin(XBSerializable serial) throws XBProcessingException, IOException {
        pullListJoin(serial);
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    @Override
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        throw new XBProcessingException(PUSH_NOT_ALLOWED_EXCEPTION, XBProcessingExceptionType.ILLEGAL_OPERATION);
    }
}
