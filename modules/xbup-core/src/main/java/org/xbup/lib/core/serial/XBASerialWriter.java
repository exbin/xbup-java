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
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTCompactingEventFilter;
import org.xbup.lib.core.serial.child.XBAChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildSerializable;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBASequenceListenerSerialHandler;
import org.xbup.lib.core.serial.sequence.XBASequenceSerializable;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;

/**
 * Interface for XBUP serialization input handler.
 *
 * @version 0.1.24 2014/12/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBASerialWriter extends XBTSerialWriter implements XBWriteSerialHandler {

    public XBASerialWriter(XBTEventListener eventListener) {
        super(eventListener instanceof XBTCompactingEventFilter ? eventListener : new XBTCompactingEventFilter(eventListener));
    }

    @Override
    public void write(XBSerializable serial) {
        if (serial instanceof XBAChildSerializable) {
            XBAChildListenerSerialHandler childOutput = new XBAChildListenerSerialHandler(this);
            childOutput.attachXBTEventListener(eventListener);
            try {
                ((XBAChildSerializable) serial).serializeToXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBASerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBASequenceSerializable) {
            XBASequenceListenerSerialHandler childOutput = new XBASequenceListenerSerialHandler(this);
            childOutput.attachXBTEventListener(eventListener);
            try {
                ((XBASequenceSerializable) serial).serializeXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBASerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTChildSerializable) {
            XBAChildListenerSerialHandler childOutput = new XBAChildListenerSerialHandler(this);
            childOutput.attachXBTEventListener(eventListener);
            try {
                ((XBTChildSerializable) serial).serializeToXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBASerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTSequenceSerializable) {
            XBASequenceListenerSerialHandler childOutput = new XBASequenceListenerSerialHandler(this);
            childOutput.attachXBTEventListener(eventListener);
            try {
                ((XBTSequenceSerializable) serial).serializeXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBASerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            super.write(serial);
        }
    }

    /**
     * Checks if writer supports serializable object.
     *
     * @param serial object to test
     * @return true if serialization supported
     */
    public static boolean isValidSerializableObject(XBSerializable serial) {
        return serial instanceof XBAChildSerializable || XBSerialWriter.isValidSerializableObject(serial);
    }
}
