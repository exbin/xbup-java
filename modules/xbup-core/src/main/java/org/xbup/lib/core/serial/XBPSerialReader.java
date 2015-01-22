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
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.serial.param.XBPSerializable;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.param.XBASequenceProviderSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBTSequenceSerializable;

/**
 * XBUP level 2 serialization object from stream writer.
 *
 * @version 0.1.24 2015/01/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSerialReader extends XBTSerialReader implements XBPReadSerialHandler {

    public XBPSerialReader(XBTPullProvider pullProvider) {
        super(pullProvider instanceof XBTPullPreLoader ? pullProvider : new XBTPullPreLoader(pullProvider));
    }

    @Override
    public void read(XBSerializable serial) {
        if (serial instanceof XBTChildSerializable || serial instanceof XBPSerializable) {
            XBPProviderSerialHandler childOutput = new XBPProviderSerialHandler(this);
            childOutput.attachXBTPullProvider(pullProvider);
            try {
                if (serial instanceof XBPSerializable) {
                    ((XBPSerializable) serial).serializeFromXB(childOutput);
                } else {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (serial instanceof XBTSequenceSerializable || serial instanceof XBPSequenceSerializable) {
            XBASequenceProviderSerialHandler childOutput = new XBASequenceProviderSerialHandler(this);
            childOutput.attachXBTPullProvider(pullProvider);
            try {
                if (serial instanceof XBPSequenceSerializable) {
                    ((XBPSequenceSerializable) serial).serializeXB(childOutput);
                } else {
                    ((XBTSequenceSerializable) serial).serializeXB(childOutput);
                }
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTSerialReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            super.read(serial);
        }
    }

    /**
     * Checks if writer supports serializable object.
     *
     * @param serial object to test
     * @return true if serialization supported
     */
    public static boolean isValidSerializableObject(XBSerializable serial) {
        return serial instanceof XBPSerializable || XBSerialReader.isValidSerializableObject(serial);
    }
}
