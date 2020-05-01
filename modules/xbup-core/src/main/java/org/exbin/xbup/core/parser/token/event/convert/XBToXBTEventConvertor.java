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
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.XBTEventProducer;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 0 to level 1 event convertor.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBToXBTEventConvertor implements XBEventListener, XBTEventProducer {

    @Nonnull
    private XBTEventListener target;
    private boolean expectType;
    @Nullable
    private UBNatural group;

    public XBToXBTEventConvertor(@Nonnull XBTEventListener target) {
        this.target = target;
        expectType = true;
    }

    @Override
    public void attachXBTEventListener(@Nonnull XBTEventListener eventListener) {
        target = eventListener;
    }

    @Override
    public void putXBToken(@Nonnull XBToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                if (expectType && group != null) {
                    target.putXBTToken(XBTTypeToken.create(new XBFixedBlockType(group.getLong(), 0)));
                }

                target.putXBTToken(XBTBeginToken.create(((XBBeginToken) token).getTerminationMode()));
                expectType = true;
                group = null;
                break;
            }

            case ATTRIBUTE: {
                if (expectType) {
                    if (group != null) {
                        target.putXBTToken(XBTTypeToken.create(new XBFixedBlockType(group.getLong(), (((XBAttributeToken) token).getAttribute()).getNaturalLong())));
                        expectType = false;
                    } else {
                        group = ((XBAttributeToken) token).getAttribute().convertToNatural();
                    }
                } else {
                    target.putXBTToken(XBTAttributeToken.create(((XBAttributeToken) token).getAttribute()));
                }
                break;
            }

            case DATA: {
                if (expectType && group != null) {
                    target.putXBTToken(XBTTypeToken.create(new XBFixedBlockType(group.getLong(), 0)));
                }

                target.putXBTToken(XBTDataToken.create(((XBDataToken) token).getData()));
                break;
            }

            case END: {
                if (expectType && group != null) {
                    target.putXBTToken(XBTTypeToken.create(new XBFixedBlockType(group.getLong(), 0)));
                }

                target.putXBTToken(XBTEndToken.create());
                break;
            }

            default:
                throw new IllegalStateException("Unexpected token type " + token.getTokenType().toString());
        }
    }
}
