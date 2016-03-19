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
package org.exbin.xbup.core.serial.param;

import java.io.IOException;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBEditableAttribute;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 2 bidirectional parameter serialization interface.
 *
 * @version 0.1.24 2015/05/03
 * @author ExBin Project (http://exbin.org)
 */
public interface XBPSequencer extends XBPListener, XBPProvider {

    /**
     * Returns currently used serialization mode.
     *
     * @return sequencing mode depending on whether data are serialized from or
     * to token stream
     */
    public XBSerializationMode getSerializationMode();

    /**
     * Performs begin token with default termination mode behavior.
     *
     * @throws java.io.IOException
     */
    public void begin() throws XBProcessingException, IOException;

    /**
     * Performs matching to given block type.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @param blockType block type to match to
     * @throws java.io.IOException
     */
    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException;

    /**
     * Performs matching to unknown block type.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @throws java.io.IOException
     */
    public void matchType() throws XBProcessingException, IOException;

    /**
     * Performs end token.
     *
     * @throws java.io.IOException
     */
    public void end() throws XBProcessingException, IOException;

    /**
     * Performs attribute token using attributeValue, either as source or
     * target.
     *
     * @param attributeValue attribute value
     * @throws java.io.IOException
     */
    public void attribute(XBEditableAttribute attributeValue) throws XBProcessingException, IOException;

    /**
     * Performs serialization using consist operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException
     */
    public void consist(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization using join operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException
     */
    public void join(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization using list consist operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException
     */
    public void listConsist(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization using list join operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException
     */
    public void listJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization on serializable object.
     *
     * @param serial serializable object
     * @throws XBProcessingException
     * @throws IOException
     */
    public void append(XBSerializable serial) throws XBProcessingException, IOException;
}
