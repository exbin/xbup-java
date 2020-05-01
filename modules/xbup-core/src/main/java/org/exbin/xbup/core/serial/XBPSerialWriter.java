/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBPSerializable;

/**
 * XBUP level 2 serialization object to stream writer.
 *
 * @version 0.1.24 2015/01/23
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
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
