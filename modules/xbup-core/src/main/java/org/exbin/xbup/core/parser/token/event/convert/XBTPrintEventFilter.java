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

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBTEventFilter;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 1 event filter printing out informations about tokens.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTPrintEventFilter implements XBTEventFilter {

    @Nonnull
    private XBTEventListener eventListener;
    @Nonnull
    private String prefix = "";

    public XBTPrintEventFilter(@Nonnull String prefix, @Nonnull XBTEventListener eventListener) {
        this(eventListener);
        this.prefix = prefix;
    }

    public XBTPrintEventFilter(@Nonnull XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void putXBTToken(@Nonnull XBTToken token) throws XBProcessingException, IOException {
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
            default:
                throw new IllegalStateException("Unexpected token type " + token.getTokenType().toString());
        }

        if (eventListener != null) {
            eventListener.putXBTToken(token);
        }
    }

    @Override
    public void attachXBTEventListener(@Nonnull XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }
}
