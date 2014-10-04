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
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;

/**
 * XBUP level 0 prefix pull provider.
 *
 * @version 0.1.23 2014/03/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBPrefixPullProvider implements XBPullProvider {

    private final XBPullProvider provider;
    private XBToken prefix;

    public XBPrefixPullProvider(XBPullProvider provider, XBToken prefix) {
        this.provider = provider;
        this.prefix = prefix;
    }

    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        if (prefix != null) {
            XBToken token = prefix;
            prefix = null;
            return token;
        }

        return provider.pullXBToken();
    }
}
