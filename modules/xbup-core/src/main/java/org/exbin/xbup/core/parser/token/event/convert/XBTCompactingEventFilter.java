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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBTEventFilter;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Level 1 filter performing compacting operation.
 *
 * This should be usable for level 2 compacting conversions.
 *
 * @version 0.2.0 2015/09/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBTCompactingEventFilter implements XBTEventFilter {

    @Nonnull
    private XBTEventListener eventListener;

    private boolean unknownMode = false;
    private boolean emptyDataMode = true;
    private int zeroAttributes = 0;
    private final List<XBBlockTerminationMode> emptyNodes = new ArrayList<>();
    @Nullable
    private XBBlockTerminationMode blockMode = null;

    public XBTCompactingEventFilter(@Nonnull XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attachXBTEventListener(@Nonnull XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void putXBTToken(@Nonnull XBTToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                blockMode = ((XBTBeginToken) token).getTerminationMode();
                emptyDataMode = true;
                zeroAttributes = 0;
                break;
            }
            case TYPE: {
                flushEmptyNodes();
                if (emptyDataMode) {
                    eventListener.putXBTToken(XBTBeginToken.create(blockMode));
                    emptyDataMode = false;
                }
                unknownMode = (((XBTTypeToken) token).getBlockType().getAsBasicType() == XBBasicBlockType.UNKNOWN_BLOCK);

                eventListener.putXBTToken(token);
                break;
            }
            case ATTRIBUTE: {
                flushEmptyNodes();
                if (emptyDataMode) {
                    eventListener.putXBTToken(XBTBeginToken.create(blockMode));
                    emptyDataMode = false;
                }

                if (!unknownMode) {
                    if (((XBTAttributeToken) token).getAttribute().isNaturalZero()) {
                        zeroAttributes++;
                        break;
                    } else {
                        flushAttributes();
                    }
                }

                eventListener.putXBTToken(token);
                break;
            }
            case DATA: {
                if (((XBTDataToken) token).isEmpty()) {
                    emptyNodes.add(blockMode);
                    break;
                } else {
                    emptyDataMode = false;
                    flushEmptyNodes();
                }

                eventListener.putXBTToken(XBTBeginToken.create(blockMode));
                eventListener.putXBTToken(token);
                break;
            }
            case END: {
                if (!emptyDataMode) {
                    eventListener.putXBTToken(token);
                    emptyNodes.clear();
                } else {
                    emptyDataMode = false;
                }

                break;
            }
        }
    }

    private void flushAttributes() throws IOException, XBProcessingException {
        for (int i = 0; i < zeroAttributes; i++) {
            eventListener.putXBTToken(XBTAttributeToken.create(new UBNat32(0)));
        }
        zeroAttributes = 0;
    }

    private void flushEmptyNodes() {
        try {
            for (XBBlockTerminationMode nodeMode : emptyNodes) {
                eventListener.putXBTToken(XBTBeginToken.create(nodeMode));
                eventListener.putXBTToken(XBTDataToken.create(new ByteArrayInputStream(new byte[0])));
                eventListener.putXBTToken(XBTEndToken.create());
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTCompactingEventFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        emptyNodes.clear();
    }
}
