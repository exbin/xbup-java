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
package org.exbin.xbup.core.parser.token.pull.convert;

import java.io.IOException;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.parser.token.pull.XBPullConsumer;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 0 to level 1 pull convertor.
 *
 * @version 0.1.23 2014/02/06
 * @author ExBin Project (http://exbin.org)
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
                return new XBTBeginToken(((XBBeginToken) token).getTerminationMode());
            }

            case ATTRIBUTE: {
                if (first) {
                    first = false;
                    buffer = pullProvider.pullXBToken();
                    XBFixedBlockType blockType;

                    if (buffer.getTokenType() == XBTokenType.ATTRIBUTE) {
                        blockType = new XBFixedBlockType(((XBAttributeToken) token).getAttribute().getNaturalLong(), ((XBAttributeToken) buffer).getAttribute().getNaturalLong());
                        buffer = null;
                    } else {
                        blockType = new XBFixedBlockType(((XBAttributeToken) token).getAttribute().getNaturalLong(), 0);
                    }

                    return new XBTTypeToken(blockType);
                } else {
                    return new XBTAttributeToken(((XBAttributeToken) token).getAttribute());
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
