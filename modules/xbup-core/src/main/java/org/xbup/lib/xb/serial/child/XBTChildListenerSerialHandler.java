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
package org.xbup.lib.xb.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.XBTAttributeToken;
import org.xbup.lib.xb.parser.token.XBTBeginToken;
import org.xbup.lib.xb.parser.token.XBTDataToken;
import org.xbup.lib.xb.parser.token.XBTEndToken;
import org.xbup.lib.xb.parser.token.XBTTypeToken;
import org.xbup.lib.xb.parser.token.event.XBTEventListener;
import org.xbup.lib.xb.serial.XBSerialException;
import org.xbup.lib.xb.serial.XBSerialState;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.XBTOutputTokenSerialHandler;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * XBUP level 1 XBTChildListener handler.
 *
 * @version 0.1 wr23.0 2014/03/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBTChildListenerSerialHandler implements XBTChildListener, XBTOutputTokenSerialHandler {

    private XBTEventListener eventListener;
    private XBSerialState state;

    public XBTChildListenerSerialHandler() {
        state = XBSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        eventListener = listener;
    }

    @Override
    public void begin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state != XBSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Unable to set block terminated mode", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putXBTToken(new XBTBeginToken(terminationMode));
        state = XBSerialState.ATTRIBUTE_PART;
    }

    @Override
    public void setType(XBBlockType type) throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN) {
            eventListener.putXBTToken(new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBTToken(new XBTTypeToken(type));
        state = XBSerialState.TYPE;
    }

    @Override
    public void addAttribute(UBNatural attribute) throws XBSerialException, XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_END) {
            throw new XBSerialException("Unable to add attributes after data or child blocks", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("Missing block type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        eventListener.putXBTToken(new XBTAttributeToken(attribute));
        state = XBSerialState.ATTRIBUTES;
    }

    @Override
    public void addChild(XBSerializable node, int methodIndex) throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN) {
            throw new XBSerialException("At least one attribute is needed before child", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_END) {
            throw new XBSerialException("Unable to add child after data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        // TODO Support different types of serialization methods
        XBTChildListenerSerialHandler childOutput = new XBTChildListenerSerialHandler();
        childOutput.attachXBTEventListener(eventListener);
        node.serializeXB(XBSerializationType.TO_XB, methodIndex, childOutput);

        state = XBSerialState.CHILDREN;
    }

    @Override
    public void addData(InputStream data) throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.ATTRIBUTES) {
            throw new XBSerialException("Data block is not allowed after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.TYPE) {
            throw new XBSerialException("Data event is not allowed after block type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        // TODO test depth for extended area
        /*
        if (state == XBSerialState.DATA) {
            throw new XBSerialException("Data event is not allowed after another data event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.CHILDREN) {
            throw new XBSerialException("Data block is not allowed after children", XBProcessingExceptionType.UNEXPECTED_ORDER);
        } */
        if (state == XBSerialState.BLOCK_BEGIN) {
            eventListener.putXBTToken(new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBTToken(new XBTDataToken(data));
        state = XBSerialState.DATA;
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN || state == XBSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("At least one attribute or data required");
        }

        eventListener.putXBTToken(new XBTEndToken());
    }
}
