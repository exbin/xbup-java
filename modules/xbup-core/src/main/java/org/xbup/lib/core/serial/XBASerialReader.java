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
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullPreLoader;
import org.xbup.lib.core.serial.child.XBAChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildSerializable;

/**
 * Interface for XBUP serialization input handler.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBASerialReader extends XBTSerialReader implements XBReadSerialHandler {

    public XBASerialReader(XBTPullProvider pullProvider) {
        super(pullProvider instanceof XBTPullPreLoader ? pullProvider : new XBTPullPreLoader(pullProvider));
    }

    @Override
    public void read(XBSerializable serial) {
        if (serial instanceof XBAChildSerializable) {
            XBAChildProviderSerialHandler childOutput = new XBAChildProviderSerialHandler(this);
            childOutput.attachXBTPullProvider(pullProvider);
            try {
                ((XBAChildSerializable) serial).serializeFromXB(childOutput);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // TODO

        super.read(serial);
    }

    /**
     * Check if writer supports serializable object.
     *
     * @param serial object to test
     * @return true if serialization supported
     */
    public static boolean isValidSerializableObject(XBSerializable serial) {
        return serial instanceof XBAChildSerializable || XBSerialReader.isValidSerializableObject(serial);
    }
}
