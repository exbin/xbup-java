/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author ExBin Project (https://exbin.org)
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
                Logger.getLogger(XBTSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTTokenSerializable) {
            XBTEventListenerSerialHandler listenerHandler = new XBTEventListenerSerialHandler();
            listenerHandler.attachXBTEventListener(eventListener);
            try {
                ((XBTTokenSerializable) serial).serializeToXB(listenerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
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
