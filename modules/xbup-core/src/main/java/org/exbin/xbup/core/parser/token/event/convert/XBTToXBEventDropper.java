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
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBEventProducer;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 1 to level 0 event convertor which drops node types.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTToXBEventDropper implements XBTEventListener, XBEventProducer {

    @Nonnull
    private XBEventListener target;
    private boolean typeProcessed;

    public XBTToXBEventDropper(XBEventListener target) {
        this.target = target;
    }

    @Override
    public void attachXBEventListener(XBEventListener eventListener) {
        target = eventListener;
        typeProcessed = false;
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        if (typeProcessed && (token.getTokenType() != XBTTokenType.ATTRIBUTE)) {
            target.putXBToken(XBAttributeToken.createZeroToken());
            typeProcessed = false;
        }

        switch (token.getTokenType()) {
            case BEGIN: {
                target.putXBToken(XBBeginToken.create(((XBTBeginToken) token).getTerminationMode()));
                break;
            }

            case TYPE: {
                typeProcessed = true;
                break;
            }

            case ATTRIBUTE: {
                target.putXBToken(XBAttributeToken.create(((XBTAttributeToken) token).getAttribute()));
                break;
            }

            case DATA: {
                target.putXBToken(XBDataToken.create(((XBTDataToken) token).getData()));
                break;
            }

            case END: {
                target.putXBToken(XBEndToken.create());
                break;
            }

            default:
                throw new IllegalStateException("Unexpected token type " + token.getTokenType().toString());
        }
    }
}
