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
package org.exbin.xbup.core.parser.token.event.convert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private XBTEventListener eventListener;

    private boolean unknownMode = false;
    private boolean emptyDataMode = true;
    private int zeroAttributes = 0;
    private final List<XBBlockTerminationMode> emptyNodes = new ArrayList<>();
    private XBBlockTerminationMode blockMode;

    public XBTCompactingEventFilter(XBTEventListener eventListener) {
        attachListener(eventListener);
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        attachListener(eventListener);
    }

    private void attachListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
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
