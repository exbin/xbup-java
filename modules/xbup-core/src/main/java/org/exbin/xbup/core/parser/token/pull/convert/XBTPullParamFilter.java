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
package org.exbin.xbup.core.parser.token.pull.convert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.definition.XBBlockParam;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.XBParserState;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.pull.XBTPullFilter;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * TODO: XBUP level 1 parameter filter.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTPullParamFilter implements XBTPullFilter {

    private XBTPullPreLoader pullProvider;
    private int currentParameter = 0;
    private int targetParameter = 0;
    private XBBlockParam currentParamType;
    private XBParserState state = XBParserState.START;
    private XBBlockType blockType;
    private final List<XBTAttributeToken> attributeList;

    public XBTPullParamFilter(XBACatalog catalog) {
        attributeList = new ArrayList<>();
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider provider) {
        pullProvider = (provider instanceof XBTPullPreLoader ? (XBTPullPreLoader) provider : new XBTPullPreLoader(provider));
    }

    public void init() throws XBProcessingException, IOException {
        if (pullProvider.pullXBTToken().getTokenType() != XBTTokenType.BEGIN) {
            throw new XBProcessingException("Begin token was expected for parameters processing", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
        XBTToken typeToken = pullProvider.pullXBTToken();
        if (typeToken.getTokenType() != XBTTokenType.TYPE) {
            throw new XBProcessingException("Type token was expected for parameters processing", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        blockType = ((XBTTypeToken) typeToken).getBlockType();
        attributeList.clear();
        while (pullProvider.getNextTokenType() == XBTTokenType.ATTRIBUTE) {
            attributeList.add((XBTAttributeToken) pullProvider.pullXBTToken());
        }
    }

    @Nonnull
    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        if (state == XBParserState.START) {
            return XBTEndToken.create();
        }

        if (currentParameter == targetParameter) {
            return pullProvider.pullXBTToken();
        }

        return XBTEndToken.create();
    }

    public void setParameterIndex(int parameterIndex) throws XBProcessingException {
        if (parameterIndex < currentParameter) {
            throw new XBProcessingException("Cannot process already processed parameter", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        targetParameter = parameterIndex;
    }
}
