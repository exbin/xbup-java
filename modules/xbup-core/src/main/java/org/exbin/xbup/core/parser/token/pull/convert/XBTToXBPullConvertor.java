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
import org.exbin.xbup.core.block.XBFBlockType;
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
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.parser.token.pull.XBTPullConsumer;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 to level 0 pull convertor.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTToXBPullConvertor implements XBTPullConsumer, XBPullProvider {

    @Nullable
    private XBTPullProvider pullProvider;
    @Nullable
    private XBToken buffer;

    public XBTToXBPullConvertor() {
    }

    @Override
    public void attachXBTPullProvider(@Nonnull XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
        buffer = null;
    }

    @Override
    @Nonnull
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        if (buffer != null) {
            XBToken token = buffer;
            buffer = null;
            return token;
        }

        XBTToken token = pullProvider.pullXBTToken();
        switch (token.getTokenType()) {
            case BEGIN:
                return XBBeginToken.create(((XBTBeginToken) token).getTerminationMode());

            case TYPE: {
                if (((XBTTypeToken) token).getBlockType() instanceof XBFBlockType) {
                    buffer = XBAttributeToken.create(((XBFBlockType) ((XBTTypeToken) token).getBlockType()).getBlockID());
                    return XBAttributeToken.create(((XBFBlockType) ((XBTTypeToken) token).getBlockType()).getGroupID());
                }

                throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
            }

            case ATTRIBUTE:
                return XBAttributeToken.create(((XBTAttributeToken) token).getAttribute());

            case DATA:
                return XBDataToken.create(((XBTDataToken) token).getData());

            case END:
                return XBEndToken.create();

            default:
                throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNKNOWN);
        }
    }
}
