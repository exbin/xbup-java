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

import java.io.IOException;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.XBTEventProducer;

/**
 * XBUP level 0 to level 1 event convertor which introduces unknown type.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBToXBTEventWrapper implements XBEventListener, XBTEventProducer {

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
                eventListener.putXBTToken(new XBTBeginToken(((XBBeginToken) token).getTerminationMode()));
                break;
            }
            case ATTRIBUTE: {
                if (!typeSent) {
                    eventListener.putXBTToken(new XBTTypeToken(unknownBlockType));
                    typeSent = true;
                }

                eventListener.putXBTToken(new XBTAttributeToken(((XBAttributeToken) token).getAttribute()));
                break;
            }
            case DATA: {
                eventListener.putXBTToken(new XBTDataToken(((XBDataToken) token).getData()));
                break;
            }
            case END: {
                eventListener.putXBTToken(new XBTEndToken());
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }
}
