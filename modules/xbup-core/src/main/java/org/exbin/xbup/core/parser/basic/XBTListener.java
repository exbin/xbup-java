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
package org.exbin.xbup.core.parser.basic;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.stream.XBInput;

/**
 * XBUP protocol level 1 data listener.
 *
 * Execution is sender side controlled (push).
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTListener extends XBInput {

    /**
     * Reports block begin.
     *
     * @param terminationMode Specify whether block is terminated
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void beginXBT(@Nonnull XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Reports type of block.
     *
     * @param blockType Returns type of block
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void typeXBT(@Nonnull XBBlockType blockType) throws XBProcessingException, IOException;

    /**
     * Reports block attribute.
     *
     * @param attribute given attribute
     * @throws XBProcessingException if processing error if unable to parse
     * attribute value
     * @throws IOException if input/output error
     */
    void attribXBT(@Nonnull XBAttribute attribute) throws XBProcessingException, IOException;

    /**
     * Reports block data.
     *
     * You have to process data before processing next event.
     *
     * @param data processed data
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void dataXBT(@Nonnull InputStream data) throws XBProcessingException, IOException;

    /**
     * Reports block end.
     *
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void endXBT() throws XBProcessingException, IOException;
}
