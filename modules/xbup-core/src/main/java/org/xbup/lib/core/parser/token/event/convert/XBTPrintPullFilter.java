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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.pull.XBTPullFilter;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 pull filter printing out informations about tokens.
 *
 * @version 0.1.24 2015/01/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPrintPullFilter implements XBTPullFilter {

    private XBTPullProvider pullProvider;

    public XBTPrintPullFilter(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBTToken token = pullProvider.pullXBTToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                System.out.println("> Begin (" + ((XBTBeginToken) token).getTerminationMode().toString() + "):");
                break;
            }
            case TYPE: {
                XBBlockType blockType = ((XBTTypeToken) token).getBlockType();
                System.out.println("  Type: " + (blockType instanceof XBFixedBlockType ? "(" + ((XBFixedBlockType) blockType).getGroupID().getInt() + "," + ((XBFixedBlockType) blockType).getBlockID().getInt() + ")" : blockType.getClass().getCanonicalName()));
                break;
            }
            case ATTRIBUTE: {
                System.out.println("  Attribute: " + ((XBTAttributeToken) token).getAttribute().getLong());
                break;
            }
            case DATA: {
                System.out.println("  Data:" + ((XBTDataToken) token).getData().available());
                break;
            }
            case END: {
                System.out.println("< End.");
                break;
            }
        }

        return token;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }
}
