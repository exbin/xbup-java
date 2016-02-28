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
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBEventProducer;
import org.xbup.lib.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 0 to level 1 event convertor which introduces unknown type.
 *
 * @version 0.1.24 2014/11/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBTToXBEventUnwrapper implements XBTEventListener, XBEventProducer {

    private XBEventListener eventListener;
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
                eventListener.putXBToken(new XBBeginToken(((XBTBeginToken) token).getTerminationMode()));
                break;
            }
            case TYPE: {
                if (!unknownBlockType.equals(((XBTTypeToken) token).getBlockType())) {
                    throw new XBProcessingException("Unexpected token type - Unknown type expected", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                }

                break;
            }
            case ATTRIBUTE: {
                eventListener.putXBToken(new XBAttributeToken(((XBTAttributeToken) token).getAttribute()));
                break;
            }
            case DATA: {
                eventListener.putXBToken(new XBDataToken(((XBTDataToken) token).getData()));
                break;
            }
            case END: {
                eventListener.putXBToken(new XBEndToken());
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
}
