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
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullProviderToProvider;
import org.xbup.lib.core.serial.basic.XBTBasicSerializable;
import org.xbup.lib.core.serial.basic.XBTProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBTSequenceProviderSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.serial.token.XBTPullProviderSerialHandler;
import org.xbup.lib.core.serial.token.XBTTokenSerializable;

/**
 * Interface for XBUP serialization input handler.
 *
 * @version 0.1.24 2014/11/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSerialReader implements XBReadSerialHandler {

    private final XBTPullProvider pullProvider;

    public XBTSerialReader(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void read(XBSerializable serial) {
        if (serial instanceof XBTBasicSerializable) {
            XBTProviderSerialHandler listenerHandler = new XBTProviderSerialHandler();
            listenerHandler.attachXBTProvider(new XBTPullProviderToProvider(pullProvider));
            try {
                ((XBTBasicSerializable) serial).serializeFromXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTTokenSerializable) {
            XBTPullProviderSerialHandler listenerHandler = new XBTPullProviderSerialHandler();
            listenerHandler.attachXBTPullProvider(pullProvider);
            try {
                ((XBTTokenSerializable) serial).serializeFromXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTChildSerializable) {
            XBTChildProviderSerialHandler childOutput = new XBTChildProviderSerialHandler(this);
            childOutput.attachXBTPullProvider(pullProvider);
            try {
                ((XBTChildSerializable) serial).serializeFromXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTSequenceSerializable) {
            XBTSequenceProviderSerialHandler listenerHandler = new XBTSequenceProviderSerialHandler(this);
            listenerHandler.attachXBTPullProvider(pullProvider);
            try {
                ((XBTSequenceSerializable) serial).serializeXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
