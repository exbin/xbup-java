/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.serial.XBSerialException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.XBTWriteSerialHandler;
import org.exbin.xbup.core.serial.token.XBTTokenOutputSerialHandler;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 serialization handler using basic parser mapping to listener.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTChildListenerSerialHandler implements XBTChildOutputSerialHandler, XBTTokenOutputSerialHandler {

    private XBTEventListener eventListener;
    private XBChildSerialState state;
    private XBTWriteSerialHandler childHandler = null;

    public XBTChildListenerSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    public XBTChildListenerSerialHandler(XBTWriteSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        eventListener = listener;
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state != XBChildSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Unable to set block terminated mode", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putXBTToken(XBTBeginToken.create(terminationMode));
        state = XBChildSerialState.ATTRIBUTE_PART;
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            eventListener.putXBTToken(XBTBeginToken.create(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBTToken(XBTTypeToken.create(type));
        state = XBChildSerialState.TYPE;
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBSerialException, XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_END) {
            throw new XBSerialException("Unable to add attributes after data or child blocks", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Missing block type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putXBTToken(XBTAttributeToken.create(attribute));
        state = XBChildSerialState.ATTRIBUTES;
    }

    @Override
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

        if (child instanceof XBTChildSerializable) {
            XBTChildListenerSerialHandler childOutput = new XBTChildListenerSerialHandler();
            childOutput.attachXBTEventListener(eventListener);
            ((XBTChildSerializable) child).serializeToXB(childOutput);
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
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.ATTRIBUTES) {
            throw new XBSerialException("Data block is not allowed after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.TYPE) {
            throw new XBSerialException("Data event is not allowed after block type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        // TODO test depth for tail data
        /*
         if (state == XBChildSerialState.DATA) {
         throw new XBSerialException("Data event is not allowed after another data event", XBProcessingExceptionType.UNEXPECTED_ORDER);
         }
         if (state == XBChildSerialState.CHILDREN) {
         throw new XBSerialException("Data block is not allowed after children", XBProcessingExceptionType.UNEXPECTED_ORDER);
         } */
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            eventListener.putXBTToken(XBTBeginToken.create(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBTToken(XBTDataToken.create(data));
        state = XBChildSerialState.DATA;
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN || state == XBChildSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("At least one attribute or data required");
        }

        eventListener.putXBTToken(XBTEndToken.create());
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
}
