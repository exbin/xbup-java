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
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTToXBPullWrapper implements XBTPullProvider, XBPullConsumer {

    @Nonnull
    private XBPullProvider pullProvider;
    private final XBFixedBlockType unknownBlockType = new XBFixedBlockType();
    private boolean typeSent = false;
    @Nullable
    private XBToken attributeToken = null;

    public XBTToXBPullWrapper(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void attachXBPullProvider(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Nonnull
    @Override
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
