/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import javax.annotation.ParametersAreNonnullByDefault;
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
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBToXBTPullUnknownConvertor implements XBPullProvider, XBTPullConsumer {

    @Nonnull
    private XBTPullProvider pullProvider;
    @Nonnull
    private final XBFixedBlockType unknownBlockType = new XBFixedBlockType();

    public XBToXBTPullUnknownConvertor(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Nonnull
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
                return XBBeginToken.create(((XBTBeginToken) token).getTerminationMode());
            }
            case ATTRIBUTE: {
                return XBAttributeToken.create(((XBTAttributeToken) token).getAttribute());
            }
            case DATA: {
                return XBDataToken.create(((XBTDataToken) token).getData());
            }
            case END: {
                return XBEndToken.create();
            }
            default:
                throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNKNOWN);
        }
    }
}
