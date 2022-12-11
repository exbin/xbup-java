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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
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
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTToXBPullConvertor implements XBTPullConsumer, XBPullProvider {

    @Nullable
    private XBTPullProvider pullProvider;
    @Nullable
    private XBToken buffer;

    public XBTToXBPullConvertor() {
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
        buffer = null;
    }

    @Nonnull
    @Override
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
