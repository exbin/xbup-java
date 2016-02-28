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
package org.xbup.lib.core.parser.token.pull.convert;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBToken;
import static org.xbup.lib.core.parser.token.XBTokenType.ATTRIBUTE;
import static org.xbup.lib.core.parser.token.XBTokenType.BEGIN;
import static org.xbup.lib.core.parser.token.XBTokenType.DATA;
import static org.xbup.lib.core.parser.token.XBTokenType.END;
import org.xbup.lib.core.parser.token.pull.XBPullFilter;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;

/**
 * XBUP level 1 pull filter printing out informations about tokens.
 *
 * @version 0.1.25 2015/07/13
 * @author ExBin Project (http://exbin.org)
 */
public class XBPrintPullFilter implements XBPullFilter {

    private XBPullProvider pullProvider;
    private String prefix = "";

    public XBPrintPullFilter(String prefix, XBPullProvider pullProvider) {
        this(pullProvider);
        this.prefix = prefix;
    }

    public XBPrintPullFilter(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        XBToken token = pullProvider.pullXBToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                System.out.println(prefix + "> Begin (" + ((XBBeginToken) token).getTerminationMode().toString() + "):");
                break;
            }
            case ATTRIBUTE: {
                System.out.println(prefix + "  Attribute: " + ((XBAttributeToken) token).getAttribute().getNaturalLong());
                break;
            }
            case DATA: {
                System.out.println(prefix + "  Data:" + ((XBDataToken) token).getData().available());
                break;
            }
            case END: {
                System.out.println(prefix + "< End.");
                break;
            }
        }

        return token;
    }

    @Override
    public void attachXBPullProvider(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }
}
