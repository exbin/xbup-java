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
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.param.XBParamConvertor;
import org.xbup.lib.core.parser.param.XBParamListener;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.pull.XBTPullFilter;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 parameter filter.
 * 
 * TODO: Emulate behavior of XBTreeParamExtractor
 *
 * @version 0.1.24 2014/12/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullParamFilter implements XBTPullFilter {

    private XBTPullPreLoader pullProvider;
    private final XBParamConvertor paramConvertor;
    private int currentParameter = 0;
    private int targetParameter = 0;
    private XBParserState state = XBParserState.START;

    public XBTPullParamFilter(XBACatalog catalog) {
        paramConvertor = new XBParamConvertor(new XBParamListener() {

            @Override
            public void beginXBParam(XBBlockParam paramType) throws XBProcessingException, IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void blockXBParam() throws XBProcessingException, IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void listXBParam() throws XBProcessingException, IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void endXBParam() throws XBProcessingException, IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }, catalog);
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = (pullProvider instanceof XBTPullPreLoader ? (XBTPullPreLoader) pullProvider : new XBTPullPreLoader(pullProvider));
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
