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
import javax.annotation.Nonnull;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceItem;

/**
 * XBUP level 2 child serialization provider interface.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public interface XBPProvider {

    /**
     * Pulls beggining of block.
     *
     * @return terminated block flag
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException;

    /**
     * Pulls block type.
     *
     * @return block type
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    XBBlockType pullType() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    XBAttribute pullAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    byte pullByteAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    short pullShortAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    int pullIntAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    long pullLongAttribute() throws XBProcessingException, IOException;

    /**
     * Pulls block data.
     *
     * @return block data stream
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nonnull
    InputStream pullData() throws XBProcessingException, IOException;

    /**
     * Pulls content of empty data token if present.
     *
     * @return true if empty data was present
     * @throws java.io.IOException if input/output error
     */
    boolean pullIfEmptyData() throws XBProcessingException, IOException;

    /**
     * Pulls empty data block if present.
     *
     * @return true if empty data was present
     * @throws java.io.IOException if input/output error
     */
    boolean pullIfEmptyBlock() throws XBProcessingException, IOException;

    /**
     * Pulls end of block.
     *
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullEnd() throws XBProcessingException, IOException;

    /**
     * Pulls single token.
     *
     * @param tokenType type of the token
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     * @return token
     */
    @Nonnull
    XBTToken pullToken(@Nonnull XBTTokenType tokenType) throws XBProcessingException, IOException;

    /**
     * Pulls single token for preserving minimal form.
     *
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     * @return token
     */
    @Nonnull
    XBTToken pullToken() throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using consist method.
     *
     * @param child serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullConsist(@Nonnull XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using join method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullJoin(@Nonnull XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using list consist method.
     *
     * @param child serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullListConsist(@Nonnull XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using list join method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullListJoin(@Nonnull XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Pulls given sequence item.
     *
     * @param item serializable object
     * @throws XBProcessingException if not matching
     * @throws java.io.IOException if input/output error
     */
    void pullItem(@Nonnull XBSerialSequenceItem item) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object appending all tokens.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullAppend(@Nonnull XBSerializable serial) throws XBProcessingException, IOException;
}
