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
package org.xbup.lib.core.parser.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Sample data and methods for testing purposes.
 *
 * @version 0.1.25 2015/07/08
 * @author XBUP Project (http://xbup.org)
 */
public class XBCoreTestSampleData {

    /**
     * Writes tokens matching file "l0_singleemptyblock.xb".
     *
     * @param listener token listener
     */
    public static void writeSampleSingleEmptyBlock(XBListener listener) {
        try {
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(0));
            listener.endXB();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCoreTestSampleData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes tokens matching file "l0_extended.xb".
     *
     * @param listener token listener
     */
    public static void writeSampleExtended(XBListener listener) {
        try {
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(0));
            byte[] extData = {57, 56, 55, 54, 53, 52, 51, 50, 49, 48};
            ByteArrayInputStream extDataStream = new ByteArrayInputStream(extData);
            listener.dataXB(extDataStream);
            listener.endXB();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCoreTestSampleData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes tokens matching file "l0_dataextended.xb".
     *
     * @param listener token listener
     */
    public static void writeSampleDataExtended(XBListener listener) {
        try {
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            byte[] data = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
            ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
            listener.dataXB(dataStream);
            byte[] extData = {57, 56, 55, 54, 53, 52, 51, 50, 49, 48};
            ByteArrayInputStream extDataStream = new ByteArrayInputStream(extData);
            listener.dataXB(extDataStream);
            listener.endXB();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCoreTestSampleData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes tokens matching file "l0_singleblock.xb".
     *
     * @param listener token listener
     */
    public static void writeSampleSingleBlock(XBListener listener) {
        try {
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(1));
            listener.attribXB(new UBNat32(2));
            listener.endXB();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCoreTestSampleData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes tokens matching file "l0_singledata.xb".
     *
     * @param listener token listener
     */
    public static void writeSampleSingleData(XBListener listener) {
        try {
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            byte[] data = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
            ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
            listener.dataXB(dataStream);
            listener.endXB();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCoreTestSampleData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes tokens matching file "l0_terminated.xb".
     *
     * @param listener token listener
     */
    public static void writeSampleTerminated(XBListener listener) {
        try {
            listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
            listener.attribXB(new UBNat32(0));
            listener.attribXB(new UBNat32(0));
            listener.attribXB(new UBNat32(1));
            listener.attribXB(new UBNat32(2));
            listener.endXB();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCoreTestSampleData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes tokens matching file "l0_sixblocks.xb".
     *
     * @param listener token listener
     */
    public static void writeSampleSixBlocks(XBListener listener) {
        try {
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(0));
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(0));
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(0));
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            byte[] data = {};
            ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
            listener.dataXB(dataStream);
            listener.endXB();
            listener.endXB();
            listener.endXB();
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(0));
            listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
            listener.attribXB(new UBNat32(0));
            listener.attribXB(new UBNat32(0));
            listener.attribXB(new UBNat32(1));
            listener.attribXB(new UBNat32(2));
            listener.attribXB(new UBNat32(3));
            listener.endXB();
            listener.endXB();
            listener.endXB();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCoreTestSampleData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
