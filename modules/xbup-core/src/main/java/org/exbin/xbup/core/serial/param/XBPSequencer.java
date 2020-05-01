/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial.param;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
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
@ParametersAreNonnullByDefault
public interface XBPSequencer extends XBPListener, XBPProvider {

    /**
     * Returns currently used serialization mode.
     *
     * @return sequencing mode depending on whether data are serialized from or
     * to token stream
     */
    @Nonnull
    XBSerializationMode getSerializationMode();

    /**
     * Performs begin token with default termination mode behavior.
     *
     * @throws java.io.IOException if input/output error
     */
    void begin() throws XBProcessingException, IOException;

    /**
     * Performs matching to given block type.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @param blockType block type to match to
     * @throws java.io.IOException if input/output error
     */
    void matchType(XBBlockType blockType) throws XBProcessingException, IOException;

    /**
     * Performs matching to unknown block type.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @throws java.io.IOException if input/output error
     */
    void matchType() throws XBProcessingException, IOException;

    /**
     * Performs end token.
     *
     * @throws java.io.IOException if input/output error
     */
    void end() throws XBProcessingException, IOException;

    /**
     * Performs attribute token using attributeValue, either as source or
     * target.
     *
     * @param attributeValue attribute value
     * @throws java.io.IOException if input/output error
     */
    void attribute(XBEditableAttribute attributeValue) throws XBProcessingException, IOException;

    /**
     * Performs serialization using consist operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException if input/output error
     */
    void consist(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization using join operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException if input/output error
     */
    void join(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization using list consist operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException if input/output error
     */
    void listConsist(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization using list join operation.
     *
     * @param serial serializable object
     * @throws java.io.IOException if input/output error
     */
    void listJoin(XBSerializable serial) throws XBProcessingException, IOException;

    /**
     * Performs serialization on serializable object.
     *
     * @param serial serializable object
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    void append(XBSerializable serial) throws XBProcessingException, IOException;
}
