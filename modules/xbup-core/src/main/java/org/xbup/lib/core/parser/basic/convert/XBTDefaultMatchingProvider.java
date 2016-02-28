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
package org.xbup.lib.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.convert.XBTListenerToToken;

/**
 * XBUP level 1 stream checker.
 *
 * @version 0.1.25 2015/02/14
 * @author ExBin Project (http://exbin.org)
 */
public class XBTDefaultMatchingProvider implements XBTMatchingProvider {

    private final XBTProvider provider;
    private final XBTListenerToToken tokenListener = new XBTListenerToToken();

    public XBTDefaultMatchingProvider(XBTProvider provider) {
        this.provider = provider;
    }

    @Override
    public XBBlockTerminationMode matchBeginXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.BEGIN) {
            return ((XBTBeginToken) token).getTerminationMode();
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchBeginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.BEGIN || terminationMode != ((XBTBeginToken) token).getTerminationMode()) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public XBBlockType matchTypeXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.TYPE) {
            return ((XBTTypeToken) token).getBlockType();
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchTypeXBT(XBBlockType type) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.TYPE || !((XBTTypeToken) token).getBlockType().equals(type)) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public XBAttribute matchAttribXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.ATTRIBUTE) {
            return ((XBTAttributeToken) token).getAttribute();
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchAttribXBT(XBAttribute value) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.ATTRIBUTE || ((XBTAttributeToken) token).getAttribute().getNaturalLong() != value.getNaturalLong()) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public InputStream matchDataXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.DATA) {
            return ((XBTDataToken) token).getData();
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchDataXBT(InputStream data) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.DATA || data != ((XBTDataToken) token).getData()) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void matchEndXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.END) {
            return;
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    public XBTToken pullToken() throws XBProcessingException, IOException {
        provider.produceXBT(tokenListener);
        return tokenListener.getToken();
    }
}
