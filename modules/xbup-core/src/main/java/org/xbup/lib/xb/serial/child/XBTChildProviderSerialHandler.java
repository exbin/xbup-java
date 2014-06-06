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
import org.xbup.lib.xb.parser.token.XBTToken;
import org.xbup.lib.xb.parser.token.XBTTokenType;
import org.xbup.lib.xb.parser.token.XBTTypeToken;
import org.xbup.lib.xb.parser.token.pull.XBTPullProvider;
import org.xbup.lib.xb.parser.token.pull.convert.XBTPrefixPullProvider;
import org.xbup.lib.xb.serial.XBSerialException;
import org.xbup.lib.xb.serial.XBSerialState;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.XBTInputTokenSerialHandler;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * XBUP level 1 XBTChildProvider handler.
 *
 * @version 0.1 wr23.0 2014/03/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBTChildProviderSerialHandler implements XBTChildProvider, XBTInputTokenSerialHandler {
    
    private XBTPullProvider pullProvider;
    private XBSerialState state;
    private XBTToken prefix = null;

    public XBTChildProviderSerialHandler() {
        state = XBSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBBlockTerminationMode begin() throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.BEGIN) {
            throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBSerialState.ATTRIBUTE_PART;
        return ((XBTBeginToken) token).getTerminationMode();
    }

    @Override
    public XBBlockType getType() throws XBProcessingException, IOException {
        if (state != XBSerialState.BLOCK_BEGIN && state != XBSerialState.ATTRIBUTE_PART) {
            throw new XBSerialException("Block type must be read before attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN) {
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.TYPE) {
            throw new XBSerialException("Missing block type or reading data block", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        state = XBSerialState.TYPE;

        return ((XBTTypeToken)token).getBlockType();
    }

    @Override
    public UBNatural nextAttribute() throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.DATA) {
            throw new XBSerialException("Unable to get attribute after data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.CHILDREN) {
            throw new XBSerialException("Unable to get attribute after children", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN) {
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBTToken token = pullProvider.pullXBTToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                state = XBSerialState.CHILDREN;
                prefix = token;
                break;
            }
            case ATTRIBUTE: {
                state = XBSerialState.ATTRIBUTES;
                return ((XBTAttributeToken) token).getAttribute();
            }
            case DATA: {
                if (state == XBSerialState.ATTRIBUTES) {
                    throw new XBSerialException("Unexpected data after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }

                throw new XBSerialException("Reading attributes of data block", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
            case END: {
                if (state != XBSerialState.BLOCK_END) {
                    throw new XBSerialException("Block without data or attributes not allowed", XBProcessingExceptionType.UNEXPECTED_ORDER);
                }
                state = XBSerialState.EOF;
            }
        }

        state = XBSerialState.ATTRIBUTES;
        return new UBNat32();
    }

    @Override
    public void nextChild(XBSerializable child, int methodIndex) throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_END) {
            throw new XBSerialException("No more children available", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.DATA) {
            throw new XBSerialException("No children after data allowed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN) {
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        if (state == XBSerialState.ATTRIBUTES && prefix == null) {
            // TODO: Skip attributes
            prefix = pullProvider.pullXBTToken();
            while (prefix.getTokenType() == XBTTokenType.ATTRIBUTE) {
                prefix = pullProvider.pullXBTToken();
            }
            if (prefix.getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Missing child", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        // TODO support for other serialization types
        state = XBSerialState.CHILDREN;
        XBTChildProviderSerialHandler childHandler = new XBTChildProviderSerialHandler();
        childHandler.attachXBTPullProvider(new XBTPrefixPullProvider(pullProvider, prefix));
        child.serializeXB(XBSerializationType.FROM_XB, 0, childHandler);
        prefix = null;
    }

    @Override
    public InputStream nextData() throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.ATTRIBUTES) {
            throw new XBSerialException("No data block allowed after attributes", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.DATA) {
            throw new XBSerialException("No data block allowed after data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        if (state == XBSerialState.BLOCK_BEGIN) {
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.DATA) {
            throw new XBSerialException("Data block missing", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBSerialState.DATA;
        return ((XBTDataToken) token).getData();
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        // TODO Skip rest
        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.END) {
            throw new XBSerialException("End method was expected but not received", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBSerialState.EOF;
    }
}
