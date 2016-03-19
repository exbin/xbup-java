/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTFilter;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Default XBUP level 1 filter.
 *
 * This filter doesn't change data which are passing thru and is intended for
 * extending. Extend this, if your filter is capable of managing size
 * precomputed tokens.
 *
 * @version 0.1.25 2015/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBTSDefaultFilter implements XBTFilter, XBTSListener {

    private XBTListener listener;

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        listener.beginXBT(terminationMode);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        if (listener instanceof XBTSListener) {
            ((XBTSListener) listener).beginXBT(terminationMode, blockSize);
        } else {
            listener.beginXBT(terminationMode);
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        listener.typeXBT(type);
    }

    @Override
    public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
        listener.attribXBT(value);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        listener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        listener.endXBT();
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }
}
