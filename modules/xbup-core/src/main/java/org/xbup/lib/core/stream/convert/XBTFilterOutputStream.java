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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBTEventFilter;
import org.xbup.lib.core.stream.XBTOutputStream;

/**
 * Standard filter stream for XBUP level 1.
 *
 * @version 0.1.19 2010/06/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBTFilterOutputStream extends XBTOutputStream {

    private XBTOutputStream target;
    private XBTEventFilter filter;

    public XBTFilterOutputStream(XBTOutputStream target, XBTEventFilter filter) {
        this.target = target;
        this.filter = filter;
    }

    public XBTFilterOutputStream(XBTOutputStream target) {
        this.target = target;
    }

    @Override
    public void putXBTToken(XBTToken item) throws IOException, XBProcessingException {
        filter.putXBTToken(item);
    }

    @Override
    public void close() throws IOException {
        target.close();
    }

    public XBTEventFilter getFilter() {
        return filter;
    }

    public void setFilter(XBTEventFilter filter) {
        this.filter = filter;
        if (filter != null) {
            filter.attachXBTEventListener(target);
        }
    }

    @Override
    public void flush() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
