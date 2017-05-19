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
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.parser.token.pull.XBTPullConsumer;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 0 to level 1 pull convertor which introduces unknown type.
 *
 * @version 0.2.1 2017/05/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBToXBTPullUnknownConvertor implements XBPullProvider, XBTPullConsumer {

    private XBTPullProvider pullProvider;
    private final XBFixedBlockType unknownBlockType = new XBFixedBlockType();

    public XBToXBTPullUnknownConvertor(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        XBTToken token = pullProvider.pullXBTToken();
        if (token.getTokenType() == XBTTokenType.TYPE) {
            if (!unknownBlockType.equals(((XBTTypeToken) token).getBlockType())) {
                throw new XBProcessingException("Unexpected token type - Unknown type expected", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
            }

            token = pullProvider.pullXBTToken();
        }

        switch (token.getTokenType()) {
            case BEGIN: {
                return XBBeginToken.createToken(((XBTBeginToken) token).getTerminationMode());
            }
            case ATTRIBUTE: {
                return new XBAttributeToken(((XBTAttributeToken) token).getAttribute());
            }
            case DATA: {
                return new XBDataToken(((XBTDataToken) token).getData());
            }
            case END: {
                return new XBEndToken();
            }
        }

        throw new IllegalStateException();
    }
}
