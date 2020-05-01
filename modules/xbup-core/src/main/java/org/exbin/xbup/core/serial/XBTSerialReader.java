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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullProviderToProvider;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullUnknownConvertor;
import org.exbin.xbup.core.serial.basic.XBTBasicSerializable;
import org.exbin.xbup.core.serial.basic.XBTProviderSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildProviderSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.serial.token.XBTPullProviderSerialHandler;
import org.exbin.xbup.core.serial.token.XBTTokenSerializable;

/**
 * XBUP level 1 serialization object from stream reader.
 *
 * @version 0.1.24 2015/01/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
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
        } else if (XBSerialReader.isValidSerializableObject(serial)) {
            XBToXBTPullUnknownConvertor pullWrapper = new XBToXBTPullUnknownConvertor(pullProvider);
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
                || XBSerialReader.isValidSerializableObject(serial);
    }
}
