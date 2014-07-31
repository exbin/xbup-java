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
package org.xbup.lib.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Todo: invalid!
 * Level 0 Skip XBUP Tree Events.
 * It doesn't validate correct order.
 *
 * @version 0.1 wr23.0 2013/11/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBEventSkipNode implements XBListener {

    private boolean finished;
    private long level;

    /** Creates a new instance of XBEventSkipNode */
    public XBEventSkipNode() {
        finished = true;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBParseException, IOException {
        if (finished) {
            finished = false;
            level = 0;
        } else {
            level++;
        }
    }

    @Override
    public void attribXB(UBNatural value) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Unexpected attribute");
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Unexpected data");
        }
    }

    @Override
    public void endXB() throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Unexpected data");
        }

        if (level==0) {
            finished = true;
        } else {
            level--;
        }
    }

    public boolean isClosed() {
        return finished;
    }
}
