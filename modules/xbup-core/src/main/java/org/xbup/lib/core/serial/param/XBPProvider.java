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
package org.xbup.lib.core.serial.param;

import org.xbup.lib.core.serial.sequence.XBSerialSequenceItem;
import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 2 child serialization provider interface.
 *
 * @version 0.1.25 2015/02/02
 * @author XBUP Project (http://xbup.org)
 */
public interface XBPProvider {

    /**
     * Pulls beggining of block.
     *
     * @return terminated block flag
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException;

    /**
     * Pulls block type.
     *
     * @return block type
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public XBBlockType pullType() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public UBNatural pullAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public byte pullByteAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public short pullShortAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public int pullIntAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public long pullLongAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block data.
     *
     * @return block data stream
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public InputStream pullData() throws XBProcessingException, IOException;

    /**
     * Pulls content of empty data token if present.
     *
     * @return true if empty data was present
     * @throws java.io.IOException
     */
    public boolean pullIfEmptyData() throws XBProcessingException, IOException;

    /**
     * Pulls empty data block if present.
     *
     * @return true if empty data was present
     * @throws java.io.IOException
     */
    public boolean pullIfEmptyBlock() throws XBProcessingException, IOException;

    /**
     * Pulls end of block.
     *
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public void pullEnd() throws XBProcessingException, IOException;

    /**
     * Pulls single token.
     *
     * @param tokenType type of the token
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     * @return token
     */
    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using consist method.
     *
     * @param child serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public void pullConsist(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using join method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public void pullJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using list consist method.
     *
     * @param child serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public void pullListConsist(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using list join method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public void pullListJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Pulls given sequence item.
     *
     * @param item serializable object
     * @throws XBProcessingException
     * @throws IOException
     */
    public void pullItem(XBSerialSequenceItem item) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object appending all tokens.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    public void pullAppend(XBSerializable serial) throws XBProcessingException, IOException;
}
