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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBFilter;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Default XBUP level 0 filter.
 *
 * This filter doesn't change data which are passing thru.
 *
 * @version 0.1.25 2015/02/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBDefaultFilter implements XBFilter, XBListener {

    private XBListener listener = null;

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        listener.beginXB(terminationMode);
    }

    @Override
    public void attribXB(UBNatural value) throws XBProcessingException, IOException {
        listener.attribXB(value);
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        listener.dataXB(data);
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        listener.endXB();
    }

    @Override
    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }
}
