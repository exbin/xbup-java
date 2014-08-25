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

import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * TODO: Filter primary document content - process/skip all basic blocks and extensions.
 *
 * @version 0.1.23 2013/11/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBTRootDocumentFilter implements XBTListener {

    /** Creates a new instance of XBRootDocumentFilter */
    public XBTRootDocumentFilter() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void typeXBT(XBBlockType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attribXBT(UBNatural value) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void endXBT() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void dataXBT(InputStream data) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
