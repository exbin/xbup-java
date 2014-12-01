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
import java.util.List;
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
import org.xbup.lib.core.parser.token.pull.convert.XBTPullPreLoader;
import org.xbup.lib.core.serial.XBReadSerialHandler;
import org.xbup.lib.core.serial.XBSerialException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.token.XBTTokenInputSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 2 serialization handler using basic parser mapping to provider.
 *
 * @version 0.1.24 2014/12/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBAChildProviderSerialHandler implements XBAChildInputSerialHandler, XBTTokenInputSerialHandler {

    private XBTPullPreLoader pullProvider;
    private XBChildSerialState state;
    private XBReadSerialHandler childHandler = null;

    public XBAChildProviderSerialHandler() {
        state = XBChildSerialState.BLOCK_BEGIN;
    }

    public XBAChildProviderSerialHandler(XBReadSerialHandler childHandler) {
        this();
        this.childHandler = childHandler;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = (pullProvider instanceof XBTPullPreLoader ? (XBTPullPreLoader) pullProvider : new XBTPullPreLoader(pullProvider));
    }

    @Override
    public XBBlockTerminationMode begin() throws XBProcessingException, IOException {
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
    public XBBlockType getType() throws XBProcessingException, IOException {
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
    public XBBlockType matchType(XBBlockType blockType) throws XBProcessingException, IOException {
        XBBlockType type = getType();
        if (blockType.equals(type)) {
            return type;
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockType matchType(List<XBBlockType> blockTypes) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UBNatural nextAttribute() throws XBProcessingException, IOException {
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
            if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
                throw new XBSerialException("Block must start with block begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        if (state == XBChildSerialState.ATTRIBUTES) {
            skipAttributes();
            if (pullProvider.getNextTokenType() != XBTTokenType.BEGIN) {
                throw new UnsupportedOperationException("Not supported yet.");
                // TODO empty blocks throw new XBSerialException("Missing child", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            state = XBChildSerialState.CHILDREN;
        }

        if (childHandler != null) {
            childHandler.read(child);
        } else {
            throw new XBProcessingException("Unsupported child serialization", XBProcessingExceptionType.UNKNOWN);
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
    public void end() throws XBProcessingException, IOException {
        if (state == XBChildSerialState.EOF) {
            throw new XBSerialException("Unexpected method after block already finished", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if (state == XBChildSerialState.ATTRIBUTES) {
            skipAttributes();
        } else if (state == XBChildSerialState.CHILDREN) {
            skipChildren();
        }

        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() != XBTTokenType.END) {
            throw new XBSerialException("End token was expected but not received", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        state = XBChildSerialState.EOF;
    }

    // TODO: Make it later, that more effective skip is used when source
    // supports it
    /**
     * Skip remaining attributes if child is requested.
     */
    private void skipAttributes() throws XBProcessingException, IOException {
        while (pullProvider.getNextTokenType() == XBTTokenType.ATTRIBUTE) {
            pullProvider.pullXBTToken();
        }
    }

    /**
     * Skip children until end token is reached.
     */
    private void skipChildren() throws XBProcessingException, IOException {
        while (pullProvider.getNextTokenType() == XBTTokenType.BEGIN) {
            skipChild();
        }
    }

    /**
     * Skip single child.
     */
    private void skipChild() throws XBProcessingException, IOException {
        int depth = 0;
        do {
            XBTToken token = pullProvider.pullXBTToken();
            switch (token.getTokenType()) {
                case BEGIN: {
                    depth++;
                    break;
                }
                case END: {
                    depth--;
                    break;
                }
            }
        } while (depth > 0);
    }
}
