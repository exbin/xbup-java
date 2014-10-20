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
package org.xbup.lib.core.serial.token;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 1 serialization token handler using token parser mapping to
 * listener.
 *
 * @version 0.1.24 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBTEventListenerSerialHandler implements XBTEventListener, XBTTokenOutputSerialHandler {

    private XBTEventListener listener;

    public XBTEventListenerSerialHandler() {
    }

    @Override
    public void attachXBTEventListener(XBTEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        listener.putXBTToken(token);
    }
}
