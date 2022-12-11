/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTMatchingProvider;
import org.exbin.xbup.core.parser.basic.XBTProvider;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.convert.XBTListenerToToken;

/**
 * XBUP level 1 stream checker.
 *
 * @author ExBin Project (https://exbin.org)
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
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchBeginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.BEGIN || terminationMode != ((XBTBeginToken) token).getTerminationMode()) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public XBBlockType matchTypeXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.TYPE) {
            return ((XBTTypeToken) token).getBlockType();
        }
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchTypeXBT(XBBlockType type) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.TYPE || !((XBTTypeToken) token).getBlockType().equals(type)) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public XBAttribute matchAttribXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.ATTRIBUTE) {
            return ((XBTAttributeToken) token).getAttribute();
        }
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchAttribXBT(XBAttribute value) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.ATTRIBUTE || ((XBTAttributeToken) token).getAttribute().getNaturalLong() != value.getNaturalLong()) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public InputStream matchDataXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.DATA) {
            return ((XBTDataToken) token).getData();
        }
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    @Override
    public void matchDataXBT(InputStream data) throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() != XBTTokenType.DATA || data != ((XBTDataToken) token).getData()) {
            throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void matchEndXBT() throws XBProcessingException, IOException {
        XBTToken token = pullToken();
        if (token.getTokenType() == XBTTokenType.END) {
            return;
        }
        throw new XBParsingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    public XBTToken pullToken() throws XBProcessingException, IOException {
        provider.produceXBT(tokenListener);
        return tokenListener.getToken();
    }
}
