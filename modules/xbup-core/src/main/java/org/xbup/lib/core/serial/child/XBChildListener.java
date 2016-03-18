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
package org.xbup.lib.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 0 child serialization listener interface.
 *
 * @version 0.1.24 2014/08/23
 * @author ExBin Project (http://exbin.org)
 */
public interface XBChildListener {

    /**
     * Puts beggining of block.
     *
     * @param terminationMode flag
     * @throws XBProcessingException
     * @throws IOException
     */
    public void begin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Puts block's attribute.
     *
     * @param attribute attribute value
     * @throws XBProcessingException
     * @throws IOException
     */
    public void addAttribute(UBNatural attribute) throws XBProcessingException, IOException;

    /**
     * Puts block's child.
     *
     * @param child serializable block
     * @throws XBProcessingException
     * @throws IOException
     */
    public void addChild(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Puts block's data.
     *
     * @param data data stream
     * @throws XBProcessingException
     * @throws IOException
     */
    public void addData(InputStream data) throws XBProcessingException, IOException;

    /**
     * Puts end of block.
     *
     * @throws XBProcessingException
     * @throws IOException
     */
    public void end() throws XBProcessingException, IOException;
}
