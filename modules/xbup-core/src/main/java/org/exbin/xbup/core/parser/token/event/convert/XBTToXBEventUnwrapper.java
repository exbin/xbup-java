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
package org.exbin.xbup.core.parser.token.event.convert;

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
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBEventProducer;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 0 to level 1 event convertor which introduces unknown type.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTToXBEventUnwrapper implements XBTEventListener, XBEventProducer {

    @Nonnull
    private XBEventListener eventListener;
    @Nonnull
    private final XBFixedBlockType unknownBlockType = new XBFixedBlockType();

    public XBTToXBEventUnwrapper(XBEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attachXBEventListener(XBEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                eventListener.putXBToken(XBBeginToken.create(((XBTBeginToken) token).getTerminationMode()));
                break;
            }
            case TYPE: {
                if (!unknownBlockType.equals(((XBTTypeToken) token).getBlockType())) {
                    throw new XBProcessingException("Unexpected token type - Unknown type expected", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                }

                break;
            }
            case ATTRIBUTE: {
                eventListener.putXBToken(XBAttributeToken.create(((XBTAttributeToken) token).getAttribute()));
                break;
            }
            case DATA: {
                eventListener.putXBToken(XBDataToken.create(((XBTDataToken) token).getData()));
                break;
            }
            case END: {
                eventListener.putXBToken(XBEndToken.create());
                break;
            }
            default:
                throw new IllegalStateException("Unexpected token type " + token.getTokenType().toString());
        }
    }
}
