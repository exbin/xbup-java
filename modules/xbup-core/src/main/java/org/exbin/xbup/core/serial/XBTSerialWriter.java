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
package org.exbin.xbup.core.serial;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.convert.XBTEventListenerToListener;
import org.exbin.xbup.core.parser.token.event.convert.XBToXBTEventWrapper;
import org.exbin.xbup.core.serial.basic.XBTBasicSerializable;
import org.exbin.xbup.core.serial.basic.XBTListenerSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildListenerSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.serial.token.XBTEventListenerSerialHandler;
import org.exbin.xbup.core.serial.token.XBTTokenSerializable;

/**
 * XBUP level 1 serialization object to stream writer.
 *
 * @version 0.1.25 2015/02/04
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTSerialWriter implements XBTWriteSerialHandler {

    protected final XBTEventListener eventListener;

    public XBTSerialWriter(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void write(XBSerializable serial) {
        if (serial instanceof XBTBasicSerializable) {
            XBTListenerSerialHandler listenerHandler = new XBTListenerSerialHandler();
            listenerHandler.attachXBTListener(new XBTEventListenerToListener(eventListener));
            try {
                ((XBTBasicSerializable) serial).serializeToXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTTokenSerializable) {
            XBTEventListenerSerialHandler listenerHandler = new XBTEventListenerSerialHandler();
            listenerHandler.attachXBTEventListener(eventListener);
            try {
                ((XBTTokenSerializable) serial).serializeToXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTChildSerializable) {
            XBTChildListenerSerialHandler childOutput = new XBTChildListenerSerialHandler(this);
            childOutput.attachXBTEventListener(eventListener);
            try {
                ((XBTChildSerializable) serial).serializeToXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (XBSerialWriter.isValidSerializableObject(serial)) {
            XBToXBTEventWrapper eventWrapper = new XBToXBTEventWrapper(eventListener);
            XBSerialWriter serialWriter = new XBSerialWriter(eventWrapper);
            serialWriter.write(serial);
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
                || XBSerialWriter.isValidSerializableObject(serial);
    }
}
