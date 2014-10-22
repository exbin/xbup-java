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
package org.xbup.lib.core.stream.convert;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.event.XBEventFilter;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.stream.XBTokenInputStream;

/**
 * Standard filter stream for XBUP level 0.
 *
 * @version 0.1.16 2008/08/03
 * @author XBUP Project (http://xbup.org)
 */
@Deprecated
public class XBFilterInputStream extends XBTokenInputStream {

    private XBTokenInputStream source;
    private XBEventFilter filter;
    private LinkedList<XBToken> buffer;

    public XBFilterInputStream(XBTokenInputStream target, XBEventFilter filter) {
        this(target);
        setFilter(filter);
    }

    public XBFilterInputStream(XBTokenInputStream target) {
        this.source = target;
        buffer = new LinkedList<XBToken>();
    }

    @Override
    public XBToken pullXBToken() throws XBProcessingException {
        try {
            while (buffer.size() == 0) {
                filter.putXBToken(source.pullXBToken());
            }
            return buffer.removeFirst();
        } catch (IOException ex) {
            Logger.getLogger(XBFilterInputStream.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        source.close();
        buffer = new LinkedList<XBToken>();
    }

    public XBEventFilter getFilter() {
        return filter;
    }

    public void setFilter(XBEventFilter filter) {
        this.filter = filter;
        if (filter != null) {
            filter.attachXBEventListener(new BufferFill(this));
        }
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean finished() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void skip(long tokenCount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class BufferFill implements XBEventListener {

        private XBFilterInputStream filterIS;

        public BufferFill(XBFilterInputStream filterIS) {
            this.filterIS = filterIS;
        }

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            filterIS.buffer.add(token);
        }
    }
}
