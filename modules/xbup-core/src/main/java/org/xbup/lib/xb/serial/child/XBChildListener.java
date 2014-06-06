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
package org.xbup.lib.xb.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * XBUP level 0 serialization structure access interface.
 *
 * @version 0.1 wr23.0 2014/02/06
 * @author XBUP Project (http://xbup.org)
 */
public interface XBChildListener {

    /**
     * Put beggining of block.
     * 
     * @param terminationMode flag
     * @throws org.xbup.lib.xb.parser.XBProcessingException
     * @throws java.io.IOException
     */
    public void begin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Put block attribute.
     * 
     * @param attribute attribute value
     * @throws org.xbup.lib.xb.parser.XBProcessingException
     * @throws java.io.IOException
     */
    public void addAttribute(UBNatural attribute) throws XBProcessingException, IOException;

    /**
     * Put block's child.
     * 
     * @param node serializable block
     * @param methodIndex method index
     * @throws org.xbup.lib.xb.parser.XBProcessingException
     * @throws java.io.IOException
     */
    public void addChild(XBSerializable node, int methodIndex) throws XBProcessingException, IOException;

    /**
     * Put block data.
     * 
     * @param data data stream
     * @throws org.xbup.lib.xb.parser.XBProcessingException
     * @throws java.io.IOException
     */
    public void addData(InputStream data) throws XBProcessingException, IOException;

    /**
     * Put end of block.
     * 
     * @throws org.xbup.lib.xb.parser.XBProcessingException
     * @throws java.io.IOException
     */
    public void end() throws XBProcessingException, IOException;
}
