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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.event.XBEventFilter;
import org.xbup.lib.core.stream.XBTokenOutputStream;

/**
 * Standard filter stream for XBUP level 0.
 *
 * @version 0.1 wr16.0 2008/08/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBFilterOutputStream extends XBTokenOutputStream {

    private XBTokenOutputStream target;
    private XBEventFilter filter;

    public XBFilterOutputStream(XBTokenOutputStream target, XBEventFilter filter) {
        this.target = target;
        setFilter(filter);
    }

    public XBFilterOutputStream(XBTokenOutputStream target) {
        this.target = target;
    }

    @Override
    public void putXBToken(XBToken item) throws IOException, XBProcessingException {
        filter.putXBToken(item);
    }

    @Override
    public void close() throws IOException {
        target.close();
    }

    public XBEventFilter getFilter() {
        return filter;
    }

    public void setFilter(XBEventFilter filter) {
        this.filter = filter;
        if (filter != null) {
            filter.attachXBEventListener(target);
        }
    }

    @Override
    public void flush() throws IOException {
        target.flush();
    }
}
