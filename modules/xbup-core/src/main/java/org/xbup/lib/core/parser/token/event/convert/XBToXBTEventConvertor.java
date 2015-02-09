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
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 0 to level 1 event convertor.
 *
 * @version 0.1.21 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBToXBTEventConvertor implements XBEventListener, XBTEventProducer {

    private XBTEventListener target;
    private boolean expectType;
    private UBNatural group;

    public XBToXBTEventConvertor(XBTEventListener target) {
        this.target = target;
        expectType = true;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        target = eventListener;
    }

    @Override
    public void putXBToken(XBToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                if (expectType && group != null) {
                    target.putXBTToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), 0)));
                }

                target.putXBTToken(new XBTBeginToken(((XBBeginToken) token).getTerminationMode()));
                expectType = true;
                group = null;
                break;
            }

            case ATTRIBUTE: {
                if (expectType) {
                    if (group != null) {
                        target.putXBTToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), (((XBAttributeToken) token).getAttribute()).getNaturalLong())));
                        expectType = false;
                    } else {
                        group = ((XBAttributeToken) token).getAttribute().convertToNatural();
                    }
                } else {
                    target.putXBTToken(new XBTAttributeToken(((XBAttributeToken) token).getAttribute()));
                }
                break;
            }

            case DATA: {
                if (expectType && group != null) {
                    target.putXBTToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), 0)));
                }

                target.putXBTToken(new XBTDataToken(((XBDataToken) token).getData()));
                break;
            }

            case END: {
                if (expectType && group != null) {
                    target.putXBTToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), 0)));
                }

                target.putXBTToken(new XBTEndToken());
                break;
            }
        }
    }
}
