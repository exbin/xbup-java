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
package org.xbup.lib.core.parser.basic;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.stream.XBOutput;

/**
 * XBUP protocol level 1 matching provider.
 *
 * Execution is receiver side controlled (pull).
 *
 * @version 0.1.25 2015/02/14
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTMatchingProvider extends XBOutput {

    /**
     * Matches block begin.
     *
     * @return termination mode
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public XBBlockTerminationMode matchBeginXBT() throws XBProcessingException, IOException;

    /**
     * Matches block begin with exact termination mode.
     *
     * @param terminationMode termination mode
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void matchBeginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Matches type of block.
     *
     * @return block type
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public XBBlockType matchTypeXBT() throws XBProcessingException, IOException;

    /**
     * Matches type of block with exact value.
     *
     * @param type block type
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void matchTypeXBT(XBBlockType type) throws XBProcessingException, IOException;

    /**
     * Matches block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if processing error if unable to parse
     * attribute value
     * @throws IOException if input/output error
     */
    public XBAttribute matchAttribXBT() throws XBProcessingException, IOException;

    /**
     * Matches block attribute with exact value.
     *
     * @param value attribute value
     * @throws XBProcessingException if processing error if unable to parse
     * attribute value
     * @throws IOException if input/output error
     */
    public void matchAttribXBT(XBAttribute value) throws XBProcessingException, IOException;

    /**
     * Matches block data.
     *
     * You have to process data before processing next event.
     *
     * @return data
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public InputStream matchDataXBT() throws XBProcessingException, IOException;

    /**
     * Matches block data comparing it to specified data.
     *
     * @param data data to compare with
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void matchDataXBT(InputStream data) throws XBProcessingException, IOException;

    /**
     * Matches block end.
     *
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void matchEndXBT() throws XBProcessingException, IOException;
}
