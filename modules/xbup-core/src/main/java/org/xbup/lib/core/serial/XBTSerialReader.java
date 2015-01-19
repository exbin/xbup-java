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
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullUnwrapper;
import org.xbup.lib.core.serial.basic.XBTBasicSerializable;
import org.xbup.lib.core.serial.basic.XBTProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBTSequenceProviderSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.serial.token.XBTPullProviderSerialHandler;
import org.xbup.lib.core.serial.token.XBTTokenSerializable;

/**
 * XBUP level 1 serialization object from stream reader.
 *
 * @version 0.1.24 2015/01/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSerialReader implements XBTReadSerialHandler {

    protected final XBTPullProvider pullProvider;

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
        } else if (XBSerialReader.isValidSerializableObject(serial)) {
            XBToXBTPullUnwrapper pullWrapper = new XBToXBTPullUnwrapper(pullProvider);
            XBSerialReader serialReader = new XBSerialReader(pullWrapper);
            serialReader.read(serial);
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
    }

    /**
     * Checks if writer supports serializable object.
     *
     * @param serial object to test
     * @return true if serialization supported
     */
    public static boolean isValidSerializableObject(XBSerializable serial) {
        return serial instanceof XBTBasicSerializable
                || serial instanceof XBTTokenSerializable
                || serial instanceof XBTChildSerializable
                || serial instanceof XBTSequenceSerializable
                || XBSerialReader.isValidSerializableObject(serial);
    }
}
