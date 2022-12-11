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
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBPSerializable;

/**
 * XBUP level 2 serialization object from stream writer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPSerialReader implements XBPReadSerialHandler {

    protected final XBTPullProvider pullProvider;

    public XBPSerialReader(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    public XBPSerialReader(InputStream inputStream) {
        XBTPullProvider provider = null;
        try {
            provider = new XBToXBTPullConvertor(new XBPullReader(inputStream));
        } catch (IOException ex) {
            Logger.getLogger(XBPSerialReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        pullProvider = provider;
    }

    @Override
    public void read(XBSerializable serial) throws XBProcessingException, IOException {
        if (serial instanceof XBPSerializable || serial instanceof XBPSequenceSerializable) {
            XBPProviderSerialHandler childOutput = new XBPProviderSerialHandler();
            childOutput.attachXBTPullProvider(pullProvider);
            childOutput.process(serial);
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
