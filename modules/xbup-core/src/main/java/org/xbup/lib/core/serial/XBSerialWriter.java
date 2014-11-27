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
import org.xbup.lib.core.parser.basic.convert.XBListenerToConsumer;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBEventListenerToListener;
import org.xbup.lib.core.serial.basic.XBBasicSerializable;
import org.xbup.lib.core.serial.basic.XBConsumerSerialHandler;
import org.xbup.lib.core.serial.child.XBChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBChildSerializable;
import org.xbup.lib.core.serial.token.XBEventListenerSerialHandler;
import org.xbup.lib.core.serial.token.XBTokenSerializable;

/**
 * Interface for XBUP serialization input handler.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBSerialWriter implements XBWriteSerialHandler {

    private final XBEventListener eventListener;

    public XBSerialWriter(XBEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void write(XBSerializable serial) {
        if (serial instanceof XBBasicSerializable) {
            XBConsumerSerialHandler listenerHandler = new XBConsumerSerialHandler();
            listenerHandler.attachXBConsumer(new XBListenerToConsumer(new XBEventListenerToListener(eventListener)));
            try {
                ((XBBasicSerializable) serial).serializeToXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTokenSerializable) {
            XBEventListenerSerialHandler listenerHandler = new XBEventListenerSerialHandler();
            listenerHandler.attachXBEventListener(eventListener);
            try {
                ((XBTokenSerializable) serial).serializeToXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBChildSerializable) {
            XBChildListenerSerialHandler childOutput = new XBChildListenerSerialHandler(this);
            childOutput.attachXBEventListener(eventListener);
            try {
                ((XBChildSerializable) serial).serializeToXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new UnsupportedOperationException("Serialization method " + serial.getClass().getCanonicalName() + " not supported.");
        }
    }

    /**
     * Check if writer supports serializable object.
     *
     * @param serial object to test
     * @return true if serialization supported
     */
    public static boolean isValidSerializableObject(XBSerializable serial) {
        return serial instanceof XBBasicSerializable
                || serial instanceof XBTokenSerializable
                || serial instanceof XBChildSerializable;
    }
}
