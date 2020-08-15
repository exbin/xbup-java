/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBMatchingProvider;
import org.exbin.xbup.core.parser.basic.XBProvider;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.parser.token.convert.XBListenerToToken;
import org.exbin.xbup.core.util.StreamUtils;

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
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchBeginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.BEGIN || terminationMode != ((XBBeginToken) token).getTerminationMode()) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public XBAttribute matchAttribXB() throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() == XBTokenType.ATTRIBUTE) {
            return ((XBAttributeToken) token).getAttribute();
        }
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchAttribXB(XBAttribute value) throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.ATTRIBUTE || ((XBAttributeToken) token).getAttribute().getNaturalLong() != value.getNaturalLong()) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public InputStream matchDataXB() throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() == XBTokenType.DATA) {
            return ((XBDataToken) token).getData();
        }
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchDataXB(InputStream data) throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.DATA || !StreamUtils.compareStreams(data, ((XBDataToken) token).getData())) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void matchEndXB() throws XBProcessingException, IOException {
        XBToken token = pullToken();
        if (token.getTokenType() != XBTokenType.END) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    public XBToken pullToken() throws XBProcessingException, IOException {
        provider.produceXB(tokenListener);
        return tokenListener.getToken();
    }
}
