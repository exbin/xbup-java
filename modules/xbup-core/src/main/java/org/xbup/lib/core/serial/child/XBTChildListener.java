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
package org.xbup.lib.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 child serialization listener interface.
 *
 * @version 0.1.24 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTChildListener {

    /**
     * Put beggining of block.
     *
     * @param terminationMode flag
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void begin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Put block type.
     *
     * @param type block type
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void setType(XBBlockType type) throws XBProcessingException, IOException;

    /**
     * Put block attribute.
     *
     * @param attribute attribute value
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void addAttribute(UBNatural attribute) throws XBProcessingException, IOException;

    /**
     * Put block's child.
     *
     * @param child serializable block
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void addChild(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Put block data.
     *
     * @param data data stream
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void addData(InputStream data) throws XBProcessingException, IOException;

    /**
     * Put end of block.
     *
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void end() throws XBProcessingException, IOException;
}
