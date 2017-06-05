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
package org.exbin.xbup.core.parser.token.pull.convert;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBProvider;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.convert.XBListenerToToken;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;

/**
 * Pull provider to provider convertor for XBUP protocol level 0.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBPullProviderToProvider implements XBProvider {

    @Nonnull
    private final XBPullProvider pullProvider;

    public XBPullProviderToProvider(@Nonnull XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void produceXB(@Nonnull XBListener listener) throws XBProcessingException, IOException {
        XBToken token = pullProvider.pullXBToken();
        XBListenerToToken.tokenToListener(token, listener);
    }
}
