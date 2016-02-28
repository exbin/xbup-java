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
package org.xbup.lib.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.XBSerialException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.XBTReadSerialHandler;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 serialization handler using basic parser mapping to provider.
 *
 * @version 0.1.24 2015/01/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBTChildProviderSerialHandler implements XBTChildInputSerialHandler, XBTTokenInputSerialHandler {

    private XBTPullProvider pullProvider;
    private XBChildSerialState state;
    private XBTReadSerialHandler childHandler = null;

    public XBTChildProviderSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    public XBTChildProviderSerialHandler(XBTReadSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.BEGIN) {
            throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBChildSerialState.ATTRIBUTE_PART;
        return ((XBTBeginToken) token).getTerminationMode();
    }

    @Override
    public XBBlockType pullType() throws XBProcessingException, IOException {
        if (state != XBChildSerialState.BLOCK_BEGIN && state != XBChildSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("Block type must be read before attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBChildSerialState.BLOCK_BEGIN) {
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.TYPE) {
            throw new XBSerialException("Missing block type or reading data block", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        state = XBChildSerialState.TYPE;

        return ((XBTTypeToken) token).getBlockType();
    }

    @Override
    public XBAttribute pullAttribute() throws XBProcessingException, IOException {
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
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBTToken token = pullProvider.pullXBTToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                state = XBChildSerialState.CHILDREN;
                break;
            }
            case ATTRIBUTE: {
                state = XBChildSerialState.ATTRIBUTES;
                return ((XBTAttributeToken) token).getAttribute();
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
    public byte pullByteAttribute() throws XBProcessingException, IOException {
        return (byte) pullAttribute().getNaturalInt();
    }

    @Override
    public short pullShortAttribute() throws XBProcessingException, IOException {
        return (short) pullAttribute().getNaturalInt();
    }

    @Override
    public int pullIntAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getNaturalInt();
    }

    @Override
    public long pullLongAttribute() throws XBProcessingException, IOException {
        return pullAttribute().getNaturalLong();
    }

    @Override
    public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
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
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        if (child instanceof XBTChildSerializable) {
            state = XBChildSerialState.CHILDREN;
            XBTChildProviderSerialHandler childInput = new XBTChildProviderSerialHandler();
            childInput.attachXBTPullProvider(pullProvider);
            ((XBTChildSerializable) child).serializeFromXB(childInput);
        } else {
            if (childHandler != null) {
                childHandler.read(child);
            } else {
                throw new XBProcessingException("Unsupported child serialization", XBProcessingExceptionType.UNKNOWN);
            }
        }
    }

    @Override
    public void pullAppend(XBSerializable child) throws XBProcessingException, IOException {
        if (child instanceof XBTChildSerializable) {
            ((XBTChildSerializable) child).serializeFromXB(this);
        } else {
            if (childHandler != null) {
                childHandler.read(child);
            } else {
                throw new XBProcessingException("Unsupported child serialization", XBProcessingExceptionType.UNKNOWN);
            }
        }
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
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.DATA) {
            throw new XBSerialException("Data block missing", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBChildSerialState.DATA;
        return ((XBTDataToken) token).getData();
    }

    @Override
    public void pullEnd() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.END) {
            throw new XBSerialException("End token was expected but not received", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBChildSerialState.EOF;
    }
}
