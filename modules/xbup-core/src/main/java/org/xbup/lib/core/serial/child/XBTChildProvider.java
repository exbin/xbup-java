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
 * XBUP level 1 child serialization provider interface.
 *
 * @version 0.1 wr24.0 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTChildProvider {

    /**
     * Get beggining of block.
     *
     * @return terminated block flag
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public XBBlockTerminationMode begin() throws XBProcessingException, IOException;

    /**
     * Get block type.
     *
     * @return block type
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public XBBlockType getType() throws XBProcessingException, IOException;

    /**
     * Get block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public UBNatural nextAttribute() throws XBProcessingException, IOException;

    /**
     * Get block child.
     *
     * @param child serializable block
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void nextChild(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Get block data.
     *
     * @return block data stream
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public InputStream nextData() throws XBProcessingException, IOException;

    /**
     * Get end of block.
     *
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void end() throws XBProcessingException, IOException;
}
