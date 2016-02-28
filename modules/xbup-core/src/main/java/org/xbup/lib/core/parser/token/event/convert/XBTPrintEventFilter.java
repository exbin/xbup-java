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
package org.xbup.lib.core.parser.token.event.convert;

import java.io.IOException;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBTEventFilter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 1 event filter printing out informations about tokens.
 *
 * @version 0.1.25 2015/03/03
 * @author ExBin Project (http://exbin.org)
 */
public class XBTPrintEventFilter implements XBTEventFilter {

    private XBTEventListener eventListener;
    private String prefix = "";

    public XBTPrintEventFilter(String prefix, XBTEventListener eventListener) {
        this(eventListener);
        this.prefix = prefix;
    }

    public XBTPrintEventFilter(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                System.out.println(prefix + "> Begin (" + ((XBTBeginToken) token).getTerminationMode().toString() + "):");
                break;
            }
            case TYPE: {
                XBBlockType blockType = ((XBTTypeToken) token).getBlockType();
                System.out.println(prefix + "  Type: " + (blockType instanceof XBFixedBlockType ? "(" + ((XBFixedBlockType) blockType).getGroupID().getInt() + "," + ((XBFixedBlockType) blockType).getBlockID().getInt() + ")" : blockType.getClass().getCanonicalName()));
                break;
            }
            case ATTRIBUTE: {
                System.out.println(prefix + "  Attribute: " + ((XBTAttributeToken) token).getAttribute().getNaturalLong());
                break;
            }
            case DATA: {
                System.out.println(prefix + "  Data:" + ((XBTDataToken) token).getData().available());
                break;
            }
            case END: {
                System.out.println(prefix + "< End.");
                break;
            }
        }

        if (eventListener != null) {
            eventListener.putXBTToken(token);
        }
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }
}
