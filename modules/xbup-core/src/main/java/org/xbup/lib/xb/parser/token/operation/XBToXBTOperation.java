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
package org.xbup.lib.xb.parser.token.operation;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.parser.token.XBAttributeToken;
import org.xbup.lib.xb.parser.token.XBBeginToken;
import org.xbup.lib.xb.parser.token.XBDataToken;
import org.xbup.lib.xb.parser.token.XBTAttributeToken;
import org.xbup.lib.xb.parser.token.XBTBeginToken;
import org.xbup.lib.xb.parser.token.XBTDataToken;
import org.xbup.lib.xb.parser.token.XBTEndToken;
import org.xbup.lib.xb.parser.token.XBTToken;
import org.xbup.lib.xb.parser.token.XBTTypeToken;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * XBUP level 0 to level 1 token convertor.
 * 
 * TODO: This is stub only
 *
 * @version 0.1 wr21.0 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBToXBTOperation extends XBOperation<XBToken, XBTToken> {

    private boolean expectType;
    private UBNatural group;

    public XBToXBTOperation() {
        expectType = false;
        group = null;
    }

    @Override
    public void operate(XBTokenOperator<XBToken, XBTToken> operator) {
        operator.setInputCache(2);
        operator.setOutputCache(2);
        try {
            XBToken token = operator.getToken();
            switch (token.getTokenType()) {
                case BEGIN: {
                    if (expectType && group != null) {
                        operator.putToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), 0)));
                        group = null;
                    }

                    operator.putToken(new XBTBeginToken(((XBBeginToken) token).getTerminationMode()));
                    expectType = true;
                    group = null;
                    break;
                }

                case ATTRIBUTE: {
                    if (expectType) {
                        if (group == null) {
                            if (operator.availableInput() == 1) {
                                operator.request(2);
                                break;
                            }

                            group = ((XBAttributeToken) token).getAttribute().clone();
                            token = operator.getToken();
                        }

                        operator.putToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), ((XBAttributeToken) token).getAttribute().getLong())));
                        group = null;
                        expectType = false;
                    } else {
                        operator.putToken(new XBTAttributeToken(((XBAttributeToken) token).getAttribute()));
                    }
                    break;
                }
                case DATA: {
                    if (expectType && group != null) {
                        operator.putToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), 0)));
                        group = null;
                    } else {
                        expectType = false;
                    }

                    operator.putToken(new XBTDataToken(((XBDataToken) token).getData()));
                    break;
                }
                case END: {
                    if (expectType && group != null) {
                        operator.putToken(new XBTTypeToken(new XBFixedBlockType(group.getLong(), 0)));
                        group = null;
                    } else {
                        expectType = false;
                    }

                    operator.putToken(new XBTEndToken());
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(XBToXBTOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
