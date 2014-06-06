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
package org.xbup.lib.xb.serial.basic;

import java.io.IOException;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.parser.basic.XBProvider;
import org.xbup.lib.xb.serial.XBInputSerialHandler;

/**
 * XBUP level 0 XBProvider handler.
 *
 * @version 0.1 wr23.0 2014/03/08
 * @author XBUP Project (http://xbup.org)
 */
public class XBProviderSerialHandler implements XBProvider, XBInputSerialHandler {

    private XBProvider provider;

    public XBProviderSerialHandler() {
    }

    @Override
    public void attachXBProvider(XBProvider provider) {
        this.provider = provider;
    }

    @Override
    public void produceXB(XBListener listener) throws XBProcessingException, IOException {
        provider.produceXB(listener);
    }
}
