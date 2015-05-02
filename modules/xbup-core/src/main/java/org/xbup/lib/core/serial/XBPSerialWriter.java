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
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPSerializable;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;

/**
 * XBUP level 2 serialization object to stream writer.
 *
 * @version 0.1.24 2015/01/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSerialWriter implements XBPWriteSerialHandler {

    protected final XBTEventListener eventListener;

    public XBPSerialWriter(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public XBPSerialWriter(OutputStream outputStream) {
        XBTEventListener listener = null;
        try {
            listener = new XBTToXBEventConvertor(new XBEventWriter(outputStream));
        } catch (IOException ex) {
            Logger.getLogger(XBPSerialReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        eventListener = listener;
    }

    @Override
    public void write(XBSerializable serial) {
        if (serial instanceof XBPSerializable || serial instanceof XBPSequenceSerializable) {
            XBPListenerSerialHandler childOutput = new XBPListenerSerialHandler();
            childOutput.attachXBTEventListener(eventListener);
            try {
                childOutput.process(serial);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBPSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        return (serial instanceof XBPSerializable) || (serial instanceof XBPSequenceSerializable);
    }
}
