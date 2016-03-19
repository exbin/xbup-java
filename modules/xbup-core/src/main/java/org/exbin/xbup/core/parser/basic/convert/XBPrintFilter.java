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
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBFilter;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Default XBUP level 0 printing filter.
 *
 * @version 0.1.25 2015/07/09
 * @author ExBin Project (http://exbin.org)
 */
public class XBPrintFilter implements XBFilter, XBSListener {

    private XBListener listener = null;

    public XBPrintFilter() {
    }

    public XBPrintFilter(XBListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        System.out.println("> Begin (" + terminationMode.toString() + "):");
        if (listener != null) {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        System.out.println("> Begin (" + terminationMode.toString() + "):");
        if (listener instanceof XBSListener) {
            ((XBSListener) listener).beginXB(terminationMode, blockSize);
        } else if (listener != null) {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
        System.out.println("  Attribute: " + value.getNaturalLong());
        if (listener != null) {
            listener.attribXB(value);
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        System.out.println("  Data:" + data.available());
        if (listener != null) {
            listener.dataXB(data);
        }
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        System.out.println("< End.");
        if (listener != null) {
            listener.endXB();
        }
    }

    @Override
    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }
}
