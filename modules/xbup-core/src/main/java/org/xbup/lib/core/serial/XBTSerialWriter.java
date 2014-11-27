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
import org.xbup.lib.core.parser.basic.convert.XBTListenerToConsumer;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBToXBTEventWrapper;
import org.xbup.lib.core.serial.basic.XBTBasicSerializable;
import org.xbup.lib.core.serial.basic.XBTConsumerSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBTSequenceListenerSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.serial.token.XBTEventListenerSerialHandler;
import org.xbup.lib.core.serial.token.XBTTokenSerializable;

/**
 * Interface for XBUP serialization input handler.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSerialWriter implements XBWriteSerialHandler {

    protected final XBTEventListener eventListener;

    public XBTSerialWriter(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void write(XBSerializable serial) {
        if (serial instanceof XBTBasicSerializable) {
            XBTConsumerSerialHandler listenerHandler = new XBTConsumerSerialHandler();
            listenerHandler.attachXBTConsumer(new XBTListenerToConsumer(new XBTEventListenerToListener(eventListener)));
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
        } else if (serial instanceof XBTSequenceSerializable) {
            XBTSequenceListenerSerialHandler listenerHandler = new XBTSequenceListenerSerialHandler(this);
            listenerHandler.attachXBTEventListener(eventListener);
            try {
                ((XBTSequenceSerializable) serial).serializeXB(listenerHandler);
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
     * Check if writer supports serializable object.
     *
     * @param serial object to test
     * @return true if serialization supported
     */
    public static boolean isValidSerializableObject(XBSerializable serial) {
        return serial instanceof XBTBasicSerializable
                || serial instanceof XBTTokenSerializable
                || serial instanceof XBTChildSerializable
                || serial instanceof XBTSequenceSerializable
                || XBSerialWriter.isValidSerializableObject(serial);
    }
}
