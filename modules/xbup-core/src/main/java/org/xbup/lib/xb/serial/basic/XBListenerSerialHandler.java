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
package org.xbup.lib.xb.serial.basic;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.serial.XBOutputSerialHandler;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * XBUP level 0 XBListener handler.
 *
 * @version 0.1 wr23.0 2014/03/08
 * @author XBUP Project (http://xbup.org)
 */
public class XBListenerSerialHandler implements XBListener, XBOutputSerialHandler {

    private XBListener listener;

    public XBListenerSerialHandler() {
    }

    @Override
    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        listener.beginXB(terminationMode);
    }

    @Override
    public void attribXB(UBNatural attribute) throws XBProcessingException, IOException {
        listener.attribXB(attribute);
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        listener.dataXB(data);
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        listener.endXB();
    }
}
