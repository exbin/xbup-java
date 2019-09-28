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
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.stream.XBOutput;

/**
 * XBUP protocol level 1 matching provider.
 *
 * Execution is receiver side controlled (pull).
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTMatchingProvider extends XBOutput {

    /**
     * Matches block begin.
     *
     * @return termination mode
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    @Nonnull
    XBBlockTerminationMode matchBeginXBT() throws XBProcessingException, IOException;

    /**
     * Matches block begin with exact termination mode.
     *
     * @param terminationMode termination mode
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchBeginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Matches type of block.
     *
     * @return block type
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    @Nonnull
    XBBlockType matchTypeXBT() throws XBProcessingException, IOException;

    /**
     * Matches type of block with exact value.
     *
     * @param type block type
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchTypeXBT(XBBlockType type) throws XBProcessingException, IOException;

    /**
     * Matches block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    @Nonnull
    XBAttribute matchAttribXBT() throws XBProcessingException, IOException;

    /**
     * Matches block attribute with exact value.
     *
     * @param value attribute value
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchAttribXBT(XBAttribute value) throws XBProcessingException, IOException;

    /**
     * Matches block data.
     *
     * You have to process data before processing next event.
     *
     * @return data
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    @Nonnull
    InputStream matchDataXBT() throws XBProcessingException, IOException;

    /**
     * Matches block data comparing it to specified data.
     *
     * @param data data to compare with
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchDataXBT(InputStream data) throws XBProcessingException, IOException;

    /**
     * Matches block end.
     *
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchEndXBT() throws XBProcessingException, IOException;
}
