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
import org.exbin.xbup.core.parser.token.pull.XBPullConsumer;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 0 to level 1 event convertor which introduces unknown type.
 *
 * @version 0.1.24 2014/11/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBTToXBPullWrapper implements XBTPullProvider, XBPullConsumer {

    private XBPullProvider pullProvider;
    private final XBFixedBlockType unknownBlockType = new XBFixedBlockType();
    private boolean typeSent = false;
    private XBToken attrToken = null;

    public XBTToXBPullWrapper(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void attachXBPullProvider(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBToken token;
        if (attrToken != null) {
            token = attrToken;
            attrToken = null;
            return new XBTAttributeToken(((XBAttributeToken) token).getAttribute());
        }

        token = pullProvider.pullXBToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                typeSent = false;
                return new XBTBeginToken(((XBBeginToken) token).getTerminationMode());
            }
            case ATTRIBUTE: {
                if (!typeSent) {
                    attrToken = token;
                    typeSent = true;
                    return new XBTTypeToken(unknownBlockType);
                }

                return new XBTAttributeToken(((XBAttributeToken) token).getAttribute());
            }
            case DATA: {
                return new XBTDataToken(((XBDataToken) token).getData());
            }
            case END: {
                return new XBTEndToken();
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
}
