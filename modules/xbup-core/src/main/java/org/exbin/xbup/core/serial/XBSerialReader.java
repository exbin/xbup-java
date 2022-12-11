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
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.parser.token.pull.convert.XBPullProviderToProvider;
import org.exbin.xbup.core.serial.basic.XBBasicSerializable;
import org.exbin.xbup.core.serial.basic.XBProviderSerialHandler;
import org.exbin.xbup.core.serial.child.XBChildProviderSerialHandler;
import org.exbin.xbup.core.serial.child.XBChildSerializable;
import org.exbin.xbup.core.serial.token.XBPullProviderSerialHandler;
import org.exbin.xbup.core.serial.token.XBTokenSerializable;

/**
 * Interface for XBUP serialization input handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBSerialReader implements XBReadSerialHandler {

    private final XBPullProvider pullProvider;

    public XBSerialReader(XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void read(XBSerializable serial) {
        if (serial instanceof XBBasicSerializable) {
            XBProviderSerialHandler providerHandler = new XBProviderSerialHandler();
            providerHandler.attachXBProvider(new XBPullProviderToProvider(pullProvider));
            try {
                ((XBBasicSerializable) serial).serializeFromXB(providerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTokenSerializable) {
            XBPullProviderSerialHandler providerHandler = new XBPullProviderSerialHandler();
            providerHandler.attachXBPullProvider(pullProvider);
            try {
                ((XBTokenSerializable) serial).serializeFromXB(providerHandler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBChildSerializable) {
            XBChildProviderSerialHandler childOutput = new XBChildProviderSerialHandler(this);
            childOutput.attachXBPullProvider(pullProvider);
            try {
                ((XBChildSerializable) serial).serializeFromXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBSerialWriter.class.getName()).log(Level.SEVERE, null, ex);
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
        return serial instanceof XBBasicSerializable
                || serial instanceof XBTokenSerializable
                || serial instanceof XBChildSerializable;
    }
}
