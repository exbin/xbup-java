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
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.serial.XBReadSerialHandler;
import org.exbin.xbup.core.serial.XBSerialException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.token.XBTokenInputSerialHandler;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 0 serialization handler using basic parser mapping to provider.
 *
 * @version 0.1.24 2014/12/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBChildProviderSerialHandler implements XBChildInputSerialHandler, XBTokenInputSerialHandler {

    private XBPullProvider pullProvider;
    private XBChildSerialState state;
    private XBReadSerialHandler childHandler = null;

    public XBChildProviderSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    public XBChildProviderSerialHandler(XBReadSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBPullProvider(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBBlockTerminationMode begin() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        XBToken token = pullProvider.pullXBToken();
        if (token.getTokenType() != XBTokenType.BEGIN) {
            throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBChildSerialState.ATTRIBUTE_PART;
        return ((XBBeginToken) token).getTerminationMode();
    }

    @Override
    public XBAttribute nextAttribute() throws XBProcessingException, IOException {
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
            if (pullProvider.pullXBToken().getTokenType() != XBTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBToken token = pullProvider.pullXBToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                state = XBChildSerialState.CHILDREN;
                break;
            }
            case ATTRIBUTE: {
                state = XBChildSerialState.ATTRIBUTES;
                return ((XBAttributeToken) token).getAttribute();
            }
            case DATA: {
                if (state == XBChildSerialState.ATTRIBUTES) {
                    throw new XBSerialException("Unexpected data after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                throw new XBSerialException("Reading attributes of data block", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
            case END: {
                if (state != XBChildSerialState.BLOCK_END) {
                    throw new XBSerialException("Block without data or attributes not allowed", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                state = XBChildSerialState.EOF;
                break;
            }
            default: {
                throw new XBSerialException("Unexpected token type", XBProcessingExceptionType.UNSUPPORTED);
            }
        }

        state = XBChildSerialState.ATTRIBUTES;
        return new UBNat32();
    }

    @Override
    public void nextChild(XBSerializable child) throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_END) {
            throw new XBSerialException("No more children available", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.DATA) {
            throw new XBSerialException("No children after data allowed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            if (pullProvider.pullXBToken().getTokenType() != XBTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        if (child instanceof XBChildSerializable) {
            state = XBChildSerialState.CHILDREN;
            XBChildProviderSerialHandler childInput = new XBChildProviderSerialHandler();
            childInput.attachXBPullProvider(pullProvider);
            ((XBChildSerializable) child).serializeFromXB(childInput);
        } else {
            if (childHandler != null) {
                childHandler.read(child);
            } else {
                throw new XBProcessingException("Unsupported child serialization", XBProcessingExceptionType.UNKNOWN);
            }
        }
    }

    @Override
    public InputStream nextData() throws XBProcessingException, IOException {
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
            if (pullProvider.pullXBToken().getTokenType() != XBTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBToken token = pullProvider.pullXBToken();
        if (token.getTokenType() != XBTokenType.DATA) {
            throw new XBSerialException("Data block missing", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBChildSerialState.DATA;
        return ((XBDataToken) token).getData();
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        // TODO process tail data
        XBToken token = pullProvider.pullXBToken();
        if (token.getTokenType() != XBTokenType.END) {
            throw new XBSerialException("End method was expected but not received", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBChildSerialState.EOF;
    }
}
