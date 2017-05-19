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
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBEventProducer;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 1 to level 0 event convertor.
 *
 * @version 0.2.1 2017/05/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTToXBEventConvertor implements XBTEventListener, XBEventProducer {

    private XBEventListener target;

    public XBTToXBEventConvertor(XBEventListener target) {
        this.target = target;
    }

    @Override
    public void attachXBEventListener(XBEventListener eventListener) {
        target = eventListener;
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                target.putXBToken(XBBeginToken.createToken(((XBTBeginToken) token).getTerminationMode()));
                break;
            }

            case TYPE: {
                if (((XBTTypeToken) token).getBlockType() instanceof XBFBlockType) {
                    target.putXBToken(new XBAttributeToken(((XBFBlockType) ((XBTTypeToken) token).getBlockType()).getGroupID()));
                    target.putXBToken(new XBAttributeToken(((XBFBlockType) ((XBTTypeToken) token).getBlockType()).getBlockID()));
                    break;
                } else {
                    throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                }
            }

            case ATTRIBUTE: {
                target.putXBToken(new XBAttributeToken(((XBTAttributeToken) token).getAttribute()));
                break;
            }

            case DATA: {
                target.putXBToken(new XBDataToken(((XBTDataToken) token).getData()));
                break;
            }

            case END: {
                target.putXBToken(new XBEndToken());
                break;
            }
        }
    }
}
