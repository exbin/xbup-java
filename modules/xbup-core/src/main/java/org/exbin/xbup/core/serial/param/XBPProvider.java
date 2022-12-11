/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial.param;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
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
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBPProvider {

    /**
     * Pulls beggining of block.
     *
     * @return terminated block flag
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nonnull
    XBBlockTerminationMode pullBegin() throws XBProcessingException, IOException;

    /**
     * Pulls block type.
     *
     * @return block type
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nonnull
    XBBlockType pullType() throws XBProcessingException, IOException;

    /**
     * Pulls block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nonnull
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
    XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException;

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
    void pullConsist(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using join method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using list consist method.
     *
     * @param child serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullListConsist(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object using list join method.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullListJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Pulls given sequence item.
     *
     * @param item serializable object
     * @throws XBProcessingException if not matching
     * @throws java.io.IOException if input/output error
     */
    void pullItem(XBSerialSequenceItem item) throws XBProcessingException, IOException;

    /**
     * Pulls serializable object appending all tokens.
     *
     * @param serial serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void pullAppend(XBSerializable serial) throws XBProcessingException, IOException;
}
