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
package org.exbin.xbup.core.serial.token;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;

/**
 * XBUP level 0 serialization token handler using token parser mapping to
 * provider.
 *
 * @version 0.2.1 2017/05/23
 * @author ExBin Project (http://exbin.org)
 */
public class XBPullProviderSerialHandler implements XBPullProvider, XBTokenInputSerialHandler {

    @Nullable
    private XBPullProvider provider;

    public XBPullProviderSerialHandler() {
    }

    @Override
    public void attachXBPullProvider(@Nonnull XBPullProvider provider) {
        this.provider = provider;
    }

    @Override
    @Nonnull
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        if (provider == null) {
            throw new XBProcessingException("Requested tokens before initialization", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        return provider.pullXBToken();
    }
}
