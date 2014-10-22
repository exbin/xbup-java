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
package org.xbup.lib.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.XBSerialException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.token.XBTTokenOutputSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 serialization handler using basic parser mapping to listener.
 *
 * @version 0.1.24 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBTChildListenerSerialHandler implements XBTChildOutputSerialHandler, XBTTokenOutputSerialHandler {

    private XBTEventListener eventListener;
    private XBChildSerialState state;

    public XBTChildListenerSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        eventListener = listener;
    }

    @Override
    public void begin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state != XBChildSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Unable to set block terminated mode", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putXBTToken(new XBTBeginToken(terminationMode));
        state = XBChildSerialState.ATTRIBUTE_PART;
    }

    @Override
    public void setType(XBBlockType type) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            eventListener.putXBTToken(new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBTToken(new XBTTypeToken(type));
        state = XBChildSerialState.TYPE;
    }

    @Override
    public void addAttribute(UBNatural attribute) throws XBSerialException, XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_END) {
            throw new XBSerialException("Unable to add attributes after data or child blocks", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Missing block type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putXBTToken(new XBTAttributeToken(attribute));
        state = XBChildSerialState.ATTRIBUTES;
    }

    @Override
    public void addChild(XBSerializable child) throws XBProcessingException, IOException {
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
            // TODO Support different types of serialization methods
            throw new UnsupportedOperationException("Not supported yet.");
        }

        state = XBChildSerialState.CHILDREN;
    }

    @Override
    public void addData(InputStream data) throws XBProcessingException, IOException {
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
            eventListener.putXBTToken(new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBTToken(new XBTDataToken(data));
        state = XBChildSerialState.DATA;
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN || state == XBChildSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("At least one attribute or data required");
        }

        eventListener.putXBTToken(new XBTEndToken());
    }
}