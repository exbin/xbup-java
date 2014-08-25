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
package org.xbup.lib.core.parser.token.operation;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 0 To level 1 token convertor.
 *
 * @version 0.1.21 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBTToXBOperation extends XBOperation<XBTToken,XBToken> {

    public XBTToXBOperation() {
    }

    @Override
    public void operate(XBTokenOperator<XBTToken, XBToken> operator) {
        operator.setInputCache(1);
        operator.setOutputCache(2);
        try {
            XBTToken token = operator.getToken();
            switch (token.getTokenType()) {
                case BEGIN: {
                    operator.putToken(new XBBeginToken(((XBTBeginToken)token).getTerminationMode()));
                    break;
                }

                case TYPE: {
                    XBFixedBlockType blockType = (XBFixedBlockType) ((XBTTypeToken)token).getBlockType();
                    operator.putToken(new XBAttributeToken(new UBNat32(blockType.getGroupID().getInt())));
                    operator.putToken(new XBAttributeToken(new UBNat32(blockType.getBlockID().getInt())));
                    break;
                }

                case ATTRIBUTE: {
                    operator.putToken(new XBAttributeToken(((XBTAttributeToken)token).getAttribute()));
                    break;
                }

                case DATA:{
                    operator.putToken(new XBDataToken(((XBTDataToken)token).getData()));
                    break;
                }

                case END: {
                    operator.putToken(new XBEndToken());
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(XBTToXBOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
