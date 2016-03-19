/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.serial.XBSerialException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.XBWriteSerialHandler;
import org.exbin.xbup.core.serial.token.XBTokenOutputSerialHandler;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 0 serialization handler using basic parser mapping to listener.
 *
 * @version 0.1.24 2014/11/26
 * @author ExBin Project (http://exbin.org)
 */
public class XBChildListenerSerialHandler implements XBChildOutputSerialHandler, XBTokenOutputSerialHandler {

    private XBEventListener eventListener;
    private XBChildSerialState state;
    private XBWriteSerialHandler childHandler = null;
    private int depth = 0;

    public XBChildListenerSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    public XBChildListenerSerialHandler(XBWriteSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBEventListener(XBEventListener listener) {
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

        depth++;
        eventListener.putXBToken(new XBBeginToken(terminationMode));
        state = XBChildSerialState.ATTRIBUTE_PART;
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
            eventListener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBToken(new XBAttributeToken(attribute));
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

        if (child instanceof XBChildSerializable) {
            XBChildListenerSerialHandler childOutput = new XBChildListenerSerialHandler();
            childOutput.attachXBEventListener(eventListener);
            ((XBChildSerializable) child).serializeToXB(childOutput);
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
    public void addData(InputStream data) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if (depth == 1 && state != XBChildSerialState.BLOCK_BEGIN && state != XBChildSerialState.BLOCK_END) {
            eventListener.putXBToken(new XBDataToken(data));
            state = XBChildSerialState.BLOCK_END;
            return;
        }

        if (state == XBChildSerialState.ATTRIBUTES) {
            throw new XBSerialException("Data block is not allowed after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if (state == XBChildSerialState.DATA) {
            throw new XBSerialException("Data event is not allowed after another data event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.CHILDREN) {
            throw new XBSerialException("Data block is not allowed after children", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            eventListener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        }

        eventListener.putXBToken(new XBDataToken(data));
        state = XBChildSerialState.DATA;
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN || state == XBChildSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("At least one attribute or data required", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        depth--;
        eventListener.putXBToken(new XBEndToken());
    }
}
