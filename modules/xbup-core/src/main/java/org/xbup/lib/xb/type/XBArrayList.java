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
package org.xbup.lib.xb.type;

import java.io.IOException;
import java.util.ArrayList;
import org.xbup.lib.xb.block.declaration.XBBlockDecl;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.stream.XBTokenInputStream;
import org.xbup.lib.xb.stream.XBTokenOutputStream;

/**
 * Encapsulation class for ArrayList.
 *
 * @version 0.1 wr16.0 2008/08/02
 * @author XBUP Project (http://xbup.org)
 * @param <E> list item
 */
public class XBArrayList<E> extends ArrayList<E> {

    public static long[] xbCatalogPath = {1, 2, 300}; // Generic List - Testing only

    public void readFromXBStream(XBTokenInputStream stream) throws XBProcessingException, XBProcessingException {
    }

    public void writeToXBStream(XBTokenOutputStream stream) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
        /*stream.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        stream.putXBToken(new XBAttributeToken(new UBNat32(size())));
        for (Iterator it = iterator(); it.hasNext();) {
            ((XBStreamable) it.next()).writeXBStream(stream);
        }
        stream.putXBToken(new XBEndToken()); */
    }

    public XBBlockDecl getBlockType(XBCatalog catalog) {
        Long[] path = new Long[xbCatalogPath.length];
        for (int i = 0; i < xbCatalogPath.length; i++) {
            path[i] = new Long(xbCatalogPath[i]);
        }
        return catalog.findBlockTypeByPath(path);
    }

/*
    public void writeXBCL1Stream(XBTOutputStream stream) throws IOException, XBParseException {
        stream.putXBL1(new XBL1Event(XBL1EventType.BEGIN, Boolean.FALSE));
// TODO:        stream.putXBL1(new XBL1Event(XBL1EventType.TYPE, getBlockType(catalog)));
        if (size() > 0) {
            stream.putXBL1(new XBL1Event(XBL1EventType.ATTRIBUTE, new UBNat32(size())));
        } // Is L2 UBList
        for (Iterator it = iterator(); it.hasNext();) {
            ((XBTCStreamable) it.next()).writeXBCL1Stream(stream);
        }
        stream.putXBL1(new XBL1Event(XBL1EventType.END, null));
    }
*/
}
