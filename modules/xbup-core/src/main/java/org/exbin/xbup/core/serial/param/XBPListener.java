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
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceItem;

/**
 * XBUP level 2 child serialization listener interface.
 *
 * @version 0.1.24 2015/01/27
 * @author ExBin Project (http://exbin.org)
 */
public interface XBPListener {

    /**
     * Puts beggining of block.
     *
     * @param terminationMode flag
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Puts block type.
     *
     * @param type block type
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putType(XBBlockType type) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attribute attribute value
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putAttribute(XBAttribute attribute) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value, only non-negative values are
     * accepted
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value, only non-negative values are
     * accepted
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block attribute.
     *
     * @param attributeValue attribute value, only non-negative values are
     * accepted
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException;

    /**
     * Puts block data.
     *
     * @param data data stream
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putData(InputStream data) throws XBProcessingException, IOException;

    /**
     * Puts end of block.
     *
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putEnd() throws XBProcessingException, IOException;

    /**
     * Puts single token.
     *
     * @param token token
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putToken(XBTToken token) throws XBProcessingException, IOException;

    /**
     * Puts serializable object using consist method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Puts serializable object using join method.
     *
     * @param serial serializable object
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Puts serializable object using list consist method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Puts serializable object using list join method.
     *
     * @param serial serializable object
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Puts sequence item.
     *
     * @param item serializable object
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException;

    /**
     * Puts serializable object appending all tokens.
     *
     * @param serial serializable object
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException;
}
