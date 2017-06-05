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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTToXBPullWrapper implements XBTPullProvider, XBPullConsumer {

    @Nonnull
    private XBPullProvider pullProvider;
    private final XBFixedBlockType unknownBlockType = new XBFixedBlockType();
    private boolean typeSent = false;
    @Nullable
    private XBToken attributeToken = null;

    public XBTToXBPullWrapper(@Nonnull XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void attachXBPullProvider(@Nonnull XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    @Nonnull
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBToken token;
        if (attributeToken != null) {
            token = attributeToken;
            attributeToken = null;
            return XBTAttributeToken.create(((XBAttributeToken) token).getAttribute());
        }

        token = pullProvider.pullXBToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                typeSent = false;
                return XBTBeginToken.create(((XBBeginToken) token).getTerminationMode());
            }
            case ATTRIBUTE: {
                if (!typeSent) {
                    attributeToken = token;
                    typeSent = true;
                    return XBTTypeToken.create(unknownBlockType);
                }

                return XBTAttributeToken.create(((XBAttributeToken) token).getAttribute());
            }
            case DATA: {
                return XBTDataToken.create(((XBDataToken) token).getData());
            }
            case END: {
                return XBTEndToken.create();
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
}
