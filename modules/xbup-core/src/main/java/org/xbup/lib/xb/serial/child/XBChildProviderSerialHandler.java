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
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.parser.token.XBAttributeToken;
import org.xbup.lib.xb.parser.token.XBBeginToken;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.XBDataToken;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.parser.token.XBTokenType;
import org.xbup.lib.xb.parser.token.pull.XBPullProvider;
import org.xbup.lib.xb.parser.token.pull.convert.XBPrefixPullProvider;
import org.xbup.lib.xb.serial.XBSerialException;
import org.xbup.lib.xb.serial.XBInputTokenSerialHandler;
import org.xbup.lib.xb.serial.XBSerialState;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * XBUP level 0 XBChildProvider handler.
 *
 * @version 0.1 wr23.0 2014/03/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBChildProviderSerialHandler implements XBChildProvider, XBInputTokenSerialHandler {

    private XBPullProvider pullProvider;
    private XBSerialState state;

    public XBChildProviderSerialHandler() {
        state = XBSerialState.BLOCK_BEGIN;
    }

    @Override
    public void attachXBPullProvider(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBBlockTerminationMode begin() throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        XBToken token = pullProvider.pullXBToken();
        if (token.getTokenType() != XBTokenType.BEGIN) {
            throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBSerialState.ATTRIBUTE_PART;
        return ((XBBeginToken) token).getTerminationMode();
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
            if (pullProvider.pullXBToken().getTokenType() != XBTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBToken token = pullProvider.pullXBToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                state = XBSerialState.CHILDREN;
                break;
            }
            case ATTRIBUTE: {
                state = XBSerialState.ATTRIBUTES;
                return ((XBAttributeToken) token).getAttribute();
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
            if (pullProvider.pullXBToken().getTokenType() != XBTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBToken prefix = null;
        if (state == XBSerialState.ATTRIBUTES) {
            // TODO: Skip attributes
            prefix = pullProvider.pullXBToken();
            while (prefix.getTokenType() == XBTokenType.ATTRIBUTE) {
                prefix = pullProvider.pullXBToken();
            }
            if (prefix.getTokenType() != XBTokenType.BEGIN) {
                throw new XBSerialException("Missing child", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        // TODO support for other serialization types
        state = XBSerialState.CHILDREN;
        XBChildProviderSerialHandler childHandler = new XBChildProviderSerialHandler();
        childHandler.attachXBPullProvider(new XBPrefixPullProvider(pullProvider, prefix));
        child.serializeXB(XBSerializationType.FROM_XB, 0, childHandler);
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
            if (pullProvider.pullXBToken().getTokenType() != XBTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        XBToken token = pullProvider.pullXBToken();
        if (token.getTokenType() != XBTokenType.DATA) {
            throw new XBSerialException("Data block missing", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBSerialState.DATA;
        return ((XBDataToken) token).getData();
    }

    @Override
    public void end() throws XBProcessingException, IOException {
        if (state == XBSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        // TODO process extended area
        // TODO Skip rest
        XBToken token = pullProvider.pullXBToken();
        if (token.getTokenType() != XBTokenType.END) {
            throw new XBSerialException("End method was expected but not received", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBSerialState.EOF;
    }
}
