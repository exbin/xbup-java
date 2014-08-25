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
package org.xbup.lib.core.stream.filter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBTEventFilter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.stream.XBTInputTokenStream;

/**
 * Standard filter stream for XBUP level 1.
 *
 * @version 0.1.19 2010/06/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBTFilterInputStream extends XBTInputTokenStream {

    private XBTInputTokenStream source;
    private XBTEventFilter filter;
    private LinkedList<XBTToken> buffer;

    public XBTFilterInputStream(XBTInputTokenStream target, XBTEventFilter filter) {
        this(target);
        this.filter = filter;
    }

    public XBTFilterInputStream(XBTInputTokenStream target) {
        this.source = target;
        buffer = new LinkedList<XBTToken>();
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException {
        try {
            while (buffer.size() == 0) {
                filter.putXBTToken(source.pullXBTToken());
            }
            return buffer.removeFirst();
        } catch (IOException ex) {
            Logger.getLogger(XBTFilterInputStream.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        source.close();
        buffer = new LinkedList<XBTToken>();
    }

    public XBTEventFilter getFilter() {
        return filter;
    }

    public void setFilter(XBTEventFilter filter) {
        this.filter = filter;
        if (filter != null) {
            filter.attachXBTEventListener(new BufferFill(this));
        }
    }

    @Override
    public void reset() throws IOException {
        source.reset();
    }

    @Override
    public boolean finished() throws IOException {
        return source.finished();
    }

    @Override
    public void skip(long tokenCount) throws XBProcessingException, IOException {
        skip(tokenCount);
    }

    private class BufferFill implements XBTEventListener {

        private XBTFilterInputStream filterIS;

        public BufferFill(XBTFilterInputStream filterIS) {
            this.filterIS = filterIS;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBParseException, IOException {
            filterIS.buffer.add(token);
        }
    }
}
