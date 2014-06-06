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
package org.xbup.lib.xb.stream.filter;

import java.io.IOException;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.token.XBTToken;
import org.xbup.lib.xb.parser.token.event.XBTEventListener;
import org.xbup.lib.xb.parser.token.event.XBTEventProducer;
import org.xbup.lib.xb.stream.XBTOutputStream;

/**
 * XBUP level 1 output stream abstract class.
 *
 * @version 0.1 wr19.0 2010/06/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBTEventOutputStream extends XBTOutputStream implements XBTEventProducer {

    private XBTEventListener eventListener;

    public XBTEventOutputStream(XBTEventListener eventListener) {
        attachXBTEventListener(eventListener);
    }

    public XBTEventOutputStream() {};

    @Override
    public void putXBTToken(XBTToken item) throws IOException, XBProcessingException {
        eventListener.putXBTToken(item);
    }

    @Override
    public void close() throws IOException {
        eventListener = null;
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void flush() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
