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
package org.xbup.lib.xb.stream.buffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.stream.XBTokenOutputStream;

/**
 * Buffer for XBUP protocol stream.
 *
 * @version 0.1 wr19.0 2010/05/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBStreamBuffer extends XBTokenOutputStream {

    private List<XBToken> itemList;

    /** Creates a new instance of XBStreamBuffer */
    public XBStreamBuffer() {
        itemList = new ArrayList<XBToken>();
    }

    @Override
    public void close() throws IOException {
        itemList = null;
    }

    @Override
    public void putXBToken(XBToken token) throws XBParseException, IOException {
        itemList.add(token);
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
