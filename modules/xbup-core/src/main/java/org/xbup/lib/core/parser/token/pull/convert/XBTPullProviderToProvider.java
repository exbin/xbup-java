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
package org.xbup.lib.core.parser.token.pull.convert;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.basic.XBTSListener;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTSBeginToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * Pull provider to provider convertor for XBUP protocol level 0.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullProviderToProvider implements XBTProvider {

    private final XBTPullProvider pullProvider;

    public XBTPullProviderToProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void produceXBT(XBTListener listener) throws XBProcessingException, IOException {
        XBTToken token = pullProvider.pullXBTToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                if ((token instanceof XBTSBeginToken) && (listener instanceof XBTSListener)) {
                    ((XBTSListener) listener).beginXBT(((XBTSBeginToken) token).getTerminationMode(), ((XBTSBeginToken) token).getBlockSize());
                } else {
                    listener.beginXBT(((XBTBeginToken) token).getTerminationMode());
                }

                break;
            }

            case TYPE: {
                listener.typeXBT(((XBTTypeToken) token).getBlockType());
                break;
            }

            case ATTRIBUTE: {
                listener.attribXBT(((XBTAttributeToken) token).getAttribute());
                break;
            }

            case DATA: {
                listener.dataXBT(((XBTDataToken) token).getData());
                break;
            }

            case END: {
                listener.endXBT();
                break;
            }
        }
    }
}
