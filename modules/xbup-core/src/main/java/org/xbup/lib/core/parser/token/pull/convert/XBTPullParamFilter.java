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
package org.xbup.lib.core.parser.token.pull.convert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.pull.XBTPullFilter;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 parameter filter.
 *
 * @version 0.1.24 2014/12/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullParamFilter implements XBTPullFilter {

    private XBTPullPreLoader pullProvider;
    private int currentParameter = 0;
    private XBBlockParam currentParamType;
    private int targetParameter = 0;
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

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        if (state == XBParserState.START) {
            return new XBTEndToken();
        }

        if (currentParameter == targetParameter) {
            return pullProvider.pullXBTToken();
        }

        return new XBTEndToken();
    }

    public void setParameterIndex(int parameterIndex) throws XBProcessingException {
        if (parameterIndex < currentParameter) {
            throw new XBProcessingException("Cannot process already processed parameter", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        targetParameter = parameterIndex;
    }
}
