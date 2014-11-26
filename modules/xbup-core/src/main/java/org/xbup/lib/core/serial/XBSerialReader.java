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
package org.xbup.lib.core.serial;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBPullProviderToProvider;
import org.xbup.lib.core.serial.basic.XBBasicSerializable;
import org.xbup.lib.core.serial.basic.XBProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBChildSerializable;
import org.xbup.lib.core.serial.token.XBPullProviderSerialHandler;
import org.xbup.lib.core.serial.token.XBTokenSerializable;

/**
 * Interface for XBUP serialization input handler.
 *
 * @version 0.1.24 2014/11/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBSerialReader implements XBReadSerialHandler {

    private final XBPullProvider pullProvider;

    public XBSerialReader(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void read(XBSerializable serial) {
        if (serial instanceof XBBasicSerializable) {
            XBProviderSerialHandler listenerHandler = new XBProviderSerialHandler();
            listenerHandler.attachXBProvider(new XBPullProviderToProvider(pullProvider));
            try {
                ((XBBasicSerializable) serial).serializeFromXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTokenSerializable) {
            XBPullProviderSerialHandler listenerHandler = new XBPullProviderSerialHandler();
            listenerHandler.attachXBPullProvider(pullProvider);
            try {
                ((XBTokenSerializable) serial).serializeFromXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBChildSerializable) {
            XBChildProviderSerialHandler childOutput = new XBChildProviderSerialHandler(this);
            childOutput.attachXBPullProvider(pullProvider);
            try {
                ((XBChildSerializable) serial).serializeFromXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
