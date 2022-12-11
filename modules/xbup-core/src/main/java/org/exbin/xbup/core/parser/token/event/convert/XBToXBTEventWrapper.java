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
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.XBTEventProducer;

/**
 * XBUP level 0 to level 1 event convertor which introduces unknown type.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBToXBTEventWrapper implements XBEventListener, XBTEventProducer {

    @Nonnull
    private XBTEventListener eventListener;
    private final XBFixedBlockType unknownBlockType = new XBFixedBlockType();
    private boolean typeSent = false;

    public XBToXBTEventWrapper(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
        typeSent = false;
    }

    @Override
    public void putXBToken(XBToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                typeSent = false;
                eventListener.putXBTToken(XBTBeginToken.create(((XBBeginToken) token).getTerminationMode()));
                break;
            }
            case ATTRIBUTE: {
                if (!typeSent) {
                    eventListener.putXBTToken(XBTTypeToken.create(unknownBlockType));
                    typeSent = true;
                }

                eventListener.putXBTToken(XBTAttributeToken.create(((XBAttributeToken) token).getAttribute()));
                break;
            }
            case DATA: {
                eventListener.putXBTToken(XBTDataToken.create(((XBDataToken) token).getData()));
                break;
            }
            case END: {
                eventListener.putXBTToken(XBTEndToken.create());
                break;
            }
            default:
                throw new IllegalStateException("Unexpected token type " + token.getTokenType().toString());
        }
    }
}
