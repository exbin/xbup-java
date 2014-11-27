/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.core.parser.token.event.convert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBTEventFilter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Level 1 filter performing compacting operation.
 *
 * This should be usable for level 2 compacting conversions.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCompactingEventFilter implements XBTEventFilter {

    private XBTEventListener eventListener;

    private boolean unknownMode = false;
    private boolean possibleDataMode = true;
    private int zeroAttributes = 0;
    private final List<XBBlockTerminationMode> emptyNodes = new ArrayList<>();
    private XBBlockTerminationMode blockMode;

    public XBTCompactingEventFilter(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                blockMode = ((XBTBeginToken) token).getTerminationMode();
                possibleDataMode = true;
                zeroAttributes = 0;
                break;
            }
            case TYPE: {
                flushEmptyNodes();
                possibleDataMode = false;
                eventListener.putXBTToken(new XBTBeginToken(blockMode));
                unknownMode = (((XBTTypeToken) token).getBlockType().getAsBasicType() == XBBasicBlockType.UNKNOWN_BLOCK);

                eventListener.putXBTToken(token);
                break;
            }
            case ATTRIBUTE: {
                flushEmptyNodes();
                if (possibleDataMode) {
                    eventListener.putXBTToken(new XBTBeginToken(blockMode));
                }
                possibleDataMode = false;
                if (!unknownMode) {
                    if (((XBTAttributeToken) token).getAttribute().getLong() == 0) {
                        zeroAttributes++;
                        break;
                    } else {
                        for (int i = 0; i < zeroAttributes; i++) {
                            eventListener.putXBTToken(new XBTAttributeToken(new UBNat32(0)));
                        }
                        zeroAttributes = 0;
                    }
                }

                eventListener.putXBTToken(token);
                break;
            }
            case DATA: {
                if (((XBTDataToken) token).getData().available() == 0) {
                    emptyNodes.add(blockMode);
                    break;
                } else {
                    flushEmptyNodes();
                }

                eventListener.putXBTToken(token);
                break;
            }
            case END: {
                if (emptyNodes.isEmpty()) {
                    eventListener.putXBTToken(token);
                }

                break;
            }
        }
    }

    private void flushEmptyNodes() {
        try {
            for (XBBlockTerminationMode nodeMode : emptyNodes) {
                eventListener.putXBTToken(new XBTBeginToken(nodeMode));
                eventListener.putXBTToken(new XBTDataToken(new ByteArrayInputStream(new byte[0])));
                eventListener.putXBTToken(new XBTEndToken());
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTCompactingEventFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        emptyNodes.clear();
    }
}
