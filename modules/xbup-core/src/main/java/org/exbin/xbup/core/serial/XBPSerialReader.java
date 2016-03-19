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
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @version 0.1.25 2015/02/02
 * @author ExBin Project (http://exbin.org)
 */
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
