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
package org.exbin.xbup.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 child serialization listener interface.
 *
 * @version 0.1.24 2014/12/02
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTChildListener {

    /**
     * Puts beggining of block.
     *
     * @param terminationMode flag
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Puts block type.
     *
     * @param type block type
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putType(XBBlockType type) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attribute attribute value
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value, only non-negative values are
     * accepted
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value, only non-negative values are
     * accepted
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value, only non-negative values are
     * accepted
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block's child.
     *
     * @param child serializable block
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putChild(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Puts serializable object appending all tokens.
     *
     * @param serial serializable object
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Puts block data.
     *
     * @param data data stream
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putData(InputStream data) throws XBProcessingException, IOException;

    /**
     * Puts end of block.
     *
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putEnd() throws XBProcessingException, IOException;
}
