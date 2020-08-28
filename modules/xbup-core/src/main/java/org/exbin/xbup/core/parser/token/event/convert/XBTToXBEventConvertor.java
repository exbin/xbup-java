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
package org.exbin.xbup.core.parser.token.event.convert;

import java.io.IOException;
import javax.annotation.Nonnull;
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
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBEventProducer;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 1 to level 0 event convertor.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTToXBEventConvertor implements XBTEventListener, XBEventProducer {

    @Nonnull
    private XBEventListener eventListener;

    public XBTToXBEventConvertor(XBEventListener eventListener) {
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
                if (((XBTTypeToken) token).getBlockType() instanceof XBFBlockType) {
                    eventListener.putXBToken(XBAttributeToken.create(((XBFBlockType) ((XBTTypeToken) token).getBlockType()).getGroupID()));
                    eventListener.putXBToken(XBAttributeToken.create(((XBFBlockType) ((XBTTypeToken) token).getBlockType()).getBlockID()));
                    break;
                } else {
                    throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                }
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
