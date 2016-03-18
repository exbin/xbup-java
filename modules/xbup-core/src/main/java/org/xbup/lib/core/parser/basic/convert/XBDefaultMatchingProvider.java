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
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.parser.token.convert.XBListenerToToken;
import org.xbup.lib.core.util.StreamUtils;

/**
 * XBUP level 0 stream checker.
 *
 * @version 0.1.25 2015/02/14
 * @author ExBin Project (http://exbin.org)
 */
public class XBDefaultMatchingProvider implements XBMatchingProvider {

    private final XBProvider provider;
    private final XBListenerToToken tokenListener = new XBListenerToToken();

    public XBDefaultMatchingProvider(XBProvider provider) {
        this.provider = provider;
    }

    @Override
    public XBBlockTerminationMode matchBeginXB() throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() == XBTokenType.BEGIN) {
            return ((XBBeginToken) token).getTerminationMode();
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchBeginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.BEGIN || terminationMode != ((XBBeginToken) token).getTerminationMode()) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public XBAttribute matchAttribXB() throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() == XBTokenType.ATTRIBUTE) {
            return ((XBAttributeToken) token).getAttribute();
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchAttribXB(XBAttribute value) throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.ATTRIBUTE || ((XBAttributeToken) token).getAttribute().getNaturalLong() != value.getNaturalLong()) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public InputStream matchDataXB() throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() == XBTokenType.DATA) {
            return ((XBDataToken) token).getData();
        }
        throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchDataXB(InputStream data) throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.DATA || !StreamUtils.compareStreams(data, ((XBDataToken) token).getData())) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void matchEndXB() throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.END) {
            throw new XBParseException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    public XBToken pullToken() throws XBProcessingException, IOException {
        provider.produceXB(tokenListener);
        return tokenListener.getToken();
    }
}
