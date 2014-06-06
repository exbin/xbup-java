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
package org.xbup.lib.xb.parser.token.pull.convert;

import java.io.IOException;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.parser.token.XBAttributeToken;
import org.xbup.lib.xb.parser.token.XBBeginToken;
import org.xbup.lib.xb.parser.token.XBDataToken;
import org.xbup.lib.xb.parser.token.XBTAttributeToken;
import org.xbup.lib.xb.parser.token.XBTBeginToken;
import org.xbup.lib.xb.parser.token.XBTDataToken;
import org.xbup.lib.xb.parser.token.XBTEndToken;
import org.xbup.lib.xb.parser.token.XBTToken;
import org.xbup.lib.xb.parser.token.XBTTypeToken;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.parser.token.XBTokenType;
import org.xbup.lib.xb.parser.token.pull.XBPullConsumer;
import org.xbup.lib.xb.parser.token.pull.XBPullProvider;
import org.xbup.lib.xb.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 0 to level 1 pull convertor.
 *
 * @version 0.1 wr23.0 2014/02/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBToXBTPullConvertor implements XBPullConsumer, XBTPullProvider {

    private XBPullProvider pullProvider;
    private boolean first;
    private XBToken buffer;

    public XBToXBTPullConvertor(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void attachXBPullProvider(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
        first = true;
        buffer = null;
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBToken token;
        if (buffer != null) {
            token = buffer;
            buffer = null;
        } else {
            token = pullProvider.pullXBToken();
        }

        switch (token.getTokenType()) {
            case BEGIN: {
                first = true;
                return new XBTBeginToken(((XBBeginToken)token).getTerminationMode());
            }

            case ATTRIBUTE: {
                if (first) {
                    first = false;
                    buffer = pullProvider.pullXBToken();
                    XBFixedBlockType decl;

                    if (buffer.getTokenType() == XBTokenType.ATTRIBUTE) {
                        decl = new XBFixedBlockType(((XBAttributeToken)token).getAttribute().getLong(), ((XBAttributeToken)buffer).getAttribute().getLong());
                        buffer = null;
                    } else {
                        decl = new XBFixedBlockType(((XBAttributeToken)token).getAttribute().getLong(), 0);
                    }
                    
                    return new XBTTypeToken(decl);
                } else {
                    return new XBTAttributeToken(((XBAttributeToken)token).getAttribute());
                }
            }

            case DATA:
                return new XBTDataToken(((XBDataToken) token).getData());

            case END:
                return new XBTEndToken();

            default:
                throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNKNOWN);
        }
    }
}
